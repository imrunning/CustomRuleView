package com.yuefor.customruleview;

import android.app.Application;

/**
 * Created by CTWLPC on 2017/5/5.
 */

public class CustomApplication extends Application {
    private static CustomApplication mHallApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        mHallApplication = this;
    }

    public static Application getGlobalContext() {
        return mHallApplication;
    }
}
