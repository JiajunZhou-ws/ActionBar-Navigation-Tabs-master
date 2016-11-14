package com.djandroid.jdroid.Eauditing;

import android.app.Application;
import android.os.Build;


/**
 * Created by dhawal sodha parmar on 4/29/2015.
 */



public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static boolean isLollipop(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return true;
        }
        return false;
    }

}
