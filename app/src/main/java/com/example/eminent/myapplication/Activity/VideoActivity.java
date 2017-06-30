package com.example.eminent.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.eminent.myapplication.R;


public class VideoActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private Toolbar toolbar;
    private String VideoURL = "https://ak9.picdn.net/shutterstock/videos/14634679/preview/stock-footage-pregnant-woman-reading-a-book.mp4";
    private Button button;
    private String VideoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        VideoUrl = intent.getStringExtra("activity_video");

        System.out.println("VidURL "+VideoUrl);

        final VideoView videoview = (VideoView) findViewById(R.id.vView);

        pDialog = new ProgressDialog(VideoActivity.this);
        pDialog.setTitle("Please wait");
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    VideoActivity.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(VideoURL);
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
                pDialog.dismiss();
                videoview.start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
