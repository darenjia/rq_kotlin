package com.bkjcb.rqapplication.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bkjcb.rqapplication.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.util.GlideUtil;
import com.bumptech.glide.Glide;

public class LocalImageHolderView extends Holder<String> {
    private ImageView imageView;

    public LocalImageHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        imageView =itemView.findViewById(R.id.banner_item);
    }

    @Override
    public void updateUI(String data) {
        Glide.with(MyApplication.getContext()).load(data).apply(GlideUtil.getRequestOption()).into(imageView);
    }
}