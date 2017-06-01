package cn.zju.id21632120;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.iipc.android.tweetlib.SubmitProgram;
import cn.iipc.android.tweetlib.YambaClient;
import cn.iipc.android.tweetlib.YambaClientException;

/**
 * twitter发布界面
 * Created by Wangli on 2017/5/14.
 */

public class StatusActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editText;
    private TextView tv_num;
    private TextView tv_user;
    private Button btn_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = (EditText) findViewById(R.id.et_content);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_user.setText(this.getPackageName());
        editText.addTextChangedListener(mTextWatcher);

        btn_post = (Button) findViewById(R.id.postBtn);
        btn_post.setOnClickListener(this);

    }

    TextWatcher mTextWatcher = new TextWatcher() {

        private CharSequence tempStr;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            tempStr = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            tv_num.setText(Integer.toString(140 - tempStr.length()));
        }
    };

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
            new SubmitProgram().doSubmit(this, "D1");
            return true;
       }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        String status = "控制系：" + editText.getText().toString();

        new PostTask().execute("<" + getPackageName() + ">" + status);
    }

    private final class PostTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(StatusActivity.this);
            String username = prefs.getString("username", "");
            String password = prefs.getString("password", "");

            System.out.println("username:" + username + "," + "password:" + password);

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                startActivity(new Intent(StatusActivity.this, SettingsActivity.class));
                return "go to settings";
            }

            YambaClient yambaClient = new YambaClient(username + "", "password");
            try {
                yambaClient.postStatus(strings[0]);
                return "Successfully posted";
            } catch (YambaClientException e) {
                e.printStackTrace();
                return "Failed to post to yamba service";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(StatusActivity.this, s, Toast.LENGTH_LONG).show();
            if (s.startsWith("Successfully"))
                editText.setText("");
        }
    }
}




