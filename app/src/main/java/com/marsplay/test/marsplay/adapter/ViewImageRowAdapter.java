package com.marsplay.test.marsplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marsplay.test.marsplay.R;
import com.marsplay.test.marsplay.adapter.holder.ViewImageRowVH;
import com.marsplay.test.marsplay.viewmodel.ViewImageRowVM;

import java.net.URL;
import java.util.ArrayList;

public class ViewImageRowAdapter extends RecyclerView.Adapter<ViewImageRowVH> {

    private ArrayList<URL> imageUrlList;
    private Context context;

    public ViewImageRowAdapter(Context context, ArrayList<URL> imageUrlList) {
        this.imageUrlList = imageUrlList;
        this.context = context;
    }

    @Override
    public ViewImageRowVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_image_row, null);
        return new ViewImageRowVH(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewImageRowVH holder, int position) {
        ViewImageRowVM viewImageRowVM = new ViewImageRowVM(context, holder, imageUrlList.get(position));
        holder.getBinding().setViewModel(viewImageRowVM);
    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }

    public ArrayList<URL> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(ArrayList<URL> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
}
