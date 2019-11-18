package com.webdealsoft.Tiara.Registeration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webdealsoft.Tiara.Activity.Home;
import com.webdealsoft.Tiara.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.webdealsoft.Tiara.Utility.Root_URL.BASE_URL;
import static com.webdealsoft.Tiara.Utility.Root_URL.LOGIN;

public class LoginActivity extends AppCompatActivity {
    AppCompatDialog progressDialog;
    String passe, Email, usename;
    String regId;
    private CheckBox show_hide_password;
    TextView register,forgot;
    String PREFS_NAME = "auth_info";
    EditText EditTextEmail, txtpassword, txtmobile;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_mainn);

        EditTextEmail = findViewById(R.id.editTextEmail);
        txtpassword = findViewById(R.id.editTextPassword);
        forgot=findViewById(R.id.forgotpass);
        show_hide_password=findViewById(R.id.password_visible);
        show_hide_password .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton button,
                                         boolean isChecked) {
                if (isChecked) {

                    txtpassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    txtpassword.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                } else {
                    show_hide_password.setText("Show Password");// change
                    txtpassword.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtpassword.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());

                }

            }
        });

    }
    public void Register(View view) {

 startActivity(new Intent(this, SignupActivity.class));
    }
    public void loginbtn(View view) {
       Email = EditTextEmail.getText().toString();
        passe = txtpassword.getText().toString();
               /* Intent intent=new Intent(Loginscreen.this,OTP_Verification.cclass);
                startActivity(intent);
                finish();*/
        if (validateEmail(Email)) {
            login();
        }

       // startActivity(new Intent(this, Home.class));
    }
    private void login() {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please Wait......");
        progress.setCancelable(false);
        progress.show();

        final String URL_PRODUCTS = BASE_URL+LOGIN + "?email=" + Email +"&password=" + passe;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("agdskjajk",URL_PRODUCTS);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject responseObj = jsonObject.getJSONObject("Response");
                            if (jsonObject.getString("Flag").equals("1")) {
                                progress.dismiss();
                                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("companyid",responseObj.getString("companyid"));
                                editor.putString("name",responseObj.getString("name"));
                                editor.putString("userid",responseObj.getString("userid"));
                                editor.putString("email",responseObj.getString("email"));
                                editor.putString("companyname",responseObj.getString("companyname"));
                                editor.putString("companylogo",responseObj.getString("logo"));
                                editor.commit();
                                startActivity(new Intent(LoginActivity.this, Home.class));
                                Toast.makeText(LoginActivity.this,"User Logged Successfully", Toast.LENGTH_SHORT).show();

                            } else if (jsonObject.getString("Flag").equals("0")) {
                                progress.dismiss();
                                Toast.makeText(LoginActivity.this, "Username and Password is incorrect", Toast.LENGTH_SHORT).show();
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
    private boolean validatePassword(String string) {
        //Validating the entered PASSWORD
        if (string.equals("")) {
            txtpassword.setError("Enter Your Password");
            return false;
        } else if (string.length() > 4) {
            txtpassword.setError("Maximum 32 Characters");
            return false;
        } else if (string.length() < 4) {
            txtpassword.setError("Minimum 6 Characters");
            return false;
        }
        return true;
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
