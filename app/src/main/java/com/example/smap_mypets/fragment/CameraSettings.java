package com.example.smap_mypets.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smap_mypets.R;

public class CameraSettings extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

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

        //základní nastavení tlačítek při načtení obrazovky
        edgeCamera.setChecked(true);
        medium.setChecked(true);
        blackWhitePhoto.setChecked(true);
        blackWhitePhoto.setEnabled(false);
        colorPhoto.setEnabled(false);
        blurYes.setChecked(true);

        //listenery na správné ovládání tlačítek
        normalCamera.setOnCheckedChangeListener((compoundButton, b) -> {
            edgeCamera.setChecked(!normalCamera.isChecked());
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

        edgeCamera.setOnCheckedChangeListener((compoundButton, b) -> {
            normalCamera.setChecked(!edgeCamera.isChecked());
            if (edgeCamera.isChecked()) {
                blackWhitePhoto.setChecked(true);
                blackWhitePhoto.setEnabled(false);
                colorPhoto.setEnabled(false);
            } else {
                blackWhitePhoto.setEnabled(true);
                colorPhoto.setEnabled(true);
            }
        });
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Nejsou udělena práva k používání fotoaparátu!", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(i1);
        }
    }

    //přejití zpět na domovskou obrazovku
    private void openHome() {
        Intent i2 = new Intent(this, Home.class);
        startActivity(i2);
    }
}
