package com.logicsoftbd.lsl.pushNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_NotificationBodyModel;
import com.logicsoftbd.lsl.ui.v_1_ui.electronic_approval.V1_ApprovalDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class FcmMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FcmMessagingService";
    private static final String CHANNEL_ID = "my_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage != null) {
            try{
                Log.e(TAG, "Data: " + remoteMessage.getData());

                JSONObject data = new JSONObject(remoteMessage.getData());

                String title = null;
                String body = null;
                String type = null;
                String image = null;
                try {
                    title = data.getString("title");
                    body = data.getString("body");
                    type = data.getString("type");
                } catch (JSONException e) {
                    Log.d(TAG, "onMessageReceived: "+e.getMessage());;
                }

                V1_NotificationBodyModel notificationBodyModels = new Gson().fromJson(body, V1_NotificationBodyModel.class);

                Log.d(TAG, "onMessageReceivedDTO: #########"+ notificationBodyModels.getDesc());
                Log.d(TAG, "onMessageReceivedDTO: ######### Type"+ type);

                String message = "";

//                if(notificationBodyModels.getUN_APPROVED() != null && notificationBodyModels.getUN_APPROVED()){
//                    message = notificationBodyModels.getUN_APPROVED_MSG().toString();
//                }else{
//                   message = notificationBodyModels.getDesc().toString();
//                }

                message = notificationBodyModels.getDesc().toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    message = String.valueOf(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
                }

                Intent intent = new Intent(this, V1_ApprovalDetailsActivity.class);
                intent.putExtra("menu_name", title);
                intent.putExtra("menu_id", notificationBodyModels.getMenuId());
                intent.putExtra("unapproved_status", "unapproved");
                intent.putExtra("unApprovedRequestId", notificationBodyModels.getId());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Intent intent1 = new Intent("custom-event");
                intent1.putExtra("message", "Approval");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
                if(type.equals("1")){
                    pushNotification(intent, title, message);
                }
            }catch (Exception e){
                Log.d(TAG, "onMessageReceived error: #########"+e.getMessage());
            }
        }
    }
    private void pushNotification(Intent intent, String title, String message) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity
                    (getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getActivity
                    (getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "alert_001");

        mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                .setStyle(inboxStyle)
                .setContentIntent(pendingIntent)
                .setOngoing(false)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(false);

        if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setColorized(true);
            mBuilder.setColor(Color.parseColor("#f38000"));
            Log.e("MANUFACTURER", Build.MANUFACTURER);
        } else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        if (mNotificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();

                NotificationChannel channel = new NotificationChannel("alert_001",
                        "Alert",
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setSound(defaultSoundUri, attributes);

                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channel.getId());
            }
            mNotificationManager.notify(1, mBuilder.build());
        }
    }


//    private void pushNotification(Intent intent, String title, String message) {
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle.addLine(message);
//
//        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "alert_001");
//
//        mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
//                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
//                .setWhen(System.currentTimeMillis())
//                .setContentTitle(title)
//                .setContentText(message)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
////                .setStyle(inboxStyle)
//                .setContentIntent(pendingIntent)
////                .setOngoing(true)
//                .setSound(defaultSoundUri)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setAutoCancel(false);
//
//        if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
//            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
//            mBuilder.setColorized(true);
//            mBuilder.setColor(Color.parseColor("#f38000"));
//            Log.e("MANUFACTURER", Build.MANUFACTURER);
//        } else {
//            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        }
//
//        if (mNotificationManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
//
//                NotificationChannel channel = new NotificationChannel("alert_001",
//                        "Alert",
//                        NotificationManager.IMPORTANCE_HIGH);
//                channel.setSound(defaultSoundUri, attributes);
//
//                mNotificationManager.createNotificationChannel(channel);
//                mBuilder.setChannelId(channel.getId());
//            }
//            mNotificationManager.notify(1, mBuilder.build());
//        }
//    }

    private void pushNotification(Intent intent, String title, String message, String image) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap bitmap = getBitmapFromURL(image);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "alert_001");


        mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setStyle(bigPictureStyle)
                .setContentIntent(pendingIntent);
//                .setOngoing(true)
//                .setSound(defaultSoundUri)
//                .setPriority(NotificationCompat.PRIORITY_LOW)
////                .setDefaults(Notification.DEFAULT_ALL)
//                .setAutoCancel(true);

        if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setColorized(true);
            mBuilder.setColor(Color.parseColor("#f38000"));
            Log.e("MANUFACTURER", Build.MANUFACTURER);
        } else {
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        if (mNotificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();

                NotificationChannel channel = new NotificationChannel("alert_001",
                        "Alert",
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setSound(defaultSoundUri, attributes);

                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channel.getId());
            }
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public final Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            URLConnection var10000 = url.openConnection();
            HttpURLConnection connection = (HttpURLConnection)var10000;
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException var5) {
            var5.printStackTrace();
            return null;
        }
    }

}

