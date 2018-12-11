package com.example.silasonyango.ewe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;



public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "FROM:" + remoteMessage.getFrom());

        //Check if the message contains data
        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Messagedata:" + remoteMessage.getData());



            Map<String, String> data = remoteMessage.getData();
            String body = data.get("body");
            String title = data.get("title");
            String NewsId = data.get("NewsId");
            String click_action = data.get("click_action");

            Intent intent = new Intent();
            intent.setAction("com.silasonyango.CUSTOM_CHAT_INTENT");
            sendBroadcast(intent);

            sendNotification(body,title,click_action);


        }


        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "Mesage body:" + remoteMessage.getNotification().getBody());



            sendNotification(remoteMessage.getNotification().getBody());
        }
    }
    /**
     * Dispay the notification
     *
     * @param body
     * @param title
     * @param body
     */
    private void sendNotification(String body, String title, String click_action) {

        Intent intent = null;
        if(click_action.equals("BOOKINGS")){

            intent = new Intent(this, Landing.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/,
                intent, PendingIntent.FLAG_ONE_SHOT);
        //Set sound of notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(body);
        bigText.setBigContentTitle(title);
        bigText.setSummaryText("New customer");
        notifiBuilder.setStyle(bigText);
        notifiBuilder.setPriority(NotificationCompat.PRIORITY_MAX);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
        }
    }
    /**
     * Dispay the notification
     * @param body
     */
    private void sendNotification(String body) {

        Intent intent = new Intent(this, Landing.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);
        //Set sound of notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Firebase Cloud Messaging")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
        }
    }
}

