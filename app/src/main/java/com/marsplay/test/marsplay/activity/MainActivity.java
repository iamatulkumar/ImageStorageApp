package com.marsplay.test.marsplay.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import com.marsplay.test.marsplay.R;
import com.marsplay.test.marsplay.databinding.ActivityMainBinding;
import com.marsplay.test.marsplay.utils.CropImage;
import com.marsplay.test.marsplay.utils.CropImageView;
import com.marsplay.test.marsplay.viewmodel.MainActivityVM;

public class MainActivity extends AppCompatActivity {

    private MainActivityVM activityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityVM = new MainActivityVM(this);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(activityVM);
        activityVM.onCreate();

    }

}
