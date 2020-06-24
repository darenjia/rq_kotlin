package com.bkjcb.rqapplication.contact.adapter;

import android.support.annotation.Nullable;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.contact.model.Level;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by DengShuai on 2020/3/4.
 * Description :
 */
public class UnitAdapter extends BaseQuickAdapter<Level, BaseViewHolder> {
    public UnitAdapter(int layoutResId) {
        super(layoutResId);
    }

    public UnitAdapter(int layoutResId, @Nullable List<Level> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Level item) {
        helper.setText(R.id.view_text,item.getDepartmentnamea());
    }
}
