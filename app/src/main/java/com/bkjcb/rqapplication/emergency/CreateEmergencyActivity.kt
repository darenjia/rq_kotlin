package com.bkjcb.rqapplication.emergency

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import butterknife.BindView
import butterknife.OnClick
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.bkjcb.rqapplication.*
import com.bkjcb.rqapplication.adapter.FileListAdapter
import com.bkjcb.rqapplication.check.model.CheckStation
import com.bkjcb.rqapplication.check.retrofit.CheckService
import com.bkjcb.rqapplication.datebase.DataUtil
import com.bkjcb.rqapplication.emergency.CreateEmergencyActivity
import com.bkjcb.rqapplication.emergency.model.EmergencyItem
import com.bkjcb.rqapplication.emergency.retrofit.EmergencyService
import com.bkjcb.rqapplication.ftp.UploadTask
import com.bkjcb.rqapplication.model.MediaFile
import com.bkjcb.rqapplication.retrofit.NetworkApi
import com.bkjcb.rqapplication.util.FileUtil
import com.bkjcb.rqapplication.util.Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.jaredrummler.materialspinner.MaterialSpinner
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter
import com.leon.lfilepickerlibrary.LFilePicker
import com.leon.lfilepickerlibrary.utils.Constant
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_emergency_create.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*
import java.util.*

/**
 * Created by DengShuai on 2020/3/9.
 * Description :
 */
class CreateEmergencyActivity : BaseSimpleActivity(), BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener {

    private lateinit var item: EmergencyItem
    private var currentView: TextView? = null
    private var isCanEditable = true
    private lateinit var imageAdapter: FileListAdapter
    private lateinit var fileList: List<MediaFile>
    private val request_code_file = 1000
    private var bottomSheet: QMUIBottomSheet? = null
    private var pickerDialog: TimePickerView? = null
    override fun setLayoutID(): Int {
        return R.layout.activity_emergency_create
    }

    override fun initView() {
        super.initView()
        initTopBar("新建事故现场", View.OnClickListener { onExit() }, appbar)
        //@OnClick(R.id.base_info_time, R.id.reporter_info_time, R.id.submit)
        base_info_time.setOnClickListener(this)
        reporter_info_time.setOnClickListener(this)
        submit.setOnClickListener(this)
    }

    private fun initFileView() {
        file_info.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = FileListAdapter(R.layout.item_image, item.status != 2)
        file_info.adapter = imageAdapter
        if (item.status != 2) {
            imageAdapter.setFooterView(createFooterView(), 0, LinearLayout.HORIZONTAL)
        }
        imageAdapter.onItemClickListener = this
        imageAdapter.onItemChildClickListener = this
        refreshFileData()
    }

    private fun createFooterView(): View {
        val view = ImageView(this)
        view.layoutParams = ViewGroup.LayoutParams(Utils.dip2px(this, 120f), Utils.dip2px(this, 120f))
        val padding = Utils.dip2px(this, 5f)
        view.setPadding(padding, padding, padding, padding)
        view.setImageResource(R.drawable.icon_add_pic)
        view.setOnClickListener { showBottomMenu() }
        return view
    }

    fun showPickImg(type: Int) {
        PictureSelector.create(this)
                .openGallery(type)
                .setOutputCameraPath(FileUtil.getFileOutputPath("EmergencyImage"))
                .compress(false)
                .imageFormat(PictureMimeType.PNG) //.selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    override fun initData() {
        StyledDialog.init(this)
        //calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        val emergencyItem: EmergencyItem? = intent.getSerializableExtra("data") as EmergencyItem
        if (emergencyItem == null) {
            item = EmergencyItem()
            item.status = 0
            item.systime = System.currentTimeMillis()
            item.uuid = Utils.buildUUID()
            item.userId = MyApplication.getUser().userId
            item.phoneftp = getFtpRemotePath(item.uuid)
        } else {
            item = emergencyItem
            isCanEditable = item.status != 2
            if (!isCanEditable) {
                submit.visibility = View.GONE
            }
            loadData()
        }
        queryDistrictName()
        initFileView()
    }

    private fun queryDistrictName() {
        disposable = NetworkApi.getService(CheckService::class.java)
                .getCheckUnit("区", null)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .map<List<String>> { checkStationResult ->
                    if (checkStationResult.pushState == 200) {
                        getLocationList(checkStationResult.datas)
                    } else null
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({ result: List<String>? ->
                    if (result != null) {
                        reporter_info_location.setAdapter(MaterialSpinnerAdapter(this, result))
                        if (!TextUtils.isEmpty(item.qushu)) {
                            for (i in result.indices) {
                                if (item.qushu == result[i]) {
                                    reporter_info_location.selectedIndex = i
                                    break
                                }
                            }
                        }
                        if (!isCanEditable) {
                            reporter_info_location.isEnabled = false
                        }
                    } else {
                        Toast.makeText(this, "获取列表失败！", Toast.LENGTH_SHORT).show()
                    }
                }) { throwable: Throwable -> Toast.makeText(this, "获取列表失败！" + throwable.message, Toast.LENGTH_SHORT).show() }
    }


    private fun getLocationList(list: List<CheckStation>?): List<String>? {
        if (list != null && list.isNotEmpty()) {
            val strings: MutableList<String> = ArrayList(list.size)
            for (station in list) {
                if (station.qiyemingcheng != null) {
                    strings.add(station.qiyemingcheng!!)
                }
            }
            return strings
        }
        return null
    }

    private fun onExit() {
        if (item.status != 2) {
            showTipDialog()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        onExit()
    }

    private fun saveData() {
        if (verify(base_info_address) && verify(base_info_time)) {
            item.accidentAddress = getText(base_info_address)
            item.accidentDate = getText(base_info_time)
            item.mainDescription = getText(base_info_des)
            item.disposalPerson = getText(base_info_department)
            item.keyPerson = getText(base_info_people)
            item.remark = getText(base_info_remark)
            item.reportingUnit = getText(reporter_info_address)
            item.reportingStaff = getText(reporter_info_name)
            item.qushu = getText(reporter_info_location)
            item.reportingDate = getText(reporter_info_time)
            item.contactPhone = getText(reporter_info_tel)
            item.status = 1
            val id: Long = DataUtil.getEmergencyItemBox().put(item)
            if (item.id == 0L) {
                item.id = id
            }
        }
    }

    private fun getFilePath(files: List<MediaFile>?): List<String> {
        val strings: MutableList<String> = ArrayList()
        for (file in files!!) {
            strings.add(file.path)
        }
        return strings
    }

    private fun submitData() {
        if (!verify()) {
            return
        }
        showLoading(true)
        disposable = UploadTask.createUploadTask(getFilePath(fileList), item.phoneftp).subscribeOn(Schedulers.io())
                .flatMap { aBoolean ->
                    if (aBoolean) NetworkApi.getService(EmergencyService::class.java)
                            .submit(item.userId,
                                    item.reportingUnit,
                                    item.qushu,
                                    item.remark,
                                    item.reportingDate,
                                    item.accidentDate,
                                    item.accidentAddress,
                                    item.disposalPerson,
                                    item.keyPerson,
                                    item.reportingStaff,
                                    item.contactPhone,
                                    item.mainDescription,
                                    item.phoneftp) else null
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ httpResult ->
                    if (httpResult.pushState == 200) {
                        showLoading(false)
                        Toast.makeText(this@CreateEmergencyActivity, "提交成功", Toast.LENGTH_SHORT).show()
                        updateStatus()
                        finish()
                    } else {
                        Toast.makeText(this@CreateEmergencyActivity, "提交失败！" + httpResult.pushMsg, Toast.LENGTH_SHORT).show()
                    }
                }) { throwable ->
                    showLoading(false)
                    Toast.makeText(this@CreateEmergencyActivity, "提交失败：" + throwable.message, Toast.LENGTH_SHORT).show()
                }
    }

    private fun hideSoftInput() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            inputMethodManager.hideSoftInputFromWindow(base_info_remark.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private fun getFtpRemotePath(uuid: String?): String {
        return "shiguxianchang/$uuid"
    }

    private fun updateStatus() {
        item.status = 2
        DataUtil.getEmergencyItemBox().put(item)
    }

    private fun loadData() {
        setText(base_info_address, item.accidentAddress)
        setText(base_info_time, item.accidentDate)
        setText(base_info_des, item.mainDescription)
        setText(base_info_department, item.disposalPerson)
        setText(base_info_people, item.keyPerson)
        setText(reporter_info_address, item.reportingUnit)
        setText(reporter_info_tel, item.contactPhone)
        setText(reporter_info_time, item.reportingDate)
        setText(reporter_info_name, item.reportingStaff)
        setText(base_info_remark, item.remark)
    }

    private fun showBottomMenu() {
        if (bottomSheet == null) {
            bottomSheet = BottomListSheetBuilder(this)
                    .addItem(R.drawable.vector_drawable_file_photo, "照片", "照片")
                    .addItem(R.drawable.vector_drawable_file_video, "视频", "视频")
                    .addItem(R.drawable.vector_drawable_file_audio, "音频", "音频")
                    .addItem(R.drawable.vector_drawable_file_table, "文件", "文件")
                    .setOnSheetItemClickListener { dialog, itemView, position, tag ->
                        when (position) {
                            0 -> showPickImg(PictureMimeType.ofImage())
                            1 -> showPickImg(PictureMimeType.ofVideo())
                            2 -> showPickImg(PictureMimeType.ofAudio())
                            3 -> showFilePicker()
                            else -> {
                            }
                        }
                        dialog.dismiss()
                    }.setTitle("选择文件类型").build()
        }
        if (!bottomSheet!!.isShowing) {
            bottomSheet!!.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == request_code_file) {
                val list: List<String> = data!!.getStringArrayListExtra(Constant.RESULT_INFO)
                handleFilePath(list)
            } else if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                val list = PictureSelector.obtainMultipleResult(data)
                handleMedia(list)
            }
            refreshFileData()
        }
    }

    private fun handleMedia(list: List<LocalMedia>) {
        var mediaFile: MediaFile
        for (media in list) {
            mediaFile = MediaFile()
            mediaFile.itemId = item.uuid
            mediaFile.type = media.mimeType
            mediaFile.path = media.path
            mediaFile.fileName = mediaFile.fileName
            MediaFile.save(mediaFile)
        }
    }

    private fun handleFilePath(list: List<String>) {
        var mediaFile: MediaFile
        for (path in list) {
            mediaFile = MediaFile()
            mediaFile.itemId = item.uuid
            mediaFile.type = 4
            mediaFile.path = path
            mediaFile.fileName = Utils.getFileName(path)
            MediaFile.save(mediaFile)
        }
    }

    fun refreshFileData() {
        fileList = MediaFile.getAll(item.uuid)
        imageAdapter.setNewData(fileList)
    }

    private fun getText(view: TextView): String {
        return view.text.toString()
    }

    private fun showFilePicker() {
        LFilePicker()
                .withActivity(this)
                .withRequestCode(request_code_file)
                .withStartPath("/storage/emulated/0/Download")
                .withBackIcon(Constant.BACKICON_STYLETHREE)
                .withTheme(R.style.LFileTheme)
                .withTitle("选择文件")
                .withMutilyMode(true)
                .start()
    }

    private fun setText(view: TextView?, text: String?) {
        if (!isCanEditable) {
            view?.isEnabled = false
            view?.hint = ""
        }
        view?.text = text
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.reporter_info_time -> {
                currentView = reporter_info_time
                createDatePicker()
            }
            R.id.base_info_time -> {
                currentView = base_info_time
                createDatePicker()
            }
            R.id.submit -> {
                saveData()
                showFinishTipDialog()
            }
            else -> {
            }
        }
    }

    private fun createDatePicker() {
        if (pickerDialog == null) {
            pickerDialog = TimePickerBuilder(this, OnTimeSelectListener { date, v -> //选中事件回调
                if (currentView?.id == R.id.base_info_time) {
                    currentView?.text = Utils.dateFormat("yyyy-MM-dd hh:mm:ss", date)
                } else {
                    currentView?.text = Utils.dateFormat("yyyy-MM-dd hh:mm", date)
                }
            })
                    .setType(booleanArrayOf(true, true, true, true, true, true)) // 默认全部显示
                    .setCancelText("取消") //取消按钮文字
                    .setSubmitText("确定") //确认按钮文字
                    .setContentTextSize(16) //滚轮文字大小
                    .setTitleSize(20) //标题文字大小
                    .setTitleText("选择时间") //标题文字
                    .setOutSideCancelable(false) //点击屏幕，点在控件外部范围时，是否取消显示
                    .isCyclic(true) //是否循环滚动
                    .setTitleColor(getColorResource(R.color.colorText)) //标题文字颜色
                    .setSubmitColor(getColorResource(R.color.color_type_0)) //确定按钮文字颜色
                    .setCancelColor(getColorResource(R.color.colorText)) //取消按钮文字颜色
                    .setTitleBgColor(getColorResource(R.color.colorAccent)) //标题背景颜色 Night mode
                    .setBgColor(getColorResource(R.color.colorText)) //滚轮背景颜色 Night mode
                    .setLabel("年", "月", "日", "时", "分", "秒") //默认设置为年月日时分秒
                    .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .isDialog(false) //是否显示为对话框样式
                    .build()
        }
        if (!pickerDialog!!.isShowing) {
            hideSoftInput()
            pickerDialog!!.show()
        }
    }

    private fun showTipDialog() {
        StyledDialog.buildIosAlert("提示", "当前记录尚未提交，是否退出？", object : MyDialogListener() {
            override fun onFirst() {
                saveData()
                finish()
            }

            override fun onSecond() {
                finish()
            }
        }).setBtnText("保存并退出", "直接退出").show()
    }

    private fun getColorResource(id: Int): Int {
        return resources.getColor(id)
    }

    private fun verify(): Boolean {
        return (verify(base_info_address)
                && verify(base_info_time)
                && verify(base_info_des)
                && verify(base_info_department)
                && verify(base_info_people) //&& verify(mBaseInfoRemark)
                && verify(reporter_info_name)
                && verify(reporter_info_time)
                && verify(reporter_info_tel)
                && verify(reporter_info_location)
                && verify(reporter_info_address))
    }

    private fun verify(view: TextView): Boolean {
        if (TextUtils.isEmpty(getText(view))) {
            view.requestFocus()
            showToast(view.hint.toString())
            return false
        }
        return true
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    protected fun showFinishTipDialog() {
        StyledDialog.buildIosAlert("提示", "是否提交当前记录(提交后将不可修改)?", object : MyDialogListener() {
            override fun onFirst() {
                submitData()
            }

            override fun onSecond() {
                finish()
            }
        }).setBtnText("提交记录", "仅保存")
                .show()
    }

    companion object {
        fun toActivity(context: Context, item: EmergencyItem?) {
            val intent = Intent(context, CreateEmergencyActivity::class.java)
            if (item != null) {
                intent.putExtra("data", item)
            }
            context.startActivity(intent)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        MediaPlayActivity.ToActivity(this@CreateEmergencyActivity, (adapter?.getItem(position) as MediaFile?)?.path)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (view?.id == R.id.iv_delete) {
            MediaFile.getBox().remove(fileList!![position])
            refreshFileData()
        }
    }
}