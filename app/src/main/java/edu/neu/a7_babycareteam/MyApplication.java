package edu.neu.a7_babycareteam;

import android.app.Application;

public class MyApplication extends Application {


    private String loginUserName;
    private static MyApplication mApp;

    public static MyApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }
}
