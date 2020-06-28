package com.bkjcb.rqapplication.check

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.OnClick
import com.bkjcb.rqapplication.BaseSimpleActivity
import com.bkjcb.rqapplication.MediaPlayActivity
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.adapter.ImageListAdapter
import com.bkjcb.rqapplication.adapter.ViewPagerAdapter
import com.bkjcb.rqapplication.check.CheckDetailActivity
import com.bkjcb.rqapplication.check.fragment.CheckItemDetailFragment
import com.bkjcb.rqapplication.check.fragment.CheckItemResultFragment
import com.bkjcb.rqapplication.check.model.CheckContentItem
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.check.model.CheckResultItem
import com.bkjcb.rqapplication.check.model.CheckResultItem_
import com.bkjcb.rqapplication.check.retrofit.CheckService
import com.bkjcb.rqapplication.datebase.DataUtil
import com.bkjcb.rqapplication.retrofit.NetworkApi.Companion.getService
import com.bkjcb.rqapplication.util.FileUtil
import com.bkjcb.rqapplication.util.PictureSelectorUtil
import com.bkjcb.rqapplication.util.Utils
import com.bkjcb.rqapplication.view.FooterView
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.tools.DateUtils
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.popup.QMUIListPopup
import com.qmuiteam.qmui.widget.popup.QMUIPopup
import io.objectbox.Box
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_check.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*
import java.util.*

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
open class CheckDetailActivity : BaseSimpleActivity(), ViewPager.OnPageChangeListener, View.OnClickListener {


    protected var checkItem: CheckItem? = null
    private lateinit var mediaList: MutableList<String>
    private lateinit var imageAdapter: ImageListAdapter
    private var currentPage = 0
    protected lateinit var fragmentList: MutableList<Fragment>
    private var listPopup: QMUIListPopup? = null
    protected lateinit var contents: MutableList<String>
    override fun setLayoutID(): Int {
        return R.layout.activity_detail_check
    }

    override fun initView() {
        initTopBar("检查详情", appbar)
        initEmptyView(empty_view)
        showLoadingView()
        check_detail_previous.setOnClickListener(this)
        check_detail_next.setOnClickListener(this)
        check_detail_finish.setOnClickListener(this)
        check_detail_pageNumber.setOnClickListener(this)

    }

    override fun initData() {
        StyledDialog.init(this)
        checkItem = intent.getSerializableExtra("data") as CheckItem
        if (checkItem == null) {
            val id = intent.getLongExtra("id", 0)
            checkItem = DataUtil.getCheckItemBox()[id]
            if (checkItem == null) {
                showErrorView(null)
                return
            }
        }
        checkNetwork()
        if (checkItem!!.status == 3) {
            check_detail_finish.setBackgroundColor(resources.getColor(R.color.colorGreenGray))
            check_detail_finish.isEnabled = false
            check_detail_finish.text = "检查已结束"
        } else if (checkItem!!.status == 0 || checkItem!!.status == 1) {
            checkItem!!.status = 1
            saveDate()
        }
    }

    override fun networkIsOk() {
        initCheckContent()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.check_detail_previous -> if (--currentPage >= 0) {
                check_detail_pager!!.setCurrentItem(currentPage, true)
            } else {
                currentPage = 0
            }
            R.id.check_detail_next -> if (++currentPage < fragmentList.size) {
                check_detail_pager!!.setCurrentItem(currentPage, true)
            }
            R.id.check_detail_finish -> {
                if (checkItem!!.status == 3) {
                    Toast.makeText(this, "当前检查已结束", Toast.LENGTH_SHORT).show()
                    return
                }
                saveResult()
            }
            R.id.check_detail_pageNumber -> showPopList()
            else -> {
            }
        }
    }

    protected open fun saveResult() {
        if (TextUtils.isEmpty(checkItem!!.jianchajieguo)) {
            if (check_detail_pager!!.currentItem == fragmentList.size - 1) {
                showSnackbar(check_detail_pageNumber, "请选择检查结果")
            } else {
                check_detail_pager!!.setCurrentItem(fragmentList.size - 1, true)
            }
        } else {
            showFinishTipDialog()
        }
    }

    protected open fun closeActivity(isSubmit: Boolean) {
        if (isSubmit) {
            checkItem!!.status = 2
        }
        if (currentPage == fragmentList.size - 1) {
            val resultFragment = fragmentList[fragmentList.size - 1] as CheckItemResultFragment
            checkItem!!.beizhu = resultFragment.remark
        }
        saveDate()
        Toast.makeText(this, "检查完成！", Toast.LENGTH_SHORT).show()
        finish()
    }

    protected open fun initCheckContent() {
        disposable = getService(CheckService::class.java)
                .getCheckItem(checkItem!!.zhandianleixing)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ checkContentItemResult ->
                    if (checkContentItemResult.pushState == 200 && checkContentItemResult.datas != null && checkContentItemResult.datas!!.isNotEmpty()) {
                        initCheckData(checkContentItemResult.datas!!)
                        initImageListView()
                        hideEmptyView()
                    } else {
                        getDateFail(checkContentItemResult.pushMsg)
                    }
                }) { throwable -> getDateFail(throwable.message) }
    }

    protected fun getDateFail(detail: String?) {
        showErrorView(detail, View.OnClickListener { initCheckContent() })
    }

    private fun initCheckData(datas: List<CheckContentItem>) {
        fragmentList = ArrayList()
        contents = ArrayList()
        for (item in datas) {
            fragmentList.add(createFragment(item, checkItem!!.c_id))
            saveResultItem(checkItem!!.c_id!!, item.id!!)
            contents.add(item.xuhao.toString() + "." + item.jianchaneirong)
        }
        fragmentList.add(CheckItemResultFragment.newInstances(checkItem))
        contents.add("检查结果")
        val adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        check_detail_pager!!.offscreenPageLimit = 3
        check_detail_pager!!.adapter = adapter
        check_detail_pager!!.addOnPageChangeListener(this)
        updateCurrentPageNumber()
    }

    protected fun createFragment(item: CheckContentItem?, id: String?): Fragment {
        return CheckItemDetailFragment.newInstances(item, id, if (checkItem!!.status == 3) 1 else 0)
    }

    protected open fun saveResultItem(cid: String, uid: String) {
        if (DataUtil.getCheckResultItemBox().query().equal(CheckResultItem_.jianchaid, cid).and().equal(CheckResultItem_.jianchaxiangid, uid).build().count() == 0L) {
            val resultItem = CheckResultItem(cid, uid)
            DataUtil.getCheckResultItemBox().put(resultItem)
        }
    }

    protected fun initImageListView() {
        imageAdapter = ImageListAdapter(R.layout.item_image, checkItem!!.status != 3)
        image_file_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        image_file_view.adapter = imageAdapter
        mediaList = ArrayList()
        if (!TextUtils.isEmpty(checkItem!!.filePath)) {
            mediaList.addAll(checkItem!!.filePath!!.split(","))
        }
        updateCountSize()
        imageAdapter.setNewData(mediaList)
        imageAdapter.setOnItemClickListener { adapter, view, position -> MediaPlayActivity.ToActivity(this@CheckDetailActivity, adapter.getItem(position) as String?) }
        imageAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.item_grid_bt) {
                mediaList.removeAt(position)
                imageAdapter.replaceData(mediaList)
                updateCountSize()
            }
        }
        if (checkItem!!.status != 3) {
            imageAdapter.setFooterView(createFooterView(), 0, LinearLayout.HORIZONTAL)
        }
    }

    private fun updateCountSize() {
        image_count.text = mediaList.size.toString()
    }

    protected fun updateCurrentPageNumber() {
        check_detail_pageNumber.text = "${(currentPage + 1)}/${fragmentList.size}"
    }

    fun showPickImg() {
        PictureSelectorUtil.selectPicture(this, "CheckImage/" + checkItem!!.c_id)
    }

    private fun createFooterView(): View {
        return FooterView.createFooter(View.OnClickListener { showPickImg() })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK) {
            val list = PictureSelector.obtainMultipleResult(data)
            for (media in list) {
                mediaList.add(media.path)
            }
            imageAdapter.replaceData(mediaList)
            updateCountSize()
        }
    }

    override fun onStop() {
        super.onStop()
        /*  if (checkItem.status == 0 || checkItem.status == 1) {
            checkItem.status = 1;
            saveDate();
        }*/
    }

    protected fun saveDate() {
        val builder = StringBuilder()
        if (mediaList.size > 0) {
            for (path in mediaList) {
                builder.append(",").append(path)
            }
            checkItem!!.filePath = builder.substring(1)
        }
        DataUtil.getCheckItemBox().put(checkItem!!)
    }

    override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
    override fun onPageSelected(i: Int) {
        currentPage = i
        updateCurrentPageNumber()
    }

    override fun onPageScrollStateChanged(i: Int) {}
    private fun showPopList() {
        if (listPopup == null) {
            val adapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.simple_list_item, contents)
            val onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
                check_detail_pager!!.currentItem = i
                if (listPopup != null) {
                    listPopup!!.dismiss()
                }
            }
            listPopup = QMUIListPopup(this, QMUIListPopup.DIRECTION_TOP, adapter)
            listPopup!!.create(QMUIDisplayHelper.dp2px(this, 250),
                    QMUIDisplayHelper.dp2px(this, 300),
                    onItemClickListener)
            listPopup!!.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
        }
        listPopup!!.show(check_detail_pageNumber)
    }

    protected fun showFinishTipDialog() {
        StyledDialog.buildIosAlert("提示", "是否结束当前检查？(结束后不可修改)", object : MyDialogListener() {
            override fun onFirst() {
                closeActivity(true)
            }

            override fun onSecond() {
                closeActivity(false)
            }
        }).setBtnText("结束检查", "仅保存", "取消")
                .show()
    }

    companion object {
        fun toActivity(context: Context, checkItem: CheckItem?) {
            val intent = Intent(context, CheckDetailActivity::class.java)
            intent.putExtra("data", checkItem)
            context.startActivity(intent)
        }

        fun toActivity(context: Context, id: Long) {
            val intent = Intent(context, CheckDetailActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }
}