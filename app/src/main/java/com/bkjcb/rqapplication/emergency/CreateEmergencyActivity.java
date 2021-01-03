package com.bkjcb.rqapplication.emergency;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bkjcb.rqapplication.Constants;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.MediaPlayActivity;
import com.bkjcb.rqapplication.base.MyApplication;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.base.adapter.FileListAdapter;
import com.bkjcb.rqapplication.base.ftp.UploadTask;
import com.bkjcb.rqapplication.base.model.HttpResult;
import com.bkjcb.rqapplication.base.model.MediaFile;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.base.util.FileUtil;
import com.bkjcb.rqapplication.base.util.RxJavaUtil;
import com.bkjcb.rqapplication.base.util.Utils;
import com.bkjcb.rqapplication.emergency.model.EmergencyItem;
import com.bkjcb.rqapplication.emergency.model.EmergencyItem_;
import com.bkjcb.rqapplication.emergency.model.EmergencyModel;
import com.bkjcb.rqapplication.emergency.retrofit.EmergencyService;
import com.bkjcb.rqapplication.stationCheck.model.CheckStation;
import com.bkjcb.rqapplication.stationCheck.model.CheckStationResult;
import com.bkjcb.rqapplication.stationCheck.retrofit.CheckService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

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
public class CreateEmergencyActivity extends SimpleBaseActivity implements SwitchDateTimeDialogFragment.OnButtonClickListener {

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
    @BindView(R.id.layout_content)
    View contentView;
    private EmergencyItem item;
    private TextView currentView;
    private boolean isCanEditable = true;
    private FileListAdapter imageAdapter;
    private List<MediaFile> fileList;
    private int request_code_file = 1000;
    private QMUIBottomSheet bottomSheet;
    private SwitchDateTimeDialogFragment pickerDialog;
    private EmergencyModel emergencyModel;
    private String pre_path;
    private View.OnClickListener clickListener;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_emergency_create;
    }

    @Override
    protected void initView() {
        super.initView();
        initTopbar("事故详情", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExit();
            }
        });
        initEmptyView();
    }

    private void initFileView() {
        mFileInfo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new FileListAdapter(R.layout.item_image, isCanEditable);
        mFileInfo.setAdapter(imageAdapter);
        if (isCanEditable) {
            imageAdapter.setFooterView(createFooterView(), 0, LinearLayout.HORIZONTAL);
        }
        imageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MediaPlayActivity.ToActivity(CreateEmergencyActivity.this, adapter.getData(),position);
            }
        });
        imageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.item_grid_bt) {
                    MediaFile file = fileList.get(position);
                    fileList.remove(position);
                    adapter.notifyDataSetChanged();
                    if (file.id > 0) {
                        MediaFile.getBox().remove(file.id);
                    }
                }
            }
        });
        initFileData();
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
                .setOutputCameraPath(FileUtil.getFileOutputPath("EmergencyImage"))
                .compress(false)
                .imageFormat(PictureMimeType.PNG)
                //.selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void initData() {
        StyledDialog.init(this);
        //calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        emergencyModel = (EmergencyModel) getIntent().getSerializableExtra("data");
        if (emergencyModel == null) {
            showErrorView(null);
        } else {
            if (emergencyModel.getState().equals("已销根")) {
                isCanEditable = false;
                mSubmit.setVisibility(View.GONE);
                handleItemResult();
            } else {
                getDetailInfo();
            }
        }
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
                        if (!isCanEditable) {
                            mReporterInfoLocation.setEnabled(false);
                        }
                    } else {
                        Toast.makeText(this, "获取列表失败！", Toast.LENGTH_SHORT).show();
                    }

                }, throwable -> {
                    Toast.makeText(this, "获取列表失败！" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void handleItemResult() {
        if (item == null) {
            item = EmergencyItem.getBox().query().equal(EmergencyItem_.uuid, emergencyModel.getAccidentid()).build().findFirst();
            if (item == null) {
                item = new EmergencyItem();
                item.setStatus(0);
                item.setSystime(System.currentTimeMillis());
                item.setUuid(emergencyModel.getAccidentid());
                item.setUserId(MyApplication.getUser().getUserId());
                pre_path = getFtpRemotePath(item.getUuid());
                item.setAccidentAddress(emergencyModel.getCompleteaddress());
                item.setAccidentDate(emergencyModel.getReceivedate());
                item.setMainDescription(emergencyModel.getMemo());
                item.setReportingUnit(emergencyModel.getReport_party());
                item.setDisposalPerson(emergencyModel.getDisposal_people());
            }
        } else {
            isCanEditable = false;
            mSubmit.setVisibility(View.GONE);
        }
        loadData();
        getDistrictName();
        initFileView();
    }

    private void getDetailInfo() {
        //showLoadingView();
        showLoading();
        disposable = NetworkApi.getService(EmergencyService.class)
                .getAccidentDetail(emergencyModel.getAccidentid())
                .compose(RxJavaUtil.getObservableTransformer())
                .subscribe(new Consumer<SimpleHttpResult<EmergencyItem>>() {
                    @Override
                    public void accept(SimpleHttpResult<EmergencyItem> result) throws Exception {
                        if (result.pushState == 200) {
                            contentView.setVisibility(View.VISIBLE);
                            item = result.getDatas();
                            handleItemResult();
                            hideEmptyView();
                        } else {
                            contentView.setVisibility(View.GONE);
                            showErrorView(createListener());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showErrorView(createListener());
                        contentView.setVisibility(View.GONE);
                    }
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
        if (item != null && isCanEditable) {
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
            item.setPhoneftp(getFilePath());
        }
    }

    private View.OnClickListener createListener() {
        if (clickListener == null) {
            clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDetailInfo();
                }
            };
        }
        return clickListener;
    }

    private String getFilePath() {
        StringBuilder builder;
        if (fileList.size() > 0) {
            builder = new StringBuilder();
            for (MediaFile mediaFile : fileList) {
                builder.append(mediaFile.getPath()).append(",");
            }
            return builder.substring(0, builder.length() - 1);
        }
        return "";
    }

    private void saveDataToDatabase() {
        item.setStatus(1);
        EmergencyItem.getBox().put(item);
    }

    private void deleteData() {
        if (item.id > 0) {
            EmergencyItem.getBox().remove(item.id);
        }
        MediaFile.getBox().remove(fileList);
    }

    private List<String> getFilePath(List<MediaFile> files) {
        List<String> strings = new ArrayList<>();
        for (MediaFile file : files) {
            strings.add(file.getPath());
        }
        return strings;
    }

    private String getRemoteFilePath() {
        if (fileList != null && fileList.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (MediaFile mediaFile : fileList) {
                builder.append(pre_path).append("/").append(mediaFile.getFileName()).append(",");
            }
            return builder.substring(0, builder.length() - 1);
        }
        return "";
    }

    private void submitData() {
        if (!verify()) {
            return;
        }
        showLoading(true);
        disposable = UploadTask.createUploadTask(getFilePath(fileList), pre_path).subscribeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<HttpResult>>() {
                    @Override
                    public ObservableSource<HttpResult> apply(Boolean aBoolean) throws Exception {
                        return aBoolean ? NetworkApi.getService(EmergencyService.class)
                                .submit(item.getUserId(),
                                        item.getUuid(),
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
                                        getRemoteFilePath()) : null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult httpResult) throws Exception {
                        if (httpResult.pushState == 200) {
                            showLoading(false);
                            Toast.makeText(CreateEmergencyActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            deleteData();
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

    private void hideSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
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
            imageAdapter.notifyDataSetChanged();
        }
    }

    private void handleMedia(List<LocalMedia> list) {
        MediaFile mediaFile;
        for (LocalMedia media : list) {
            mediaFile = new MediaFile();
            mediaFile.setItemId(item.getUuid());
            mediaFile.setType(media.getMimeType());
            mediaFile.setPath(media.getPath());
            mediaFile.setFileName(Utils.getFileName(media.getPath()));
            MediaFile.save(mediaFile);
            fileList.add(mediaFile);
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
            fileList.add(mediaFile);
        }

    }

    public void initFileData() {
        fileList = new ArrayList<>();
        if (item.getStatus() == 0 && !TextUtils.isEmpty(item.getPhoneftp())) {
            String[] strings = item.getPhoneftp().split(",");
            for (String path : strings) {
                MediaFile mediaFile = new MediaFile();
                mediaFile.setType(Utils.getFileType(path));
                mediaFile.setPath(Constants.IMAGE_URL + path);
                fileList.add(mediaFile);
            }
        } else if (!TextUtils.isEmpty(item.getUuid())){
            fileList = MediaFile.getAll(item.getUuid());
        }
        imageAdapter.setNewData(fileList);
    }

    public static void ToActivity(Context context, EmergencyModel item) {
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
            pickerDialog = SwitchDateTimeDialogFragment.newInstance("选择时间", "确定", "取消");
            pickerDialog.set24HoursMode(false);
            pickerDialog.setOnButtonClickListener(this);
            pickerDialog.setDefaultDateTime(new Date());
        }
        if (!pickerDialog.isVisible()) {
            hideSoftInput();
            pickerDialog.show(getSupportFragmentManager(), "TIME_PICKER");
        }
    }

    private void showTipDialog() {
        StyledDialog.buildIosAlert("提示", "当前记录尚未提交，是否退出？", new MyDialogListener() {
            @Override
            public void onFirst() {
                saveData();
                saveDataToDatabase();
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
                && verify(mBaseInfoDes,"请填写情况描述！")
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
    private boolean verify(TextView view,String tip) {
        if (TextUtils.isEmpty(getText(view))) {
            view.requestFocus();
            showToast(tip);
            return false;
        }
        return true;
    }
    private boolean verify(TextView view) {
        if (TextUtils.isEmpty(getText(view))) {
            view.requestFocus();
            showToast(view.getHint().toString());
            return false;
        }
        return true;
    }
    private boolean verify(TextView view,String tip) {
        if (TextUtils.isEmpty(getText(view))) {
            view.requestFocus();
            showToast(tip);
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
                saveDataToDatabase();
                finish();
            }
        }).setBtnText("提交记录", "仅保存")
                .show();

    }

    @Override
    public void onPositiveButtonClick(Date date) {
        currentView.setText(Utils.dateFormat("yyyy-MM-dd hh:mm", date));
    }

    @Override
    public void onNegativeButtonClick(Date date) {

    }
}
