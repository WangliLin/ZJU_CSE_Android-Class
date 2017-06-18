package cn.zju.id21632120.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import cn.iipc.android.tweetlib.Status;
import cn.iipc.android.tweetlib.YambaClient;
import cn.iipc.android.tweetlib.YambaClientException;
import cn.zju.id21632120.dbutil.DbHelper;
import cn.zju.id21632120.dbutil.StatusContract;

/**
 * 用于定时联网更新
 * Created by Wangli on 2017/6/11.
 */

public class UpdateService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "UpdateService";
    static long DELAY = 60000;
    private String username = "student";
    private String password = "password";
    public static boolean runFlag = false;
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

            DbHelper dbHelper = new DbHelper(UpdateService.this);

            while (runFlag) {
                Log.d(TAG, "Running background thread");

                SQLiteDatabase db = dbHelper.getWritableDatabase();



                    //从云端获取数据
                    YambaClient cloud = new YambaClient(username, password);
                    try {
                        List<Status> timeline = cloud.getTimeline(20);
                        ContentValues values = new ContentValues();

                        Log.d(TAG, "获取记录数：" + timeline.size());

                        int count = 0;
                        long rowID = 0;

                        for (Status status : timeline) {
                            String usr = status.getUser();
                            String msg = status.getMessage();

                            values.clear();

                            values.put(StatusContract.Column.ID, status.getId());
                            values.put(StatusContract.Column.USER, status.getUser());
                            values.put(StatusContract.Column.MESSAGE, status.getMessage());
                            values.put(StatusContract.Column.CREATED_AT, status.getCreatedAt().getTime());

                            rowID = db.insertWithOnConflict(StatusContract.TABLE, null, values,
                                    SQLiteDatabase.CONFLICT_IGNORE);
                            if (rowID != -1) {
                                count ++;
                                Log.d(TAG, String.format("%s, %s", usr, msg));
                            }

                        }


                    } catch (YambaClientException e) {
                        Log.d(TAG, "failed to fetch up the timeline", e);
                        e.printStackTrace();
                    } finally {
                        db.close();
                    }

                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    runFlag = false;
                }
            }
        }
    }


}
