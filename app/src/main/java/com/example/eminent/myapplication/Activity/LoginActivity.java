package com.example.eminent.myapplication.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.eminent.myapplication.Model.ApiResponse;
import com.example.eminent.myapplication.Model.Config;
import com.example.eminent.myapplication.Model.SessionManager;
import com.example.eminent.myapplication.R;
import com.example.eminent.myapplication.Retrofit.APIService;
import com.example.eminent.myapplication.Retrofit.ApiClient;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private Button joinnowButton, freeDemoButton;
    private EditText edittextId;
    private Toolbar toolbar;
    private String id = "1234";
    private String string_date;

    private int currentNotificationID = 0;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    private Bitmap icon;

    private SessionManager session;
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;

    private ProgressDialog progressDialog;

    public static boolean isConnectd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        sharedPreferences = LoginActivity.this.getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog = new ProgressDialog(this);

        joinnowButton = (Button) findViewById(R.id.joinnowButton);
        freeDemoButton = (Button) findViewById(R.id.demoButton);
        edittextId = (EditText) findViewById(R.id.codeEdittext);

        joinnowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                login();
            }
        });

        freeDemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oncelogin();
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);
    }

    private void oncelogin() {

    }

    private void login() {

        if (validate() == false) {
//            onLoginFailed();
            return;
        }

        //_loginButton.setEnabled(false);

        if (!isConnectd(this)) {
            displayAlert();
        }else {
            loginByServer();
        }


    }

    private void onLoginFailed() {
        Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show();
    }

    private void loginByServer() {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String unniqueId = edittextId.getText().toString();

        APIService service = ApiClient.getClient().create(APIService.class);

        service.userLogIn(unniqueId).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                int success = response.body().getSuccess();
                progressDialog.dismiss();
                if (success == 1) {
                    //successfull
                    String message = response.body().getMessage();
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                    numPickDialog();
//                    datePickerDialog();
                    session.createLoginSession();

                    String user_id = response.body().getUser_id();
                    String user_email = response.body().getUser_email();
                    String user_address = response.body().getUser_address();
                    String user_city = response.body().getUser_city();
                    String user_state = response.body().getUser_state();
                    String user_name = response.body().getUser_name();
                    String user_country = response.body().getUser_country();
                    String user_contactno = response.body().getUser_contactno();
                    String homepage_title = response.body().getHomepage_title();
                    String homepage_tagline = response.body().getHomepage_tagline();
                    String homepage_desc = response.body().getHomepage_desc();
                    String homepage_image = response.body().getHomepage_image();

                    editor.putString(Config.USER_ID, user_id);
                    editor.putString(Config.NAME, user_name);
                    editor.putString(Config.ADDRESS, user_address);
                    editor.putString(Config.CITY, user_city);
                    editor.putString(Config.COUNTRY, user_country);
                    editor.putString(Config.PHONE, user_contactno);
                    editor.putString(Config.STATE, user_state);
                    editor.putString(Config.EMAIL, user_email);
                    editor.putString(Config.HOMEPAGE_TITLE, homepage_title);
                    editor.putString(Config.HOMEPAGE_TAGLINE, homepage_tagline);
                    editor.putString(Config.HOMEPAGE_DESC, homepage_desc);
                    editor.putString(Config.HOMEPAGE_IMAGE, homepage_image);

                    editor.commit();

                } else {
                    String message = response.body().getMessage();
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressDialog.dismiss();

                System.out.println("callapi " + call);
                System.out.println("throwble " + t);
            }
        });


    }

    private void numPickDialog() {

        final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(LoginActivity.this)
                .minValue(1)
                .maxValue(280)
                .defaultValue(1)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.DKGRAY)
                .textColor(Color.RED)
                .textSize(20)
                .enableFocusability(true)
                .wrapSelectorWheel(true)
                .build();

        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Enter Your Pregnancy Day")
                .setView(numberPicker)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setDataForSimpleNotification();

                        Intent navigateIntent = new Intent(getApplicationContext(),HomeActivity.class);
                        startActivity(navigateIntent);
                        finish();

                        Toast.makeText(LoginActivity.this, "Welcome to Garbhsanskar", Toast.LENGTH_SHORT).show();
                        System.out.println("numpick "+numberPicker.getValue());
                        editor.putString(Config.PREGNANCY_DAY, String.valueOf(numberPicker.getValue())).apply();

                        Date theDate = new Date();

                        Calendar myCal = new GregorianCalendar();
                        myCal.setTime(theDate);

                        String day = String.valueOf(myCal.get(Calendar.DAY_OF_MONTH));
                        String month = String.valueOf(myCal.get(Calendar.MONTH));
                        String year = String.valueOf(myCal.get(Calendar.YEAR));
                        System.out.println("Day: " + day);
                        System.out.println("Month: " + month);
                        System.out.println("Year: " + year);

                        editor.putString(Config._DAY, day).apply();
                        editor.putString(Config._MONTH, month).apply();
                        editor.putString(Config._YEAR, year).apply();

                    }
                })
                .show();
    }

    public boolean validate() {
        boolean valid = true;

        String uniqueid = edittextId.getText().toString();

        if (uniqueid.isEmpty()) {
            edittextId.setError("enter a unique id");
            requestFocus(edittextId);
            valid = false;
        } else {
            edittextId.setError(null);
        }

        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void setDataForSimpleNotification() {

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle("GarbhSanskar")
                .setContentText("Welcome to Garbhsanskar");
        sendNotification();
        
    }

    private void sendNotification() {

        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        icon = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_launcher);

        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        currentNotificationID++;
        int notificationId = currentNotificationID;
        if (notificationId == Integer.MAX_VALUE - 1)
            notificationId = 0;
        notificationManager.notify(notificationId, notification);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        finish();
    }


    public void displayAlert() {

        new AlertDialog.Builder(this).setMessage("Please check your internet connection and try again!")
                .setTitle((Html.fromHtml("<font color='#F52887'>Internet Connection Error</font>")))
                .setCancelable(true)
                .setNeutralButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                .show();
    }


}
