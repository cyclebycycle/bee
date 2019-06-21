package com.ccnu.hiic.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {
	private final String TAG = "MyBootBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            Intent sayHelloIntent = new Intent(context, MainActivity.class);
//            sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(sayHelloIntent);
//        }
        Log.e(TAG,"启动成功");
        Intent sayHelloIntent = new Intent(context, NotificationListenerService.class);
        sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(sayHelloIntent);
    }
}
