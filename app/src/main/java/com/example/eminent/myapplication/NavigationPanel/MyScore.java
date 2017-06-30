package com.example.eminent.myapplication.NavigationPanel;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String KEY_DATE = "date";

    private String string_date;
    private ProgressDialog progressDialog;
    private TextView totalActivityies, completedActvities, myscore;
    private int completedCount,totalCount;
    private MyActivitiesModel myActivitiesModel;
    private ArcProgress arcProgress;
    private String userId;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);

        sharedPreferences = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getString(Config.USER_ID, "");

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


    }

    private void prepareAPICall() {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.BASE_URL + "API/completedactivitiesofdate.php",
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

                                     completedCount = jsonArray.getInt(0);
                                     totalCount = jsonArray.getInt(1);

                                     myActivitiesModel.setCompleted_activity_score(completedCount);

                                     completedActvities.setText(String.valueOf(completedCount));
                                     myscore.setText(String.valueOf(completedCount)+" / " +"10");
                                     arcProgress.setProgress(completedCount);



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
                params.put(KEY_USERID, userId);
                params.put(KEY_DATE, string_date);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
