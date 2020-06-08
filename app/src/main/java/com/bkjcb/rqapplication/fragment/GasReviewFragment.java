package com.bkjcb.rqapplication.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.bkjcb.rqapplication.Constants;
import com.bkjcb.rqapplication.MediaPlayActivity;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.adapter.FileListAdapter;
import com.bkjcb.rqapplication.interfaces.OnPageButtonClickListener;
import com.bkjcb.rqapplication.model.MediaFile;
import com.bkjcb.rqapplication.model.ReviewRecord;
import com.bkjcb.rqapplication.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.dialog.StyledDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public class GasReviewFragment extends BaseSimpleFragment implements DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.info_unit)
    EditText mInfoUnit;
    @BindView(R.id.info_station)
    TextView mInfoStation;
    @BindView(R.id.info_name_title)
    TextView mInfoNameTitle;
    @BindView(R.id.check_result_radio_ok)
    RadioButton mCheckResultRadioOk;
    @BindView(R.id.check_result_radio_failure)
    RadioButton mCheckResultRadioFailure;
    @BindView(R.id.check_result_radioGroup)
    RadioGroup mCheckResultRadioGroup;
    @BindView(R.id.check_yhq)
    SuperTextView mCheckYhq;
    @BindView(R.id.check_yhq_time)
    TextView mCheckYhqTime;
    @BindView(R.id.check_yhq_layout)
    LinearLayout mCheckYhqLayout;
    @BindView(R.id.check_tyq_count)
    EditText mCheckTyqCount;
    @BindView(R.id.check_tyq_change)
    SuperTextView mCheckTyqChange;
    @BindView(R.id.check_tyq_time)
    TextView mCheckTyqTime;
    @BindView(R.id.check_tyq_layout)
    LinearLayout mCheckTyqLayout;
    @BindView(R.id.check_ljg_count)
    EditText mCheckLjgCount;
    @BindView(R.id.check_ljg_change)
    SuperTextView mCheckLjgChange;
    @BindView(R.id.check_ljg_time)
    TextView mCheckLjgTime;
    @BindView(R.id.check_ljg_layout)
    LinearLayout mCheckLjgLayout;
    @BindView(R.id.check_rj_count)
    EditText mCheckRjCount;
    @BindView(R.id.check_rj_change)
    SuperTextView mCheckRjChange;
    @BindView(R.id.check_rj_time)
    TextView mCheckRjTime;
    @BindView(R.id.check_rj_layout)
    LinearLayout mCheckRjLayout;
    @BindView(R.id.content_layout)
    LinearLayout mContentLayout;
    @BindView(R.id.record_submit)
    TextView mSubmit;
    @BindView(R.id.file_info)
    RecyclerView mFileInfo;
    @BindView(R.id.file_count)
    TextView mFileCount;
    @BindView(R.id.info_username)
    TextView mUserName;
    private DatePickerDialog pickerDialog;
    private TextView currentTextView;
    private ReviewRecord record;
    private boolean isCanChange = true;
    private OnPageButtonClickListener listener;
    private FileListAdapter imageAdapter;

    public void setRecord(ReviewRecord record) {
        this.record = record;
    }

    public void setListener(OnPageButtonClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_review_detail;
    }

    public static GasReviewFragment newInstance(ReviewRecord record) {
        GasReviewFragment fragment = new GasReviewFragment();
        fragment.setRecord(record);
        return fragment;
    }

    public static GasReviewFragment newInstance(ReviewRecord record, OnPageButtonClickListener listener) {
        GasReviewFragment fragment = new GasReviewFragment();
        fragment.setRecord(record);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    protected void initView() {
        isCanChange = record.status == 1;
        setText(mUserName, record.yonghuming);
        if (isCanChange) {
            mCheckYhq.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setViewVisibility(mContentLayout, !isChecked);
                    setViewVisibility(mCheckYhqLayout, isChecked);
                    record.yehuaqi_shiyong = isChecked ? "是" : "否";
                }
            });
            mCheckTyqChange.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setViewVisibility(mCheckTyqLayout, isChecked);
                    record.tiaoyaqi_zhenggai = isChecked ? "是" : "否";
                }
            });
            mCheckLjgChange.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setViewVisibility(mCheckLjgLayout, isChecked);
                    record.lianjieguan_zhenggai = isChecked ? "是" : "否";
                }
            });
            mCheckRjChange.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setViewVisibility(mCheckRjLayout, isChecked);
                    record.ranju_zhenggai = isChecked ? "是" : "否";
                }
            });
        } else {
            setText(mInfoUnit, record.jianchadanwei);
            setText(mInfoStation, record.jianchariqi);
            mCheckResultRadioOk.setEnabled(false);
            mCheckResultRadioFailure.setEnabled(false);
            setChecked(mCheckYhq, "是".equals(record.yehuaqi_shiyong), mCheckYhqLayout, mCheckYhqTime, record.tuihuriqi);
            if ("是".equals(record.yehuaqi_shiyong)) {
                setViewVisibility(mContentLayout, false);
            }
            setText(mCheckTyqCount, record.tiaoyaqi_geshu);
            setChecked(mCheckTyqChange, "是".equals(record.tiaoyaqi_zhenggai), mCheckTyqLayout, mCheckTyqTime, record.tiaoyaqi_zhenggairiqi);
            setText(mCheckLjgCount, record.lianjieguan_geshu);
            setChecked(mCheckLjgChange, "是".equals(record.lianjieguan_zhenggai), mCheckLjgLayout, mCheckLjgTime, record.lianjieguan_zhenggairiqi);
            setText(mCheckRjCount, record.ranju_geshu);
            setChecked(mCheckRjChange, "是".equals(record.ranju_zhenggai), mCheckRjLayout, mCheckRjTime, record.ranju_zhenggairiqi);
        }
        initCheckBox();
    }

    @Override
    protected void initData() {
        if (isCanChange) {
            StyledDialog.init(getContext());
            record.tiaoyaqi_zhenggai = "否";
            record.ranju_zhenggai = "否";
            record.yehuaqi_shiyong = "否";
            record.lianjieguan_zhenggai = "否";
        } else {
            setViewVisibility(mSubmit, false);
        }
        initFileView();
    }

    @OnClick({R.id.info_station, R.id.check_yhq_time, R.id.check_tyq_time, R.id.check_ljg_time, R.id.check_rj_time, R.id.record_submit})
    public void onClick(View v) {
        if (v.getId() != R.id.record_submit) {
            switch (v.getId()) {
                case R.id.info_station:
                    currentTextView = mInfoStation;
                    break;
                case R.id.check_yhq_time:
                    currentTextView = mCheckYhqTime;
                    break;
                case R.id.check_tyq_time:
                    currentTextView = mCheckTyqTime;
                    break;
                case R.id.check_ljg_time:
                    currentTextView = mCheckLjgTime;
                    break;
                case R.id.check_rj_time:
                    currentTextView = mCheckRjTime;
                    break;
                default:
                    break;
            }
            createDatePicker();
        } else {
            collectParams();
            if (verifyAllData()) {
                listener.onNext(imageAdapter.getData());
            }
        }
    }

    private void setViewVisibility(View v, boolean isShow) {
        v.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void createDatePicker() {
        if (pickerDialog == null) {
            Calendar calendar = Utils.getCalendar();
            pickerDialog = DatePickerDialog.newInstance(
                    this,
                    calendar.get(Calendar.YEAR), // Initial year selection
                    calendar.get(Calendar.MONTH), // Initial month selection
                    calendar.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );
        }
        if (!pickerDialog.isAdded()) {
            pickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        currentTextView.setText(String.format(Locale.CHINESE, "%s-%s-%s", year, monthOfYear + 1, dayOfMonth));
    }

    private void initCheckBox() {
        if (!TextUtils.isEmpty(record.jianchajieguo)) {
            if ("合格".equals(record.jianchajieguo)) {
                mCheckResultRadioGroup.check(R.id.check_result_radio_ok);
                changeTextColor(true);
            } else {
                mCheckResultRadioGroup.check(R.id.check_result_radio_failure);
                changeTextColor(false);
            }
        } else {
            mCheckResultRadioGroup.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.check_result_radio_ok) {
            record.jianchajieguo = "合格";
            changeTextColor(true);
        } else {
            record.jianchajieguo = "不合格";
            changeTextColor(false);
        }
    }

    private void changeTextColor(boolean isOk) {
        if (isOk) {
            mCheckResultRadioOk.setTextColor(getResources().getColor(R.color.colorText));
            mCheckResultRadioFailure.setTextColor(getResources().getColor(R.color.colorSecondDrayText));
        } else {
            mCheckResultRadioOk.setTextColor(getResources().getColor(R.color.colorSecondDrayText));
            mCheckResultRadioFailure.setTextColor(getResources().getColor(R.color.colorText));
        }
    }

    private void setText(TextView view, String value) {
        view.setText(value);
        view.setEnabled(false);
    }

    private void setChecked(SuperTextView box, boolean enable, View layout, TextView view, String value) {
        box.setSwitchIsChecked(enable);
        box.setSwitchClickable(false);
        setViewVisibility(layout, enable);
        if (enable && view != null) {
            setText(view, value);
        }
    }

    private String getText(TextView view) {
        return view.getText().toString();
    }

    private boolean verifyAllData() {
        return verify(mInfoUnit, record.jianchadanwei, "请填写检查单位") &&
                verify(mInfoStation, record.jianchariqi, "请选择检查日期") &&
                verify(mInfoNameTitle, record.jianchajieguo, "请选择检查结果") &&
                verify(mFileInfo, "请添加三到五张照片") &&
                (("是".equals(record.yehuaqi_shiyong) &&
                        verify(mCheckYhqTime, record.tuihuriqi, "请选择退户日期")
                ) || ("否".equals(record.yehuaqi_shiyong) &&
                        verify(mCheckTyqCount, record.tiaoyaqi_geshu, "请填写已置换调压器个数")
                        &&
                        (("是".equals(record.tiaoyaqi_zhenggai) &&
                                verify(mCheckTyqTime, record.tiaoyaqi_zhenggairiqi, "请选择整改日期")
                        ) || ("否".equals(record.tiaoyaqi_zhenggai)))
                        &&
                        verify(mCheckLjgCount, record.lianjieguan_geshu, "请填写已置换连接管个数")
                        &&
                        (("是".equals(record.lianjieguan_zhenggai) &&
                                verify(mCheckLjgTime, record.lianjieguan_zhenggairiqi, "请选择整改日期")
                        ) || ("否".equals(record.lianjieguan_zhenggai)))
                        &&
                        verify(mCheckRjCount, record.ranju_geshu, "请填写已置换调压器个数") &&
                        (("是".equals(record.ranju_zhenggai) &&
                                verify(mCheckRjTime, record.ranju_zhenggairiqi, "请选择整改日期")
                        ) || ("否".equals(record.ranju_zhenggai)))))
                ;
    }

    private boolean verify(View view, String value, String tip) {
        if (TextUtils.isEmpty(value)) {
            view.requestFocus();
            Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean verify(View view, String tip) {
        int size = imageAdapter.getData().size();
        if (size < 3 || size > 6) {
            view.requestFocus();
            Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void collectParams() {
        record.jianchariqi = getText(mInfoStation);
        record.jianchadanwei = getText(mInfoUnit);
        record.tiaoyaqi_geshu = getText(mCheckTyqCount);
        record.tiaoyaqi_zhenggairiqi = getText(mCheckTyqTime);
        record.lianjieguan_geshu = getText(mCheckLjgCount);
        record.lianjieguan_zhenggairiqi = getText(mCheckLjgTime);
        record.ranju_geshu = getText(mCheckRjCount);
        record.ranju_zhenggairiqi = getText(mCheckRjTime);
        record.tuihuriqi = getText(mCheckYhqTime);
    }

    private View createFooterView() {
        int width = Utils.dip2px(getContext(), 120);
        ImageView view = new ImageView(getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(width, width));
        int padding = Utils.dip2px(getContext(), 5);
        view.setPadding(padding, padding, padding, padding);
        view.setImageResource(R.drawable.icon_add_pic);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickImg();
            }
        });
        return view;
    }

    private void initFileView() {

        mFileInfo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new FileListAdapter(R.layout.item_image, isCanChange);
        mFileInfo.setAdapter(imageAdapter);
        if (isCanChange) {
            imageAdapter.setFooterView(createFooterView());
        }
        imageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MediaPlayActivity.ToActivity(getContext(), ((MediaFile) adapter.getItem(position)).getPath());
            }
        });
        imageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.item_grid_bt) {
                    imageAdapter.remove(position);
                    refreshFileCount();
                }
            }
        });
        if (!TextUtils.isEmpty(record.phoneftp)) {
            String[] paths = record.phoneftp.split(",");
            for (String path : paths) {
                MediaFile mediaFile = new MediaFile();
                mediaFile.setPath(Constants.IMAGE_URL + path);
                mediaFile.setLocal(false);
                imageAdapter.addData(mediaFile);
            }
        }
        refreshFileCount();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
            handleMedia(list);
        }
    }

    private void handleMedia(List<LocalMedia> list) {
        MediaFile mediaFile;
        for (LocalMedia media : list) {
            mediaFile = new MediaFile();
            mediaFile.setType(media.getMimeType());
            mediaFile.setPath(media.getCompressPath());
            imageAdapter.addData(mediaFile);
        }
        refreshFileCount();
    }

    private void refreshFileCount() {
        mFileCount.setText(String.valueOf(imageAdapter.getData().size()));
    }

    public void showPickImg() {
        PictureSelector.create(this)
                .openGallery(PictureConfig.TYPE_IMAGE)
                .setOutputCameraPath("/RQApp/GasImage/")
                .compress(true)
          /*      .compressSavePath(Environment.getExternalStorageDirectory()
                        + "/RQApp/GasImage/compress")*/
                .minimumCompressSize(300)
                .imageFormat(PictureMimeType.PNG)
                //.selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
