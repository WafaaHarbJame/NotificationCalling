package com.call.callnotification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.call.callnotification.ApiHandler.DataFeacher;
import com.call.callnotification.Classes.Constants;
import com.call.callnotification.Classes.MyNotificationModel;
import com.call.callnotification.Classes.NotificationModel;
import com.call.callnotification.Classes.SharedPManger;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MessageService extends Service {
    private static final String CHANNEL_ID = "channel_id";
    Context context;
    ArrayList<MyNotificationModel> list;
    SharedPManger sharedPManger;
    //    int last_id;
    CountDownTimer cTimer = null;
    private Timer timer;
    private final long DELAY_TIME = 3 * 1000;
    boolean isRun;
    private NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        list = new ArrayList<>();
        sharedPManger = new SharedPManger(getApplicationContext());
//        last_id = sharedPManger.getDataInt(Constants.last_id, 1);
//        Log.i("tag", "Log list Service " + last_id);

        timer = new Timer();
        timer.scheduleAtFixedRate(new ProgressUpdate(), 0, DELAY_TIME);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        String CHANNEL_ONE_ID = "The Calling";
        String CHANNEL_ONE_NAME = "Calling_channel";
        NotificationChannel notificationChannel = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.setShowBadge(false);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            notificationChannel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/notification_sound"), attributes);
            manager.createNotificationChannel(notificationChannel);

            int NOTIFICATION_ID = (int) (System.currentTimeMillis() % 10000);
            startForeground(NOTIFICATION_ID, new Notification.Builder(this, CHANNEL_ONE_ID).build());
        }

//        startTimer(3);

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        super.onTaskRemoved(rootIntent);
    }

    public void sendNotification(int id, String message, String title) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_meduim);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.logo_small)
                .setAutoCancel(true)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/raw/notification_sound"))
//                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/notification_sound"), AudioManager.STREAM_NOTIFICATION)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        notificationManager.notify(id, builder.build());


    }


    private void setupChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence adminChannelName = getString(R.string.app_name);
            String adminChannelDescription = getString(R.string.app_name);
            NotificationChannel adminChannel;
            adminChannel = new NotificationChannel(CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
            adminChannel.setDescription(adminChannelDescription);
            adminChannel.enableLights(true);
            adminChannel.setLightColor(Color.RED);
            adminChannel.enableVibration(true);
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            adminChannel.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/notification_sound"), attributes);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(adminChannel);
            }
        }

    }

    private class ProgressUpdate extends TimerTask {

        ProgressUpdate() {
            super();
        }

        @Override
        public void run() {

            GetList2();
        }
    }


    public void GetList2() {

        int last_id = sharedPManger.getDataInt(Constants.last_id, 0);
        Log.i("tag", "Log list finish GetList2 " + last_id);

        AndroidNetworking.get("https://risteh.com/Cashiers/api/v1/Notification")
                .addQueryParameter("last_id", String.valueOf(last_id))
                .addHeaders("ApiKey", Constants.api_key).setTag(this).setPriority(Priority.LOW).
                setPriority(Priority.MEDIUM).build()
                .getAsObject(NotificationModel.class, new ParsedRequestListener<NotificationModel>() {
                    @Override
                    public void onResponse(NotificationModel notificationModel) {
                        list = notificationModel.getData();

                        if (list.size() > 0) {
                            MyNotificationModel myNotificationModel = list.get(list.size() - 1);
//                            Log.i("tag", "Log list last id" + list.get(list.size() - 1).getId());

                            int last_id = sharedPManger.getDataInt(Constants.last_id, 0);
                            Log.i("tag", "Log list finish GetList2 " + last_id);

                            if (last_id < myNotificationModel.getId()) {
                                Log.i("tag", "Log list end now  " + myNotificationModel.getId());
                                sharedPManger.SetData(Constants.last_id, myNotificationModel.getId());
                                sendNotification(myNotificationModel.getId(), getString(R.string.mech) + " " + myNotificationModel.getMechNo(), getString(R.string.new_message));
                            }

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        // handle error
                    }
                });


    }
}


