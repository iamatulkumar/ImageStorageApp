package com.marsplay.test.marsplay.viewmodel;


import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.marsplay.test.marsplay.R;
import com.marsplay.test.marsplay.activity.ImageUploadActivity;
import com.marsplay.test.marsplay.fileutils.Constants;
import com.marsplay.test.marsplay.fileutils.NetworkUtils;
import com.marsplay.test.marsplay.fileutils.Util;
import com.marsplay.test.marsplay.utils.CropImage;
import com.marsplay.test.marsplay.utils.CropImageView;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Observable;

public class ImageUploadActivityVM extends Observable {

    /**
     * Activity Context
     */
    private ImageUploadActivity activity;

    /**
     * Boolean for image upload
     */
    private boolean isImageAvailable;

    /**
     * Image Upload URI
     */
    private Uri imageURi;

    /**
     * Aws utils
     */
    private Util util;

    /**
     * The TransferUtility is the primary class for managing transfer to S3
     */
    private TransferUtility transferUtility;

    public ImageUploadActivityVM(ImageUploadActivity activity) {
        this.activity = activity;
    }

    public void onCreate() {
        util = new Util();
        transferUtility = util.getTransferUtility(activity);
    }

    /**
     * Choose image for cropping
     */
    public void onChooseImage() {
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(activity);
    }

    /**
     * Upload Image on Aws
     */
    public void onUploadImage() {

        if (!NetworkUtils.isConnectingToInternet(activity)) {
            showToast("Please check your internet connection");
            return;
        }

        if (!isImageAvailable) {
            showToast("This image is already present try to upload another image");
            return;
        }

        Uri uri = imageURi;
        try {
            String path = getPath(uri);
            beginUpload(path);
        } catch (URISyntaxException e) {
            showToast("Unable to get the file from the given URI. See error log for details");
            Log.e("upload image", "Unable to upload file from the given uri", e);
        }
    }


    public void setImageURi(Uri uRi) {
        this.imageURi = uRi;
        isImageAvailable = true;
        ((ImageView) activity.findViewById(R.id.img_image_upload_preview)).setImageURI(uRi);
    }

    private void beginUpload(String filePath) {
        if (filePath == null) {
            showToast("Could not find the filepath of the selected file");
            return;
        }

        File file = new File(filePath);
        TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME,
                file.getName(),
                file);

        observer.setTransferListener(new UploadListener());
    }

    @SuppressLint("NewApi")
    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(activity, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = activity.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    class UploadListener implements TransferListener {

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e("upload image", "Error during upload: " + id, e);
            showToast("Something went wrong, please try again later.");
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d("upload image", String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d("upload image", "onStateChanged: " + id + ", " + newState);
            if (newState == TransferState.COMPLETED) {
                isImageAvailable = false;
                showToast("Upload Successful");
            }

        }
    }

    private void showToast(String message) {
        Toast.makeText(activity,
                message,
                Toast.LENGTH_LONG).show();
    }

    public void onClickClose() {
        activity.finish();
    }

}
