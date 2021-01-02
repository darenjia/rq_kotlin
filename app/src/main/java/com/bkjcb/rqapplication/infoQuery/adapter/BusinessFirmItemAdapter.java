package com.bkjcb.rqapplication.infoQuery.adapter;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.infoQuery.model.SimpleBusinessFirmModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2020/12/30.
 * Description :
 */
public class BusinessFirmItemAdapter extends BaseQuickAdapter<SimpleBusinessFirmModel, BaseViewHolder> {
    public BusinessFirmItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, SimpleBusinessFirmModel item) {
        helper.setText(R.id.station_name,item.getQiyemingcheng());
    }

    public void showEmptyView(){
        setEmptyView(R.layout.empty_textview);
    }

    public void showErrorView(){
        setEmptyView(R.layout.error_view);
    }
}
