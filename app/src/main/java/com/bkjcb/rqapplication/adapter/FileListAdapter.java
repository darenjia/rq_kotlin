package com.bkjcb.rqapplication.adapter;

import android.widget.ImageView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.MediaFile;
import com.bkjcb.rqapplication.util.GlideUtil;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2020/1/8.
 * Description :
 */
public class FileListAdapter extends BaseQuickAdapter<MediaFile, BaseViewHolder> {
    private boolean isShowDelete = true;

    public FileListAdapter(int layoutResId) {
        super(layoutResId);
    }

    public FileListAdapter(int layoutResId, boolean isShowDelete) {
        super(layoutResId);
        this.isShowDelete = isShowDelete;
    }

    @Override
    protected void convert(BaseViewHolder helper, MediaFile item) {
        ImageView imageView = helper.getView(R.id.item_grid_image);
        if (item.isLocal()) {
            if (item.getType() == 1) {
                Glide.with(helper.itemView).load(item.getPath()).apply(GlideUtil.getRequestOption()).into(imageView);
            } else {
                Glide.with(helper.itemView).load(getFileImage(item)).apply(GlideUtil.getRequestOption()).into(imageView);
            }
        } else {
            Glide.with(helper.itemView).load(item.getPath()).apply(GlideUtil.getRequestOption()).into(imageView);
        }
        helper.setGone(R.id.item_grid_bt, isShowDelete);
        helper.addOnClickListener(R.id.item_grid_bt);
    }

    private int getFileImage(MediaFile mediaFile) {
        int resId = R.drawable.error_image;
        switch (mediaFile.getType()) {
            case 2:
                resId = R.drawable.vector_drawable_file_video;
                break;
            case 3:
                resId = R.drawable.vector_drawable_file_audio;
                break;
            case 4:
                resId = R.drawable.vector_drawable_file_table;
                break;
            default:

        }
        return resId;
    }
}
