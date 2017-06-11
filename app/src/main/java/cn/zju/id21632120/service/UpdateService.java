package cn.zju.id21632120.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import cn.iipc.android.tweetlib.Status;
import cn.iipc.android.tweetlib.YambaClient;
import cn.iipc.android.tweetlib.YambaClientException;

/**
 * 用于定时联网更新
 * Created by Wangli on 2017/6/11.
 */

public class UpdateService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "UpdateService";
    static long DELAY = 60000;
    private String username = "student";
    private String password = "password";
    private boolean runFlag = false;
    private Updater updater;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        DELAY = Long.parseLong(prefs.getString("interval", "60"))*1000;

        this.updater = new Updater();
        Log.d(TAG, "onCreated");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!runFlag) {
            this.runFlag = true;
            this.updater.start();
        }

        Log.d(TAG, "onStarted");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.runFlag = false;
        this.updater.interrupt();
        this.updater = null;
        Log.d(TAG, "onDestroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals("username"))
            username = sharedPreferences.getString("username", "student");
        if (s.equals("password"))
            password = sharedPreferences.getString("password", "password");
        if (s.equals("interval"))
            DELAY = Long.parseLong(sharedPreferences.getString("interval", "60")) * 1000;

    }

    private class Updater extends Thread {
        public Updater() {
            super("UpdateService-Thread");
        }

        @Override
        public void run() {
            while (runFlag) {
                Log.d(TAG, "Running background thread");
                try {

                    //从云端获取数据
                    YambaClient cloud = new YambaClient(username, password);
                    try {
                        List<Status> timeline = cloud.getTimeline(20);
                        for (Status status : timeline) {
                            Log.d(TAG, String.format("%s: %s", status.getUser(), status.getMessage()));
                        }
                    } catch (YambaClientException e) {
                        Log.d(TAG, "failed to fetch up the timeline", e);
                        e.printStackTrace();
                    }

                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    runFlag = false;
                }
            }
        }
    }


}
