package com.bkjcb.rqapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bkjcb.rqapplication.adapter.FileListAdapter;
import com.bkjcb.rqapplication.ftp.FtpUtils;
import com.bkjcb.rqapplication.ftp.UploadTask;
import com.bkjcb.rqapplication.model.CheckStation;
import com.bkjcb.rqapplication.model.CheckStationResult;
import com.bkjcb.rqapplication.model.EmergencyItem;
import com.bkjcb.rqapplication.model.HttpResult;
import com.bkjcb.rqapplication.model.MediaFile;
import com.bkjcb.rqapplication.retrofit.CheckService;
import com.bkjcb.rqapplication.retrofit.EmergencyService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/3/9.
 * Description :
 */
public class CreateEmergencyActivity extends SimpleBaseActivity {

    @BindView(R.id.file_info)
    RecyclerView mFileInfo;
    @BindView(R.id.submit)
    Button mSubmit;
    @BindView(R.id.reporter_info_address)
    EditText mReporterInfoAddress;
    @BindView(R.id.reporter_info_location)
    MaterialSpinner mReporterInfoLocation;
    @BindView(R.id.reporter_info_time)
    TextView mReporterInfoTime;
    @BindView(R.id.reporter_info_name)
    EditText mReporterInfoName;
    @BindView(R.id.reporter_info_tel)
    EditText mReporterInfoTel;
    @BindView(R.id.base_info_address)
    EditText mBaseInfoAddress;
    @BindView(R.id.base_info_time)
    TextView mBaseInfoTime;
    @BindView(R.id.base_info_des)
    EditText mBaseInfoDes;
    @BindView(R.id.base_info_department)
    EditText mBaseInfoDepartment;
    @BindView(R.id.base_info_people)
    EditText mBaseInfoPeople;
    @BindView(R.id.base_info_remark)
    EditText mBaseInfoRemark;
    private EmergencyItem item;
    private TextView currentView;
    private boolean isCanEditable = true;
    private FileListAdapter imageAdapter;
    private List<MediaFile> fileList;
    private int request_code_file = 1000;
    private QMUIBottomSheet bottomSheet;
    private TimePickerView pickerDialog;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_emergency_create;
    }

    @Override
    protected void initView() {
        super.initView();
        initTopbar("新建事故现场", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExit();
            }
        });
    }

    private void initFileView() {
        mFileInfo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new FileListAdapter(R.layout.item_image, item.getStatus() != 2);
        mFileInfo.setAdapter(imageAdapter);
        if (item.getStatus() != 2) {
            imageAdapter.setFooterView(createFooterView());
        }
        imageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MediaPlayActivity.ToActivity(CreateEmergencyActivity.this, ((MediaFile) adapter.getItem(position)).getPath());
            }
        });
        imageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_delete) {
                    MediaFile.getBox().remove(fileList.get(position));
                    refreshFileData();
                }
            }
        });
        refreshFileData();
    }

    private View createFooterView() {
        ImageView view = new ImageView(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(Utils.dip2px(this, 120), Utils.dip2px(this, 120)));
        int padding = Utils.dip2px(this, 5);
        view.setPadding(padding, padding, padding, padding);
        view.setImageResource(R.drawable.icon_add_pic);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomMenu();
            }
        });
        return view;
    }

    public void showPickImg(int type) {
        PictureSelector.create(this)
                .openGallery(type)
                .setOutputCameraPath(Environment.getExternalStorageDirectory()
                        + "/RQApp/RegisterImage/")
                .compress(false)
                .imageFormat(PictureMimeType.PNG)
                //.selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void initData() {
        StyledDialog.init(this);
        //calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        item = (EmergencyItem) getIntent().getSerializableExtra("data");
        if (item == null) {
            item = new EmergencyItem();
            item.setStatus(0);
            item.setSystime(System.currentTimeMillis());
            item.setUuid(Utils.getUUID());
            item.setUserId(MyApplication.user.getUserId());
            item.setPhoneftp(getFtpRemotePath(item.getUuid()));
        } else {
            isCanEditable = item.getStatus() != 2;
            if (!isCanEditable){
                mSubmit.setVisibility(View.GONE);
            }
            loadData();
        }
        getDistrictName();
        initFileView();
    }

    private void getDistrictName() {
        disposable = NetworkApi.getService(CheckService.class)
                .getCheckUnit("区", null)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .map(new Function<CheckStationResult, List<String>>() {
                    @Override
                    public List<String> apply(CheckStationResult checkStationResult) throws Exception {
                        if (checkStationResult.pushState == 200) {
                            return getLocationList(checkStationResult.getDatas());
                        }
                        return null;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (result != null) {
                        mReporterInfoLocation.setAdapter(new MaterialSpinnerAdapter<>(this, result));
                        if (!TextUtils.isEmpty(item.getQushu())) {
                            for (int i = 0; i < result.size(); i++) {
                                if (item.getQushu().equals(result.get(i))) {
                                    mReporterInfoLocation.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }
                        if (!isCanEditable){
                            mReporterInfoLocation.setEnabled(false);
                        }
                    } else {
                        Toast.makeText(this, "获取列表失败！", Toast.LENGTH_SHORT).show();
                    }

                }, throwable -> {
                    Toast.makeText(this, "获取列表失败！" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private List<String> getLocationList(List<CheckStation> list) {
        if (list != null && list.size() > 0) {
            List<String> strings = new ArrayList<>(list.size());
            for (CheckStation station :
                    list) {
                strings.add(station.getQiyemingcheng());
            }
            return strings;
        }
        return null;
    }

    private void onExit() {
        if (item.getStatus() != 2) {
            showTipDialog();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onExit();
    }

    private void saveData() {
        if (verify(mBaseInfoAddress) && verify(mBaseInfoTime)) {
            item.setAccidentAddress(getText(mBaseInfoAddress));
            item.setAccidentDate(getText(mBaseInfoTime));
            item.setMainDescription(getText(mBaseInfoDes));
            item.setDisposalPerson(getText(mBaseInfoDepartment));
            item.setKeyPerson(getText(mBaseInfoPeople));
            item.setRemark(getText(mBaseInfoRemark));
            item.setReportingUnit(getText(mReporterInfoAddress));
            item.setReportingStaff(getText(mReporterInfoName));
            item.setQushu(mReporterInfoLocation.getText().toString());
            item.setReportingDate(getText(mReporterInfoTime));
            item.setContactPhone(getText(mReporterInfoTel));
            item.setStatus(1);
            long id = EmergencyItem.getBox().put(item);
            if (item.id == 0) {
                item.id = id;
            }
        }
    }

    private List<String> getFilePath(List<MediaFile> files) {
        List<String> strings = new ArrayList<>();
        for (MediaFile file : files) {
            strings.add(file.getPath());
        }
        return strings;
    }

    private void submitData() {
        if (!verify()) {
            return;
        }
        showLoading(true);
        disposable = UploadTask.createUploadTask(getFilePath(fileList), item.getPhoneftp(), new FtpUtils.UploadProgressListener() {
            @Override
            public void onUploadProgress(String currentStep, long uploadSize, long size, File file) {

            }
        }).subscribeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<HttpResult>>() {
                    @Override
                    public ObservableSource<HttpResult> apply(Boolean aBoolean) throws Exception {
                        return aBoolean ? NetworkApi.getService(EmergencyService.class)
                                .submit(item.getUserId(),
                                        item.getReportingUnit(),
                                        item.getQushu(),
                                        item.getRemark(),
                                        item.getReportingDate(),
                                        item.getAccidentDate(),
                                        item.getAccidentAddress(),
                                        item.getDisposalPerson(),
                                        item.getKeyPerson(),
                                        item.getReportingStaff(),
                                        item.getContactPhone(),
                                        item.getMainDescription(),
                                        item.getPhoneftp()) : null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult httpResult) throws Exception {
                        if (httpResult.pushState == 200) {
                            showLoading(false);
                            Toast.makeText(CreateEmergencyActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            updateStatus();
                            finish();
                        } else {
                            Toast.makeText(CreateEmergencyActivity.this, "提交失败！" + httpResult.pushMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showLoading(false);
                        Toast.makeText(CreateEmergencyActivity.this, "提交失败：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void hideSoftInput(){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()){
            inputMethodManager.hideSoftInputFromWindow(mBaseInfoRemark.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private String getFtpRemotePath(String uuid) {
        return "shiguxianchang/" + uuid;
    }

    private void updateStatus() {
        item.setStatus(2);
        EmergencyItem.getBox().put(item);
    }

    private void loadData() {
        setText(mBaseInfoAddress, item.getAccidentAddress());
        setText(mBaseInfoTime, item.getAccidentDate());
        setText(mBaseInfoDes, item.getMainDescription());
        setText(mBaseInfoDepartment, item.getDisposalPerson());
        setText(mBaseInfoPeople, item.getKeyPerson());
        setText(mReporterInfoAddress, item.getReportingUnit());
        setText(mReporterInfoTel, item.getContactPhone());
        setText(mReporterInfoTime, item.getReportingDate());
        setText(mReporterInfoName, item.getReportingStaff());
        setText(mBaseInfoRemark, item.getRemark());
    }

    private void showBottomMenu() {
        if (bottomSheet == null) {
            bottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(this)
                    .addItem(R.drawable.vector_drawable_file_photo, "照片", "照片")
                    .addItem(R.drawable.vector_drawable_file_video, "视频", "视频")
                    .addItem(R.drawable.vector_drawable_file_audio, "音频", "音频")
                    .addItem(R.drawable.vector_drawable_file_table, "文件", "文件")
                    .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                        @Override
                        public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                            switch (position) {
                                case 0:
                                    showPickImg(PictureMimeType.ofImage());
                                    break;
                                case 1:
                                    showPickImg(PictureMimeType.ofVideo());
                                    break;
                                case 2:
                                    showPickImg(PictureMimeType.ofAudio());
                                    break;
                                case 3:
                                    showFilePicker();
                                    break;
                                default:
                            }
                            dialog.dismiss();
                        }
                    }).setTitle("选择文件类型").build();
        }
        if (!bottomSheet.isShowing()) {
            bottomSheet.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == request_code_file) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                handleFilePath(list);
            } else if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
                handleMedia(list);
            }
            refreshFileData();
        }
    }

    private void handleMedia(List<LocalMedia> list) {
        MediaFile mediaFile;
        for (LocalMedia media : list) {
            mediaFile = new MediaFile();
            mediaFile.setItemId(item.getUuid());
            mediaFile.setType(media.getMimeType());
            mediaFile.setPath(media.getPath());
            mediaFile.setFileName(mediaFile.getFileName());
            MediaFile.save(mediaFile);
        }
    }

    private void handleFilePath(List<String> list) {
        MediaFile mediaFile;
        for (String path : list) {
            mediaFile = new MediaFile();
            mediaFile.setItemId(item.getUuid());
            mediaFile.setType(4);
            mediaFile.setPath(path);
            mediaFile.setFileName(Utils.getFileName(path));
            MediaFile.save(mediaFile);
        }

    }

    public void refreshFileData() {
        fileList = MediaFile.getAll(item.getUuid());
        imageAdapter.setNewData(fileList);
    }

    public static void ToActivity(Context context, EmergencyItem item) {
        Intent intent = new Intent(context, CreateEmergencyActivity.class);
        if (item != null) {
            intent.putExtra("data", item);
        }
        context.startActivity(intent);
    }

    private String getText(TextView view) {
        return view.getText().toString();
    }

    private void showFilePicker() {
        new LFilePicker()
                .withActivity(this)
                .withRequestCode(request_code_file)
                .withStartPath("/storage/emulated/0/Download")
                .withBackIcon(Constant.BACKICON_STYLETHREE)
                .withTheme(R.style.LFileTheme)
                .withTitle("选择文件")
                .withMutilyMode(true)
                .start();
    }

    private void setText(TextView view, String text) {
        if (!isCanEditable) {
            view.setEnabled(false);
            view.setHint("");
        }
        view.setText(text);
    }

    @OnClick({R.id.base_info_time, R.id.reporter_info_time, R.id.submit})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.reporter_info_time:
                currentView = mReporterInfoTime;
                createDatePicker();
                break;
            case R.id.base_info_time:
                currentView = mBaseInfoTime;
                createDatePicker();
                break;
            case R.id.submit:
                saveData();
                showFinishTipDialog();
                break;
        }
    }

    private void createDatePicker() {

        if (pickerDialog == null) {
            pickerDialog = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                    if (currentView.getId() == R.id.base_info_time) {
                        currentView.setText(Utils.dateFormat("yyyy-MM-dd hh:mm:ss", date));
                    } else {
                        currentView.setText(Utils.dateFormat("yyyy-MM-dd hh:mm", date));
                    }

                }
            })
                    .setType(new boolean[]{true, true, true, true, true, true})// 默认全部显示
                    .setCancelText("取消")//取消按钮文字
                    .setSubmitText("确定")//确认按钮文字
                    .setContentTextSize(16)//滚轮文字大小
                    .setTitleSize(20)//标题文字大小
                    .setTitleText("选择时间")//标题文字
                    .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                    .isCyclic(true)//是否循环滚动
                    .setTitleColor(getColorResource(R.color.colorText))//标题文字颜色
                    .setSubmitColor(getColorResource(R.color.color_type_0))//确定按钮文字颜色
                    .setCancelColor(getColorResource(R.color.colorText))//取消按钮文字颜色
                    .setTitleBgColor(getColorResource(R.color.colorAccent))//标题背景颜色 Night mode
                    .setBgColor(getColorResource(R.color.colorText))//滚轮背景颜色 Night mode
                    .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                    .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .isDialog(false)//是否显示为对话框样式
                    .build();
        }
        if (!pickerDialog.isShowing()) {
            hideSoftInput();
            pickerDialog.show();
        }
    }

    private void showTipDialog() {
        StyledDialog.buildIosAlert("提示", "当前记录尚未提交，是否退出？", new MyDialogListener() {
            @Override
            public void onFirst() {
                saveData();
                finish();
            }

            @Override
            public void onSecond() {
                finish();
            }
        }).setBtnText("保存并退出", "直接退出").show();
    }

    private int getColorResource(int id) {
        return getResources().getColor(id);
    }

    private boolean verify() {
        return verify(mBaseInfoAddress)
                && verify(mBaseInfoTime)
                && verify(mBaseInfoDes)
                && verify(mBaseInfoDepartment)
                && verify(mBaseInfoPeople)
                //&& verify(mBaseInfoRemark)

                && verify(mReporterInfoName)
                && verify(mReporterInfoTime)
                && verify(mReporterInfoTel)
                && verify(mReporterInfoLocation)
                && verify(mReporterInfoAddress)
                ;
    }

    private boolean verify(TextView view) {
        if (TextUtils.isEmpty(getText(view))) {
            view.requestFocus();
            showToast(view.getHint().toString());
            return false;
        }
        return true;
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    protected void showFinishTipDialog() {
        StyledDialog.buildIosAlert("提示", "是否提交当前记录(提交后将不可修改)?", new MyDialogListener() {
            @Override
            public void onFirst() {
                submitData();
            }

            @Override
            public void onSecond() {
                finish();
            }
        }).setBtnText("提交记录", "仅保存")
                .show();

    }
}
