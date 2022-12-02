package com.example.smap_mypets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smap_mypets.Fragment.NormalCameraFragment;
import com.example.smap_mypets.Fragment.EdgeCameraFragment;
import com.example.smap_mypets.Fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Button btnNormalCamera;
    private Button btnEdgeCamera;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private void openSettings() {
        Intent i1 = new Intent(this, SettingsFragment.class);
        startActivity(i1);
    }

    public void openNormalCamera() {
        Intent i2 = new Intent(this, EdgeCameraFragment.class);
        startActivity(i2);
    }

    public void openEdgeCamera() {
        Intent i3 = new Intent(this, NormalCameraFragment.class);
        startActivity(i3);
    }
}
