package com.bkjcb.rqapplication.infoQuery.adapter;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.infoQuery.model.BaseFirmModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2020/12/30.
 * Description :
 */
public class FirmItemAdapter extends BaseQuickAdapter<BaseFirmModel, BaseViewHolder> {
    public FirmItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseFirmModel item) {
        helper.setText(R.id.station_name,item.name);
    }

    public void showEmptyView(){
        setEmptyView(R.layout.empty_textview);
    }
    public void showLoadingView(){
        setEmptyView(R.layout.loading_view);
    }

    public void showErrorView(){
        setEmptyView(R.layout.error_view);
    }
}
