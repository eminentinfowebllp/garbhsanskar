package com.example.eminent.myapplication.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eminent.myapplication.Adapter.SliderAdapter;
import com.example.eminent.myapplication.Model.Common;
import com.example.eminent.myapplication.Model.Config;
import com.example.eminent.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DescriptionActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private Button buttonComplete;
    private WebView webView;
    private ProgressDialog progressDialog, initialProgressdialog;
    private String activity_id,user_id,activity_completed;
    private ArrayList<String> activity_image;
    private SharedPreferences sharedPreferences;
    private VideoView videoview;
    private ViewPager acvitivityImageView;
    private SliderAdapter sliderAdapter;
    private LinearLayout ll_dots;
    private TextView[] dots;
    private RelativeLayout sliderLayout,videoLayout;
    private ImageView videoPlayIcon, videoImg;
    private TextView activityTitle;

    public static final String KEY_USERID = "user_id";
    public static final String KEY_ACTIVITYID = "activity_id";

    public ProgressBar progressBar;
    private String activity_videoURL,activity_title;

    // = "https://ak9.picdn.net/shutterstock/videos/14634679/preview/stock-footage-pregnant-woman-reading-a-book.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        sharedPreferences = getSharedPreferences(Config.PREF_NAME,MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.webviewDetails);
        buttonComplete = (Button) findViewById(R.id.completebtn);
        videoview = (VideoView) findViewById(R.id.vView);
        acvitivityImageView = (ViewPager) findViewById(R.id.activityImage);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        sliderLayout = (RelativeLayout) findViewById(R.id.sliderLayout);
        videoLayout = (RelativeLayout) findViewById(R.id.RelativeVideoLayout);
//        videoImg = (ImageView) findViewById(R.id.imgVideoImage);
        videoPlayIcon = (ImageView) findViewById(R.id.imgVideoPlayIcon);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        activityTitle = (TextView) findViewById(R.id.activityTitleTv);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        progressDialog = new ProgressDialog(this);
        initialProgressdialog = new ProgressDialog(this);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(R.drawable.ic_back);

        Intent intent = getIntent();

        if(intent!=null)
        {

            initialProgressdialog.show();

            String webviewContent = intent.getStringExtra("activity_desc");
            activity_id = intent.getStringExtra("activity_id");
            user_id = sharedPreferences.getString(Config.USER_ID,"");
            activity_completed = intent.getStringExtra("activity_completed");
            activity_image = intent.getStringArrayListExtra("activity_image");
            activity_videoURL = intent.getStringExtra("activity_video");
            activity_title = intent.getStringExtra("activity_title");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(webviewContent, "text/html", "UTF-8");
            System.out.println("imageAct "+activity_image);
            initialProgressdialog.dismiss();

        }

        if (activity_title!=null)
        {
            activityTitle.setText(activity_title);
        }

        if (activity_image!=null)
        {
            sliderLayout.setVisibility(View.VISIBLE);
            acvitivityImageView.setVisibility(View.VISIBLE);
            ll_dots.setVisibility(View.VISIBLE);
            sliderAdapter = new SliderAdapter(DescriptionActivity.this, activity_image);
            acvitivityImageView.setAdapter(sliderAdapter);
            addBottomDots(0);

            acvitivityImageView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    addBottomDots(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }

            });
        }


        if (!activity_videoURL.isEmpty())
        {

            videoview.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
//            videoImg.setVisibility(View.VISIBLE);
            videoPlayIcon.setVisibility(View.VISIBLE);

//            Glide.with(getApplicationContext())
//                    .load(activity_videoURL).asBitmap().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
//                    .into(videoImg);

            try {
                // Start the MediaController
                MediaController mediacontroller = new MediaController(DescriptionActivity.this);
                mediacontroller.setAnchorView(videoview);
                Uri video = Uri.parse(activity_videoURL);
                videoview.setMediaController(mediacontroller);
                videoview.setVideoURI(video);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            videoview.requestFocus();
            videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    progressBar.setVisibility(View.GONE);
                    mp.start();
                    videoview.start();

                }
            });

        }


        if(activity_completed.equalsIgnoreCase("1"))
        {
            buttonComplete.setText("Completed");
            buttonComplete.setBackgroundColor((getResources().getColor(R.color.divider_color)));
            buttonComplete.setEnabled(false);
        }

        videoPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),VideoActivity.class);
                intent1.putExtra("activity_video",activity_videoURL);
                startActivity(intent1);
            }
        });

        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                callCompleteAPI();

            }
        });



    }

    private void callCompleteAPI()
    {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.BASE_URL + "API/complete-activity.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int successCode = jsonObject.getInt("success");
                            if(successCode == 1)
                            {
                                String message = jsonObject.getString("message");
                                Toast.makeText(DescriptionActivity.this, message, Toast.LENGTH_SHORT).show();
                                buttonComplete.setText("Completed");
                                buttonComplete.setBackgroundColor((getResources().getColor(R.color.divider_color)));
                                buttonComplete.setEnabled(false);
                            }
                            else
                            {
                                String message = jsonObject.getString("message");
                                Toast.makeText(DescriptionActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley_Error",error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                System.out.println("activityId "+activity_id);
                params.put(KEY_USERID,user_id);
                params.put(KEY_ACTIVITYID,activity_id);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[activity_image.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#9E9E9E"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#f44336"));
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
    }

}
