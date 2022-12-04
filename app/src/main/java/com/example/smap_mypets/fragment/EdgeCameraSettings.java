package com.example.smap_mypets.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smap_mypets.R;

public class EdgeCameraSettings extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private Button btnStartCamera;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edge_camera_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Nastavení kamery");

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> openHome());

        btnStartCamera = findViewById(R.id.btn_startCamera);
        btnStartCamera.setOnClickListener(v -> openEdgeCamera());
    }

    //otevření hranové kamery + kontrola práv
    private void openEdgeCamera() {
        Intent i1 = new Intent(this, EdgeCamera.class);
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
