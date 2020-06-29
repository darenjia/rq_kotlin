package com.bkjcb.rqapplication.contact

import android.content.Context
import android.content.Intent
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.contact.fragment.ContactChildFragment
import com.bkjcb.rqapplication.contact.fragment.ContactSearchFragment
import com.bkjcb.rqapplication.contact.model.User
import com.hss01248.dialog.StyledDialog
import kotlinx.android.synthetic.main.with_bg_top_bar.*

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
class ContactSearchActivity : ContactActivity() {
    override fun setLayoutID(): Int {
        return R.layout.activity_main_contact
    }

    override fun initView() {
        StyledDialog.init(this)
        initTopBar("搜索",appbar)
    }

    override fun initData() {
        val mainFragment = ContactSearchFragment()
        mainFragment.initListener(object :ContactSearchFragment.OnClickListener{
            override fun onClick(user: User) {
                alertUserInfo(user)
            }
        });
        supportFragmentManager.beginTransaction().add(R.id.contact_content, mainFragment).commit()
    }

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, ContactSearchActivity::class.java)
            context.startActivity(intent)
        }
    }
}