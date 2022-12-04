package com.example.smap_mypets;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.smap_mypets.fragment.Home;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    BiometricManager biometricManager;

    String jmeno = "admin";
    String heslo = "admin";

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_login = findViewById(R.id.btn_login);
        Button btn_login_finger = findViewById(R.id.btn_login_finger);

        username = findViewById(R.id.login_name);
        password = findViewById(R.id.login_password);

        biometricManager = BiometricManager.from(getApplicationContext());

        //kontrola údajů pro přihlášení (jméno a heslo)
        btn_login.setOnClickListener(v -> {
            if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Musíte vyplnit jméno a heslo!", Toast.LENGTH_SHORT).show();
            } else if (username.getText().toString().equals(jmeno)) {
                if (password.getText().toString().equals(heslo)) {
                    openHome();
                    Toast.makeText(getApplicationContext(), "Úšpěšně přihlášen.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Špatné jméno/heslo!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Špatné jméno/heslo!", Toast.LENGTH_SHORT).show();
            }
        });

        //kontrola při přihlášení díky otisku prstu
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "Toto zařízení nemá senzor na otisk prstu.", Toast.LENGTH_LONG).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, 0);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                Toast.makeText(getApplicationContext(), "Senzor k otisku prstu není momentálně dostupný.", Toast.LENGTH_LONG).show();
                break;
        }

        //špatné přihlášení díky otisku prstu
        Executor executor = ContextCompat.getMainExecutor(getApplicationContext());
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Přihlášení nebylo úspěšné.", Toast.LENGTH_SHORT).show();
            }

            //úspěšné přihlášení díky otisku prstu
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                openHome();
                Toast.makeText(getApplicationContext(), "Úšpěšně přihlášen.", Toast.LENGTH_SHORT).show();
            }

            //špatné přihlášení díky otisku prstu
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Přihlášení nebylo úspěšné.", Toast.LENGTH_SHORT).show();
            }
        });

        //tabulka ve které se naachází místo pro otisk prstu (+ info okolo)
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Přihlášení")
                .setDescription("Použijte váš otisk prstu pro přihlášení do aplikace")
                .setNegativeButtonText("Zrušit")
                .build();

        //tlačítko po němž se ukáže tabulka pro přihlášení díky otisku prstu
        btn_login_finger.setOnClickListener(v -> biometricPrompt.authenticate(promptInfo));
    }

    //otevření domovské obrazovky
    private void openHome() {
        Intent i1 = new Intent(this, Home.class);
        startActivity(i1);
    }
}
