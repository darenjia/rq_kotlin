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
import com.amap.api.maps.model.LatLng
import com.bkjcb.rqapplication.Constants
import com.bkjcb.rqapplication.MediaPlayActivity
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.adapter.FileListAdapter
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment
import com.bkjcb.rqapplication.gasrecord.SearchGasUserActivity
import com.bkjcb.rqapplication.gasrecord.adapter.GasCompanyAdapter
import com.bkjcb.rqapplication.gasrecord.model.GasCompanyResult.GasCompany
import com.bkjcb.rqapplication.gasrecord.model.GasRecordModel
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService
import com.bkjcb.rqapplication.interfaces.OnPageButtonClickListener
import com.bkjcb.rqapplication.model.MediaFile
import com.bkjcb.rqapplication.retrofit.NetworkApi
import com.bkjcb.rqapplication.treatmentdefect.fragment.MapLocationFragment
import com.bkjcb.rqapplication.treatmentdefect.fragment.MapLocationFragment.AddressQueryListener
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult.UserInfo
import com.bkjcb.rqapplication.util.FileUtil
import com.bkjcb.rqapplication.util.Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.interfaces.MyDialogListener
import com.jakewharton.rxbinding2.view.RxView
import com.jaredrummler.materialspinner.MaterialSpinner
import com.jaredrummler.materialspinner.MaterialSpinner.OnNothingSelectedListener
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.orhanobut.logger.Logger
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_gas_user_detail.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
class GasRecordDetailFragment : BaseSimpleFragment(), DatePickerDialog.OnDateSetListener, CompoundButton.OnCheckedChangeListener {


    private val type1List = listOf("有", "无")
    private var pickerDialog: DatePickerDialog? = null
    private var currentTextView: EditText? = null
    private var userInfo: UserInfo? = null
    private var currentLatLng: LatLng? = null
    private var address: String? = null
    private var recordModel: GasRecordModel? = null
    private var listener: OnPageButtonClickListener<*>? = null
    private lateinit var imageAdapter: FileListAdapter
    private var isCanChange = true
    private var isTemp = false
    private var disposable1: Disposable? = null
    fun setListener(listener: OnPageButtonClickListener<*>?) {
        this.listener = listener
    }

    fun setUserInfo(userInfo: UserInfo?) {
        this.userInfo = userInfo
    }

    fun setRecordModel(recordModel: GasRecordModel?) {
        this.recordModel = recordModel
    }

    override fun initView() {
        if (recordModel == null) {
            activity!!.finish()
            return
        }
        setText(street_name, recordModel!!.jiedao)
        isCanChange = recordModel!!.getType() != 0
        isTemp = recordModel!!.id > 0
        if (recordModel!!.getType() == 1) {
            setText(record_create_time, "建档日期：" + recordModel!!.jiandangriqi)
            initSpinnerView()
            setViewVisibility(record_save, true)
        } else {
            setText(record_user_name, recordModel!!.yonghuming)
            setText(record_user_address, recordModel!!.dizhi)
            setText(record_user, recordModel!!.fuzeren)
            setText(record_user_tel, recordModel!!.dianhua)
            setText(record_legal, recordModel!!.faren)
            if (recordModel!!.getType() == 0) {
                setText(record_create_time, "建档日期：" + Utils.dateFormat(recordModel!!.jiandangriqi))
                setText(record_system, recordModel!!.ranqiguanlizhidu)
                setText(record_license, recordModel!!.yingyezhizhao)
                setText(record_company, recordModel!!.gongqiqiye)
                setText(record_contract, recordModel!!.yongqihetong)
                setText(record_bjq, recordModel!!.ranqixieloubaojinqi)
                setText(record_check, recordModel!!.qiyeanjianjilu)
                setViewVisibility(record_submit, false)
            } else {
                setText(record_create_time, "建档日期：" + recordModel!!.jiandangriqi)
                initSpinnerView()
                setViewVisibility(record_save, isTemp)
            }
            if ("已签" == recordModel!!.yongqihetong) {
                setText(record_signed_time, Utils.dateFormat(recordModel!!.qiandingriqi))
                setViewVisibility(record_signed_layout, true)
            } else {
                setViewVisibility(record_signed_layout, false)
            }
            if (!TextUtils.isEmpty(recordModel!!.tiaoyafa)) {
                setChecked(record_tyf_1, recordModel!!.tiaoyafa.contains("不可调节"))
                setChecked(record_tyf_2, recordModel!!.tiaoyafa.contains(",可调节"))
            }
            if (!TextUtils.isEmpty(recordModel!!.tiaoyafa_geshu) && recordModel!!.tiaoyafa_geshu.toInt() > 0) {
                setChecked(record_tyf_2, true)
                setViewVisibility(record_tyf_layout, true)
                setText(record_tyf_number, recordModel!!.tiaoyafa_geshu)
            } else {
                setChecked(record_tyf_1, true)
            }
            if (!TextUtils.isEmpty(recordModel!!.lianjieguan)) {
                setChecked(record_ljg_1, recordModel!!.lianjieguan.contains("硬管连接"))
                setChecked(record_ljg_2, recordModel!!.lianjieguan.contains("橡胶管"))
                setChecked(record_ljg_3, recordModel!!.lianjieguan.contains("其他管"))
            }
            setText(record_rj_1_number, recordModel!!.zaojuleixing_dafeng)
            setText(record_rj_2_number, recordModel!!.zaojuleixing_gufeng)
            if (!TextUtils.isEmpty(recordModel!!.xihuobaohu)) {
                setChecked(record_xh_1, recordModel!!.xihuobaohu.contains("是"))
                setChecked(record_xh_2, recordModel!!.xihuobaohu.contains("否"))
            }
            if (!TextUtils.isEmpty(recordModel!!.xihuobaohu_geshu) && recordModel!!.xihuobaohu_geshu.toInt() > 0) {
                setChecked(record_xh_2, true)
                setViewVisibility(record_xh_layout, true)
                setText(record_xh_number, recordModel!!.xihuobaohu_geshu)
            } else {
                setChecked(record_xh_1, true)
            }
            if ("有" == recordModel!!.qiyeanjianjilu) {
                setViewVisibility(record_last_check_layout, true)
                setText(record_last_check_time, Utils.dateFormat(recordModel!!.anjianriqi))
            } else {
                setViewVisibility(record_last_check_layout, false)
            }
            setText(record_remark, recordModel!!.beizhu)
        }
        addMapView()
    }

    private fun initSpinnerView() {
        record_check.setOnItemSelectedListener { view, position, id, item -> setViewVisibility(record_last_check_layout, position == 0) }
        record_contract.setOnItemSelectedListener { view, position, id, item -> setViewVisibility(record_signed_layout, position == 0) }
        initSpinner(record_license, type1List, recordModel!!.yingyezhizhao)
        initSpinner(record_system, type1List, recordModel!!.ranqiguanlizhidu)
        initSpinner(record_contract, listOf("已签", "未签"), recordModel!!.yongqihetong)
        initSpinner(record_bjq, listOf("已安装", "未安装"), recordModel!!.ranqixieloubaojinqi)
        initSpinner(record_check, type1List, recordModel!!.qiyeanjianjilu)
        obtainGasCompany()
    }

    private fun addMapView() {
        val locationFragment: MapLocationFragment
        if (recordModel!!.getType() == 1) {
            locationFragment = MapLocationFragment.newInstance(object : AddressQueryListener {
                override fun onSuccess(s: String?, latLng: LatLng?) {
                    address = s
                    record_location_address.setText(s)
                    currentLatLng = latLng
                }

                override fun onClick() {
                    if (!TextUtils.isEmpty(address) && !address!!.contains("正在") && !address!!.contains("未")) {
                        showUserAddressTip()
                    }
                }
            })
        } else {
            if (!TextUtils.isEmpty(recordModel!!.location) && recordModel!!.location.contains(",")) {
                val strings = recordModel!!.location.split(",".toRegex()).toTypedArray()
                currentLatLng = LatLng(strings[0].toDouble(), strings[1].toDouble())
            }
            if (recordModel!!.getType() == 2 && currentLatLng == null) {
                locationFragment = MapLocationFragment.newInstance(object : AddressQueryListener {
                    override fun onSuccess(s: String?, latLng: LatLng?) {
                        currentLatLng = latLng
                    }

                    override fun onClick() {}
                }, true)
            } else {
                locationFragment = MapLocationFragment.newInstance(currentLatLng)
            }
            setViewVisibility(record_location_layout, false)
        }
        if (recordModel!!.getType() != 0 || currentLatLng != null) {
            childFragmentManager
                    .beginTransaction()
                    .add(R.id.record_map, locationFragment)
                    .commit()
        } else {
            setViewVisibility(record_map, false)
        }
    }

    override fun initData() {
        if (recordModel == null) {
            return
        }
        if (recordModel!!.getType() == 0) {
            if (!TextUtils.isEmpty(recordModel!!.rquserid)) {
                setViewVisibility(link_layout, true)
                link_name.text = recordModel!!.rqyonghuming
                link_address.text = recordModel!!.rqdizhi
                setViewVisibility(unlink, false)
            }
        } else {
            StyledDialog.init(context)
            setListener()
            initUserInfo()
        }
        initFileView()
    }

    private fun initUserInfo() {
        if (userInfo == null) {
            setViewVisibility(link_layout, false)
            setViewVisibility(record_link_btn, true)
        } else {
            setViewVisibility(record_link_btn, false)
            setViewVisibility(link_layout, true)
            link_name.text = userInfo!!.userName
            link_address.text = userInfo!!.userAddress
            record_user_name.setText(userInfo!!.userName)
            if (recordModel!!.getType() == 1 || isTemp) {
                record_user_address.setText(userInfo!!.userAddress)
            }
            if (recordModel!!.getType() == 2 && !isTemp) {
                record_user_address.isEnabled = false
            }
        }
    }

    private fun setListener() {
        record_tyf_2.setOnCheckedChangeListener(this)
        record_xh_2.setOnCheckedChangeListener(this)
        if (disposable1 == null) {
            disposable1 = RxView.clicks(record_submit).throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        collectParams()
                        if (verifyAllData()) {
                            listener?.onNext(imageAdapter.data)
                        }
                    }
        }
    }

    private fun setCompanyListener(flag: Boolean) {
        record_company.setOnNothingSelectedListener(if (flag) OnNothingSelectedListener {
            Logger.d("click spinner")
            obtainGasCompany()
        } else null)
    }

    private fun initSpinner(spinner: MaterialSpinner?, strings: List<String>, value: String) {
        spinner!!.setAdapter(MaterialSpinnerAdapter(context, strings))
        if (!TextUtils.isEmpty(value)) {
            if (strings.indexOf(value) != -1) {
                spinner.selectedIndex = strings.indexOf(value)
            }
        }
    }

    //R.id.record_submit添加防抖操作
    @OnClick(R.id.record_signed_time, R.id.record_last_check_time, R.id.unlink, R.id.record_link_btn, R.id.record_link_info, R.id.record_save)
    fun onClick(v: View) {
        if (recordModel!!.getType() == 0) {
            return
        }
        when (v.id) {
            R.id.record_signed_time -> {
                currentTextView = record_signed_time
                createDatePicker()
            }
            R.id.record_last_check_time -> {
                currentTextView = record_last_check_time
                createDatePicker()
            }
            R.id.unlink -> showUnlinkTip()
            R.id.record_link_info, R.id.record_link_btn -> SearchGasUserActivity.toActivity(this)
            R.id.record_save -> saveTempData()
            else -> {
            }
        }
    }

    private fun saveTempData() {
        collectParams()
        if (TextUtils.isEmpty(recordModel!!.yonghuming) || TextUtils.isEmpty(recordModel!!.dizhi)) {
            Toast.makeText(context, "用户名和地址不能为空！", Toast.LENGTH_SHORT).show()
        } else {
            if (imageAdapter.data.size > 0) {
                val fileName: MutableList<String> = ArrayList()
                for (file in imageAdapter.data) {
                    fileName.add(file.path)
                }
                recordModel!!.phoneftp = Utils.listToString(fileName)
            }
            GasRecordModel.save(recordModel)
            Toast.makeText(context, "数据保存成功！", Toast.LENGTH_SHORT).show()
            activity!!.finish()
        }
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
        }
        if (!pickerDialog!!.isAdded) {
            pickerDialog!!.show(activity!!.fragmentManager, "DatePickerDialog")
        }
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        currentTextView!!.setText(String.format(Locale.CHINESE, "%s-%s-%s", year, monthOfYear + 1, dayOfMonth))
    }

    private fun setViewVisibility(v: View?, isShow: Boolean) {
        v!!.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun showUnlinkTip() {
        StyledDialog.buildIosAlert("提示", "是否解除当前绑定？", object : MyDialogListener() {
            override fun onFirst() {
                userInfo = null
                initUserInfo()
            }

            override fun onSecond() {}
        }).setBtnText("是", "否").show()
    }

    private fun showUserAddressTip() {
        StyledDialog.buildIosAlert("提示", "是否使用当前定位地址？", object : MyDialogListener() {
            override fun onFirst() {
                record_user_address.setText(address)
            }

            override fun onSecond() {}
        }).setBtnText("是", "否").show()
    }

    private fun setText(view: TextView?, value: String?) {
        view!!.text = value
        view.isEnabled = isCanChange
    }

    private fun setChecked(box: CheckBox?, enable: Boolean) {
        box!!.isChecked = enable
        box.isClickable = isCanChange
    }

    private fun getText(view: TextView?): String {
        return view!!.text.toString()
    }

    private fun verifyAllData(): Boolean {
        if (record_company.getItems<Any>() == null || record_company.getItems<Any>().size == 0) {
            Toast.makeText(context, "获取供气企业失败，正在重试", Toast.LENGTH_LONG).show()
            obtainGasCompany()
            return false
        }
        return verify(record_user_name, recordModel!!.yonghuming, "请填写用户名称") &&
                verify(record_user_address, recordModel!!.dizhi, "请填写地址") &&
                verify(record_user, recordModel!!.fuzeren, "请填写负责人") &&
                verify(record_user_tel, recordModel!!.dianhua, "请填写电话") &&
                verify(record_tyf_1, recordModel!!.tiaoyafa, "请选择调压阀类型") &&
                verify(record_xh_1, recordModel!!.xihuobaohu, "请选择有无熄火保护装置") &&  //verify(mRecordLegal, recordModel.faren, "请填写法人") &&
                verify(record_company, recordModel!!.gongqiqiye, "请选择供气企业") &&
                ("已签" == recordModel!!.yongqihetong &&
                        verify(record_signed_time, recordModel!!.qiandingriqi, "请选择签订日期") || "未签" == recordModel!!.yongqihetong) &&
                record_tyf_2.isChecked &&
                verify(record_tyf_number, recordModel!!.tiaoyafa_geshu, "请填写可调节调压阀个数(数量需大于0)", true) &&
                verify(record_ljg_1, recordModel!!.lianjieguan, "请选择连接管") &&
                verify(record_rj_1_number, recordModel!!.zaojuleixing_dafeng, "请填写大气式燃具个数") &&
                verify(record_rj_2_number, recordModel!!.zaojuleixing_gufeng, "请填写鼓风式燃具个数") &&
                record_xh_2.isChecked &&
                verify(record_xh_number, recordModel!!.xihuobaohu_geshu, "请填写没有熄火保护装置的个数(数量需大于0))", true) &&
                ("有" == recordModel!!.qiyeanjianjilu &&
                        verify(record_last_check_time, recordModel!!.anjianriqi, "请选择最后安检日期") || "无" == recordModel!!.qiyeanjianjilu) && verify(file_info, "请添加3到5张照片")
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

    private fun verify(view: View?, value: String, tip: String): Boolean {
        if (TextUtils.isEmpty(value)) {
            view!!.requestFocus()
            Toast.makeText(context, tip, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun verify(view: View?, value: String, tip: String, verifyValue: Boolean): Boolean {
        if (TextUtils.isEmpty(value)) {
            view!!.requestFocus()
            Toast.makeText(context, tip, Toast.LENGTH_SHORT).show()
            return false
        }
        if (verifyValue) {
            if (value.toInt() == 0) {
                view!!.requestFocus()
                Toast.makeText(context, tip, Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun collectParams() {
        recordModel!!.yonghuming = getText(record_user_name)
        recordModel!!.dizhi = getText(record_user_address)
        recordModel!!.fuzeren = getText(record_user)
        recordModel!!.dianhua = getText(record_user_tel)
        recordModel!!.faren = getText(record_legal)
        recordModel!!.ranqiguanlizhidu = getText(record_system)
        recordModel!!.yingyezhizhao = getText(record_license)
        recordModel!!.gongqiqiye = getText(record_company)
        recordModel!!.gongqiqiyeid = if (record_company.selected == null) "" else (record_company.selected as GasCompany).cid
        recordModel!!.yongqihetong = getText(record_contract)
        recordModel!!.qiandingriqi = if (recordModel!!.yongqihetong == "已签") getText(record_signed_time) else ""
        recordModel!!.zaojuleixing_dafeng = getText(record_rj_1_number)
        recordModel!!.zaojuleixing_gufeng = getText(record_rj_2_number)
        recordModel!!.ranqixieloubaojinqi = getText(record_bjq)
        recordModel!!.qiyeanjianjilu = getText(record_check)
        recordModel!!.anjianriqi = if (recordModel!!.qiyeanjianjilu == "有") getText(record_last_check_time) else ""
        recordModel!!.beizhu = getText(record_remark)
        var builder = StringBuilder()
        if (record_tyf_1.isChecked) {
            builder.append("不可调节")
        }
        if (record_tyf_2.isChecked) {
            builder.append(if (builder.isNotEmpty()) "," else "").append("可调节")
            recordModel!!.tiaoyafa_geshu = getText(record_tyf_number)
        }
        recordModel!!.tiaoyafa = builder.toString()
        builder = StringBuilder()
        if (record_xh_1.isChecked) {
            builder.append("是")
        }
        if (record_xh_2.isChecked) {
            builder.append(if (builder.isNotEmpty()) "," else "").append("否")
            recordModel!!.xihuobaohu_geshu = getText(record_xh_number)
        }
        recordModel!!.xihuobaohu = builder.toString()
        builder = StringBuilder()
        if (record_ljg_1.isChecked) {
            builder.append("硬管连接")
        }
        if (record_ljg_2.isChecked) {
            builder.append(if (builder.isNotEmpty()) "," else "").append("橡胶管")
        }
        if (record_ljg_3.isChecked) {
            builder.append(if (builder.isNotEmpty()) "," else "").append("其他管")
        }
        recordModel!!.lianjieguan = builder.toString()
        if (currentLatLng != null) {
            recordModel!!.location = String.format(Locale.CHINESE, "%s,%s", currentLatLng!!.latitude, currentLatLng!!.longitude)
        }
        if (userInfo != null) {
            recordModel!!.rquserid = userInfo!!.userGuid
            recordModel!!.rqyonghuming = userInfo!!.userName
            recordModel!!.rqdizhi = userInfo!!.userAddress
            recordModel!!.mbuid = userInfo!!.mbu_id
        }
    }

    private fun obtainGasCompany(){
            disposable = NetworkApi.getService(GasService::class.java)
                    .getComboList("B2")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        if (result.pushState == 200 && result.datas != null && result.datas.size > 0) {
                            val adapter = GasCompanyAdapter(context, result.datas)
                            record_company.setAdapter(adapter)
                            if (!TextUtils.isEmpty(recordModel!!.gongqiqiyeid)) {
                                for (i in result.datas.indices) {
                                    if (recordModel!!.gongqiqiyeid == result.datas[i].cid) {
                                        record_company.selectedIndex = i
                                        break
                                    }
                                }
                            }
                            setCompanyListener(false)
                        } else {
                            record_company.hint = "获取供气企业失败，请稍后重试"
                            Toast.makeText(context, "获取供气企业失败，请稍后重试", Toast.LENGTH_SHORT).show()
                            setCompanyListener(true)
                        }
                    }) {
                        setCompanyListener(true)
                        record_company.hint = "获取供气企业失败，请稍后重试"
                        Toast.makeText(context, "获取供气企业失败，请稍后重试", Toast.LENGTH_SHORT).show()
                    }
        }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        setViewVisibility(if (buttonView.id == R.id.record_tyf_2) record_tyf_layout else record_xh_layout, isChecked)
        if (!isChecked) {
            setText(if (buttonView.id == R.id.record_tyf_2) record_tyf_number else record_xh_number, null)
        }
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
        if (!TextUtils.isEmpty(recordModel!!.phoneftp)) {
            val paths = recordModel!!.phoneftp.split(",".toRegex()).toTypedArray()
            for (path in paths) {
                val mediaFile = MediaFile()
                if (isTemp) {
                    mediaFile.type = 1
                    mediaFile.path = path
                } else {
                    mediaFile.path = Constants.IMAGE_URL + path
                    mediaFile.isLocal = false
                }
                imageAdapter.addData(mediaFile)
            }
        }
        refreshFileCount()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                val list = PictureSelector.obtainMultipleResult(data)
                handleMedia(list)
            } else {
                userInfo = data.getSerializableExtra("data") as UserInfo
                initUserInfo()
            }
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
        PictureSelector.create(this)
                .openGallery(PictureConfig.TYPE_IMAGE)
                .setOutputCameraPath(FileUtil.getFileOutputPath("GasImage"))
                .compress(true) //.compressSavePath("/RQApp/GasImage/")
                .minimumCompressSize(300)
                .imageFormat(PictureMimeType.PNG) //.selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable1 != null && !disposable1!!.isDisposed) {
            disposable1!!.dispose()
        }
    }

    override fun initResID(): Int {
        return R.layout.fragment_gas_user_detail
    }

    companion object {
        fun newInstance(userInfo: UserInfo?, model: GasRecordModel?): GasRecordDetailFragment {
            val fragment = GasRecordDetailFragment()
            fragment.setUserInfo(userInfo)
            fragment.setRecordModel(model)
            return fragment
        }

        fun newInstance(listener: OnPageButtonClickListener<*>?, userInfo: UserInfo?, model: GasRecordModel?): GasRecordDetailFragment {
            val fragment = GasRecordDetailFragment()
            fragment.setListener(listener)
            fragment.setUserInfo(userInfo)
            fragment.setRecordModel(model)
            return fragment
        }
    }
}