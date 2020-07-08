package com.duty.dutycalculator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {

    Retrofit retrofit = null;
    String baseApiUrl = "http://toluowolabi.com/ImportDutyCalculation/";
    PreferenceHelperDemo preferenceHelperDemo;


    @Override
    public void onCreate() {
        super.onCreate();

        setupRetrofit();
        preferenceHelperDemo = new PreferenceHelperDemo(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    public void setupRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        if (retrofit == null)
            setupRetrofit();

        return retrofit;
    }

    static MyApplication myApplication;

    public static MyApplication getInstance() {
        if (myApplication == null)
            myApplication = new MyApplication();

        return myApplication;
    }

    boolean isEmptyText(String text) {
        if (text == null || text.isEmpty() || text.trim().length() < 1)
            return true;
        else
            return false;
    }

    public void setLoginLogout(Context context, boolean isLogin) {
        preferenceHelperDemo = new PreferenceHelperDemo(context);
        preferenceHelperDemo.setKey(isLogin ? "1" : "0");
    }

    public boolean isLogin(Context context) {
        preferenceHelperDemo = new PreferenceHelperDemo(context);
        return preferenceHelperDemo.getKey().equalsIgnoreCase("1");
    }

}
