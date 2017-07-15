package com.example.edelsteindo.androidproject;

import android.app.Application;
import android.content.Context;

/**
 * Created by Dor-New on 29/05/2017.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }


    public static Context getMyContext(){
        return context;
    }
}
