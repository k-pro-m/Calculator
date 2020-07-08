package com.duty.dutycalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelperDemo {

    private final SharedPreferences mPrefs;

    public PreferenceHelperDemo(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String PREF_Key= "login";

    public String getKey() {
        String str = mPrefs.getString(PREF_Key, "");
        return str;
    }

    public void setKey(String pREF_Key) {
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString(PREF_Key, pREF_Key);
        mEditor.apply();
    }
}
