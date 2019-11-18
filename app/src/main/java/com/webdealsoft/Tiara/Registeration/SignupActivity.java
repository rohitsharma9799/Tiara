package com.webdealsoft.Tiara.Registeration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.webdealsoft.Tiara.R;
import com.webdealsoft.Tiara.Splash;
import com.webdealsoft.Tiara.Utility.Root_URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    EditText et_CompName,et_Email,et_Address,et_Password,et_ConPassword;
    Button bt_Submit;
    String str_CompName,str_Email,str_Address,str_Password,str_ConPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_CompName=findViewById(R.id.editTextCompany);
        et_Email=findViewById(R.id.editTextEmail);
        et_Address=findViewById(R.id.editTextAddresss);
        et_Password=findViewById(R.id.editTextPassword);
        et_ConPassword=findViewById(R.id.editTextConfrimPassword);

        bt_Submit=findViewById(R.id.buttonSignOrKonfetti);

        bt_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_CompName=et_CompName.getText().toString().trim();
                str_Email=et_Email.getText().toString().trim();
                str_Address=et_Address.getText().toString().trim();
                str_Password=et_Password.getText().toString().trim();
                str_ConPassword=et_ConPassword.getText().toString().trim();
                if (validateName(str_CompName) && validateEmail(str_Email) && validateAddress(str_Address) && validatePassword(str_Password) && cvalidatePassword(str_ConPassword))
                {
                    SignUpData();
                }
            }
        });



        BackPress();
    }
    public void LogIn(View view) {

        startActivity(new Intent(this, LoginActivity.class));
    }
    private void SignUpData() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please Wait......");
        progress.setCancelable(false);
        progress.show();

        final String URL_Product = Root_URL.SIGNUP+"name="+str_CompName+"&email="+str_Email+"&address="+str_Address+"&password="+str_Password;
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
                                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
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
    private boolean validateName(String str1) {
        if (str1.equals(""))
        {
            et_CompName.setError("Enter Your Company Name");
            return false;
        }
        return true;
    }
    private boolean validatePassword(String str4) {
        if (str4.equals("")) {
            et_Password.setError("Enter Your Password");
            return false;
        } else if (str4.length() > 32) {
            et_Password.setError("Password Maximum 32 Characters");
            return false;
        } else if (str4.length() < 6) {
            et_Password.setError("Password Minimum 6 Characters");
            return false;
        }
        return true;
    }
    private boolean cvalidatePassword(String str5) {
        if (str5.equals("")) {
            et_ConPassword.setError("Enter Your Password");
            return false;
        } else if (str5.length() > 32) {
            et_ConPassword.setError("Password Maximum 32 Characters");
            return false;
        } else if (str5.length() < 6) {
            et_ConPassword.setError("Password Minimum 6 Characters");
            return false;
        }
        else if (!str5.equals(str_Password))
        {
            et_ConPassword.setError("Password Don't Matched");
            return false;
        }
        return true;
    }
    private boolean validateAddress(String str2) {
        if (str2.equals("")) {
            et_Address.setError("Enter Your Address");
            return false;
        }
        return true;
    }
    private boolean validateEmail(String str3) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(str3);
        check = m.matches();
        if (str3.equals("")) {
            et_Email.setError("Enter Your Email Address");
            return false;
        }
        if(!check) {
            et_Email.setError("Not Valid Email");
        }
        return check;
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
}
