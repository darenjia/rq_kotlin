package com.bkjcb.rqapplication.base

import android.view.View
import android.widget.AdapterView
import butterknife.BindView
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.actionregister.ActionRegisterActivity
import com.bkjcb.rqapplication.base.adapter.LocalImageHolderView
import com.bkjcb.rqapplication.base.adapter.MenuGridAdapter
import com.bkjcb.rqapplication.check.CheckMainActivity
import com.bkjcb.rqapplication.contact.ContactActivity
import com.bkjcb.rqapplication.emergency.EmergencyActivity
import com.bkjcb.rqapplication.gasrecord.GasUserRecordActivity
import com.bkjcb.rqapplication.base.model.MenuItem
import com.bkjcb.rqapplication.treatmentdefect.DefectTreatmentMainActivity
import com.bkjcb.rqapplication.base.view.MyGridView
import kotlinx.android.synthetic.main.activity_mian.*
import java.util.*

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
class MainActivity : BaseSimpleActivity() {

    private var userType = 0
    override fun setLayoutID(): Int {
        return R.layout.activity_mian
    }

    override fun initData() {
        initUserType()
        initMenu()
        initBanner()
        initMessage()
    }

    private fun initUserType() {
        userType = when (MyApplication.getUser().userleixing) {
            "市用户" -> {
                0
            }
            "区用户" -> {
                1
            }
            else -> {
                2
            }
        }
    }

    private fun initMessage() {}

    private fun initMenu() {
        val adapter = MenuGridAdapter(initMenuItems())
        main_menu_grid.adapter = adapter
        main_menu_grid.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = adapter.getItem(position) as MenuItem
            if (item.purview) {
                when (item.type) {
                    1 -> GasUserRecordActivity.toActivity(this@MainActivity)
                    2 -> CheckMainActivity.toActivity(this@MainActivity, 0)
                    3 -> CheckMainActivity.toActivity(this@MainActivity, 1)
                    4 -> ContactActivity.toActivity(this@MainActivity)
                    5 -> ActionRegisterActivity.toActivity(this@MainActivity)
                    6 -> EmergencyActivity.toActivity(this@MainActivity)
                    7 -> DefectTreatmentMainActivity.toActivity(this@MainActivity)
                    8 -> SettingActivity.toActivity(this@MainActivity)
                    else -> {
                    }
                }
            } else {
                showSnackbar(main_menu_grid, "该功能暂未开放！")
            }
        }
    }

    private fun initMenuItems(): List<MenuItem> =
            when (userType) {
                0 -> {
                    MenuItem.getMunicipalMenu()
                }
                1 -> {
                    MenuItem.getDistrictMenu()
                }
                else -> {
                    MenuItem.getStreetMenu()
                }
            }


    private fun initBanner() {
        val list: MutableList<String> = ArrayList()
        list.add("https://bucket-shgas.oss-cn-shanghai.aliyuncs.com/portalWebSite/static/home/home3.jpg")
        list.add("https://bucket-shgas.oss-cn-shanghai.aliyuncs.com/portalWebSite/static/home/home2.jpg")
        list.add("https://bucket-shgas.oss-cn-shanghai.aliyuncs.com/portalWebSite/static/home9.jpg")
        (convenientBanner as ConvenientBanner<String>).setPages(object : CBViewHolderCreator {
            override fun createHolder(itemView: View): Holder<String> {
                return LocalImageHolderView(itemView)
            }

            override fun getLayoutId(): Int {
                return R.layout.item_banner_view
            }
        }, list)
                .startTurning(5000)
                .setPageIndicator(intArrayOf(R.drawable.vector_drawable_dot_normal, R.drawable.vector_drawable_dot_focus))
    }

}