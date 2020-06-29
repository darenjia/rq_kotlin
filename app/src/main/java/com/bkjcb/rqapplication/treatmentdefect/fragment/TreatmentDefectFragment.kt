package com.bkjcb.rqapplication.treatmentdefect.fragment

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
import com.bkjcb.rqapplication.Constants
import com.bkjcb.rqapplication.MediaPlayActivity
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.adapter.FileListAdapter
import com.bkjcb.rqapplication.model.MediaFile
import com.bkjcb.rqapplication.treatmentdefect.model.DefectDetail
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel
import com.bkjcb.rqapplication.util.Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jaredrummler.materialspinner.MaterialSpinner
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_treatment_defect.*
import java.util.*

/**
 * Created by DengShuai on 2020/6/12.
 * Description :
 */
class TreatmentDefectFragment : TreatmentBackFragment(), DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener {

    private lateinit var imageAdapter: FileListAdapter
    private var pickerDialog: DatePickerDialog? = null
    private var remotePath: String? = null


    override fun initResID() = R.layout.fragment_treatment_defect

    override fun initView() {
        super.initView()
        initDateView()
        initSpinner()
        initCheckBox()
        initFileView()
    }

    private fun initDateView() {
        if (result == null) {
            record_time.text = Utils.getCurrentTime()
            record_time.setOnClickListener{
                createDatePicker()
            }
        } else {
            record_time.text = Utils.dateFormat(result!!.disposalTime)
            record_time.isEnabled = false
            isCanChange = false
        }
    }

    private fun initSpinner() {
        val list = listOf("未发现使用液化气", "用户继续使用液化气", "其他")
        record_des.setAdapter(MaterialSpinnerAdapter(context, list))
        record_des.setOnItemSelectedListener { view, position, id, item ->
            if (position == 2) {
                record_des_detail.visibility = View.VISIBLE
            } else {
                record_des_detail.visibility = View.GONE
            }
        }
        if (result != null) {
            var index = 0
            if (list.indexOf(result!!.feedbackRemark).also { index = it } == -1) {
                index = 2
                record_des_detail.visibility = View.VISIBLE
                record_remark.setText(result!!.feedbackRemark)
                record_remark.isEnabled = false
            }
            record_des.selectedIndex = index
            record_des.isEnabled = false
        }
    }

    override fun initData() {
        super.initData()
        remotePath = "yinhuanchuzhi/" + Utils.getUUID()
    }

    private fun initCheckBox() {
        if (result != null && !TextUtils.isEmpty(result!!.disposalResult)) {
            if ("无异常" == result!!.disposalResult) {
                check_result_radioGroup.check(R.id.check_result_radio_ok)
            } else {
                check_result_radioGroup.check(R.id.check_result_radio_failure)
            }
            check_result_radio_ok.isEnabled = false
            check_result_radio_failure.isEnabled = false
        }
        check_result_radioGroup.setOnCheckedChangeListener(this)
    }

    private fun createFooterView(): View {
        val width = Utils.dip2px(context, 120f)
        val view = ImageView(context)
        view.layoutParams = ViewGroup.LayoutParams(width, width)
        val padding = Utils.dip2px(context, 5f)
        view.setPadding(padding, padding, padding, padding)
        view.setImageResource(R.drawable.icon_add_pic)
        view.setOnClickListener { showPickImg() }
        return view
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
        if (result != null) {
            if (!TextUtils.isEmpty(result!!.jzImg)) {
                val paths = result!!.jzImg.split(",".toRegex()).toTypedArray()
                for (path in paths) {
                    val mediaFile = MediaFile()
                    mediaFile.path = Constants.IMAGE_URL + path
                    mediaFile.isLocal = false
                    imageAdapter.addData(mediaFile)
                }
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

    private fun showPickImg() {
        PictureSelector.create(this)
                .openGallery(PictureConfig.TYPE_IMAGE)
                .setOutputCameraPath("/RQApp/GasImage/")
                .compress(true) /*      .compressSavePath(Environment.getExternalStorageDirectory()
                              + "/RQApp/GasImage/compress")*/
                .minimumCompressSize(300)
                .imageFormat(PictureMimeType.PNG) //.selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    private fun createDatePicker() {
        if (pickerDialog == null) {
            val calendar = Utils.getCalendar()
            pickerDialog = DatePickerDialog.newInstance(
                    this,
                    calendar[Calendar.YEAR],  // Initial year selection
                    calendar[Calendar.MONTH],  // Initial month selection
                    calendar[Calendar.DAY_OF_MONTH] // Inital day selection
            )
            pickerDialog!!.maxDate = calendar
        }
        if (!pickerDialog!!.isAdded) {
            pickerDialog!!.show(activity!!.fragmentManager, "DatePickerDialog")
        }
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        record_time.text = String.format(Locale.CHINESE, "%s-%s-%s", year, monthOfYear + 1, dayOfMonth)
    }

    override fun collectParams() {
        //super.collectParams();
        result!!.disposalTime = getText(record_time)
        result!!.feedbackRemark = record_des.selected.toString()
        result!!.other = getText(record_remark)
        result!!.type = "1"
        result!!.remotePath = remotePath
        if (imageAdapter.data.size > 0) {
            val paths: MutableList<String> = ArrayList()
            val builder = StringBuilder()
            for (file in imageAdapter.data) {
                paths.add(file.path)
                builder.append(remotePath).append("/").append(Utils.getFileName(file.path)).append(",")
            }
            result!!.jzImg = builder.substring(0, builder.length - 1)
            result!!.filePaths = paths
        }
    }

    override fun verifyData(): Boolean {
        return (verify(null, result!!.jzImg, "请至少添加一张照片!")
                && verify(check_result_radioGroup, result!!.disposalResult, "请选择处置结果")
                && (record_des.selectedIndex != 2 || record_des.selectedIndex == 2 && verify(record_remark, result!!.other, "请填写反馈描述！")))
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        result!!.disposalResult = if (checkedId == R.id.check_result_radio_failure) "有异常" else "无异常"
    }

    companion object {
        fun newInstance(model: DefectTreatmentModel?): TreatmentDefectFragment {
            val fragment = TreatmentDefectFragment()
            fragment.model = model
            return fragment
        }

        fun newInstance(model: DefectTreatmentModel?, result: DefectDetail?): TreatmentDefectFragment {
            val fragment = TreatmentDefectFragment()
            fragment.model = model
            fragment.result = result
            return fragment
        }
    }
}