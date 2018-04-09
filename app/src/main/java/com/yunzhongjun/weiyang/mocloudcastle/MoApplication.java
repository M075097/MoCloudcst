package com.yunzhongjun.weiyang.mocloudcastle;

import android.app.Application;

/**
 * Created by weiyang on 2018/4/9 0009.
 */

public class MoApplication extends Application {
    private static MoApplication instance = null;

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public static MoApplication getInstance() {
        return instance;
    }
}
