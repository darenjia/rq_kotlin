package com.bkjcb.rqapplication.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bkjcb.rqapplication.Constants;
import com.bkjcb.rqapplication.MediaPlayActivity;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.adapter.FileListAdapter;
import com.bkjcb.rqapplication.model.DefectDetail;
import com.bkjcb.rqapplication.model.DefectTreatmentModel;
import com.bkjcb.rqapplication.model.MediaFile;
import com.bkjcb.rqapplication.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by DengShuai on 2020/6/12.
 * Description :
 */
public class TreatmentDefectFragment extends TreatmentBackFragment implements DatePickerDialog.OnDateSetListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.record_time)
    TextView mRecordTime;
    @BindView(R.id.record_des)
    MaterialSpinner mRecordDes;
    @BindView(R.id.check_result_radio_ok)
    RadioButton mCheckResultRadioOk;
    @BindView(R.id.check_result_radio_failure)
    RadioButton mCheckResultRadioFailure;
    @BindView(R.id.check_result_radioGroup)
    RadioGroup mCheckResultRadioGroup;
    @BindView(R.id.file_title)
    TextView mFileTitle;
    @BindView(R.id.file_count)
    TextView mFileCount;
    @BindView(R.id.file_info)
    RecyclerView mFileInfo;
    @BindView(R.id.record_des_detail)
    LinearLayout mDesDetail;
    private FileListAdapter imageAdapter;
    private DatePickerDialog pickerDialog;
    private String remotePath;

    public void setModel(DefectTreatmentModel model) {
        this.model = model;
    }


    public static TreatmentDefectFragment newInstance(DefectTreatmentModel model) {
        TreatmentDefectFragment fragment = new TreatmentDefectFragment();
        fragment.setModel(model);
        return fragment;
    }

    public static TreatmentDefectFragment newInstance(DefectTreatmentModel model, DefectDetail result) {
        TreatmentDefectFragment fragment = new TreatmentDefectFragment();
        fragment.setModel(model);
        fragment.setResult(result);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_treatment_defect;
    }

    @Override
    protected void initView() {
        super.initView();
        initDateView();
        initSpinner();
        initCheckBox();
        initFileView();
    }

    private void initDateView() {
        if (result == null) {
            mRecordTime.setText(Utils.getCurrentTime());
        } else {
            mRecordTime.setText(Utils.dateFormat(result.getDisposalTime()));
            mRecordTime.setEnabled(false);
            isCanChange = false;
        }
    }

    private void initSpinner() {
        List<String> list = Arrays.asList("未发现使用液化气", "用户继续使用液化气", "其他");
        mRecordDes.setAdapter(new MaterialSpinnerAdapter<>(context, list));
        mRecordDes.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position == 2) {
                    mDesDetail.setVisibility(View.VISIBLE);
                } else {
                    mDesDetail.setVisibility(View.GONE);
                }
            }
        });
        if (result != null) {
            int index = 0;
            if ((index = list.indexOf(result.getFeedbackRemark())) == -1) {
                index = 2;
                mDesDetail.setVisibility(View.VISIBLE);
                mRecordRemark.setText(result.getFeedbackRemark());
                mRecordRemark.setEnabled(false);
            }
            mRecordDes.setSelectedIndex(index);
            mRecordDes.setEnabled(false);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        remotePath = "yinhuanchuzhi/" + Utils.getUUID();
    }

    private void initCheckBox() {
        if (result != null && !TextUtils.isEmpty(result.getDisposalResult())) {
            if ("无异常".equals(result.getDisposalResult())) {
                mCheckResultRadioGroup.check(R.id.check_result_radio_ok);
            } else {
                mCheckResultRadioGroup.check(R.id.check_result_radio_failure);
            }
            mCheckResultRadioOk.setEnabled(false);
            mCheckResultRadioFailure.setEnabled(false);
        }
        mCheckResultRadioGroup.setOnCheckedChangeListener(this);
    }


    private View createFooterView() {
        int width = Utils.dip2px(context, 120);
        ImageView view = new ImageView(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(width, width));
        int padding = Utils.dip2px(context, 5);
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
            imageAdapter.setFooterView(createFooterView(), 0, LinearLayout.HORIZONTAL);
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
        if (result != null) {
            if (!TextUtils.isEmpty(result.getJzImg())) {
                String[] paths = result.getJzImg().split(",");
                for (String path : paths) {
                    MediaFile mediaFile = new MediaFile();
                    mediaFile.setPath(Constants.IMAGE_URL + path);
                    mediaFile.setLocal(false);
                    imageAdapter.addData(mediaFile);
                }
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

    @OnClick(R.id.record_time)
    public void onClick(View v) {
        if (v.getId() == R.id.record_time) {
            createDatePicker();
        }
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
            pickerDialog.setMaxDate(calendar);
        }
        if (!pickerDialog.isAdded()) {
            pickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mRecordTime.setText(String.format(Locale.CHINESE, "%s-%s-%s", year, monthOfYear + 1, dayOfMonth));
    }

    @Override
    protected void collectParams() {
        //super.collectParams();
        result.setDisposalTime(getText(mRecordTime));
        result.setFeedbackRemark(mRecordDes.getSelected().toString());
        result.setOther(getText(mRecordRemark));
        result.setType("1");
        result.setRemotePath(remotePath);
        if (imageAdapter.getData().size() > 0) {
            List<String> paths = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            for (MediaFile file : imageAdapter.getData()) {
                paths.add(file.getPath());
                builder.append(remotePath).append("/").append(Utils.getFileName(file.getPath())).append(",");
            }
            result.setJzImg(builder.substring(0, builder.length() - 1));
            result.setFilePaths(paths);
        }
    }

    @Override
    protected boolean verifyData() {
        return verify(null, result.getJzImg(), "请至少添加一张照片!")
                && verify(mCheckResultRadioGroup, result.getDisposalResult(), "请选择处置结果")
                && (mRecordDes.getSelectedIndex() != 2 || (mRecordDes.getSelectedIndex() == 2 && verify(mRecordRemark, result.getOther(), "请填写反馈描述！")));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        result.setDisposalResult(checkedId == R.id.check_result_radio_failure ? "有异常" : "无异常");
    }
}
