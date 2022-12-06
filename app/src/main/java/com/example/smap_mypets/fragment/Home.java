package com.example.smap_mypets.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smap_mypets.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Button btnCamera;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        btnCamera = findViewById(R.id.camera_button);
        btnCamera.setOnClickListener(v -> openEdgeCameraSettings());

        //spodní navigace (domů a nastavení)
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

    //otevřít nastavení (práva k používání kamery)
    private void openSettings() {
        Intent i1 = new Intent(this, Settings.class);
        startActivity(i1);
    }

    //otevření nastavení hranové kamery
    public void openEdgeCameraSettings() {
        Intent i3 = new Intent(this, CameraSettings.class);
        startActivity(i3);
    }
}
