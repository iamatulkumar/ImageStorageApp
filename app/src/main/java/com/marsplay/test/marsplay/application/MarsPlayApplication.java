package com.marsplay.test.marsplay.application;

import android.app.Application;
import android.content.Intent;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferService;

public class MarsPlayApplication extends Application {
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!

        // Network service
        getApplicationContext().startService(new Intent(getApplicationContext(), TransferService.class));
    }
}
