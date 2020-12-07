package com.call.callnotification;

import android.app.Activity;
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
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.call.callnotification.ApiHandler.DataFeacher;
import com.call.callnotification.Classes.Constants;
import com.call.callnotification.Classes.Notification;
import com.call.callnotification.Classes.NotificationModel;
import com.call.callnotification.Classes.SharedPManger;

import java.util.ArrayList;
import java.util.List;

public class MessageService extends Service {
    private static final String CHANNEL_ID = "channel_id";
    Context context;
    ArrayList<Notification> list;
    SharedPManger sharedPManger;
    int last_id;
    CountDownTimer cTimer = null;
    private NotificationManager notificationManager;

    public MessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        context = this;
        list = new ArrayList<>();
        sharedPManger = new SharedPManger(getApplicationContext());
        last_id = sharedPManger.getDataInt(Constants.last_id, 0);
        Log.i("tag", "Log list Service " + last_id);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startTimer(1);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        super.onTaskRemoved(rootIntent);
    }

    private void getList() {
        new DataFeacher((Activity) getApplicationContext(), (obj, func, IsSuccess) -> {
            NotificationModel result = (NotificationModel) obj;
            if (func.equals(Constants.ERROR)) {
                String message = getString(R.string.fail_to_get_data);
                if (result != null && result.getMessage() != null) {
                    message = result.getMessage();
                }
            } else {
                if (IsSuccess) {
                    //list = result.getData();

                    list.add(new Notification(1, "message1", "", 200));
                    list.add(new Notification(2, "message2", "", 200));
                    list.add(new Notification(3, "message3", "", 200));
                    list.add(new Notification(4, "message14", "", 200));
                    list.add(new Notification(5, "message5", "", 200));
                    list.add(new Notification(6, "message6", "", 200));
                    Notification notification = list.get(list.size() - 1);

                    Log.i("tag", "Log last id service   " + list.get(list.size() - 1).getId());

                    if (last_id != notification.getId()) {
                        Log.i("tag", "Log list send " + last_id);
                        sharedPManger.SetData(Constants.last_id, notification.getId());
                        sendNotification(notification.getMessage(), getString(R.string.new_message));
                    }


                }
            }
            startTimer(1);


        }).NotificationHandle(last_id);

    }

    public void sendNotification(String message, String title) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setLargeIcon(largeIcon).setContentTitle(title)
                .setContentText(message).setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/notification_sound"),
                        AudioManager.STREAM_NOTIFICATION)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setContentIntent(pendingIntent);

        notificationManager.notify(0 /*ID of notification*/, builder.build());


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

    void startTimer(long timeMinutes) {

        long timeSecond = timeMinutes * 60;
        final long timeMilSecond = timeSecond * 1000;
        cTimer = new CountDownTimer(timeMilSecond, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Toast.makeText(context, "onFinish", Toast.LENGTH_SHORT).show();
                //  getList();
                GetList2(last_id);
            }
        };
        cTimer.start();
    }

    public void GetList2(int last_id) {
        Log.i("tag", "Log list finish GetList2 " + last_id);

        AndroidNetworking.get("https://risteh.com/Cashiers/api/v1/Notification").addQueryParameter("last_id",
                String.valueOf(last_id)).addHeaders("ApiKey",
                Constants.api_key).setTag(this).setPriority(Priority.LOW).build().getAsObjectList(Notification.class,
                new ParsedRequestListener<List<Notification>>() {
            @Override
            public void onResponse(List<Notification> notificationList) {
                Log.i("tag", "Log list finish GetList size  " +notificationList);

                // do anything with response
                list.add(new Notification(1, "message1", "", 200));
                list.add(new Notification(2, "message2", "", 200));
                list.add(new Notification(3, "message3", "", 200));
                list.add(new Notification(4, "message14", "", 200));
                list.add(new Notification(5, "message5", "", 200));
                list.add(new Notification(6, "message6", "", 200));
                Log.i("tag", "Log list finish GetList size  " + list.size());

                int last_id_list = list.get((list.size()) - 1).getId();
                if (last_id != last_id_list) {
                    sharedPManger.SetData(Constants.last_id, last_id_list);
                    sendNotification(list.get((list.size()) - 1).getMessage(), getString(R.string.new_message));

                }


            }

            @Override
            public void onError(ANError anError) {
                Log.i("tag", "Log list finish GetList size  " +anError.getMessage());

                // handle error
            }
        });
        startTimer(1);
    }
}


