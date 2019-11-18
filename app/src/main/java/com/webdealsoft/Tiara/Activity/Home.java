package com.webdealsoft.Tiara.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.webdealsoft.Tiara.Activity.Modelclass.SilderData;
import com.webdealsoft.Tiara.Activity.QR.QRActivity;
import com.webdealsoft.Tiara.Activity.Salesadapter.SliderLargeAdapter;
import com.webdealsoft.Tiara.R;
import com.webdealsoft.Tiara.Registeration.LoginActivity;
import com.webdealsoft.Tiara.Registeration.SignupActivity;
import com.webdealsoft.Tiara.Utility.Root_URL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    String PREFS_NAME = "auth_info";
    private SliderLayout mDemoSlider;
    private ViewPager viewPager, viewPager2;
    private TabLayout indicator, indicator2;
    private List<Integer> color;
    private List<SilderData>adsLargeDataList=new ArrayList<>();
    SharedPreferences sharedPreferences;
    ImageView nav_Logo;
    //activityhome
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
        setContentView(R.layout.activity_home);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(sharedPreferences.getString("companyname",""));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        nav_Logo=header.findViewById(R.id.imageView);
        if (sharedPreferences.getString("companylogo","").equals("")) {

        }
        else {
            Picasso.with(getApplicationContext()).load(sharedPreferences.getString("companylogo","")).into(nav_Logo);
        }

        navigationView.setNavigationItemSelectedListener(this);

        viewPager=findViewById(R.id.viewPager1);
        indicator=findViewById(R.id.indicator1);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 2000, 3000);

        //AdsLarge();




    }

    private void AdsLarge() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String URL_Product = Root_URL.SILDER+"companyid="+sharedPreferences.getString("companyid","");
        StringRequest request = new StringRequest(Request.Method.GET, URL_Product, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("flag").equals("1"))
                    {
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            SilderData id = new SilderData();
                            id.setImage(obj.getString("image"));
                            adsLargeDataList.add(id);
                        }

                        viewPager.setAdapter(new SliderLargeAdapter(getApplicationContext(), adsLargeDataList));
                        indicator.setupWithViewPager(viewPager, true);
                    }
                    else
                    {
                    }
                }
                catch (Exception ex)
                {
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.adduser ){
            startActivity(new Intent(this, adduserActivity.class));
        } else if (id == R.id.report) {
            startActivity(new Intent(this, Receipt.class));
        } else if (id == R.id.seals) {
            startActivity(new Intent(this, SalesActivity.class));
        } else if (id == R.id.contatus) {
            startActivity(new Intent(this, contactus.class));
        } else if (id == R.id.help) {
            startActivity(new Intent(this, about.class));
        } else if (id == R.id.Logout) {
            logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logoutUser() {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Do you want to logout?")
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.apply();
                                    editor.commit();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }).setNegativeButton("No", null).show();


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            if (getApplicationContext() != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < adsLargeDataList.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }
    }
    public void Saleshomebtn(View view) {
        startActivity(new Intent(this, SalesActivity.class));
    }
    public void Contactusbtn(View view) {
        startActivity(new Intent(this, contactus.class));
    }
    public void Aboutusbtn(View view) {
        startActivity(new Intent(this, about.class));
    }
    public void receiptbtn(View view) {
        startActivity(new Intent(this, Receipt.class));
    }
    public void UserAdd(View view) {
        startActivity(new Intent(this, adduserActivity.class));
    }
    public void qrcode(View view) {
        startActivity(new Intent(this, QRActivity.class));
    }
    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(Home.this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }
    @Override
    public void onPageScrollStateChanged(int state) {}

}
