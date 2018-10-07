package com.marsplay.test.marsplay.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.marsplay.test.marsplay.R;
import com.marsplay.test.marsplay.databinding.ActivityImageUploadBinding;
import com.marsplay.test.marsplay.utils.CropImage;
import com.marsplay.test.marsplay.viewmodel.ImageUploadActivityVM;

public class ImageUploadActivity extends AppCompatActivity {

    private ImageUploadActivityVM activityVM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityVM = new ImageUploadActivityVM(this);
        ActivityImageUploadBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_image_upload);
        binding.setViewModel(activityVM);
        activityVM.onCreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                    activityVM.setImageURi(result.getUri());
                Toast.makeText(
                        this, "Cropping successful", Toast.LENGTH_LONG)
                        .show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
