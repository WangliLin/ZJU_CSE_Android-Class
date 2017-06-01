package cn.zju.id21632120.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import cn.zju.id21632120.R;

/**
 * Created by Wangli on 2017/6/1.
 */

public class MyPreferenceFragment extends PreferenceFragment {

    public MyPreferenceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
