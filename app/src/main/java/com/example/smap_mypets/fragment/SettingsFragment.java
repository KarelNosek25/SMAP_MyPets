package com.example.smap_mypets.fragment;

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

import com.example.smap_mypets.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsFragment extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Button btnAskRight;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Nastavení");

        btnAskRight = findViewById(R.id.btn_foto_ask);
        btnAskRight.setOnClickListener(v -> requestCamera());

        //spodní navigace (domů, přidat, nastavení)
        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.getMenu().findItem(R.id.navigation_settings).setChecked(true);
        bottomNav.getMenu().findItem(R.id.navigation_settings).setEnabled(false);

        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    openHome();
                case R.id.navigation_settings:
                    return true;
            }
            return false;
        });
    }

    //otevření domovské obrazovky
    private void openHome() {
        Intent i1 = new Intent(this, HomeFragment.class);
        startActivity(i1);
    }

    //požádání o přidělení práv
    private void requestCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Práva máme, není potřeba o ně žádat
            Toast.makeText(this, "Práva již přidělena, není nutné znovu žádat.", Toast.LENGTH_LONG).show();
        } else {
            // Požádáme o práva
            int PERMISSION_REQUEST_CAMERA = 0;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            Toast.makeText(this, "Požádáno o práva.", Toast.LENGTH_SHORT).show();
        }
    }
}
