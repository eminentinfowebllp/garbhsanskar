package com.example.eminent.myapplication.Model;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eminent.myapplication.Activity.HomeActivity;
import com.example.eminent.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eminent on 6/29/2017.
 */

public class DailyNotification extends Service {

    private final static String TAG = "DailyNotification";
    private static Long MILLISECS_PER_DAY = 86400000L;

    private static long delay = MILLISECS_PER_DAY;

    private int completedCount;
    private String userId;
    private String pregnancy_day;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor userEditor;

    private static final String KEY_USERID = "user_id";
    private static final String KEY_DAY = "day";

    private SessionManager sessionManager;

    @Override
    public void onCreate() {
        super.onCreate();

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        userEditor = sharedPreferences.edit();
        userId = sharedPreferences.getString(Config.USER_ID, "");
        pregnancy_day = sharedPreferences.getString(Config.ACTUAL_DAY, "");

        SharedPreferences settings = getSharedPreferences(HomeActivity.PREFS, MODE_PRIVATE);

        // Are notifications enabled?
        if (settings.getBoolean("enabled", true)) {
            // Is it time for a notification?
            if (settings.getLong("DailylastRun", Long.MAX_VALUE) < System.currentTimeMillis() - delay)

                if (!userId.isEmpty()) {

                    userEditor.remove(Config.DAILY_NOTI_COUNT);
                    userEditor.apply();

                    prepareAPICall(userId, pregnancy_day);
                    Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
                }
        } else
        {
            Log.i(TAG, "Notifications are disabled");
        }

//        if (!userId.isEmpty()) {
//
//                    userEditor.remove(Config.DAILY_NOTI_COUNT);
//                    userEditor.apply();
//
//                    prepareAPICall(userId, pregnancy_day);
//                    Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
//
//                    Log.i(TAG, "Notifications are disabled");
//                }

                stopSelf();
    }


    private void sendNotification(int completedCount) {

        Intent mainIntent = new Intent(this, HomeActivity.class);
        @SuppressWarnings("deprecation")
        Notification noti = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 1, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Garbh Sanskar")
                .setContentText(completedCount + " "+"Activities Completed")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Daily Reminder!")
                .setWhen(System.currentTimeMillis())
                .getNotification();

        NotificationManager notificationManager
                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, noti);

        Log.v(TAG, "Notification sent");
    }

    public void setAlarm(int completedCount) {


        userEditor.putString(Config.DAILY_NOTI_COUNT, String.valueOf(completedCount)).apply();

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);

        System.out.println("completedcount" + sharedPreferences.getString(Config.DAILY_NOTI_COUNT,"") );
        int ID = (int) System.currentTimeMillis();
        alarmIntent.putExtra("ID",ID);
        Log.d("setRepeatedNotification", "ID:" + ID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 0);

        if(calendar.before(now)){
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void prepareAPICall(final String userId, final String string_date) {

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.BASE_URL + "API/completedactivitiesofday.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Toast.makeText(DailyNotification.this, "hello", Toast.LENGTH_SHORT).show();

                        try {

                            JSONObject jsonObject = new JSONObject(s);

                            int successCode = jsonObject.getInt("success");

                            if (successCode == 1)
                            {
                                if (jsonObject.has("activity_number")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("activity_number");
                                    completedCount = jsonArray.length();
                                    System.out.println("completed_count1 "+completedCount);
                                    Toast.makeText(DailyNotification.this, "hello1", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    String message = jsonObject.getString("message");
                                    System.out.println("arrayData "+message);
                                    completedCount = 0;
                                    System.out.println("completed_count2 "+completedCount);
                                    Toast.makeText(DailyNotification.this, "hello2", Toast.LENGTH_SHORT).show();

                                }
                            }

                            setAlarm(completedCount);
//                            sendNotification(completedCount);
                            System.out.println("completed_count3 "+completedCount);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("errorMess "+ e.getMessage());
                        }

                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(DailyNotification.this, "hello3", Toast.LENGTH_SHORT).show();
                        System.out.println("volleyerrorMess "+ volleyError.getMessage());
                    }
                })

             {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_USERID, userId);
                params.put(KEY_DAY, string_date);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
