package com.marsplay.test.marsplay.viewmodel;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.marsplay.test.marsplay.R;
import com.marsplay.test.marsplay.adapter.holder.ViewImageRowVH;

import java.net.URL;
import java.util.Observable;

public class ViewImageRowVM extends Observable {

    private URL imageUrl;
    private Context context;
    private ViewImageRowVH holder;

    public ViewImageRowVM(Context context, ViewImageRowVH holder, URL url) {
        this.imageUrl = url;
        this.context = context;
        this.holder = holder;
        setData();
    }

    private void setData() {
        ImageView imageView = holder.itemView.findViewById(R.id.img_view_image_url);
        Glide.with(context).load(imageUrl)
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .into(imageView);
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }
}
