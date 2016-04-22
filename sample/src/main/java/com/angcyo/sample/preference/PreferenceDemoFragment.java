package com.angcyo.sample.preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.angcyo.sample.R;


public class PreferenceDemoFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_setting);
    }
}
