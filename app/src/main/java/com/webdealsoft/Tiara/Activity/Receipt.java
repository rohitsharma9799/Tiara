package com.webdealsoft.Tiara.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.webdealsoft.Tiara.Activity.Modelclass.ClientModel;
import com.webdealsoft.Tiara.Activity.Salesadapter.SalesAdapterAutoname;
import com.webdealsoft.Tiara.Activity.Salesadapter.SalesAdapterAutoname1;
import com.webdealsoft.Tiara.R;
import com.webdealsoft.Tiara.Utility.Root_URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Receipt extends AppCompatActivity {
    static TextView et_account;
    EditText et_amount,et_metal,et_title,et_weight;
    String str_account,str_amount,str_metal,str_title,str_weight;
    Dialog dialog;
    RecyclerView namerecylerview;
    String str_name,str_email,str_mobile;
    static String name,cid;
    String cidd;
    String PREFS_NAME = "auth_info";
    ArrayList<ClientModel> modelclasses;
    SalesAdapterAutoname1 salesAdapterAutoname;
    Button btn_Submit;
    static TextView tv_cid;

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
        setContentView(R.layout.activity_receipt);
        et_account=findViewById(R.id.txt_account);
        et_amount=findViewById(R.id.txt_amount);
        et_metal=findViewById(R.id.txt_metal);
        et_title=findViewById(R.id.txt_title);
        et_weight=findViewById(R.id.txt_weight);


        Init();
        OnBackpress();
    }

    private void Init() {
        tv_cid=findViewById(R.id.tv_cid);
        et_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrepareData();
            }
        });
        btn_Submit=findViewById(R.id.cirLoginButton);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_amount=et_amount.getText().toString().trim();
                str_metal=et_metal.getText().toString().trim();
                str_title=et_title.getText().toString().trim();
                str_weight=et_weight.getText().toString().trim();
                if (validateaccount(et_account.getText().toString()) && validateName(str_amount) && validateName1(str_metal) && validateName2(str_title) && validateName3(str_weight)) {
                    ReciptData();
                }
            }
        });

    }
    private boolean validateaccount(String str1) {
        if (str1.equals("")) {
            et_account.setError("Select One");
            return false;
        }
        return true;
    }
    private boolean validateName(String str1) {
        if (str1.equals(""))
        {
            et_amount.setError("Amount");
            return false;
        }
        return true;
    }
    private boolean validateName1(String str1) {
        if (str1.equals(""))
        {
            et_metal.setError("Metal");
            return false;
        }
        return true;
    }
    private boolean validateName2(String str1) {
        if (str1.equals(""))
        {
            et_title.setError("Title");
            return false;
        }
        return true;
    }
    private boolean validateName3(String str1) {
        if (str1.equals(""))
        {
            et_weight.setError("Weight");
            return false;
        }
        return true;
    }
    private void ReciptData() {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please Wait......");
        progress.setCancelable(false);
        progress.show();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String URL_Product = Root_URL.RECIPT+"companyid="+sharedPreferences.getString("companyid","")+"&clientid="+sharedPreferences.getString("ccc","")+"&amount="+str_amount+"&metal="+str_metal+"&title="+str_title+"&weight="+str_weight;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_Product.replaceAll(" ","%20"),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("ghg",URL_Product);
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));

                            if (jsonObject.getString("flag").equals("1")) {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("Message"), Toast.LENGTH_LONG).show();
                                finish();
                                Intent intent=new Intent(getApplicationContext(), Home.class);
                                startActivity(intent);

                            } else {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("Message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            progress.dismiss();
                            e.printStackTrace();
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

    private void PrepareData() {
        //////alert
        dialog = new Dialog(Receipt.this);
        dialog.setContentView(R.layout.feedback);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final EditText et_mobile,et_mail,et_name;
        et_name=dialog.findViewById(R.id.name_dialog);
        et_mail=dialog.findViewById(R.id.email_dialog);
        et_mobile=dialog.findViewById(R.id.mobile_dialog);
        ImageButton close=dialog.findViewById(R.id.close_dialog);
        Button bt_search=dialog.findViewById(R.id.search_dialog);
        namerecylerview=dialog.findViewById(R.id.namerecylerview);

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_name=et_name.getText().toString().trim();
                str_email=et_mail.getText().toString().trim();
                str_mobile=et_mobile.getText().toString().trim();

                GetData();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void GetData() {
        namerecylerview.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Receipt.this,1,GridLayoutManager.VERTICAL,false);
        namerecylerview.setLayoutManager(gridLayoutManager);
        namerecylerview.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String Auto_name = Root_URL.GETCLIENT+"companyid="+sharedPreferences.getString("companyid","")+"&name="+str_name+"&mobile="+str_mobile+"&email="+str_email;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Auto_name, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));

                            modelclasses = new ArrayList<>();
                            if(jsonObject.getString("Flag").equals("0")){
                                Toast.makeText(Receipt.this,"Sorry, no information available..",Toast.LENGTH_LONG).show();
                            }
                            else {

                                JSONArray jsonArray = jsonObject.getJSONArray("Response");
                                namerecylerview.setVisibility(View.VISIBLE);
                                for(int i=0 ; i<jsonArray.length(); i++){
                                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                    ClientModel item = new ClientModel();
                                    item.setName(jsonObject1.getString("name"));
                                    item.setCid(jsonObject1.getString("cid"));
                                    item.setEmail(jsonObject1.getString("email"));
                                    item.setMobile(jsonObject1.getString("mobile"));
                                    modelclasses.add(item);
                                    salesAdapterAutoname = new SalesAdapterAutoname1(modelclasses,Receipt.this,dialog,cidd);
                                    namerecylerview.setAdapter(salesAdapterAutoname);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Receipt.this);
        requestQueue.add(jsonObjectRequest);
    }
    public static void PincodeSet(String selectname,String id){
        name=selectname;
        et_account.setText(selectname);
        cid=id;
        tv_cid.setText(cid);
    }
    private void OnBackpress() {
        ImageView back=findViewById(R.id.backpress);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
