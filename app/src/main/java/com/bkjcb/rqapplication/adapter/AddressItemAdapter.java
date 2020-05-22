package com.bkjcb.rqapplication.adapter;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.UserInfoResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by DengShuai on 2019/10/22.
 * Description :
 */
public class AddressItemAdapter extends BaseQuickAdapter<UserInfoResult.UserInfo, BaseViewHolder> {
    public AddressItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInfoResult.UserInfo item) {
        helper.setText(R.id.road_name, item.getUserName())
                .setText(R.id.road_address, item.getUserAddress())
                .setVisible(R.id.code, item.getYijiandang().equals("1"));
    }
}
