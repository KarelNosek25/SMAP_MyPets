package com.example.smap_mypets.fragment;

import static android.content.ContentValues.TAG;
import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgproc.Imgproc.Canny;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    private boolean normal, edge, small, medium, big, blurYes, blurNo, color, blackWhite;

    private ImageView btnCapture;
    private int ambilGamba = 0;
    private Mat mRgba;

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
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Foto", Toast.LENGTH_SHORT).show();
                ambilGamba = 1;
            }
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
        mRgba = inputFrame.rgba();

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
        ambilGamba = fungsiCaptureGambar(ambilGamba, mRgba);
        return mRgba;
    }

    private int fungsiCaptureGambar(int ambilGambar, Mat mRgba) {
        if (ambilGambar == 1) {

            boolean sukses = true;

            Mat save_mat = new Mat();
            Core.flip(mRgba.t(), save_mat, 1);

            if(color){
                Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGBA2BGRA);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String waktuSekarang = sdf.format(new Date());
            String fileName = getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + waktuSekarang + ".jpg";

            Boolean cekSave = Imgcodecs.imwrite(fileName, mRgba);
            ambilGambar = 0;
        }
        return ambilGambar;
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
