package cn.zju.id21632120;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import cn.iipc.android.tweetlib.SubmitProgram;

/**
 * 提交FileWrite测试画面
 * Created by Wangli on 2017/6/1.
 */

public class StorageActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn1, btn2, btn3;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        tv = (TextView) findViewById(R.id.tv_log);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);


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
        if (id == R.id.action_settings) {
            new SubmitProgram().doSubmit(this, "E1");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                fileTestWrite(getFilesDir().getPath());
                break;
            case R.id.btn2:
                fileTestWrite(Environment.getExternalStorageDirectory().getPath());
                break;
            case R.id.btn3:
                fileTestWrite(getExternalFilesDir(null).getPath());
                break;
            default:
                break;
        }
    }

    private void fileTestWrite(String dir) {
        String fn = dir + "/hello.txt";

        try {
            tv.setText(tv.getText() + "\nWrite to: " + fn);
            PrintWriter o = new PrintWriter(new BufferedWriter(new FileWriter(fn)));
            o.println(fn);
        } catch (IOException e) {
            tv.setText(tv.getText() + "\nWrite to " + e.toString());
        }
    }

}
