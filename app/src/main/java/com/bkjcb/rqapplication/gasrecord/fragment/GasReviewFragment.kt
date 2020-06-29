package com.bkjcb.rqapplication.gasrecord.fragment

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.OnClick
import com.allen.library.SuperTextView
import com.bkjcb.rqapplication.Constants
import com.bkjcb.rqapplication.MediaPlayActivity
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.adapter.FileListAdapter
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment
import com.bkjcb.rqapplication.gasrecord.model.ReviewRecord
import com.bkjcb.rqapplication.interfaces.OnPageButtonClickListener
import com.bkjcb.rqapplication.model.MediaFile
import com.bkjcb.rqapplication.util.PictureSelectorUtil
import com.bkjcb.rqapplication.util.Utils
import com.bkjcb.rqapplication.view.FooterView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hss01248.dialog.StyledDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_review_detail.*
import java.util.*

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
class GasReviewFragment : BaseSimpleFragment(), DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {


    private var pickerDialog: DatePickerDialog? = null
    private var currentTextView: TextView? = null
    private var record: ReviewRecord? = null
    private var isCanChange = true
    private var listener: OnPageButtonClickListener<*>? = null
    private lateinit var imageAdapter: FileListAdapter
    fun setRecord(record: ReviewRecord?) {
        this.record = record
    }

    fun setListener(listener: OnPageButtonClickListener<*>?) {
        this.listener = listener
    }

    override fun initView() {
        isCanChange = record!!.status == 1
        setText(info_username, record!!.yonghuming)
        if (isCanChange) {
            check_yhq.setSwitchCheckedChangeListener { buttonView, isChecked ->
                setViewVisibility(content_layout, !isChecked)
                setViewVisibility(check_yhq_layout, isChecked)
                record!!.yehuaqi_shiyong = if (isChecked) "是" else "否"
            }
            check_tyq_change.setSwitchCheckedChangeListener { buttonView, isChecked ->
                setViewVisibility(check_tyq_layout, isChecked)
                record!!.tiaoyaqi_zhenggai = if (isChecked) "是" else "否"
            }
            check_ljg_change.setSwitchCheckedChangeListener { buttonView, isChecked ->
                setViewVisibility(check_ljg_layout, isChecked)
                record!!.lianjieguan_zhenggai = if (isChecked) "是" else "否"
            }
            check_rj_change.setSwitchCheckedChangeListener { buttonView, isChecked ->
                setViewVisibility(check_rj_layout, isChecked)
                record!!.ranju_zhenggai = if (isChecked) "是" else "否"
            }
            //@OnClick(R.id.info_station, R.id.check_yhq_time, R.id.check_tyq_time, R.id.check_ljg_time, R.id.check_rj_time, R.id.record_submit)
            info_station.setOnClickListener(this)
            check_yhq_time.setOnClickListener(this)
            check_tyq_time.setOnClickListener(this)
            check_ljg_time.setOnClickListener(this)
            check_rj_time.setOnClickListener(this)
            record_submit.setOnClickListener(this)
        } else {
            setText(info_unit, record!!.jianchadanwei)
            setText(info_station, record!!.jianchariqi)
            check_result_radio_ok.isEnabled = false
            check_result_radio_failure.isEnabled = false
            setChecked(check_yhq, "是" == record!!.yehuaqi_shiyong, check_yhq_layout, check_yhq_time, record!!.tuihuriqi)
            if ("是" == record!!.yehuaqi_shiyong) {
                setViewVisibility(content_layout, false)
            }
            setText(check_tyq_count, record!!.tiaoyaqi_geshu)
            setChecked(check_tyq_change, "是" == record!!.tiaoyaqi_zhenggai, check_tyq_layout, check_tyq_time, record!!.tiaoyaqi_zhenggairiqi)
            setText(check_ljg_count, record!!.lianjieguan_geshu)
            setChecked(check_ljg_change, "是" == record!!.lianjieguan_zhenggai, check_ljg_layout, check_ljg_time, record!!.lianjieguan_zhenggairiqi)
            setText(check_rj_count, record!!.ranju_geshu)
            setChecked(check_rj_change, "是" == record!!.ranju_zhenggai, check_rj_layout, check_rj_time, record!!.ranju_zhenggairiqi)
        }
        initCheckBox()
    }

    override fun initData() {
        if (isCanChange) {
            StyledDialog.init(context)
            record!!.tiaoyaqi_zhenggai = "否"
            record!!.ranju_zhenggai = "否"
            record!!.yehuaqi_shiyong = "否"
            record!!.lianjieguan_zhenggai = "否"
        } else {
            setViewVisibility(record_submit, false)
        }
        initFileView()
    }


  override  fun onClick(v: View) {
        if (v.id != R.id.record_submit) {
            when (v.id) {
                R.id.info_station -> currentTextView = info_station
                R.id.check_yhq_time -> currentTextView = check_yhq_time
                R.id.check_tyq_time -> currentTextView = check_tyq_time
                R.id.check_ljg_time -> currentTextView = check_ljg_time
                R.id.check_rj_time -> currentTextView = check_rj_time
                else -> {
                }
            }
            createDatePicker()
        } else {
            collectParams()
            if (verifyAllData()) {
                listener!!.onNext(imageAdapter.data)
            }
        }
    }

    private fun setViewVisibility(v: View?, isShow: Boolean) {
        v!!.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun createDatePicker() {
        if (pickerDialog == null) {
            val calendar = Utils.obtainCalendar()
            pickerDialog = DatePickerDialog.newInstance(
                    this,
                    calendar[Calendar.YEAR],  // Initial year selection
                    calendar[Calendar.MONTH],  // Initial month selection
                    calendar[Calendar.DAY_OF_MONTH] // Inital day selection
            )
        }
        if (!pickerDialog!!.isAdded) {
            pickerDialog!!.show(activity!!.fragmentManager, "DatePickerDialog")
        }
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        currentTextView!!.text = String.format(Locale.CHINESE, "%s-%s-%s", year, monthOfYear + 1, dayOfMonth)
    }

    private fun initCheckBox() {
        if (!TextUtils.isEmpty(record!!.jianchajieguo)) {
            if ("合格" == record!!.jianchajieguo) {
                check_result_radioGroup.check(R.id.check_result_radio_ok)
                changeTextColor(true)
            } else {
                check_result_radioGroup.check(R.id.check_result_radio_failure)
                changeTextColor(false)
            }
        } else {
            check_result_radioGroup.setOnCheckedChangeListener(this)
        }
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        if (checkedId == R.id.check_result_radio_ok) {
            record!!.jianchajieguo = "合格"
            changeTextColor(true)
        } else {
            record!!.jianchajieguo = "不合格"
            changeTextColor(false)
        }
    }

    private fun changeTextColor(isOk: Boolean) {
        if (isOk) {
            check_result_radio_ok.setTextColor(resources.getColor(R.color.colorText))
            check_result_radio_failure.setTextColor(resources.getColor(R.color.colorSecondDrayText))
        } else {
            check_result_radio_ok.setTextColor(resources.getColor(R.color.colorSecondDrayText))
            check_result_radio_failure.setTextColor(resources.getColor(R.color.colorText))
        }
    }

    private fun setText(view: TextView?, value: String) {
        view!!.text = value
        view.isEnabled = false
    }

    private fun setChecked(box: SuperTextView?, enable: Boolean, layout: View?, view: TextView?, value: String) {
        box!!.switchIsChecked = enable
        box.setSwitchClickable(false)
        setViewVisibility(layout, enable)
        if (enable && view != null) {
            setText(view, value)
        }
    }

    private fun getText(view: TextView?): String {
        return view!!.text.toString()
    }

    private fun verifyAllData(): Boolean {
        return verify(info_unit, record!!.jianchadanwei, "请填写检查单位") &&
                verify(info_station, record!!.jianchariqi, "请选择检查日期") &&
                verify(info_name_title, record!!.jianchajieguo, "请选择检查结果") &&
                verify(file_info, "请添加三到五张照片") &&
                ("是" == record!!.yehuaqi_shiyong &&
                        verify(check_yhq_time, record!!.tuihuriqi, "请选择退户日期") || ("否" == record!!.yehuaqi_shiyong &&
                        verify(check_tyq_count, record!!.tiaoyaqi_geshu, "请填写已置换调压器个数")
                        &&
                        ("是" == record!!.tiaoyaqi_zhenggai &&
                                verify(check_tyq_time, record!!.tiaoyaqi_zhenggairiqi, "请选择整改日期") || "否" == record!!.tiaoyaqi_zhenggai)
                        &&
                        verify(check_ljg_count, record!!.lianjieguan_geshu, "请填写已置换连接管个数")
                        &&
                        ("是" == record!!.lianjieguan_zhenggai &&
                                verify(check_ljg_time, record!!.lianjieguan_zhenggairiqi, "请选择整改日期") || "否" == record!!.lianjieguan_zhenggai)
                        &&
                        verify(check_rj_count, record!!.ranju_geshu, "请填写已置换调压器个数") &&
                        ("是" == record!!.ranju_zhenggai &&
                                verify(check_rj_time, record!!.ranju_zhenggairiqi, "请选择整改日期") || "否" == record!!.ranju_zhenggai)))
    }

    private fun verify(view: View?, value: String, tip: String): Boolean {
        if (TextUtils.isEmpty(value)) {
            view!!.requestFocus()
            Toast.makeText(context, tip, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun verify(view: View?, tip: String): Boolean {
        val size = imageAdapter.data.size
        if (size < 3 || size > 6) {
            view!!.requestFocus()
            Toast.makeText(context, tip, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun collectParams() {
        record!!.jianchariqi = getText(info_station)
        record!!.jianchadanwei = getText(info_unit)
        record!!.tiaoyaqi_geshu = getText(check_tyq_count)
        record!!.tiaoyaqi_zhenggairiqi = getText(check_tyq_time)
        record!!.lianjieguan_geshu = getText(check_ljg_count)
        record!!.lianjieguan_zhenggairiqi = getText(check_ljg_time)
        record!!.ranju_geshu = getText(check_rj_count)
        record!!.ranju_zhenggairiqi = getText(check_rj_time)
        record!!.tuihuriqi = getText(check_yhq_time)
    }

    private fun createFooterView(): View {
        return FooterView.createFooter(View.OnClickListener {
            showPickImg()
        })
    }

    private fun initFileView() {
        file_info.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = FileListAdapter(R.layout.item_image, isCanChange)
        file_info.adapter = imageAdapter
        if (isCanChange) {
            imageAdapter.setFooterView(createFooterView(), 0, LinearLayout.HORIZONTAL)
        }
        imageAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> MediaPlayActivity.ToActivity(context, (adapter.getItem(position) as MediaFile?)!!.path) }
        imageAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.item_grid_bt) {
                imageAdapter.remove(position)
                refreshFileCount()
            }
        }
        if (!TextUtils.isEmpty(record!!.phoneftp)) {
            val paths = record!!.phoneftp.split(",".toRegex()).toTypedArray()
            for (path in paths) {
                val mediaFile = MediaFile()
                mediaFile.path = Constants.IMAGE_URL + path
                mediaFile.isLocal = false
                imageAdapter.addData(mediaFile)
            }
        }
        refreshFileCount()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            val list = PictureSelector.obtainMultipleResult(data)
            handleMedia(list)
        }
    }

    private fun handleMedia(list: List<LocalMedia>) {
        var mediaFile: MediaFile
        for (media in list) {
            mediaFile = MediaFile()
            mediaFile.type = media.mimeType
            mediaFile.path = media.compressPath
            imageAdapter.addData(mediaFile)
        }
        refreshFileCount()
    }

    private fun refreshFileCount() {
        file_count.text = imageAdapter.data.size.toString()
    }

    fun showPickImg() {
       PictureSelectorUtil.selectPicture(this,"GasImage",true)
    }

    override fun initResID(): Int {
        return R.layout.fragment_review_detail
    }

    companion object {
        fun newInstance(record: ReviewRecord?): GasReviewFragment {
            val fragment = GasReviewFragment()
            fragment.setRecord(record)
            return fragment
        }

        fun newInstance(record: ReviewRecord?, listener: OnPageButtonClickListener<*>?): GasReviewFragment {
            val fragment = GasReviewFragment()
            fragment.setRecord(record)
            fragment.setListener(listener)
            return fragment
        }
    }
}