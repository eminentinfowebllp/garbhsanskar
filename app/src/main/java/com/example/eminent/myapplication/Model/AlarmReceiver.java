//package com.example.eminent.myapplication.Model;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.TaskStackBuilder;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//import android.content.SharedPreferences;
//import android.support.v4.app.NotificationCompat;
//
//import com.example.eminent.myapplication.Activity.HomeActivity;
//import com.example.eminent.myapplication.Activity.LoginActivity;
//import com.example.eminent.myapplication.R;
//
//import static android.content.Context.MODE_PRIVATE;
//import static android.content.Context.UI_MODE_SERVICE;
//
//
///**
// * Created by eminent on 6/14/2017.
// */
//
//public class AlarmReceiver extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        String a = intent.getStringExtra("activity_completed");
//        Log.d("ZeroId1", a);
//
////        int completedActivityCount = intent.getIntExtra("activity_completed", 0);
//        SharedPreferences sharedPreferences;
//        SharedPreferences.Editor userEditor;
//
//
//
//            sharedPreferences = context.getSharedPreferences(Config.PREF_NAME, MODE_PRIVATE);
//            userEditor = sharedPreferences.edit();
//            String completedActivityCount = sharedPreferences.getString(Config.DAILY_NOTI_COUNT, "");
//
//            System.out.println("countAct " + completedActivityCount);
//
//            Intent notificationIntent = new Intent(context, HomeActivity.class);
//
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//            stackBuilder.addParentStack(HomeActivity.class);
//            stackBuilder.addNextIntent(notificationIntent);
//
//            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//
//            Notification notification = builder.setContentTitle("Daily Notification")
//                    .setContentText(completedActivityCount + " " + "Activity completed..")
//                    .setTicker("New Message Alert!")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentIntent(pendingIntent).build();
//
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(0, notification);
//
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(0, notification);
//
//            userEditor.remove(completedActivityCount);
//            userEditor.apply();
//
//    }
//}