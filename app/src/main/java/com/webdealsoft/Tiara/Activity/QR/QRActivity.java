package com.webdealsoft.Tiara.Activity.QR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.webdealsoft.Tiara.Activity.SalesActivity;
import com.webdealsoft.Tiara.R;
import com.webdealsoft.Tiara.Utility.Root_URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class QRActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    boolean isEmail = false;
    String PREFS_NAME = "auth_info";
    String stockid1,Sku1,Title1,Item1,Metal1,Pcs1,Gwt1,Nwt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blackTextColor));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.blackTextColor));
        }
        setContentView(R.layout.activity_qr);
        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intentData.length() > 0) {
                    if (isEmail) {
                     //   startActivity(new Intent(ScannedBarcodeActivity.this, EmailActivity.class).putExtra("email_address", intentData));
                    }else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
                    }
                }
            }
        });
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(QRActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(QRActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
               // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;

                                String string = intentData;
                                String[] parts = string.split(",");
                                String stockid = parts[0]; // 004
                                String Sku = parts[1]; // 034556
                                String Item = parts[2];
                                String Metal = parts[3];
                                String Title = parts[4];
                                String Pcs = parts[5];
                                String Gwt = parts[6];
                                String Nwt = parts[7];
                                txtBarcodeValue.setText("ss"+stockid+"/"+Title);
                                Toast.makeText(getApplicationContext(),"Not Value Found",Toast.LENGTH_LONG).show();
                                isEmail = true;
                                btnAction.setText("ADD CONTENT TO THE MAIL");
                            } else {
                                cameraSource.stop();
                                isEmail = false;
                                btnAction.setText("LAUNCH URL");
                                intentData = barcodes.valueAt(0).displayValue;
                                String string = intentData;
                                String[] parts = string.split(",");
                                 stockid1 = parts[0].replace("Stockid:",""); // 004
                                 Sku1 = parts[1].replace("Sku:",""); // 034556
                                 Item1 = parts[2].replace("Item:","");
                                 Metal1 = parts[3].replace("Metal:","");
                                 Title1 = parts[4].replace("Title:","");
                                 Pcs1 = parts[5].replace("Pcs:","");
                                 Gwt1 = parts[6].replace("Gwt:","");
                                 Nwt1 = parts[7].replace("Nwt:","").trim().replaceAll("%20","");
                                final Dialog dialog = new Dialog(QRActivity.this);
                                dialog.setContentView(R.layout.item_layout);
                                dialog.getWindow().setGravity(Gravity.CENTER);
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(false);
                                ///
                                TextView stockid,sku,metal,gross,net;
                                Button Yes,No;
                                stockid=dialog.findViewById(R.id.tv_stockid);
                                sku=dialog.findViewById(R.id.tv_sku);
                                metal=dialog.findViewById(R.id.tv_metal);
                                gross=dialog.findViewById(R.id.tv_gross);
                                net=dialog.findViewById(R.id.tv_net);
                                No=dialog.findViewById(R.id.no);
                                Yes=dialog.findViewById(R.id.yes);

                                stockid.setText(stockid1);
                                sku.setText(Sku1);
                                metal.setText(Metal1);
                                gross.setText(Gwt1);
                                net.setText(Nwt1);

                                No.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();

                                        try {
                                            cameraSource.start(surfaceView.getHolder());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                dialog.show();

                                Yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        PrepareData();
                                        dialog.dismiss();

                                    }
                                });
                                dialog.show();
                                //txtBarcodeValue.setText("ss"+stockid+"/"+Title);
                            }
                        }
                    });
                }
            }
        });
    }

    private void PrepareData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url = Root_URL.QRCODESEND+"companyid="+sharedPreferences.getString("companyid","")+"&userid="+sharedPreferences.getString("userid","")+
                "&stockid="+stockid1+"&sku="+Sku1+"&item="+Item1+"&metal="+Metal1+"&title="+Title1+"&pcs="+Pcs1+"&gwt="+Gwt1+"&nwt="+Nwt1;
        StringRequest request = new StringRequest(Request.Method.GET, url.replaceAll("%20",""), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    Log.d("ghg",url);
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("Flag").equals("1"))
                    {
                        txtBarcodeValue.setText(jsonObject.getString("Message"));
                        Toast.makeText(getApplicationContext(),jsonObject.getString("Message"),Toast.LENGTH_SHORT).show();
                        cameraSource.start(surfaceView.getHolder());
                    }
                    else
                    {
                        txtBarcodeValue.setText(jsonObject.getString("Message"));
                        Toast.makeText(getApplicationContext(),jsonObject.getString("Message"),Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex)
                {
                    //Toast.makeText(getApplicationContext(),"No Data Found",Toast.LENGTH_LONG).show(); ex.printStackTrace();
                }
            }
        }
                ,
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                    }
                }

        );
        requestQueue.add(request);
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();

    }
}
