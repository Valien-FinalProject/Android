package com.theironyard.finalproject;

import android.app.Application;

/**
 * Created by vasantia on 8/19/16.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        ParentChoreService.initChorePrefs(this);
    }
}
