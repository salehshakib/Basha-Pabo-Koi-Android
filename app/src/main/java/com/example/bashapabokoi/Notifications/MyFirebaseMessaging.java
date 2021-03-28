package com.example.bashapabokoi.Notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.bashapabokoi.ChatActivity;
import com.example.bashapabokoi.ChatsFragment;
import com.example.bashapabokoi.MapActivity;
import com.example.bashapabokoi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessaging extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "com.example.bashapabokoi";
    private static final String CHANNEL_NAME = "Chat Notification";
    private static final String CHANNEL_DESCRIPTION = "chat application";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("From :" ,remoteMessage.getFrom());

        String sented = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        /*if (remoteMessage.getData()!= null) {
            //Log.d(TAG, "Message data payload: " + remoteMessage.getData().get("body"));

            if (firebaseUser != null && sented.equals(firebaseUser.getUid())){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(remoteMessage);
                }
                else{
                    sendNotification(remoteMessage);
                }
            }*/

            //if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.

            /*} else {
                // Handle message within 10 seconds

            }*/

        //}

        // Check if message contains a notification payload.


        /*if (remoteMessage.getNotification() != null) {
            Log.d("Message Notification Body: " ,remoteMessage.getNotification().getBody());
        }*/


        if (remoteMessage.getData().size() > 0) {
            if (firebaseUser != null && sented.equals(firebaseUser.getUid())){
                    sendNotification(remoteMessage);
                }
        }
        if (remoteMessage.getNotification() != null){
            if (firebaseUser != null && sented.equals(firebaseUser.getUid())) {
                sendNotification(remoteMessage);
            }
        }




    }

    private void sendNotification(RemoteMessage remoteMessage) {
        int notification_id = (int) System.currentTimeMillis();
        NotificationManager notificationManager = null;
        NotificationCompat.Builder builder;
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String body = remoteMessage.getData().get("body");
        String title = remoteMessage.getData().get("title");

        Log.d("sendNotification", title + " : " +body);
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        if (notificationManager == null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID);

            if (channel == null){
                channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
                channel.setDescription(CHANNEL_DESCRIPTION);
                channel.enableVibration(true);
                channel.setLightColor(Color.GREEN);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(channel);
            }

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            builder.setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_baseline_attach_email_24) //todo icon
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setDefaults(Notification.DEFAULT_ALL);

        } else {
            builder = new NotificationCompat.Builder(this);
            builder.setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_baseline_attach_email_24) //todo icon
                    .setContentText(body)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setDefaults(Notification.DEFAULT_VIBRATE);

        }

        notificationManager.notify(1002, builder.build());

    }

}

