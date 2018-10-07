package com.marsplay.test.marsplay.adapter.holder;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.marsplay.test.marsplay.databinding.ViewImageRowVMBinding;

public class ViewImageRowVH extends RecyclerView.ViewHolder {

   private ViewImageRowVMBinding binding;

    public ViewImageRowVH(View itemView) {
        super(itemView);

        binding= DataBindingUtil.bind(itemView);
    }

    public ViewImageRowVMBinding getBinding() {
        return binding;
    }

    public void setBinding(ViewImageRowVMBinding binding) {
        this.binding = binding;
    }
}
