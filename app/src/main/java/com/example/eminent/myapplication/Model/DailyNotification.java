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
    private String string_date;

    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor userEditor;

    private static final String KEY_USERID = "user_id";
    private static final String KEY_DATE = "date";

    private SessionManager sessionManager;

    @Override
    public void onCreate() {
        super.onCreate();

        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
        userEditor = sharedPreferences.edit();
        userId = sharedPreferences.getString(Config.USER_ID, "");

        SharedPreferences settings = getSharedPreferences(HomeActivity.PREFS, MODE_PRIVATE);

        // Are notifications enabled?
        if (settings.getBoolean("enabled", true)) {
            // Is it time for a notification?
            if (settings.getLong("lastRun", Long.MAX_VALUE) < System.currentTimeMillis() - delay);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            string_date = dateFormat.format(date);
            System.out.println("Date " + string_date);

                prepareAPICall(userId,string_date);

        } else {
            Log.i(TAG, "Notifications are disabled");
        }

        // Set an alarm for the next time this service should run:
        setAlarm();

        Log.v(TAG, "Service stopped");
        stopSelf();
    }


    private void sendNotification(int completedCount) {

        Intent mainIntent = new Intent(this, HomeActivity.class);
        @SuppressWarnings("deprecation")
        Notification noti = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("Garbh Sanskar")
                .setContentText(completedCount + "Activities Completed")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Daily Reminder!")
                .setWhen(System.currentTimeMillis())
                .getNotification();

        NotificationManager notificationManager
                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(131315, noti);

        Log.v(TAG, "Notification sent");
    }

    public void setAlarm() {

        Intent serviceIntent = new Intent(this, DailyNotification.class);
        PendingIntent pi = PendingIntent.getService(this, 131312, serviceIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi);
        Log.v(TAG, "Alarm set");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void prepareAPICall(final String userId, final String string_date) {

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Common.BASE_URL + "API/completedactivitiesofdate.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        try {

                            JSONObject jsonObject = new JSONObject(s);

                            int successCode = jsonObject.getInt("success");

                            if (successCode == 1)
                            {
                                if (jsonObject.has("activity_number")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("activity_number");
                                    completedCount = jsonArray.getInt(0);
                                    System.out.println("completed_count1 "+completedCount);
                                }
                                else
                                {
                                    String message = jsonObject.getString("message");
                                    System.out.println("arrayData "+message);
                                    completedCount = 0;
                                    System.out.println("completed_count2 "+completedCount);
                                }
                            }


                            sendNotification(completedCount);
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

//                        System.out.println("volleyerrorMess "+ volleyError.getMessage());
                    }
                })
        {
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
