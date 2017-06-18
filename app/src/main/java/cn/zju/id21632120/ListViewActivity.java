package cn.zju.id21632120;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.iipc.android.tweetlib.SubmitProgram;
import cn.zju.id21632120.Adapter.MyAdapter;
import cn.zju.id21632120.Adapter.Tweet;
import cn.zju.id21632120.dbutil.DbHelper;
import cn.zju.id21632120.dbutil.StatusContract;
import cn.zju.id21632120.service.UpdateService;

/**
 * tweet - listView
 * Created by Wangli on 2017/6/18.
 */

public class ListViewActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Cursor cursor;
    DbHelper dbhlp;

    List<Tweet> tweets = new ArrayList<Tweet>();

    private ListView listView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbhlp = new DbHelper(this);
        db = dbhlp.getReadableDatabase();
        cursor = db.query(StatusContract.TABLE, null, null, null, null, null,
                StatusContract.DEFAULT_SORT);
        startManagingCursor(cursor);

        while (cursor.moveToNext()){

                  /*  cursor.getString(cursor.getColumnIndex(StatusContract.Column.USER)),
                    cursor.getString(cursor.getColumnIndex(StatusContract.Column.MESSAGE))));*/

            Tweet tweet = new Tweet();
            tweet.setID(cursor.getString(cursor.getColumnIndex(StatusContract.Column.ID)));
            tweet.setUser(cursor.getString(cursor.getColumnIndex(StatusContract.Column.USER)));
            tweet.setMessage(cursor.getString(cursor.getColumnIndex(StatusContract.Column.MESSAGE)));
            tweet.setCreateTime(cursor.getString(cursor.getColumnIndex(StatusContract.Column.CREATED_AT)));

            tweets.add(tweet);

        }

        listView = (ListView) findViewById(R.id.listView);
        adapter = new MyAdapter(this, tweets);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(ListViewActivity.this).setTitle(tweets.get(i).getUser())
                        .setMessage(tweets.get(i).getMessage())
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case R.id.action_settings:
                new SubmitProgram().doSubmit(this, "G2");
                return true;
            case R.id.action_start:
                startService(new Intent(this, UpdateService.class));
                return true;
            case R.id.action_stop:
                stopService(new Intent(this, UpdateService.class));
                return true;
            case R.id.action_calc:
                startActivity(new Intent(this, CalcActivity.class));
                return true;
            case R.id.action_filetest:
                startActivity(new Intent(this, StorageActivity.class));
                return true;
            case R.id.action_music:
                startActivity(new Intent(this, MusicActivity.class));
                return true;
            case R.id.action_scroll_weibo:
                startActivity(new Intent(this, ScrollViewActivity.class));
                return true;
            case R.id.action_list_weibo:
                startActivity(new Intent(this, ListViewActivity.class));
                return true;
            case R.id.action_finish:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

