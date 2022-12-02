package com.example.smap_mypets.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.smap_mypets.MainActivity;
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
        //btnAskRight.setOnClickListener(v -> requestCamera());


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

    private void openHome() {
        Intent i1 = new Intent(this, MainActivity.class);
        startActivity(i1);
    }

    /*
    //požádání o přidělení práv
    private void requestCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Práva máme, není potřeba o ně žádat
            Toast.makeText(getContext(), "Práva již přidělena, není nutné znovu žádat.", Toast.LENGTH_LONG).show();
        } else {
            // Požádáme o práva
            int PERMISSION_REQUEST_CAMERA = 0;
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            Toast.makeText(getContext(), "Požádáno o práva.", Toast.LENGTH_SHORT).show();
        }
    }*/
}
