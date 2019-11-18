package com.webdealsoft.Tiara.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.webdealsoft.Tiara.Activity.DatabaseSqlite.Controllerdb;
import com.webdealsoft.Tiara.Activity.Salesadapter.CustomAdapter;
import com.webdealsoft.Tiara.R;
import com.webdealsoft.Tiara.Registeration.LoginActivity;
import com.webdealsoft.Tiara.Utility.Root_URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FinalSales extends AppCompatActivity {
    private TextView tv_Gwt,tv_AddGwt,tv_AddMetal,tvAddOther,tv_finalamt;
    private EditText et_Loss,et_Metal,et_Other,et_AmountRed;
    private String clientname,str_Gwt,clientid;
    private Integer Loss_per;
    private Integer Gwt;
    double temp,temp1,metalamount,otheramount,otheramount1;
    double value1,value2,value3,value4;
    DecimalFormat precision;
    String PREFS_NAME = "auth_info";
    ///////db//////
    Controllerdb controllerdb = new Controllerdb(this);
    SQLiteDatabase db;
    private ArrayList<String> Id = new ArrayList<String>();
    private ArrayList<String> Stockid = new ArrayList<String>();
    private ArrayList<String> Sku = new ArrayList<String>();
    private ArrayList<String> Item = new ArrayList<String>();
    private ArrayList<String> Metal = new ArrayList<String>();
    private ArrayList<String> Title = new ArrayList<String>();
    private ArrayList<String> Pcs = new ArrayList<String>();
    private ArrayList<String> Gwt1 = new ArrayList<String>();
    private ArrayList<String> Nwt = new ArrayList<String>();
    BigDecimal bd;


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
        setContentView(R.layout.activity_final_sales);
        precision = new DecimalFormat("#####.###");
         //new BigDecimal(input).setScale(3, RoundingMode.HALF_UP);
        //////id///////
        tv_Gwt=findViewById(R.id.tv_totalgwt);


        Init();
        Intent intent=getIntent();
        tv_Gwt.setText(String.valueOf(new BigDecimal(intent.getStringExtra("total")).setScale(3, RoundingMode.HALF_UP)));
        tv_AddGwt.setText(String.valueOf(new BigDecimal(intent.getStringExtra("total")).setScale(3, RoundingMode.HALF_UP)));
        tv_AddMetal.setText(String.valueOf(new BigDecimal(Double.valueOf(intent.getStringExtra("total"))).setScale(3, RoundingMode.HALF_UP)));
        //tvAddOther.setText(String.valueOf(new BigDecimal(Double.valueOf(tv_AddMetal.getText().toString())).setScale(2, RoundingMode.HALF_UP)));
        //tv_finalamt.setText(String.valueOf(new BigDecimal(Double.valueOf(intent.getStringExtra("total"))).setScale(2, RoundingMode.HALF_UP)));
        clientname=intent.getStringExtra("clientname");
        clientid=intent.getStringExtra("cid");
        displayData();
        CalculationDemo();
    }

    private void CalculationDemo() {
        value1 = TextUtils.isEmpty(et_Loss.getText().toString()) ? 0 : Double.parseDouble(et_Loss.getText().toString());
        temp=(Double.valueOf(tv_Gwt.getText().toString())*value1)/100;
        temp1=Double.valueOf(tv_Gwt.getText().toString())+temp;
        BigDecimal bd = new BigDecimal(temp1).setScale(2, RoundingMode.HALF_EVEN);
        temp1 = bd.doubleValue();
        tv_AddGwt.setText(String.valueOf(new BigDecimal(temp1).setScale(3, RoundingMode.HALF_UP)));
        tv_AddMetal.setText(String.valueOf(new BigDecimal(Double.valueOf(tv_AddGwt.getText().toString())-value2).setScale(3, RoundingMode.HALF_UP)));
        tvAddOther.setText(String.valueOf(new BigDecimal(Double.valueOf(tv_AddMetal.getText().toString())*value3).setScale(2, RoundingMode.HALF_UP)));
        tv_finalamt.setText(String.valueOf(new BigDecimal(Double.valueOf(tvAddOther.getText().toString())-value4).setScale(2, RoundingMode.HALF_UP)));
    }

    private void Init() {
        tv_AddGwt=findViewById(R.id.txt_addloss);
        tv_AddMetal=findViewById(R.id.txt_addmental);
        tvAddOther=findViewById(R.id.tv_addother);
        tv_finalamt=findViewById(R.id.finalTotal);
        et_Loss=findViewById(R.id.txt_loss);
        et_Metal=findViewById(R.id.txt_mentalrecipt);
        et_Other=findViewById(R.id.txt_other);
        et_AmountRed=findViewById(R.id.txt_AmtRecived);
        /////////////////////loss percent/////////////////

            et_Loss.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if(!s.equals("") )
                    {
                        value1 = TextUtils.isEmpty(et_Loss.getText().toString()) ? 0 : Double.parseDouble(et_Loss.getText().toString());
                        temp=(Double.valueOf(tv_Gwt.getText().toString())*value1)/100;
                        temp1=Double.valueOf(tv_Gwt.getText().toString())+temp;
                        BigDecimal bd = new BigDecimal(temp1).setScale(2, RoundingMode.HALF_EVEN);
                        temp1 = bd.doubleValue();
                        tv_AddGwt.setText(String.valueOf(new BigDecimal(temp1).setScale(3, RoundingMode.HALF_UP)));
                        tv_AddMetal.setText(String.valueOf(new BigDecimal(Double.valueOf(tv_AddGwt.getText().toString())-value2).setScale(3, RoundingMode.HALF_UP)));
                        //tvAddOther.setText(String.valueOf(new BigDecimal(Double.valueOf(tv_AddMetal.getText().toString())*value3).setScale(2, RoundingMode.HALF_UP)));
                        //tv_finalamt.setText(String.valueOf(new BigDecimal(Double.valueOf(tvAddOther.getText().toString())-value4).setScale(2, RoundingMode.HALF_UP)));
                    }
                }
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }
                public void afterTextChanged(Editable s) {
                }
            });

        /////////////////////////add metal////////////////////

            et_Metal.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if(!s.equals("") )
                    {
                        value2 = TextUtils.isEmpty(et_Metal.getText().toString()) ? 0 : Double.parseDouble(et_Metal.getText().toString());
                        tv_AddMetal.setText(String.valueOf(new BigDecimal(Double.valueOf(tv_AddGwt.getText().toString())-value2).setScale(3, RoundingMode.HALF_UP)));
                        tvAddOther.setText(String.valueOf(new BigDecimal(Double.valueOf(tv_AddMetal.getText().toString())*value3).setScale(2, RoundingMode.HALF_UP)));
                        tv_finalamt.setText(String.valueOf(new BigDecimal(Double.valueOf(tvAddOther.getText().toString())-value4).setScale(2, RoundingMode.HALF_UP)));

                    }
                }
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }
                public void afterTextChanged(Editable s) {
                }
            });




        ///////////////////////other amount

            et_Other.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if(!s.equals("") )
                    {
                        value3 = TextUtils.isEmpty(et_Other.getText().toString()) ? 0 : Double.parseDouble(et_Other.getText().toString());
                        tvAddOther.setText(String.valueOf(new BigDecimal(Double.valueOf(tv_AddMetal.getText().toString())*value3).setScale(2, RoundingMode.HALF_UP)));
                        tv_finalamt.setText(String.valueOf(new BigDecimal(Double.valueOf(tvAddOther.getText().toString())-value4).setScale(2, RoundingMode.HALF_UP)));

                    }
                }
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }
                public void afterTextChanged(Editable s) {
                }
            });


        ///////////////////////amount Reciiveddd///////////

            et_AmountRed.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if(!s.equals("") )
                    {
                        value4 = TextUtils.isEmpty(et_AmountRed.getText().toString()) ? 0 : Double.parseDouble(et_AmountRed.getText().toString());
                        tv_finalamt.setText(String.valueOf(new BigDecimal(Double.valueOf(tvAddOther.getText().toString())-value4).setScale(2, RoundingMode.HALF_UP)));

                    }
                }
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }
                public void afterTextChanged(Editable s) {
                }
            });



        BackPress();
    }
    public void BackPress() {
        ImageView back=findViewById(R.id.bdccdck);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void displayData() {
        db = controllerdb.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM  UserDetails",null);
        Id.clear();
        Stockid.clear();
        Sku.clear();
        Item.clear();
        Metal.clear();
        Title.clear();
        Pcs.clear();
        Gwt1.clear();
        Nwt.clear();
        if (cursor.moveToFirst()) {
            do {
                Id.add(cursor.getString(cursor.getColumnIndex("Id")));
                Stockid.add(cursor.getString(cursor.getColumnIndex("Stockid")));
                Sku.add(cursor.getString(cursor.getColumnIndex("Sku")));
                Item.add(cursor.getString(cursor.getColumnIndex("Item")));
                Metal.add(cursor.getString(cursor.getColumnIndex("Metal")));
                Title.add(cursor.getString(cursor.getColumnIndex("Title")));
                Pcs.add(cursor.getString(cursor.getColumnIndex("Pcs")));
                Gwt1.add(cursor.getString(cursor.getColumnIndex("Gwt")));
                Nwt.add(cursor.getString(cursor.getColumnIndex("Nwt")));
            } while (cursor.moveToNext());
        }

        //code to set adapter to populate list
        cursor.close();
    }

    public void Savebtn(View view) throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        JSONObject json1=new JSONObject();
        JSONArray jsonarray=new JSONArray();
        json1.put("clientid",sharedPreferences.getString("cc",""));
        json1.put("userid",sharedPreferences.getString("userid",""));
        json1.put("companyid",sharedPreferences.getString("companyid",""));
        ////
        json1.put("totalgwt",tv_Gwt.getText().toString());
        json1.put("loss",et_Loss.getText().toString().trim());
        json1.put("total",tv_AddGwt.getText().toString());
        json1.put("metalrcpt",et_Metal.getText().toString().trim());
        json1.put("price",et_Other.getText().toString().trim());
        json1.put("amount",et_AmountRed.getText().toString().trim());
        json1.put("amountrcpt",tv_finalamt.getText().toString());
        ///
        json1.put("items",jsonarray);


////
        for(int i=0 ; i<Stockid.size(); i++) {

            JSONObject json = new JSONObject();
            json.put("stockid", Stockid.get(i));
            json.put("sku", Sku.get(i));
            json.put("item", Item.get(i));
            json.put("metal", Metal.get(i));
            json.put("title", Title.get(i));
            json.put("pcs", Pcs.get(i));
            json.put("gwt", Gwt1.get(i));
            json.put("nwt", Nwt.get(i));
            json.put("loss", "0");


            jsonarray.put(json);

        }
/////
        Log.d("JSOn",json1.toString());


        PrepareData(json1);
        //startActivity(new Intent(this, Home.class));
    }

    private void PrepareData(JSONObject json1) {



        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please Wait......");
        progress.setCancelable(false);
        progress.show();

        final String URL_Product = Root_URL.FINALSALE+"json="+json1.toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_Product.replaceAll(" ","%20"),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("ghg",URL_Product);
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));

                            if (jsonObject.getString("Flag").equals("1")) {
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
}
