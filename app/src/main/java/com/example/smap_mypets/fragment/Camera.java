package com.example.smap_mypets.fragment;

import static org.opencv.core.CvType.CV_8UC1;
import static org.opencv.imgproc.Imgproc.Canny;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smap_mypets.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Camera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    private boolean normal, edge, small, medium, big, blurYes, blurNo, color, blackWhite;

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
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        Mat edges = new Mat(rgba.size(), CV_8UC1);

        //barevnost obrazu
        if (blackWhite) {
            Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        }

        //rozmazání
        if (blurYes) {
            Imgproc.blur(rgba, rgba, new Size(5, 5));
        }

        //druh a citlivost kamery
        if (edge && small) {
            Canny(rgba, edges, 200, 200);
            return edges;
        } else if (edge && medium) {
            Canny(rgba, edges, 100, 100);
            return edges;
        } else if (edge && big) {
            Canny(rgba, edges, 50, 50);
            return edges;
        } else {
            Mat frame = inputFrame.rgba();
            //při černobílém obrazu potřebuji vrátit "edges"
            if (blackWhite) {
                return edges;
            } else {
                return frame;
            }
        }
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
