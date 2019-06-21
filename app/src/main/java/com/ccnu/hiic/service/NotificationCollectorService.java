package com.ccnu.hiic.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.ccnu.hjjc.R;
import com.ccnu.hiic.activity.MainActivity;

import java.lang.reflect.Field;
import java.util.List;


/**
 * Created by dong0 on 2017-12-27.
 */

public class NotificationCollectorService extends NotificationListenerService {
    private static final String TAG = "NotifiCollectorMonitor";
    public static final String EXTRA_TITLE = "android.title";

    public static final String EXTRA_TEXT = "android.text";
    public static final String EXTRA_SUB_TEXT = "android.subText";
    public static final String EXTRA_LARGE_ICON = "android.largeIcon";

    private NotificationManager manager;
    private Notification notification;
    private Camera camera = null;
    private CameraManager cameramanager;
    private long[] pattern = {10, 5000,1000};
    private boolean isOpenFlashLight = false;
    private String channelID = "10";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
        ensureCollectorRunning();
        manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, MainActivity.class), 0);
        Uri sound = Uri.parse("android.resource://" + getPackageName()
                + "/" + R.raw.warning1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, "Channel1",NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setSound(sound,null);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300});
            manager.createNotificationChannel(channel);
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("报警通知！")
                    .setContentText("监测环境突发烟雾报警，请立即处理！")
                    .setContentIntent(contentIntent)
                    .setVibrate(pattern)
                    .setSound(sound)
                    .setChannelId(channelID)
                    .build();// getNotification()
        }else{
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("报警通知！")
                    .setContentText("监测环境突发烟雾报警，请立即处理！")
                    .setContentIntent(contentIntent)
                    .setVibrate(pattern)
                    .setSound(sound)
                    .build();// getNotification()
        }
        notification.flags = Notification.FLAG_INSISTENT;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called");
        toggleNotificationListenerService();
        return START_STICKY;
    }

    private void ensureCollectorRunning() {
        ComponentName collectorComponent = new ComponentName(this,NotificationCollectorService.class);
        Log.v(TAG, "ensureCollectorRunning collectorComponent: " + collectorComponent);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        boolean collectorRunning = false;
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices == null ) {
            Log.w(TAG, "ensureCollectorRunning() runningServices is NULL");
            return;
        }
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (service.service.equals(collectorComponent)) {
                Log.w(TAG, "ensureCollectorRunning service - pid: " + service.pid + ", currentPID: " + Process.myPid() + ", clientPackage: " + service.clientPackage + ", clientCount: " + service.clientCount
                        + ", clientLabel: " + ((service.clientLabel == 0) ? "0" : "(" + getResources().getString(service.clientLabel) + ")"));
                if (service.pid == Process.myPid()) {
                    collectorRunning = true;
                }
            }
        }
        if (collectorRunning) {
            Log.d(TAG, "ensureCollectorRunning: collector is running");
            return;
        }
        Log.d(TAG, "ensureCollectorRunning: collector not running, reviving...");
        toggleNotificationListenerService();
    }

    private void toggleNotificationListenerService() {
        Log.d(TAG, "toggleNotificationListenerService() called");
        ComponentName thisComponent = new ComponentName(this,NotificationCollectorService.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification n = sbn.getNotification();
        if (n == null) {
            return;
        }
        // 标题和时间
        String title = "";
        if (n.tickerText != null) {
            title = n.tickerText.toString();
        }
        long when = n.when;
        // 其它的信息存在一个bundle中，此bundle在android4.3及之前是私有的，需要通过反射来获取；android4.3之后可以直接获取
        Bundle bundle = null;
        try {
            Field field = Notification.class.getDeclaredField("extras");
            bundle = (Bundle) field.get(n);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // 内容标题、内容、副内容
        String contentTitle = bundle.getString(EXTRA_TITLE);
        if (contentTitle == null) {
            contentTitle = "";
        }
        String contentText = bundle.getString(EXTRA_TEXT);
        if (contentText == null) {
            contentText = "";
        }
        String contentSubtext = bundle.getString(EXTRA_SUB_TEXT);
        if (contentSubtext == null) {
            contentSubtext = "";
        }
        Log.e(TAG, "notify msg: title=" + title + " ,when=" + when
                + " ,contentTitle=" + contentTitle + " ,contentText="
                + contentText + " ,contentSubtext=" + contentSubtext);
        if (title.contains("监控系统报警信息通知")) {
            isOpenFlashLight = true;
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try{
                        manager.notify(1, notification);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    while (isOpenFlashLight) {
                        OpenFlashLight();
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CloseFlashLight();
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }



    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // TODO Auto-generated method stub
        try {

            Bundle extras = sbn.getNotification().extras;
            //  获取接收消息APP的包名
            String notificationPkg = sbn.getPackageName();
            // 获取接收消息的抬头
            String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
            // 获取接收消息的内容
            String notificationText = extras.getString(Notification.EXTRA_TEXT);
            Log.e(TAG, "Notification removed " + notificationTitle + " & " + notificationText + " & " + notificationPkg);
            if (notificationTitle.contains("报警通知！")) {
                if (notificationText.contains("监测环境突发烟雾报警，请立即处理！")) {
                    isOpenFlashLight = false;
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    private void OpenFlashLight(){
        try{
            camera = Camera.open();
            Camera.Parameters mParameters;
            mParameters = camera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(mParameters);
        } catch(Exception ex){
            ex.printStackTrace();
        }
        try {
            cameramanager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            cameramanager.setTorchMode("0", true);// "0"是主闪光灯
        } catch (Exception e) {}
    }

    private void CloseFlashLight(){
        try{
            Camera.Parameters mParameters;
            mParameters = camera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(mParameters);
            camera.release();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        try {
            if (cameramanager == null) {
                return;
            }
            cameramanager.setTorchMode("0", false);
        } catch (Exception e) {}
    }
}
