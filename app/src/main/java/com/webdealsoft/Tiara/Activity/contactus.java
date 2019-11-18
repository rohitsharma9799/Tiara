package com.webdealsoft.Tiara.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.webdealsoft.Tiara.R;
import com.webdealsoft.Tiara.Utility.Root_URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class contactus extends AppCompatActivity {
    ImageView backcont;
    TextView tv_Companyname,tv_Address,tv_Address2,tv_Mobile,tv_Email,tv_Url;
    String PREFS_NAME = "auth_info";
    String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorwhite));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.activity_contectus);
        backcont=findViewById(R.id.backcont);
        tv_Companyname=findViewById(R.id.txt_companyname);
        tv_Address=findViewById(R.id.txt_address);
        tv_Address2=findViewById(R.id.txt_address2);
        tv_Mobile=findViewById(R.id.txt_mobile);
        tv_Email=findViewById(R.id.txt_email);
        tv_Url=findViewById(R.id.txt_url);
        backcont.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onBackPressed();
    }
});
        tv_Mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               num =tv_Mobile.getText().toString();

                Uri number = Uri.parse("tel:+"+num);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
              

            }
        });
        tv_Url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://"+tv_Url.getText().toString()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        PrepareData();


    }

    private void PrepareData() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String URL_Product = Root_URL.CONTACTUS+"companyid="+sharedPreferences.getString("companyid","");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_Product.replaceAll(" ","%20"),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("ghg",URL_Product);
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));

                            if (jsonObject.getString("Flag").equals("1")) {
                                JSONArray jsonArray=jsonObject.getJSONArray("response");
                                JSONObject jsonObject1=jsonArray.getJSONObject(0);
                                tv_Companyname.setText(jsonObject1.getString("companyname"));
                                tv_Address.setText(jsonObject1.getString("address1")+", "+jsonObject1.getString("address2"));
                                tv_Address2.setText(jsonObject1.getString("city")+", "+jsonObject1.getString("state")+"-"+jsonObject1.getString("zipcode"));
                                tv_Mobile.setText(jsonObject1.getString("mobileno"));
                                tv_Email.setText(jsonObject1.getString("email"));
                                tv_Url.setText(jsonObject1.getString("website"));
                               // Toast.makeText(getApplicationContext(), jsonObject.getString("Message"), Toast.LENGTH_LONG).show();
                            } else {
                                //Toast.makeText(getApplicationContext(), jsonObject.getString("Message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                           // e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
}
