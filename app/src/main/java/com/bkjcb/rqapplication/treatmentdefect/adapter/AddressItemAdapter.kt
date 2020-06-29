package com.bkjcb.rqapplication.treatmentdefect.adapter

import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult.UserInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by DengShuai on 2019/10/22.
 * Description :
 */
class AddressItemAdapter(layoutResId: Int) : BaseQuickAdapter<UserInfo, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: UserInfo) {
        helper.setText(R.id.road_name, item.userName)
                .setText(R.id.road_address, item.userAddress)
                .setVisible(R.id.code, item.yijiandang == "1")
    }
}