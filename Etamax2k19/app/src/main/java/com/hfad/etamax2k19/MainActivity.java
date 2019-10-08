package com.hfad.etamax2k19;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView) findViewById(R.id.cam);
        textView = (TextView) findViewById(R.id.text);
        //JSONObject data;

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> qrcode = detections.getDetectedItems();
                if (qrcode.size()!=0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(qrcode.valueAt(0).displayValue);
                            JSONObject data;
                            StringBuffer response = new StringBuffer();
                            try {
                                String id = qrcode.valueAt(0).displayValue;
                                URL url = new URL("https://etamax.fcrit.ac.in/sp/api/admin/getDetails.php");
                                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                                connection.setDoOutput(true);
                                connection.setDoInput(true);
                                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                connection.setRequestProperty("Accept", "application/json");
                                connection.setRequestMethod("POST");
                                //id="81";
                                JSONObject object = new JSONObject();
                                object.put("pass", "et198079963");
                                object.put("id", id);

                                OutputStream os = connection.getOutputStream();
                                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(os, "UTF-8"));
                                writer.write(object.toString());
                                writer.flush();
                                writer.close();
                                os.close();

                                connection.connect();

                                BufferedReader in = new BufferedReader(
                                        new InputStreamReader(connection.getInputStream()));
                                String inputLine;

                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                in.close();

                                data = new JSONObject(response.toString());
                                textView.setText(qrcode.valueAt(0).displayValue);
                                Log.wtf("flag1", response.toString());
                                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(1000);
                                Intent intent = new Intent(MainActivity.this, Activity2.class);
                                intent.putExtra("name",data.getString("name"));
                                intent.putExtra("branch",data.getString("branch"));
                                intent.putExtra("rollNo",data.getString("rollNo"));
                                intent.putExtra("cost",data.getString("cost"));
                                intent.putExtra("id",id);
                                JSONArray jsonArray = data.getJSONArray("unpaidTeamEvents");
                                String team = "";
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject explrObject = jsonArray.getJSONObject(i);
                                    team = team + explrObject.getString("name") + ":" + explrObject.getString("fullName") +"\n";
                                }
                                intent.putExtra("team",team);
                                intent.putExtra("teamc",jsonArray.length());
                                startActivity(intent);

                            } catch (Exception e) {
                                //Log.wtf("flag2", "flag2");
                                e.printStackTrace();
                            }

                        }
                    });
                }

            }
        });

    }
}