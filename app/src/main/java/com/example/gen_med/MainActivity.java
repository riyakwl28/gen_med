package com.example.gen_med;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    EditText editText;
    Button proceed;
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    Camera mCamera;
    boolean mPreviewRunning;
    Button btncapture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText) findViewById(R.id.editText1) ;
        proceed=(Button)findViewById(R.id.button) ;

        btncapture=(Button) findViewById(R.id.btncapture);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btncapture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //take picture here
                mCamera.takePicture(null, null, mPictureCallback);
            }
        });
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] imageData, Camera c) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData , 0, imageData .length);
            String file_path=saveToInternalSorage(bitmap);
            Toast.makeText(getApplicationContext(),"Image stored succesfully at "+file_path,Toast.LENGTH_LONG).show();
        }
    };

    private String saveToInternalSorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"marina1.jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera=Camera.open();


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w,
                               int h) {
        if (mPreviewRunning) {
            mCamera.stopPreview();
        }

        Camera.Parameters p =mCamera.getParameters();
        List<Camera.Size> previewSizes = p.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(0);
        p.setPreviewSize(previewSize.width, previewSize.height);
        mCamera.setParameters(p);
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
        mPreviewRunning = true;

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mPreviewRunning = false;
        mCamera.release();

    }

}

