package com.example.smap_mypets.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smap_mypets.R;

public class CameraSettings extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Button btnStartCamera;
    private Button btnBack;
    private CheckBox check, check2, check3; //nove


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Nastavení kamery");

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> openHome());

        btnStartCamera = findViewById(R.id.btn_startCamera);
        btnStartCamera.setOnClickListener(v -> openEdgeCamera());

        check = findViewById(R.id.checkBox);
        check2 = findViewById(R.id.checkBox2);
        check3 = findViewById(R.id.checkBox3);

        check.setChecked(true);

        check.setOnCheckedChangeListener((compoundButton, b) -> {
            check2.setChecked(!check.isChecked());
        });

        check2.setOnCheckedChangeListener((compoundButton, b) -> {
            check.setChecked(!check2.isChecked());
            check3.setEnabled(!check2.isChecked());

            if (check3.isChecked()) {
                check3.setChecked(!check2.isChecked());
            }
        });
    }


    //otevření hranové kamery + kontrola práv
    private void openEdgeCamera() {

        boolean checkStatus = check.isChecked(); //nove
        Intent i1 = new Intent(getApplicationContext(), Camera.class);//upravene
        i1.putExtra("checkBoxStatus", checkStatus);//nove

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
