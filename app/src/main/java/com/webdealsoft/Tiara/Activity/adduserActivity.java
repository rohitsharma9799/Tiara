package com.webdealsoft.Tiara.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webdealsoft.Tiara.R;
import com.webdealsoft.Tiara.Registeration.SignupActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.webdealsoft.Tiara.Utility.Root_URL.ADDUSER;
import static com.webdealsoft.Tiara.Utility.Root_URL.BASE_URL;
import static com.webdealsoft.Tiara.Utility.Root_URL.LOGIN;

public class adduserActivity extends AppCompatActivity {
    AppCompatDialog progressDialog;
    String passe, Email, usename;
    String companyid,userid,name,company,mobileno,email;
    private CheckBox show_hide_password;
    TextView register,forgot;
    String PREFS_NAME = "auth_info";
    ImageView back;
    EditText EditTextEmail,EditTextuserid,Edittextcompany,Edittextname,Edittextcompid,Edittextmob;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    SharedPreferences sharedPreferences;
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
        setContentView(R.layout.activity_adduse);
        back=findViewById(R.id.feback);
        EditTextEmail = findViewById(R.id.email);
        Edittextcompany = findViewById(R.id.company);
        Edittextname = findViewById(R.id.name);
        Edittextmob = findViewById(R.id.mobileno);

        back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onBackPressed();
    }
});
        }
    public void Register(View view) {
            startActivity(new Intent(this, SignupActivity.class));
    }
    public void loginbtn(View view) {
        email = EditTextEmail.getText().toString().replaceAll(" ","%20");
        company = Edittextcompany.getText().toString().replaceAll(" ","%20");
        name = Edittextname.getText().toString().replaceAll(" ","%20");
        mobileno = Edittextmob.getText().toString().replaceAll(" ","%20");
        if (validateEmail(email)) {
            login();
        }

       // startActivity(new Intent(this, Home.class));
    }
    private void login() {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please Wait......");
        progress.setCancelable(false);
        progress.show();

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String URL_PRODUCTS = BASE_URL+ADDUSER + "?email=" +email+"&companyid="+sharedPreferences.getString("companyid","")+"&userid="+sharedPreferences.getString("userid","")+"&name="+ name+"&company=" +company+"&mobileno="+mobileno;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS.replaceAll(" ","%20"),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("adduserid",URL_PRODUCTS);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("Flag").equals("1")) {
                                Toast.makeText(adduserActivity.this,jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                onBackPressed();


                            } else if (jsonObject.getString("Flag").equals("0")) {
                                progress.dismiss();
                                Toast.makeText(adduserActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                                EditTextEmail.setText("");
                                Edittextcompany.setText("");
                                Edittextname.setText("");
                                Edittextmob.setText("");
                            } else  {
                                // Toast.makeText(OTP_Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        //      progressbar.setVisibility(View.GONE);
                        progress.dismiss();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack());
        requestQueue.add(stringRequest).setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0; //retry turn off
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }
    private boolean validateEmail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();
        if (email.equals("")) {
           EditTextEmail.setError("Enter Your Email Address");
            return false;
        }
        if(!check) {
            EditTextEmail.setError("Not Valid Email");
        }
        return check;
    }

}
