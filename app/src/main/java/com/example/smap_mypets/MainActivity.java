package com.example.smap_mypets;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.smap_mypets.database.UserDatabase;
import com.example.smap_mypets.fragment.LoginFragment;
import com.example.smap_mypets.fragment.NormalCameraFragment;
import com.example.smap_mypets.fragment.EdgeCameraFragment;
import com.example.smap_mypets.fragment.SettingsFragment;
import com.example.smap_mypets.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Button btnNormalCamera;
    private Button btnEdgeCamera;

    private UserDatabase userDatabase;
    private User user;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //kontrola zdali je uživatel přihlášen
        if (!mainActivity.getUser().isAuthenticated()) {
            Intent i0 = new Intent(this, LoginFragment.class);
            startActivity(i0);
        }

        userDatabase = new UserDatabase(getApplicationContext());
        user = userDatabase.getUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Výběr kamery");

        btnNormalCamera = findViewById(R.id.normal_camera_button);
        btnNormalCamera.setOnClickListener(v -> openNormalCamera());

        btnEdgeCamera = findViewById(R.id.edge_camera_button);
        btnEdgeCamera.setOnClickListener(v -> openEdgeCamera());

        //spodní navigace (domů, přidat, nastavení)
        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.getMenu().findItem(R.id.navigation_home).setChecked(true);
        bottomNav.getMenu().findItem(R.id.navigation_home).setEnabled(false);

        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                case R.id.navigation_settings:
                    openSettings();
                    return true;
            }
            return false;
        });
    }

    //otevřít nastavení
    private void openSettings() {
        Intent i1 = new Intent(this, SettingsFragment.class);
        startActivity(i1);
    }

    //otevření normální kamery + kontrola práv
    public void openNormalCamera() {
        Intent i2 = new Intent(this, EdgeCameraFragment.class);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Nejsou udělena práva k používání fotoaparátu!", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(i2);
        }
    }

    //otevření hranové kamery + kontrola práv
    public void openEdgeCamera() {
        Intent i3 = new Intent(this, NormalCameraFragment.class);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Nejsou udělena práva k používání fotoaparátu!", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(i3);
        }
    }

    //získání údajů uživatele
    public User getUser() {
        return user;
    }
}
