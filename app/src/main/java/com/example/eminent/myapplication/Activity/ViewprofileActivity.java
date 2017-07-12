package com.example.eminent.myapplication.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eminent.myapplication.Model.Config;
import com.example.eminent.myapplication.R;

import java.util.ArrayList;


public class ViewprofileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textViewName,textViewEmai,textViewMobile,textViewCity,textViewAddress;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);

        sharedPreferences = getSharedPreferences(Config.PREF_NAME,MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        textViewName = (TextView) findViewById(R.id.txtName);
        textViewEmai = (TextView) findViewById(R.id.txtEmail);
        textViewMobile = (TextView) findViewById(R.id.txtPhone);
        textViewCity = (TextView) findViewById(R.id.txtCity);
        textViewAddress = (TextView) findViewById(R.id.txtAddress);

        String name = sharedPreferences.getString(Config.NAME,"");
        textViewName.setText(name);

        String email = sharedPreferences.getString(Config.EMAIL,"");
        textViewEmai.setText(email);

        String mobile = sharedPreferences.getString(Config.PHONE,"");
        textViewMobile.setText(mobile);

        String city = sharedPreferences.getString(Config.CITY,"");
        textViewCity.setText(city);

        String Address = sharedPreferences.getString(Config.ADDRESS,"");
        textViewAddress.setText(Address);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(R.mipmap.ic_reply_grey600_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
