package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bkjcb.rqapplication.adapter.FileListAdapter;
import com.bkjcb.rqapplication.ftp.FtpUtils;
import com.bkjcb.rqapplication.ftp.UploadTask;
import com.bkjcb.rqapplication.model.ActionRegisterItem;
import com.bkjcb.rqapplication.model.HttpResult;
import com.bkjcb.rqapplication.model.MediaFile;
import com.bkjcb.rqapplication.retrofit.ActionRegsiterService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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
public class CreateActionRegisterActivity extends SimpleBaseActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.base_info_from)
    EditText mBaseInfoFrom;
    @BindView(R.id.base_info_di)
    EditText mBaseInfoDi;
    @BindView(R.id.base_info_zi)
    EditText mBaseInfoZi;
    @BindView(R.id.base_info_hao)
    EditText mBaseInfoHao;
    @BindView(R.id.base_info_time)
    TextView mBaseInfoTime;
    @BindView(R.id.base_info_address)
    EditText mBaseInfoAddress;
    @BindView(R.id.base_info_detail)
    EditText mBaseInfoDetail;
    @BindView(R.id.litigant_info_name)
    EditText mLitigantInfoName;
    @BindView(R.id.litigant_info_address)
    EditText mLitigantInfoAddress;
    @BindView(R.id.litigant_info_tel)
    EditText mLitigantInfoTel;
    @BindView(R.id.informer_info_name)
    EditText mInformerInfoName;
    @BindView(R.id.informer_info_address)
    EditText mInformerInfoAddress;
    @BindView(R.id.informer_info_tel)
    EditText mInformerInfoTel;
    @BindView(R.id.undertaker_info_name)
    EditText mUndertakerInfoName;
    @BindView(R.id.undertaker_info_time)
    TextView mUndertakerInfoTime;
    @BindView(R.id.undertaker_info_remark)
    EditText mUndertakerInfoRemark;
    @BindView(R.id.file_info)
    RecyclerView mFileInfo;
    @BindView(R.id.submit)
    Button mSubmit;
    private DatePickerDialog pickerDialog;
    private Calendar calendar;
    private ActionRegisterItem item;
    private TextView currentView;
    private boolean isCanEditable = true;
    private FileListAdapter imageAdapter;
    private List<MediaFile> fileList;
    private int request_code_file = 1000;
    private QMUIBottomSheet bottomSheet;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_register_create;
    }

    @Override
    protected void initView() {
        super.initView();
        initTopbar("新建立案", new View.OnClickListener() {
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
                MediaPlayActivity.ToActivity(CreateActionRegisterActivity.this, ((MediaFile) adapter.getItem(position)).getPath());
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
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        item = (ActionRegisterItem) getIntent().getSerializableExtra("data");
        if (item == null) {
            item = new ActionRegisterItem();
            item.setStatus(0);
            item.setSystime(System.currentTimeMillis());
            item.setUuid(Utils.getUUID());
            item.setUserId(MyApplication.user.getUserId());
            item.setWid("null");
            item.setPhoneftp(getFtpRemotePath(item.getUuid()));
        } else {
            isCanEditable = item.getStatus() != 2;
            if (!isCanEditable){
                mSubmit.setVisibility(View.GONE);
            }
            loadData();
        }
        initFileView();
    }

    private void onExit() {
        if (item.getStatus() != 2 && !isSave()) {
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
        item.setCase_source(getText(mBaseInfoFrom));
        item.setDi(getText(mBaseInfoDi));
        item.setZi(getText(mBaseInfoZi));
        item.setHao(getText(mBaseInfoHao));
        item.setCrime_time(getText(mBaseInfoTime));
        item.setCrime_address(getText(mBaseInfoAddress));
        item.setCase_introduction(getText(mBaseInfoDetail));

        item.setParty(getText(mLitigantInfoName));
        item.setParty_address(getText(mLitigantInfoAddress));
        item.setParty_phone(getText(mLitigantInfoTel));

        item.setReporter(getText(mInformerInfoName));
        item.setReporter_address(getText(mInformerInfoAddress));
        item.setReporter_phone(getText(mInformerInfoTel));

        item.setUndertaker(getText(mUndertakerInfoName));
        item.setUndertaker_time(getText(mUndertakerInfoTime));
        item.setUndertaker_opinion(getText(mUndertakerInfoRemark));
        item.setStatus(1);
        long id = ActionRegisterItem.getBox().put(item);
        if (item.id == 0) {
            item.id = id;
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
        showLoading(true);
        disposable = UploadTask.createUploadTask(getFilePath(fileList), item.getPhoneftp(), new FtpUtils.UploadProgressListener() {
            @Override
            public void onUploadProgress(String currentStep, long uploadSize, long size, File file) {

            }
        }).subscribeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<HttpResult>>() {
                    @Override
                    public ObservableSource<HttpResult> apply(Boolean aBoolean) throws Exception {
                        return aBoolean ? NetworkApi.getService(ActionRegsiterService.class)
                                .submit(item, MyApplication.user.getUserId()) : null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult httpResult) throws Exception {
                        showLoading(false);
                        if (httpResult.pushState == 200) {
                            Toast.makeText(CreateActionRegisterActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            updateStatus();
                            finish();
                        } else {
                            Toast.makeText(CreateActionRegisterActivity.this, "提交失败！" + httpResult.pushMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showLoading(false);
                        Toast.makeText(CreateActionRegisterActivity.this, "提交失败：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getFtpRemotePath(String uuid) {
        return "jichazhifa/lian/" + uuid;
    }

    private void updateStatus() {
        item.setStatus(2);
        ActionRegisterItem.getBox().put(item);
    }

    private void loadData() {
        setText(mBaseInfoFrom, item.getCase_source());
        setText(mBaseInfoDi, item.getDi());
        setText(mBaseInfoZi, item.getZi());
        setText(mBaseInfoHao, item.getHao());
        setText(mBaseInfoTime, item.getCrime_time());
        setText(mBaseInfoAddress, item.getCrime_address());
        setText(mBaseInfoDetail, item.getCase_introduction());

        setText(mLitigantInfoName, item.getParty());
        setText(mLitigantInfoAddress, item.getParty_address());
        setText(mLitigantInfoTel, item.getParty_phone());

        setText(mInformerInfoName, item.getReporter());
        setText(mInformerInfoAddress, item.getReporter_address());
        setText(mInformerInfoTel, item.getReporter_phone());

        setText(mUndertakerInfoName, item.getUndertaker());
        setText(mUndertakerInfoTime, item.getUndertaker_time());
        setText(mUndertakerInfoRemark, item.getUndertaker_opinion());
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

    public static void ToActivity(Context context, ActionRegisterItem item) {
        Intent intent = new Intent(context, CreateActionRegisterActivity.class);
        if (item != null) {
            intent.putExtra("data", item);
        }
        context.startActivity(intent);
    }

    private String getText(EditText view) {
        return view.getText().toString();
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

    @OnClick({R.id.base_info_time, R.id.undertaker_info_time, R.id.submit})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.base_info_time:
                currentView = mBaseInfoTime;
                createDatePicker();
                break;
            case R.id.undertaker_info_time:
                currentView = mUndertakerInfoTime;
                createDatePicker();
                break;
            case R.id.submit:
                if (!isSave()) {
                    saveData();
                }
                if (verify()) {
                    showFinishTipDialog();
                }
                break;
        }
    }

    private void createDatePicker() {
        if (pickerDialog == null) {
            pickerDialog = DatePickerDialog.newInstance(
                    this,
                    calendar.get(Calendar.YEAR), // Initial year selection
                    calendar.get(Calendar.MONTH), // Initial month selection
                    calendar.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );
        }
        if (!pickerDialog.isAdded()) {
            pickerDialog.show(getFragmentManager(), "DatePickerDialog");
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        currentView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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

    private boolean verify() {
        return verify(mBaseInfoFrom)
                && verify(mBaseInfoDetail)
                && verify(mBaseInfoTime)
                && verify(mBaseInfoAddress)
                && verify(mBaseInfoDi)
                && verify(mBaseInfoZi)
                && verify(mBaseInfoHao)

                && verify(mLitigantInfoName)
                && verify(mLitigantInfoAddress)
                && verify(mLitigantInfoTel)

                && verify(mInformerInfoName)
                && verify(mInformerInfoAddress)
                && verify(mInformerInfoTel)

                && verify(mUndertakerInfoName)
                && verify(mUndertakerInfoRemark)
                && verify(mUndertakerInfoTime)
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

    private boolean isSave() {
        return TextUtils.isEmpty(getText(mBaseInfoFrom)) || TextUtils.isEmpty(getText(mBaseInfoTime)) || TextUtils.isEmpty(getText(mBaseInfoAddress));
    }

    protected void showFinishTipDialog() {
        StyledDialog.buildIosAlert("提示", "是否提交当前记录(提交后将不可修改)？", new MyDialogListener() {
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
