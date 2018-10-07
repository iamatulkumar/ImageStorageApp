package com.marsplay.test.marsplay.viewmodel;


import android.content.Intent;

import com.marsplay.test.marsplay.activity.ImageUploadActivity;
import com.marsplay.test.marsplay.activity.MainActivity;
import com.marsplay.test.marsplay.activity.ViewImagesActivity;

import java.util.Observable;

public class MainActivityVM extends Observable {

    private MainActivity activity;

    public MainActivityVM(MainActivity mainActivity) {
        this.activity = mainActivity;
    }

    public void onCreate() {

    }

   public void onClickUploadImage() {
        activity.startActivity(new Intent(activity, ImageUploadActivity.class));
   }

    public void onClickShowImageList() {
        activity.startActivity(new Intent(activity, ViewImagesActivity.class));
    }

}
