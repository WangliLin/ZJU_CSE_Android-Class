package cn.zju.id21632120;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import cn.iipc.android.tweetlib.SubmitProgram;
import cn.zju.id21632120.dbutil.DbHelper;
import cn.zju.id21632120.dbutil.StatusContract;
import cn.zju.id21632120.service.UpdateService;

/**
 * 微博一览
 * Created by Wangli on 2017/6/18.
 */

public class ScrollViewActivity extends AppCompatActivity {

    private TextView textView;
    SQLiteDatabase db;
    Cursor cursor;
    DbHelper dbhlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView);

        dbhlp = new DbHelper(this);
        db = dbhlp.getReadableDatabase();
        cursor = db.query(StatusContract.TABLE, null, null, null, null, null,
                StatusContract.DEFAULT_SORT);
        startManagingCursor(cursor);

    }

    @Override
    protected void onStart() {
        super.onStart();
        StringBuilder sb = new StringBuilder();

        while (cursor.moveToNext()){
            sb.append(String.format("作者： %s\n内容： %s\n\n",
                    cursor.getString(cursor.getColumnIndex(StatusContract.Column.USER)),
                    cursor.getString(cursor.getColumnIndex(StatusContract.Column.MESSAGE))));
        }

        textView.setText(sb.toString());

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

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                new SubmitProgram().doSubmit(this, "G1");
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

