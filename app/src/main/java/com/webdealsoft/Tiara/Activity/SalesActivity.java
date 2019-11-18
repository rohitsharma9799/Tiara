package com.webdealsoft.Tiara.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webdealsoft.Tiara.Activity.DatabaseSqlite.Controllerdb;
import com.webdealsoft.Tiara.Activity.Modelclass.ClientModel;
import com.webdealsoft.Tiara.Activity.Modelclass.Modelclass;
import com.webdealsoft.Tiara.Activity.Modelclass.Mymethord;
import com.webdealsoft.Tiara.Activity.Salesadapter.CustomAdapter;
import com.webdealsoft.Tiara.Activity.Salesadapter.SalesAdapterAutoname;
import com.webdealsoft.Tiara.Activity.Salesadapter.SalesGetStock;
import com.webdealsoft.Tiara.R;
import com.webdealsoft.Tiara.Utility.Root_URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SalesActivity extends AppCompatActivity implements Mymethord {

    //--------Auto Search name-------
    String PREFS_NAME = "auth_info";
    SharedPreferences sharedPreferences ;
    ImageView bck;
    static TextView name;
    static EditText stockid;
    static TextView autonametext;
    static String nameemd,cid;
    static RecyclerView namerecylerview;
    ArrayList<ClientModel> modelclasses;
    SalesAdapterAutoname salesAdapterAutoname;
    //---------------getstock-------------
    SalesGetStock salesGetStock;
    Mymethord mymethord;

    //---------------DB Create list view---
    //  Controllerdb db =new Controllerdb(this);
    Controllerdb controllerdb = new Controllerdb(this);
    SQLiteDatabase db;
    private ArrayList<String> Id = new ArrayList<String>();
    private ArrayList<String> Stockid = new ArrayList<String>();
    private ArrayList<String> Sku = new ArrayList<String>();
    private ArrayList<String> Item = new ArrayList<String>();
    private ArrayList<String> Metal = new ArrayList<String>();
    private ArrayList<String> Title = new ArrayList<String>();
    private ArrayList<String> Pcs = new ArrayList<String>();
    private ArrayList<String> Gwt = new ArrayList<String>();
    private ArrayList<String> Nwt = new ArrayList<String>();
    ListView lv;
    TextView amttotal,tv_cid;
    private Button deleteAll;
    double total;
    String str_name,str_email,str_mobile;
    Dialog dialog;
    BigDecimal bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorwhite));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        bck=findViewById(R.id.bdccdck);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        name=findViewById(R.id.autoname);
        stockid=findViewById(R.id.stockid);
        amttotal=findViewById(R.id.total);
        lv=findViewById(R.id.lv);
        deleteAll=findViewById(R.id.bt_deleteAll);
        tv_cid=findViewById(R.id.cid);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrepareData();
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAll();
            }
        });
        displayData();
        BackPress();
        sum();
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

    private void PrepareData() {
        //////alert
        dialog = new Dialog(SalesActivity.this);
        dialog.setContentView(R.layout.feedback);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ///
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SalesActivity.this,1,GridLayoutManager.VERTICAL,false);
        namerecylerview.setLayoutManager(gridLayoutManager);
        namerecylerview.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String Auto_name =Root_URL.GETCLIENT+"companyid="+sharedPreferences.getString("companyid","")+"&name="+str_name+"&mobile="+str_mobile+"&email="+str_email;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Auto_name, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));

                            modelclasses = new ArrayList<>();
                            if(jsonObject.getString("Flag").equals("0")){
                                Toast.makeText(SalesActivity.this,"Sorry, no information available..",Toast.LENGTH_LONG).show();
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
                                    salesAdapterAutoname = new SalesAdapterAutoname(modelclasses,SalesActivity.this,dialog);
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
        RequestQueue requestQueue = Volley.newRequestQueue(SalesActivity.this);
        requestQueue.add(jsonObjectRequest);
    }

    //--Onclick event in sales next btn----
    public void Salesnext(View view) {
        if (name.getText().toString().equals("")){
            Toast.makeText(SalesActivity.this, "Please enter Name", Toast.LENGTH_SHORT).show();
        }else if (stockid.getText().toString().equals(null)){
            Toast.makeText(SalesActivity.this, "Please enter your Stock id", Toast.LENGTH_SHORT).show();
        }else {
            getstock();
        }
    }

    private void getstock() {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please Wait......");
        progress.setCancelable(false);
        progress.show();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String GETSTOCK = Root_URL.GETSTOCK+"companyid="+sharedPreferences.getString("companyid","")+"&stockid="+stockid.getText().toString().trim();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GETSTOCK, null,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ghg",GETSTOCK);
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));

                            modelclasses = new ArrayList<>();
                            if(jsonObject.getString("Flag").equals("0")){
                                progress.dismiss();
                                Toast.makeText(SalesActivity.this,"Not Found",Toast.LENGTH_LONG).show();
                            }
                            else {
                                progress.dismiss();
                                JSONArray jsonArray = jsonObject.getJSONArray("Response");
                                for(int i=0 ; i<jsonArray.length(); i++){
                                    final JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                    String  gwt=String.valueOf(new BigDecimal(Double.valueOf(jsonObject1.getString("gwt"))).setScale(3, RoundingMode.HALF_UP));

                    Log.d("SSS",gwt);
                                    try {
                                        db = controllerdb.getWritableDatabase();
                                        db.execSQL("INSERT INTO UserDetails(Stockid,Sku,Item,Metal,Title,Pcs,Gwt,Nwt)VALUES('"+jsonObject1.getString("stockid")+"','"+jsonObject1.getString("sku")+"','"+jsonObject1.getString("item")+"','"+jsonObject1.getString("metal")+"','"+jsonObject1.getString("title")+"','"+jsonObject1.getString("pcs")+"','"+Double.parseDouble(gwt)+"','"+(new BigDecimal(jsonObject1.getString("nwt")).setScale(3, RoundingMode.HALF_UP))+"')" );
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(SalesActivity.this,"Item Added",Toast.LENGTH_LONG).show();
                                    stockid.setText("");
                                    deleteAll.setVisibility(View.VISIBLE);
                                    amttotal.setVisibility(View.GONE);
                                    sum();
                                    displayData();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(SalesActivity.this);
        requestQueue.add(jsonObjectRequest);
    }

    //-------Search Name---------

    private void Searchnamemethord() {


    }

    public static void PincodeSet(String selectname,String id){
        name.setText(selectname);
        cid=id;

    }
    public void deleteAll() {
        db = controllerdb.getReadableDatabase();
        db.execSQL("delete from "+"UserDetails");
        db.close();
        displayData();
        sum();
    }

    public void sum(){

        db = controllerdb.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + "Gwt" + ") as Total FROM " + "UserDetails", null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndex("Total"));
            DecimalFormat df = new DecimalFormat("#.####");
            BigDecimal bd = new BigDecimal(total).setScale(3, RoundingMode.HALF_EVEN);
            total = bd.doubleValue();
            amttotal.setText("Total G WT: "+total);

            if (String.valueOf(total).equals("0.0"))
            {
                deleteAll.setVisibility(View.GONE);
                amttotal.setVisibility(View.GONE);
            }

            Log.i("dfsdfsdfs", String.valueOf(total));
           // return total;
        }

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
        Gwt.clear();
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
                Gwt.add(cursor.getString(cursor.getColumnIndex("Gwt")));
                Nwt.add(cursor.getString(cursor.getColumnIndex("Nwt")));
            } while (cursor.moveToNext());
        }
        CustomAdapter ca = new CustomAdapter(SalesActivity.this,Id, Stockid,Sku,Item,Metal,Title,Pcs,Gwt,Nwt);
        lv.setAdapter(ca);
        //code to set adapter to populate list
        cursor.close();
    }

    public void btnshow(View view) {

        if (name.getText().toString().equals(""))
        {
            name.setError("Select Name");
        }
        else
        {
        Intent intent=new Intent(SalesActivity.this,FinalSales.class);
        intent.putExtra("total",String.valueOf(total));
        intent.putExtra("clientname",name.getText().toString().trim());
        intent.putExtra("cid",cid);
        startActivity(intent);
        }
    }
}
