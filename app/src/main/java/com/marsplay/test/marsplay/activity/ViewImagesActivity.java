package com.marsplay.test.marsplay.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.marsplay.test.marsplay.R;
import com.marsplay.test.marsplay.databinding.ActivityViewImagesBinding;
import com.marsplay.test.marsplay.viewmodel.ViewImagesActivityVM;

public class ViewImagesActivity extends AppCompatActivity {

    private ViewImagesActivityVM activityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityVM = new ViewImagesActivityVM(this);
        ActivityViewImagesBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_view_images);
        binding.setViewModel(activityVM);
        activityVM.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the file list.
        activityVM.onResume();
    }

}
