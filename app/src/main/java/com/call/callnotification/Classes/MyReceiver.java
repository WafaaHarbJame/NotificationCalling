package com.call.callnotification.Classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.call.callnotification.MessageService;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        context.startService(new Intent(context, MessageService.class));
    }
}