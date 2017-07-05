package com.example.eminent.myapplication.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cuboid.cuboidcirclebutton.CuboidButton;
import com.eminayar.panter.DialogType;
import com.eminayar.panter.PanterDialog;
import com.eminayar.panter.interfaces.OnSingleCallbackConfirmListener;
import com.example.eminent.myapplication.Model.AbstractClass;

import com.example.eminent.myapplication.Model.CheckRecentRun;
import com.example.eminent.myapplication.Model.Common;
import com.example.eminent.myapplication.Model.Config;
import com.example.eminent.myapplication.Model.DailyNotification;
import com.example.eminent.myapplication.Model.SessionManager;
import com.example.eminent.myapplication.NavigationPanel.MyActivities;
import com.example.eminent.myapplication.NavigationPanel.MyScore;
import com.example.eminent.myapplication.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    //eminentinfoweb
    private Toolbar toolbar;
    private CuboidButton buttonToday,buttonYesterday,buttonDaybfrystrday;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TextView textViewDays,textViewProfile,textViewUsername;
    private TextView homepage_title, homepage_tagline, homepage_desc;
    private ImageView homepage_image;
    private long days;
    private int total_pregnecydays;
    private String freeDemoDays;
    private int dialog_position;

    private final static String TAG = "MainActivity";
    public final static String PREFS = "PrefsFile";

    private SharedPreferences settings = null;//for 3 day notification
    private SharedPreferences.Editor editor = null;//for 3 day noification

    private SharedPreferences Dailysettings = null;//for daily notification
    private SharedPreferences.Editor Dailyeditor = null;//for daily noification

    private static String KEY_LANGUAGE_ID = "";

    private static final String KEY_USERID = "user_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_DAY = "day";

    private SessionManager sessionManager;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor userEditor;

    private String userId,string_date;
    private String homeTitle, homeTagline, homeDesc, homeImage;
    private int completedCount;
    private ProgressDialog progressDialog;
    private String thatDay_pregnency_days;

    private AbstractClass abstractClass;

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
        setContentView(R.layout.activity_home_screen);

        abstractClass = new AbstractClass(getApplicationContext());

        sessionManager = new SessionManager(getApplicationContext());

        homepage_title = (TextView) findViewById(R.id.homePageTitle);
        homepage_tagline = (TextView) findViewById(R.id.homePageTagline);
        homepage_desc = (TextView) findViewById(R.id.homePageDescription);
        homepage_image = (ImageView) findViewById(R.id.homePageImage);

        buttonToday = (CuboidButton) findViewById(R.id.buttonToday);
        buttonYesterday = (CuboidButton) findViewById(R.id.buttonYesterday);
        buttonDaybfrystrday = (CuboidButton) findViewById(R.id.buttonDaybfrystrday);

        buttonToday.setOnClickListener(this);
        buttonYesterday.setOnClickListener(this);
        buttonDaybfrystrday.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        userEditor = sharedPreferences.edit();

        userId = sharedPreferences.getString(Config.USER_ID, "");

        if (userId.isEmpty())
        {
            buttonYesterday.setEnabled(false);
            buttonDaybfrystrday.setEnabled(false);
        }
//        ------------------------------ Daily Notification ---------------------------

        progressDialog = new ProgressDialog(HomeActivity.this);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        string_date = dateFormat.format(date);

        Dailysettings = getSharedPreferences(PREFS, MODE_PRIVATE);
        Dailyeditor = Dailysettings.edit();

        if (!Dailysettings.contains("DailylastRun"))
        {
            Toast.makeText(this, "daily 1", Toast.LENGTH_SHORT).show();
            enableDailyNotification(null);
        }
        else
        {
            recordDailyRunTime();
            Toast.makeText(this, "daily 2", Toast.LENGTH_SHORT).show();

        }

        startService(new Intent(this, DailyNotification.class));

//        prepareCompletedActivityAPICall();

//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent alarmIntent = new Intent(HomeActivity.this, AlarmReceiver.class);
//        alarmIntent.putExtra("activity_completed ",completedCount);
//        System.out.println("activity_completed"+String.valueOf(completedCount));
//        int ID = (int) System.currentTimeMillis();
//        alarmIntent.putExtra("ID",ID);
//        Log.d("setRepeatedNotification", "ID:" + ID);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Calendar calendar = Calendar.getInstance();
//        Calendar now = Calendar.getInstance();
//        calendar.set(Calendar.HOUR, 23);
//        calendar.set(Calendar.MINUTE, 45);
//        calendar.set(Calendar.SECOND, 0);
//
//        //check whether the time is earlier than current time. If so, set it to tomorrow. Otherwise, all alarms for earlier time will fire
//
//        if(calendar.before(now)){
//            calendar.add(Calendar.DATE, 1);
//        }
//
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        System.out.println("intervaltime "+AlarmManager.INTERVAL_DAY);
//        System.out.println("time "+calendar.getTimeInMillis());

        //  ------------------------- 3 day notification ------------------------------


        settings = getSharedPreferences(PREFS, MODE_PRIVATE);
        editor = settings.edit();

        // First time running app?
        if (!settings.contains("lastRun"))
            enableNotification(null);
        else
            recordRunTime();

        Log.v(TAG, "Starting CheckRecentRun service...");
        startService(new Intent(this, CheckRecentRun.class));

        // ------------------------------------------------------------------------------------ //

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Setup drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        View view = nvDrawer.getHeaderView(0);

        textViewProfile = (TextView) view.findViewById(R.id.txtViewprofile);

        if (!userId.isEmpty()) {
            textViewProfile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent viewprofileIntent = new Intent(getApplicationContext(), ViewprofileActivity.class);
                    startActivity(viewprofileIntent);
                }
            });
        } else
            {
            textViewProfile.setVisibility(View.INVISIBLE);
        }


        textViewUsername = (TextView) view.findViewById(R.id.txtUsername);
        String username = sharedPreferences.getString(Config.NAME, "");
        textViewUsername.setText(username);

        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                selectDrawerItem(menuItem);
                return true;
            }
        });

        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationIcon(R.mipmap.ic_sort_black_24dp);

        textViewDays = (TextView) findViewById(R.id.dayTv);

        if (sessionManager.isLoggedIn()) {


            homeTitle = sharedPreferences.getString(Config.HOMEPAGE_TITLE, "");
            homeTagline = sharedPreferences.getString(Config.HOMEPAGE_TAGLINE, "");
            homeDesc = sharedPreferences.getString(Config.HOMEPAGE_DESC, "");
            homeImage = sharedPreferences.getString(Config.HOMEPAGE_IMAGE, "");

            thatDay_pregnency_days = sharedPreferences.getString(Config.PREGNANCY_DAY, "");


            if (!thatDay_pregnency_days.isEmpty()) {
                String day = sharedPreferences.getString(Config._DAY, "");
                String month = sharedPreferences.getString(Config._MONTH, "");
                String year = sharedPreferences.getString(Config._YEAR, "");

                Calendar thatDay = Calendar.getInstance();
                thatDay.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                thatDay.set(Calendar.MONTH, Integer.parseInt(month)); // 0-11 so 1 less
                thatDay.set(Calendar.YEAR, Integer.parseInt(year));

                Calendar today = Calendar.getInstance();

                long diff = today.getTimeInMillis() - thatDay.getTimeInMillis();

                days = diff / (24 * 60 * 60 * 1000);

                total_pregnecydays = Integer.parseInt(thatDay_pregnency_days) + (int) days;
                textViewDays.setText("Day " + total_pregnecydays);

                        if (!homeTitle.isEmpty()) {
                            homepage_title.setText(homeTitle);
                        }
                        else {
                            homepage_title.setText("Welcome to GarbhSanskar");
                        }

                         if (!homeTagline.isEmpty()) {
                             homepage_tagline.setText(homeTagline);
                         } else {
                             homepage_tagline.setText("You are going to like us");
                         }

                      if (!homeDesc.isEmpty()) {
                          homepage_desc.setText(homeDesc);
                      } else {
                          homepage_desc.setText("The Sanskrit word Garbh means foetus in the womb and Sanskar means educating the mind. So, Garbh sanskar essentially means educating the mind of the foetus. It is traditionally believed that a child\\'s mental and behavioural development starts as soon as he is conceived. His personality begins to take shape in the womb, and this can be influenced by the mother\\'s state of mind during pregnancy. This knowledge can be traced back to ancient scriptures and is included in the Ayurveda.The Sanskrit word Garbh means foetus in the womb and Sanskar means educating the mind. So, Garbh sanskar essentially means educating the mind of the foetus. It is traditionally believed that a child\\'s mental and behavioural development starts as soon as he is conceived. His personality begins to take shape in the womb, and this can be influenced by the mother\\'s state of mind during pregnancy. This knowledge can be traced back to ancient scriptures and is included in the Ayurveda.The Sanskrit word Garbh means foetus in the womb and Sanskar means educating the mind. So, Garbh sanskar essentially means educating the mind of the foetus. It is traditionally believed that a child\\'s mental and behavioural development starts as soon as he is conceived. His personality begins to take shape in the womb, and this can be influenced by the mother\\'s state of mind during pregnancy. This knowledge can be traced back to ancient scriptures and is included in the Ayurveda.");
                      }

                         if (!homeImage.isEmpty()) {
                             Glide.with(this).load(homeImage)
                                     .thumbnail(0.5f)
                                     .crossFade()
                                     .diskCacheStrategy(DiskCacheStrategy.ALL)
                                     .into(homepage_image);

                         } else
                              {
                                  homepage_image.setBackgroundResource(R.drawable.bottomback);

                              }


            }


        } else {

            Intent intent_ = getIntent();
            freeDemoDays = intent_.getStringExtra("freeDemoDay");
            textViewDays.setText("Day " + freeDemoDays);

            homepage_title.setText("Welcome to GarbhSanskar");
            homepage_tagline.setText("You are going to like us");
            homepage_desc.setText("The Sanskrit word Garbh means foetus in the womb and Sanskar means educating the mind. So, Garbh sanskar essentially means educating the mind of the foetus. It is traditionally believed that a child\\'s mental and behavioural development starts as soon as he is conceived. His personality begins to take shape in the womb, and this can be influenced by the mother\\'s state of mind during pregnancy. This knowledge can be traced back to ancient scriptures and is included in the Ayurveda.The Sanskrit word Garbh means foetus in the womb and Sanskar means educating the mind. So, Garbh sanskar essentially means educating the mind of the foetus. It is traditionally believed that a child\\'s mental and behavioural development starts as soon as he is conceived. His personality begins to take shape in the womb, and this can be influenced by the mother\\'s state of mind during pregnancy. This knowledge can be traced back to ancient scriptures and is included in the Ayurveda.The Sanskrit word Garbh means foetus in the womb and Sanskar means educating the mind. So, Garbh sanskar essentially means educating the mind of the foetus. It is traditionally believed that a child\\'s mental and behavioural development starts as soon as he is conceived. His personality begins to take shape in the womb, and this can be influenced by the mother\\'s state of mind during pregnancy. This knowledge can be traced back to ancient scriptures and is included in the Ayurveda.");
            homepage_image.setBackgroundResource(R.drawable.bottomback);

        }

        userEditor.putString(Config.ACTUAL_DAY, String.valueOf(total_pregnecydays)).apply();
//        sendNotification();

    }

    private void recordDailyRunTime() {
        Dailyeditor.putLong("DailylastRun", System.currentTimeMillis());
        Dailyeditor.commit();
    }

    private void enableDailyNotification(Object o) {
        Dailyeditor.putLong("DailylastRun", System.currentTimeMillis());
        Dailyeditor.putBoolean("enabled", true);
        Dailyeditor.commit();
        Log.v(TAG, "Notifications enabled");
    }

    private void recordRunTime() {
        editor.putLong("lastRun", System.currentTimeMillis());
        editor.commit();
    }

    private void enableNotification(Object o) {
        editor.putLong("lastRun", System.currentTimeMillis());
        editor.putBoolean("enabled", true);
        editor.commit();
        Log.v(TAG, "Notifications enabled");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked

        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:

                if (!isConnectd(HomeActivity.this))
                {
                    displayAlert();

                }else
                {
                    if (userId.isEmpty())
                    {
                        Toast.makeText(this, "Register with us", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Intent intent = new Intent(getApplicationContext(), MyActivities.class);
                        startActivity(intent);

                    }

                }
                break;

            case R.id.nav_second_fragment:

                if (!isConnectd(HomeActivity.this))
                {
                    displayAlert();

                }else
                {
                    if (userId.isEmpty())
                    {
                        Toast.makeText(this, "Register with us", Toast.LENGTH_SHORT).show();

                    }else
                    {
                        Intent intent1 = new Intent(getApplicationContext(), MyScore.class);
                        startActivity(intent1);
                    }

                }
                break;

            case R.id.sub_item_1:
                break;
            case R.id.sub_item_2:

                if (!isConnectd(HomeActivity.this))
                {
                    displayAlert();

                }else if (userId.isEmpty())
                {
                    Toast.makeText(this, "Register with us", Toast.LENGTH_SHORT).show();
                }else
                {
                    //languageAPICall(pos);
                    sessionManager.logoutUser();
                    stopService(new Intent(this,  DailyNotification.class));
                    sharedPreferences = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
                    userEditor = sharedPreferences.edit();
                    userEditor.clear();
                    userEditor.commit();
                    finish();

                }
                break;

            default:
        }

        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.langSelection:
                showdialog();
                break;
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
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

    public void showdialog(){

        new PanterDialog(this)
                .setHeaderBackground(R.color.colorPrimary)
                .setDialogType(DialogType.SINGLECHOICE)
                .isCancelable(true)
                .setTitle("Select Language")
                .isCancelable(false)
                .items(R.array.dialog_choices, new OnSingleCallbackConfirmListener() {

                    @Override
                    public void onSingleCallbackConfirmed(PanterDialog dialog, int pos, String text) {

                        if (!isConnectd(HomeActivity.this))
                        {
                            displayAlert();

                        }else if (userId.isEmpty())
                        {
                            Toast.makeText(HomeActivity.this, "Register with us", Toast.LENGTH_SHORT).show();
                            //languageAPICall(pos);
                        }else
                        {
                            dialog_position = pos;
                            Toast.makeText(HomeActivity.this, "position : " + String.valueOf(dialog_position) +
                                        " value = " + text,
                                Toast.LENGTH_LONG).show();
                            userEditor.putString(Config.DIALOG_POSITION,String.valueOf(dialog_position));
                        }

                    }

                })
                .show();
    }

    @Override
    public void onClick(View viewAction) {

        if (!isConnectd(this)) {
            displayAlert();
        }
         else if(viewAction == buttonToday)
        {
            Intent intent = Common.createIntent(getApplicationContext(),DetailsActivity.class);
            if (userId.isEmpty())
            {
                intent.putExtra("days", Integer.parseInt(freeDemoDays));
            }else
            {
                intent.putExtra("days", total_pregnecydays);
            }
            startActivity(intent);

        }

        else if (viewAction == buttonYesterday)

        {
            if (userId.isEmpty())
            {
                Toast.makeText(this, "Register with us", Toast.LENGTH_SHORT).show();
            }else
            {
                Intent intent = Common.createIntent(getApplicationContext(),DetailsActivity.class);
                intent.putExtra("days", total_pregnecydays-1);
                startActivity(intent);
            }

        }

        else if (viewAction == buttonDaybfrystrday)
        {
            if (userId.isEmpty())
            {
                Toast.makeText(this, "Register with us", Toast.LENGTH_SHORT).show();

            }else {
                Intent intent = Common.createIntent(getApplicationContext(),DetailsActivity.class);
                intent.putExtra("days", total_pregnecydays-2);
                startActivity(intent);
            }


        }
    }


    private void sendNotification() {

//        SharedPreferences dailynoti = getSharedPreferences(PREFS, MODE_PRIVATE);
//        editor = dailynoti.edit();
//
//        // First time running app?
//        if (!dailynoti.contains("lastRun"))
//            enableNotification(null);
//        else
//            recordRunTime();
//
//        Log.v(TAG, "Starting CheckRecentRun service...");



        if (!userId.isEmpty())
        startService(new Intent(this,  DailyNotification.class));

//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent alarmIntent = new Intent(HomeActivity.this, AlarmReceiver.class);
//
//        System.out.println("activity_completed"+String.valueOf(completedCount));
//
//        int ID = (int) System.currentTimeMillis();
////        alarmIntent.putExtra("ID",ID);
//
//        Log.d("setRepeatedNotification", "ID:" + ID);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        userEditor.putString(Config.DAILY_NOTI_COUNT, String.valueOf(completedCount)).apply();
//        userEditor.putString(Config.DAILY_NOTI_ID, String.valueOf(ID)).apply();
//
//        Calendar calendar = Calendar.getInstance();
//        Calendar now = Calendar.getInstance();
//        calendar.set(Calendar.HOUR, 23);
//        calendar.set(Calendar.MINUTE, 45);
//        calendar.set(Calendar.SECOND, 0);
//
//        //check whether the time is earlier than current time. If so, set it to tomorrow. Otherwise, all alarms for earlier time will fire
//
//        if(calendar.before(now)){
//            calendar.add(Calendar.DATE, 1);
//        }
//
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        System.out.println("intervaltime "+AlarmManager.INTERVAL_DAY);
//        System.out.println("time "+calendar.getTimeInMillis());
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
