package com.example.eminent.myapplication.NavigationPanel;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
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
import com.example.eminent.myapplication.Adapter.ActivityAdapter;
import com.example.eminent.myapplication.Adapter.MyActivitiesAdapter;
import com.example.eminent.myapplication.Model.ActivityModel;
import com.example.eminent.myapplication.Model.Common;
import com.example.eminent.myapplication.Model.Config;
import com.example.eminent.myapplication.Model.MyActivitiesModel;
import com.example.eminent.myapplication.R;
import com.github.lzyzsd.circleprogress.ArcProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyActivities extends AppCompatActivity {


    private RecyclerView recyclerView;
    MyActivitiesAdapter activityAdapter;
    private List<MyActivitiesModel> activityModelList;
    private ProgressDialog progressDialog;
    private String userId;

    private static final String KEY_USERID = "user_id" ;

    private SharedPreferences sharedPreferences;

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
        setContentView(R.layout.activity_my_activities);

        sharedPreferences = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        userId = sharedPreferences.getString(Config.USER_ID, "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(R.mipmap.ic_reply_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(MyActivities.this);

        recyclerView = (RecyclerView) findViewById(R.id.recyvlerview);

        activityModelList = new ArrayList<>();

        prepareAPICall();
        if (!isConnectd(this)) {
            displayAlert();
        }
        else
        {
            prepareAPICall();
        }

//        prepareActivityData();
        activityAdapter = new MyActivitiesAdapter(this,activityModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyActivities.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(activityAdapter);
    }

    private void prepareAPICall() {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.BASE_URL + "API/getactivityscore.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        System.out.println("scoreResponse " + s);

                        try {
                            JSONObject jsonRootObject = new JSONObject(s);

                            int successCode = jsonRootObject.getInt("success");

                            if (successCode == 1)
                            {
                                JSONObject activity = jsonRootObject.getJSONObject("activity");

                                for (int i = 1; i<=activity.length(); i++)
                                {
                                    JSONObject jsonObject_activity1 = activity.getJSONObject(String.valueOf(i));
                                    System.out.println("jsonObject "+jsonObject_activity1);
                                    String completed_activity = jsonObject_activity1.getString("completed");
                                    String total_activity = jsonObject_activity1.getString("total");
                                    System.out.println("completed_activity "+completed_activity);
                                    System.out.println("total_activity "+total_activity);

                                    MyActivitiesModel model = new MyActivitiesModel();
                                    model.setCompleted_activities(completed_activity);
                                    model.setTotal_activities(total_activity);
                                    model.setActivities(String.valueOf(i));

                                    activityModelList.add(model);
                                    activityAdapter = new MyActivitiesAdapter(MyActivities.this, activityModelList);
                                    recyclerView.setAdapter(activityAdapter);
                                    activityAdapter.notifyDataSetChanged();
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("activityError "+e.getMessage());

                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                progressDialog.dismiss();
                Log.d("Volley_Error", volleyError.toString());
                System.out.println("Volley_Error " + volleyError.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_USERID, userId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
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
