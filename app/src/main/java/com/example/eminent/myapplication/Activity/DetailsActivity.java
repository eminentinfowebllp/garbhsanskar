package com.example.eminent.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.MenuItem;
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
import com.example.eminent.myapplication.Adapter.ActivityAdapter;
import com.example.eminent.myapplication.Model.ActivityModel;
import com.example.eminent.myapplication.Model.Common;
import com.example.eminent.myapplication.Model.Config;
import com.example.eminent.myapplication.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class DetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ActivityAdapter activityAdapter;
    private List<ActivityModel> activityModelList;
    private TextView textViewStatus;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    private int days;
    private  String userId;

    public static final String KEY_DAY = "day";
    public static final String KEY_USERID = "user_id";
    String activity_title ;
    String activity_desc ;
    String selected_day;
    int count;

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
        setContentView(R.layout.activity_details);

        if (!isConnectd(this))
        {
            displayAlert();
        }

        sharedPreferences = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Intent intent = getIntent();
        if (intent!=null)
        {
            if (intent.getIntExtra("count",0)==0)
            {
                days = intent.getIntExtra("days",0);
                editor.putString(Config.SELECTED_DAY, String.valueOf(days)).apply();
            }else
            {
                Log.d("TAG","Description intent");
            }

        }
//        onRestart();




//        selected_day = sharedPreferences.getString(Config.SELECTED_DAY,"");


        recyclerView = (RecyclerView) findViewById(R.id.recyvlerview);
        textViewStatus = (TextView) findViewById(R.id.txtStatus);


        progressDialog = new ProgressDialog(DetailsActivity.this);

        String prgnancyDay = sharedPreferences.getString(Config.ACTUAL_DAY, "");
        userId = sharedPreferences.getString(Config.USER_ID, "");

        activityModelList = new ArrayList<>();
        /*abstractClass.languageAPICall(Integer.parseInt(dialog_pos),activityModelList);*/

        getActivityfromAPI(userId);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
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


        activityAdapter = new ActivityAdapter(DetailsActivity.this, activityModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



    }

    public void getActivityfromAPI( final String userId) {

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        final String language = sharedPreferences.getString(Config.LANGUAGE_SELECTION,"");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.BASE_URL + "API/getactivity.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        String imageArray = null;

                        try {

                            JSONObject rootObject = new JSONObject(response);
                            int successCode = rootObject.getInt("success");

                            if (successCode == 1) {

                                JSONObject childObject = rootObject.getJSONObject("activitydata");
                                JSONArray jsonArray = childObject.getJSONArray("activities");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String activity_id = jsonObject.getString("activity_id");
                                    String activity_day = jsonObject.getString("activity_day");

                                    if(language.equalsIgnoreCase("English"))
                                    {
                                        activity_title = jsonObject.getString("activity_title_eng");
                                        activity_desc = jsonObject.getString("activity_desc_eng");
                                    }
                                    else if(language.equalsIgnoreCase("Hindi"))
                                    {
                                        activity_title = jsonObject.getString("activity_title_hindi");
                                        activity_desc = jsonObject.getString("activity_desc_hindi");
                                    }
                                    else if(language.equalsIgnoreCase("Gujarati"))
                                    {
                                        activity_title = jsonObject.getString("activity_title_guj");
                                        activity_desc = jsonObject.getString("activity_desc_guj");
                                    }
                                    else
                                    {
                                        activity_title = jsonObject.getString("activity_title_eng");
                                        activity_desc = jsonObject.getString("activity_desc_eng");
                                    }

                                    String activity_video = jsonObject.getString("activity_video");
                                    String activity_status = jsonObject.getString("activity_status");
                                    String activity_added_date = jsonObject.getString("activity_added_date");
                                    String activity_audio = jsonObject.getString("activity_audio");

                                    ActivityModel model = new ActivityModel();
                                    ArrayList<String> imagList = new ArrayList<String>();

                                    if (jsonObject.has("activity_image")) {
                                        JSONArray genreArry = jsonObject.getJSONArray("activity_image");
                                        ArrayList<String> genre = new ArrayList<String>();

                                        for (int j = 0; j < genreArry.length(); j++)
                                        {
                                            genre.add((String) genreArry.get(j));
                                        }

                                        model.setActivity_image(genre);

                                    }

                                    String activity_completed = jsonObject.getString("completed");

                                    model.setActivity_id(activity_id);
                                    model.setActivity_date(activity_day);
                                    model.setActivity_title(activity_title);
                                    model.setActivity_desc(activity_desc);
                                    model.setActivity_video(activity_video);
                                    model.setActivity_status(activity_status);
                                    model.setActivity_added_date(activity_added_date);
                                    model.setActivity_completed(activity_completed);
                                    model.setActivity_audio(activity_audio);

                                    activityModelList.add(model);

                                }

                                activityAdapter = new ActivityAdapter(DetailsActivity.this, activityModelList);
                                recyclerView.setAdapter(activityAdapter);
                                activityAdapter.notifyDataSetChanged();
                            } else
                                {
                                textViewStatus.setVisibility(View.VISIBLE);
                                textViewStatus.setText("No Activities Found");
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.d("Volley_Error", error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

               String selected_day = sharedPreferences.getString(Config.SELECTED_DAY,"");


                params.put(KEY_DAY, selected_day);
                params.put(KEY_USERID, userId);
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

        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void displayAlert() {

        new AlertDialog.Builder(this).setMessage("Please check your internet connection and try again!")
                .setTitle((Html.fromHtml("<font color='#f44336'>Internet Connection Error</font>")))
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
