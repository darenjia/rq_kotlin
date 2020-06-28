package com.bkjcb.rqapplication.contact.adapter

import android.view.View
import cn.carbs.android.avatarimageview.library.AvatarImageView
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.contact.model.User
import com.bkjcb.rqapplication.util.Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
class ContactItemAdapter(layoutResId: Int) : BaseQuickAdapter<User, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: User) {
        val iv = helper.getView<View>(R.id.item_avatar) as AvatarImageView
        iv.setTextAndColor(item.username!!.substring(0, 1), Utils.getColor(mContext, helper.layoutPosition))
        helper.setText(R.id.info_tv, item.username)
                .setText(R.id.info_dz, item.duty)
                .setText(R.id.info_qx, if (item.unit != null) item.unit!!.districtName else "")
                .setGone(R.id.class_a, item.role_a == 1)
                .setGone(R.id.class_b, item.role_b == 1)
                .setGone(R.id.class_c, item.role_c == 1)
                .setGone(R.id.class_d, item.role_d == 1)
    }

    private fun showEmptyView() {
        setEmptyView(R.layout.empty_textview)
    }

    override fun setNewData(data: List<User>?) {
        super.setNewData(data)
        if (data == null || data.isEmpty()) {
            showEmptyView()
        }
    }
}