package com.webdealsoft.Tiara.Activity;


import android.content.SharedPreferences;
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


public class about extends AppCompatActivity {
    ImageView bck;
    TextView tv_companyname,tv_description;
    String PREFS_NAME = "auth_info";
    SharedPreferences sharedPreferences;
    ImageView nav_Logo;
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
        setContentView(R.layout.activity_about);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        nav_Logo= findViewById(R.id.imageView);
        if (sharedPreferences.getString("companylogo","").equals("")) {

        }
        else {
            Picasso.with(getApplicationContext()).load(sharedPreferences.getString("companylogo","")).into(nav_Logo);
        }
        tv_companyname=findViewById(R.id.txt_companyname);
        tv_description=findViewById(R.id.txt_decsritpion);
        bck=findViewById(R.id.bck);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        /////
        FetchAboutUs();

    }

    private void FetchAboutUs() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String URL_Product = Root_URL.ABOUTUS+"companyid="+sharedPreferences.getString("companyid","");
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
                                tv_companyname.setText(jsonObject1.getString("title"));
                                tv_description.setText(jsonObject1.getString("description"));
                                //Toast.makeText(getApplicationContext(), jsonObject.getString("Message"), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("Message"), Toast.LENGTH_LONG).show();
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
