package com.bkjcb.rqapplication.actionregister

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bkjcb.rqapplication.*
import com.bkjcb.rqapplication.base.datebase.DataUtil.getActionRegisterBox
import com.bkjcb.rqapplication.actionregister.model.ActionRegisterItem
import com.bkjcb.rqapplication.actionregister.retrofit.ActionRegisterService
import com.bkjcb.rqapplication.base.adapter.FileListAdapter
import com.bkjcb.rqapplication.base.BaseSimpleActivity
import com.bkjcb.rqapplication.base.MediaPlayActivity
import com.bkjcb.rqapplication.base.ftp.UploadTask
import com.bkjcb.rqapplication.base.model.MediaFile
import com.bkjcb.rqapplication.base.retrofit.NetworkApi
import com.bkjcb.rqapplication.base.util.FileUtil
import com.bkjcb.rqapplication.base.util.Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.leon.lfilepickerlibrary.LFilePicker
import com.leon.lfilepickerlibrary.utils.Constant
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register_create.*
import kotlinx.android.synthetic.main.with_bg_top_bar.*
import java.util.*

/**
 * Created by DengShuai on 2020/3/9.
 * Description :
 */
class CreateActionRegisterActivity : BaseSimpleActivity(), DatePickerDialog.OnDateSetListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, View.OnClickListener {

    private var pickerDialog: DatePickerDialog? = null
    private var calendar: Calendar? = null
    private lateinit var item: ActionRegisterItem
    private var currentView: TextView? = null
    private var isCanEditable = true
    private var imageAdapter: FileListAdapter? = null
    private var fileList: List<MediaFile>? = null
    private val request_code_file = 1000
    private var bottomSheet: QMUIBottomSheet? = null

    override fun setLayoutID(): Int {
        return R.layout.activity_register_create
    }

    override fun initView() {
        initTopBar("新建立案", View.OnClickListener { onExit() }, appbar)
        //@OnClick(R.id.base_info_time, R.id.undertaker_info_time, R.id.submit)
        base_info_time.setOnClickListener(this)
        undertaker_info_time.setOnClickListener(this)
        submit.setOnClickListener(this)
    }

    private fun initFileView() {
        file_info.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = FileListAdapter(R.layout.item_image, item.status != 2)
        file_info.adapter = imageAdapter
        if (item.status != 2) {
            imageAdapter?.setFooterView(createFooterView(), 0, LinearLayout.HORIZONTAL)
        }
        imageAdapter?.onItemClickListener = this
        imageAdapter?.onItemChildClickListener = this
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
                .setOutputCameraPath(FileUtil.getFileOutputPath("RegisterImage"))
                .compress(false)
                .imageFormat(PictureMimeType.PNG) //.selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    override fun initData() {
        StyledDialog.init(this)
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))
        val dateItem: ActionRegisterItem? = intent.getSerializableExtra("data") as ActionRegisterItem
        if (dateItem == null) {
            item = ActionRegisterItem()
            item.status = 0
            item.systime = System.currentTimeMillis()
            item.uuid = Utils.buildUUID()
            item.userId = MyApplication.getUser().userId
            item.wid = "null"
            item.phoneftp = getFtpRemotePath(item.uuid)
        } else {
            item = dateItem;
            isCanEditable = item.status != 2
            if (!isCanEditable) {
                submit!!.visibility = View.GONE
            }
            loadData()
        }
        initFileView()
    }

    private fun onExit() {
        if (item.status != 2 && !isSave) {
            showTipDialog()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        onExit()
    }

    private fun saveData() {
        item.case_source = getText(base_info_from)
        item.di = getText(base_info_di)
        item.zi = getText(base_info_zi)
        item.hao = getText(base_info_hao)
        item.crime_time = getText(base_info_time)
        item.crime_address = getText(base_info_address)
        item.case_introduction = getText(base_info_detail)
        item.party = getText(litigant_info_name)
        item.party_address = getText(litigant_info_address)
        item.party_phone = getText(litigant_info_tel)
        item.reporter = getText(informer_info_name)
        item.reporter_address = getText(informer_info_address)
        item.reporter_phone = getText(informer_info_tel)
        item.undertaker = getText(undertaker_info_name)
        item.undertaker_time = getText(undertaker_info_time)
        item.undertaker_opinion = getText(undertaker_info_remark)
        item.status = 1
        val id = getActionRegisterBox().put(item)
        if (item.id == 0L) {
            item.id = id
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
        showLoading(true)
        disposable = UploadTask.createUploadTask(getFilePath(fileList), item.phoneftp).subscribeOn(Schedulers.io())
                .flatMap { aBoolean ->
                    if (aBoolean) NetworkApi.getService(ActionRegisterService::class.java)
                            .submit(item, MyApplication.getUser().userId) else null
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ httpResult ->
                    showLoading(false)
                    if (httpResult.pushState == 200) {
                        Toast.makeText(this@CreateActionRegisterActivity, "提交成功", Toast.LENGTH_SHORT).show()
                        updateStatus()
                        finish()
                    } else {
                        Toast.makeText(this@CreateActionRegisterActivity, "提交失败！" + httpResult.pushMsg, Toast.LENGTH_SHORT).show()
                    }
                }) { throwable ->
                    showLoading(false)
                    Toast.makeText(this@CreateActionRegisterActivity, "提交失败：" + throwable.message, Toast.LENGTH_SHORT).show()
                }
    }

    private fun getFtpRemotePath(uuid: String?): String {
        return "jichazhifa/lian/$uuid"
    }

    private fun updateStatus() {
        item.status = 2
        getActionRegisterBox().put(item)
    }

    private fun loadData() {
        setText(base_info_from, item.case_source)
        setText(base_info_di, item.di)
        setText(base_info_zi, item.zi)
        setText(base_info_hao, item.hao)
        setText(base_info_time, item.crime_time)
        setText(base_info_address, item.crime_address)
        setText(base_info_detail, item.case_introduction)
        setText(litigant_info_name, item.party)
        setText(litigant_info_address, item.party_address)
        setText(litigant_info_tel, item.party_phone)
        setText(litigant_info_name, item.reporter)
        setText(informer_info_address, item.reporter_address)
        setText(informer_info_tel, item.reporter_phone)
        setText(undertaker_info_name, item.undertaker)
        setText(undertaker_info_time, item.undertaker_time)
        setText(undertaker_info_remark, item.undertaker_opinion)
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
            bottomSheet?.show()
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

    private fun refreshFileData() {
        fileList = MediaFile.getAll(item.uuid)
        imageAdapter?.setNewData(fileList)
    }

    private fun getText(view: EditText?): String {
        return view?.text.toString()
    }

    private fun getText(view: TextView?): String {
        return view?.text.toString()
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
            view!!.isEnabled = false
            view.hint = ""
        }
        view!!.text = text
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.base_info_time -> {
                currentView = base_info_time
                createDatePicker()
            }
            R.id.undertaker_info_time -> {
                currentView = undertaker_info_time
                createDatePicker()
            }
            R.id.submit -> {
                if (!isSave) {
                    saveData()
                }
                if (verify()) {
                    showFinishTipDialog()
                }
            }
            else -> {
            }
        }
    }

    private fun createDatePicker() {
        if (pickerDialog == null) {
            pickerDialog = DatePickerDialog.newInstance(
                    this,
                    calendar!![Calendar.YEAR],  // Initial year selection
                    calendar!![Calendar.MONTH],  // Initial month selection
                    calendar!![Calendar.DAY_OF_MONTH] // Inital day selection
            )
        }
        if (!pickerDialog!!.isAdded) {
            pickerDialog!!.show(fragmentManager, "DatePickerDialog")
        }
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        currentView?.text = String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth)
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

    private fun verify(): Boolean {
        return (verify(base_info_from)
                && verify(base_info_address)
                && verify(base_info_time)
                && verify(base_info_address)
                && verify(base_info_di)
                && verify(base_info_zi)
                && verify(base_info_hao)
                && verify(litigant_info_name)
                && verify(litigant_info_address)
                && verify(litigant_info_tel)
                && verify(informer_info_name)
                && verify(informer_info_address)
                && verify(informer_info_tel)
                && verify(undertaker_info_name)
                && verify(undertaker_info_remark)
                && verify(undertaker_info_time))
    }

    private fun verify(view: TextView?): Boolean {
        if (TextUtils.isEmpty(getText(view))) {
            view!!.requestFocus()
            showToast(view.hint.toString())
            return false
        }
        return true
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    private val isSave: Boolean = TextUtils.isEmpty(getText(base_info_from)) || TextUtils.isEmpty(getText(base_info_time)) || TextUtils.isEmpty(getText(base_info_address))

    private fun showFinishTipDialog() {
        StyledDialog.buildIosAlert("提示", "是否提交当前记录(提交后将不可修改)？", object : MyDialogListener() {
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
        fun toActivity(context: Context, item: ActionRegisterItem?) {
            val intent = Intent(context, CreateActionRegisterActivity::class.java)
            if (item != null) {
                intent.putExtra("data", item)
            }
            context.startActivity(intent)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (view?.id == R.id.iv_delete) {
            MediaFile.getBox().remove(fileList!![position])
            refreshFileData()
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        MediaPlayActivity.toActivity(this@CreateActionRegisterActivity, (adapter?.getItem(position) as MediaFile?)?.path)
    }
}