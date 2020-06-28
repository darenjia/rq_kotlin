package com.bkjcb.rqapplication.contact

import android.content.Context
import android.content.Intent
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.contact.fragment.ContactChildFragment
import com.hss01248.dialog.StyledDialog
import kotlinx.android.synthetic.main.with_bg_top_bar.*

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
class ContactDetailActivity : ContactActivity() {
    private var type: Int = 0;
    override fun initView() {
        StyledDialog.init(this)
        initTopBar("应急通讯录", appbar)
        val mainFragment = ContactChildFragment.newInstance(type) { user -> alertUserInfo(user) }
        supportFragmentManager
                .beginTransaction()
                .add(R.id.contact_content, mainFragment)
                .commit()
    }

    override fun initData() {
        type = intent.getIntExtra("data", 0)
    }


    companion object {
        fun toActivity(context: Context, type: Int) {
            val intent = Intent(context, ContactDetailActivity::class.java)
            intent.putExtra("data", type)
            context.startActivity(intent)
        }
    }
}