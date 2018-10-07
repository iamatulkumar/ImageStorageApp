package com.marsplay.test.marsplay.viewmodel;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.marsplay.test.marsplay.R;
import com.marsplay.test.marsplay.activity.ViewImagesActivity;
import com.marsplay.test.marsplay.adapter.ViewImageRowAdapter;
import com.marsplay.test.marsplay.fileutils.Constants;
import com.marsplay.test.marsplay.fileutils.ItemOffsetDecoration;
import com.marsplay.test.marsplay.fileutils.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ViewImagesActivityVM extends Observable {

    private ViewImagesActivity activity;
    private ViewImageRowAdapter adapter;
    private AmazonS3Client amazonS3Client;
    private Util util;

    private ArrayList<URL> imageUrlList;

    private RecyclerView recyclerView;

    public ViewImagesActivityVM(ViewImagesActivity activity) {
        this.activity = activity;
    }

    public void onCreate() {
        util = new Util();
        amazonS3Client = util.getS3Client(activity);
        imageUrlList = new ArrayList<>();
        recyclerView = activity.findViewById(R.id.rv_view_image);

        setData();
    }

    public void onResume() {
        new GetFileListTask().execute();
    }

    private void setData() {
        adapter = new ViewImageRowAdapter(activity, imageUrlList);

        recyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(activity, R.dimen.dp4);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerView.setAdapter(adapter);
    }

    private class GetFileListTask extends AsyncTask<Void, Void, Void> {
        // The list of objects we find in the S3 bucket
        private List<S3ObjectSummary> s3ObjList;
        // A dialog to let the user know we are retrieving the files
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(activity,
                    activity.getString(R.string.refreshing),
                    activity.getString(R.string.please_wait));
        }

        @Override
        protected Void doInBackground(Void... inputs) {
            s3ObjList = amazonS3Client.listObjects(Constants.BUCKET_NAME).getObjectSummaries();

            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 60 * 60;
            expiration.setTime(expTimeMillis);

            for (S3ObjectSummary summary : s3ObjList) {
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(Constants.BUCKET_NAME, summary.getKey())
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
                imageUrlList.add(url);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            adapter.setImageUrlList(imageUrlList);
            adapter.notifyDataSetChanged();

        }
    }

    public void onClickClose() {
        activity.finish();
    }

}
