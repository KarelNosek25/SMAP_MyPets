package com.example.smap_mypets.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smap_mypets.R;

public class CameraSettings extends HideBottomBar implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Button btnStartCamera;
    private Button btnBack;
    private RadioButton normalCamera, edgeCamera, small, big, medium, blurYes, blurNo, colorPhoto, blackWhitePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera_settings);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> openHome());

        btnStartCamera = findViewById(R.id.btn_startCamera);
        btnStartCamera.setOnClickListener(v -> openEdgeCamera());

        normalCamera = findViewById(R.id.normalCamera);
        edgeCamera = findViewById(R.id.edgeCamera);
        small = findViewById(R.id.small);
        big = findViewById(R.id.big);
        medium = findViewById(R.id.medium);
        blurYes = findViewById(R.id.blurYes);
        blurNo = findViewById(R.id.blurNo);
        colorPhoto = findViewById(R.id.colorPhoto);
        blackWhitePhoto = findViewById(R.id.blackWhitePhoto);

        //zachování stavu tlačítek na základě předchozí hodnoty (např. při opuštění stránky)
        normalCamera.setChecked(Update("btnOne"));
        edgeCamera.setChecked(Update("btnTwo"));
        small.setChecked(Update("btnThree"));
        big.setChecked(Update("btnFour"));
        medium.setChecked(Update("btnFive"));
        blurYes.setChecked(Update("btnSix"));
        blurNo.setChecked(Update("btnSeven"));
        colorPhoto.setChecked(Update("btnEight"));
        blackWhitePhoto.setChecked(Update("btnNine"));

        checkCameraSettings();

        //správné nastavení listenerů všech tlačítek "Radio Button"
        normalCamera.setOnCheckedChangeListener((compoundButton, btnOne) -> {
            SavePreferences("btnOne", btnOne);
            if (normalCamera.isChecked()) {
                small.setEnabled(false);
                medium.setEnabled(false);
                medium.setChecked(true);
                big.setEnabled(false);

            } else {
                small.setEnabled(true);
                medium.setEnabled(true);
                big.setEnabled(true);
            }
        });

        edgeCamera.setOnCheckedChangeListener((compoundButton, btnTwo) -> {
            SavePreferences("btnTwo", btnTwo);
            if (edgeCamera.isChecked()) {
                blackWhitePhoto.setChecked(true);
                blackWhitePhoto.setEnabled(false);
                colorPhoto.setEnabled(false);
            } else {
                blackWhitePhoto.setEnabled(true);
                colorPhoto.setEnabled(true);
            }
        });

        small.setOnCheckedChangeListener((compoundButton, btnThree) -> SavePreferences("btnThree", btnThree));
        big.setOnCheckedChangeListener((compoundButton, btnFour) -> SavePreferences("btnFour", btnFour));
        medium.setOnCheckedChangeListener((compoundButton, btnFive) -> SavePreferences("btnFive", btnFive));
        blurYes.setOnCheckedChangeListener((compoundButton, btnSix) -> SavePreferences("btnSix", btnSix));
        blurNo.setOnCheckedChangeListener((compoundButton, btnSeven) -> SavePreferences("btnSeven", btnSeven));
        colorPhoto.setOnCheckedChangeListener((compoundButton, btnEight) -> SavePreferences("btnEight", btnEight));
        blackWhitePhoto.setOnCheckedChangeListener((compoundButton, btnNine) -> SavePreferences("btnNine", btnNine));
    }

    //při znovuspuštění obrazovky se správně nastaví dostupnost různého nastavení
    private void checkCameraSettings() {
        if (edgeCamera.isChecked()) {
            blackWhitePhoto.setChecked(true);
            blackWhitePhoto.setEnabled(false);
            colorPhoto.setEnabled(false);
        } else {
            blackWhitePhoto.setEnabled(true);
            colorPhoto.setEnabled(true);
        }
        if (normalCamera.isChecked()) {
            small.setEnabled(false);
            medium.setEnabled(false);
            medium.setChecked(true);
            big.setEnabled(false);

        } else {
            small.setEnabled(true);
            medium.setEnabled(true);
            big.setEnabled(true);
        }
    }

    //metody potřebné k zapamatování si stavu každého "Radio button"
    private void SavePreferences(String key, boolean value) {
        SharedPreferences sp = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean Update(String key) {
        SharedPreferences sp = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    //otevření hranové kamery + kontrola práv
    private void openEdgeCamera() {

        //kontrola co je a co není zvoleno
        //druh kamery
        boolean d1 = normalCamera.isChecked();
        boolean d2 = edgeCamera.isChecked();

        //citlivost kamery
        boolean c1 = small.isChecked();
        boolean c2 = medium.isChecked();
        boolean c3 = big.isChecked();

        //rozmazání
        boolean r1 = blurYes.isChecked();
        boolean r2 = blurNo.isChecked();

        //druh obrazu
        boolean o1 = colorPhoto.isChecked();
        boolean o2 = blackWhitePhoto.isChecked();

        //odeslání stavu nastavení do kamery
        Intent i1 = new Intent(getApplicationContext(), Camera.class);
        i1.putExtra("normalStatus", d1);
        i1.putExtra("edgeStatus", d2);
        i1.putExtra("smallStatus", c1);
        i1.putExtra("mediumStatus", c2);
        i1.putExtra("bigStatus", c3);
        i1.putExtra("blurYesStatus", r1);
        i1.putExtra("blurNoStatus", r2);
        i1.putExtra("colorStatus", o1);
        i1.putExtra("blackWhiteStatus", o2);

        //spouštění kamery + kontrola práv a správného nastavení kamery
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Nejsou udělena práva k používání fotoaparátu!", Toast.LENGTH_SHORT).show();
        } else {
            //kontrola toho, jestli je při spuštění fotoaparátu zaškrtnuto všechno
            if ((normalCamera.isChecked() || edgeCamera.isChecked()) && (small.isChecked() || medium.isChecked() || big.isChecked()) &&
                    (blurYes.isChecked() || blurNo.isChecked()) && (colorPhoto.isChecked() || blackWhitePhoto.isChecked())) {
                startActivity(i1);
            } else {
                Toast.makeText(this, "Nemáte zaškrtnuty všechna pole!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //přejití zpět na domovskou obrazovku
    private void openHome() {
        Intent i2 = new Intent(this, Home.class);
        startActivity(i2);
    }
}
