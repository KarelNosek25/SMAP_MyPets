package com.example.smap_mypets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smap_mypets.Fragment.NormalCameraFragment;
import com.example.smap_mypets.Fragment.EdgeCameraFragment;

public class MainActivity extends AppCompatActivity {

    private Button btnNormalCamera;
    private Button btnEdgeCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNormalCamera = findViewById(R.id.normal_camera_button);
        btnNormalCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNormalCamera();
            }
        });

        btnEdgeCamera = findViewById(R.id.edge_camera_button);
        btnEdgeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEdgeCamera();
            }
        });
    }

    public void openNormalCamera(){
        Intent i1 = new Intent(this, EdgeCameraFragment.class);
        startActivity(i1);
    }

    public void openEdgeCamera(){
        Intent i2 = new Intent(this, NormalCameraFragment.class);
        startActivity(i2);
    }
}
