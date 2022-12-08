package com.example.smap_mypets.fragment;

import static org.opencv.core.Core.ROTATE_90_CLOCKWISE;
import static org.opencv.imgproc.Imgproc.Canny;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smap_mypets.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends HideBottomBar implements CameraBridgeViewBase.CvCameraViewListener2, ActivityCompat.OnRequestPermissionsResultCallback {

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    private boolean normal, edge, small, medium, big, blurYes, blurNo, color, blackWhite;

    private ImageView btnCapture, btnBack;
    private int take_image = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera);

        cameraBridgeViewBase = (JavaCameraView) findViewById(R.id.CameraView);
        cameraBridgeViewBase.setVisibility(View.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);
                switch (status) {
                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };

        // načtení stavu tlačítek
        Intent intent = getIntent();
        normal = intent.getBooleanExtra("normalStatus", false);
        edge = intent.getBooleanExtra("edgeStatus", true);

        small = intent.getBooleanExtra("smallStatus", false);
        medium = intent.getBooleanExtra("mediumStatus", true);
        big = intent.getBooleanExtra("bigStatus", false);

        blurYes = intent.getBooleanExtra("blurYesStatus", true);
        blurNo = intent.getBooleanExtra("blurNoStatus", false);

        color = intent.getBooleanExtra("colorStatus", false);
        blackWhite = intent.getBooleanExtra("blackWhiteStatus", true);

        btnCapture = findViewById(R.id.capBtn);
        btnCapture.setOnClickListener(view -> {
            //kontrola práv na úložiště
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Nejsou udělena práva k přístupu do úložiště", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Úspěšně vyfoceno", Toast.LENGTH_SHORT).show();
                if (take_image == 0) {
                    take_image = 1;
                } else {
                    take_image = 0;
                }
            }
        });

        //tlačtko zpět
        btnBack = findViewById(R.id.backBtn);
        btnBack.setOnClickListener(view -> {
            Intent i1 = new Intent(this, CameraSettings.class);
            startActivity(i1);
        });
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat mRgba = inputFrame.rgba();

        //barevnost obrazu
        if (blackWhite) {
            Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGB2GRAY, 4);
        }

        //rozmazání
        if (blurYes) {
            Imgproc.blur(mRgba, mRgba, new Size(5, 5));
        }

        //druh a citlivost kamery
        if (edge && small) {
            Canny(mRgba, mRgba, 200, 200);
        } else if (edge && medium) {
            Canny(mRgba, mRgba, 100, 100);
        } else if (edge && big) {
            Canny(mRgba, mRgba, 50, 50);
        }

        take_image = takePicture(take_image, mRgba);
        return mRgba;
    }

    //uložení fotky
    private int takePicture(int takeImage, Mat mRgba) {
        if (takeImage == 1) {

            //otočení fotky
            //Core.flip(mRgba.t(), mRgba, 1);

            //znovunačtení fotoaparátu (bez toho bychom se vrátili na "CameraSettings")
            //Intent i2 = new Intent(this, Camera.class);
            //startActivity(i2);

            if (color) {
                Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGBA2BGRA);
            }

            //uložení do telefonu (interniUloziste/Android/data/com.example.smap_mypets/files/Pictures/)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String date = sdf.format(new Date());
            String fileName = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + date + ".jpg";

            Imgcodecs.imwrite(fileName, mRgba);
            takeImage = 0;
        }
        return takeImage;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }
    }
}
