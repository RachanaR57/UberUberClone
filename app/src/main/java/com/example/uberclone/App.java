package com.example.uberclone;

import android.app.AppComponentFactory;
import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("6hfmOB0YRSGc2q39UwkGJHMqBOfyifNUxbmhQF3T")
                .clientKey("sNPJt1hRTwZs9rTKDWw28bZV7YBXbTnCUz9jt9Ga")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
