package com.example.eminent.myapplication.NavigationPanel;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eminent.myapplication.Activity.DetailsActivity;
import com.example.eminent.myapplication.Model.Common;
import com.example.eminent.myapplication.Model.Config;
import com.example.eminent.myapplication.Model.MyActivitiesModel;
import com.example.eminent.myapplication.R;
import com.github.lzyzsd.circleprogress.ArcProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MyScore extends AppCompatActivity {

    private static final String KEY_USERID = "user_id";
    private static final String KEY_DAY = "day";

    private String string_date;
    private ProgressDialog progressDialog;
    private TextView totalActivityies, completedActvities, myscore;
    private int completedCount,totalCount;
    private MyActivitiesModel myActivitiesModel;
    private ArcProgress arcProgress;
    private String userId, pregnancy_day;

    private SharedPreferences sharedPreferences;

    private int activitiesCount;


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
        setContentView(R.layout.activity_my_score);

        sharedPreferences = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getString(Config.USER_ID, "");
        pregnancy_day = sharedPreferences.getString(Config.ACTUAL_DAY, "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(R.mipmap.ic_reply_white_24dp);
        progressDialog = new ProgressDialog(MyScore.this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        myActivitiesModel = new MyActivitiesModel();

        totalActivityies = (TextView) findViewById(R.id.totalActivitesTv);
        completedActvities = (TextView) findViewById(R.id.completedActivitiesTv);
        myscore = (TextView) findViewById(R.id.myScoreTv);
        arcProgress = (ArcProgress) findViewById(R.id.arc_progress);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        string_date = dateFormat.format(date);
        System.out.println("Date " + string_date);

        prepareAPICall();

        if (!isConnectd(this))
        {
            displayAlert();
        }
        else
        {
            prepareAPICall();
        }


    }

    private void prepareAPICall() {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.BASE_URL + "API/completedactivitiesofday.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        progressDialog.dismiss();

                        try {

                            JSONObject jsonObject = new JSONObject(s);

                            int successCode = jsonObject.getInt("success");

                            if (successCode == 1)
                            {
                                if (jsonObject.has("activity_number")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("activity_number");

                                    int completed_activity = jsonArray.length();

                                     System.out.println("arrayLen "+completed_activity);
                                     completedActvities.setText(String.valueOf(completed_activity));
                                     myscore.setText(String.valueOf(completed_activity)+" / " +"10");
                                     arcProgress.setProgress(completed_activity);

                                    activitiesCount = jsonArray.length();

                                     completedActvities.setText(String.valueOf(activitiesCount));
                                     myscore.setText(String.valueOf(activitiesCount)+" / " +"10");
                                     arcProgress.setProgress(activitiesCount);

                                }
                                else
                                {
                                    String message = jsonObject.getString("message");
                                    System.out.println("arrayData "+message);

                                    completedActvities.setText(String.valueOf("0"));
                                    myscore.setText("0/10");
                                    arcProgress.setProgress(0);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("errorMess "+ e.getMessage());
                        }

                    }
                },


                new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                progressDialog.dismiss();
                System.out.println("volleyerrorMess "+ volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                System.out.println("pregnencyDAY "+pregnancy_day);
                params.put(KEY_USERID, userId);
                params.put(KEY_DAY, pregnancy_day);

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
