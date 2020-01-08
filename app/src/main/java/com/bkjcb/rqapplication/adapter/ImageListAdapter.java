package com.bkjcb.rqapplication.adapter;

import android.widget.ImageView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.util.GlideUtil;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;

/**
 * Created by DengShuai on 2020/1/8.
 * Description :
 */
public class ImageListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private boolean isShowDelete = true;

    public ImageListAdapter(int layoutResId) {
        super(layoutResId);
    }

    public ImageListAdapter(int layoutResId, boolean isShowDelete) {
        super(layoutResId);
        this.isShowDelete = isShowDelete;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView imageView = helper.getView(R.id.item_grid_image);
        if (item.contains("http")) {
            Glide.with(helper.itemView).load(item).apply(GlideUtil.getRequestOption()).into(imageView);
        } else {
            Glide.with(helper.itemView).load(new File(item)).apply(GlideUtil.getRequestOption()).into(imageView);
        }
        helper.setGone(R.id.item_grid_bt, isShowDelete);
        helper.addOnClickListener(R.id.item_grid_bt);
    }

}
