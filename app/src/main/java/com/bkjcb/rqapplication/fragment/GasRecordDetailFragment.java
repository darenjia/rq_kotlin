package com.bkjcb.rqapplication.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.bkjcb.rqapplication.Constants;
import com.bkjcb.rqapplication.MediaPlayActivity;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.SearchGasUserActivity;
import com.bkjcb.rqapplication.adapter.FileListAdapter;
import com.bkjcb.rqapplication.adapter.GasCompanyAdapter;
import com.bkjcb.rqapplication.interfaces.OnPageButtonClickListener;
import com.bkjcb.rqapplication.model.GasCompanyResult;
import com.bkjcb.rqapplication.model.GasRecordModel;
import com.bkjcb.rqapplication.model.MediaFile;
import com.bkjcb.rqapplication.model.UserInfoResult;
import com.bkjcb.rqapplication.retrofit.GasService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public class GasRecordDetailFragment extends BaseSimpleFragment implements DatePickerDialog.OnDateSetListener, CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.street_name)
    TextView mStreetName;
    @BindView(R.id.record_create_time)
    TextView mRecordCreateTime;
    @BindView(R.id.record_user_name)
    EditText mRecordUserName;
    @BindView(R.id.record_user_address)
    EditText mRecordUserAddress;
    @BindView(R.id.record_user)
    EditText mRecordUser;
    @BindView(R.id.record_user_tel)
    EditText mRecordUserTel;
    @BindView(R.id.record_legal)
    EditText mRecordLegal;
    @BindView(R.id.record_system)
    MaterialSpinner mRecordSystem;
    @BindView(R.id.record_license)
    MaterialSpinner mRecordLicense;
    @BindView(R.id.record_company)
    MaterialSpinner mRecordCompany;
    @BindView(R.id.record_contract)
    MaterialSpinner mRecordContract;
    @BindView(R.id.record_signed_time)
    EditText mRecordSignedTime;
    @BindView(R.id.record_tyf_1)
    CheckBox mRecord_tyf_1;
    @BindView(R.id.record_tyf_2)
    CheckBox mRecord_tyf_2;
    @BindView(R.id.record_tyf_number)
    EditText mRecordTyfNumber;
    @BindView(R.id.record_tyf_layout)
    LinearLayout mRecordTyfLayout;
    @BindView(R.id.record_ljg_1)
    CheckBox mRecordLjg1;
    @BindView(R.id.record_ljg_2)
    CheckBox mRecordLjg2;
    @BindView(R.id.record_ljg_3)
    CheckBox mRecordLjg3;
    @BindView(R.id.record_rj_1_number)
    EditText mRecordRj1Number;
    @BindView(R.id.record_rj_2_number)
    EditText mRecordRj2Number;
    @BindView(R.id.record_xh_1)
    CheckBox mRecordXh_1;
    @BindView(R.id.record_xh_2)
    CheckBox mRecordXh_2;
    @BindView(R.id.record_xh_number)
    EditText mRecordXhNumber;
    @BindView(R.id.record_xh_layout)
    LinearLayout mRecordXhLayout;
    @BindView(R.id.record_bjq)
    MaterialSpinner mRecordBjq;
    @BindView(R.id.record_last_check_time)
    EditText mRecordLastCheckTime;
    @BindView(R.id.record_check)
    MaterialSpinner mRecordCheck;
    @BindView(R.id.link_layout)
    LinearLayout mLinkLayout;
    @BindView(R.id.link_name)
    TextView mLinkName;
    @BindView(R.id.unlink)
    ImageView mUnlinkBtn;
    @BindView(R.id.link_address)
    TextView mLinkAddress;
    @BindView(R.id.record_location_address)
    EditText mLocationAddress;
    @BindView(R.id.record_last_check_layout)
    LinearLayout mRecordLastCheckLayout;
    @BindView(R.id.record_submit)
    Button mSubmit;
    @BindView(R.id.record_location_layout)
    LinearLayout mRecordLocationLayout;
    @BindView(R.id.file_info)
    RecyclerView mFileInfo;
    @BindView(R.id.file_count)
    TextView mFileCount;
    @BindView(R.id.record_remark)
    EditText mRecordRemark;
    @BindView(R.id.record_link_btn)
    TextView mRecordLinkBtn;
    @BindView(R.id.record_link_info)
    LinearLayout mRecordLinkInfo;
    @BindView(R.id.record_signed_layout)
    LinearLayout mRecordSignedInfo;
    private List<String> type1List = Arrays.asList("有", "无");
    private DatePickerDialog pickerDialog;
    private EditText currentTextView;
    private UserInfoResult.UserInfo userInfo;
    private MapLocationFragment locationFragment;
    private LatLng currentLatLng;
    private String address;
    private GasRecordModel recordModel;
    private OnPageButtonClickListener listener;
    private FileListAdapter imageAdapter;
    private boolean isCanChange = true;

    public void setListener(OnPageButtonClickListener listener) {
        this.listener = listener;
    }

    public void setUserInfo(UserInfoResult.UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setRecordModel(GasRecordModel recordModel) {
        this.recordModel = recordModel;
    }

    public static GasRecordDetailFragment newInstance(UserInfoResult.UserInfo userInfo, GasRecordModel model) {
        GasRecordDetailFragment fragment = new GasRecordDetailFragment();
        fragment.setUserInfo(userInfo);
        fragment.setRecordModel(model);
        return fragment;
    }

    public static GasRecordDetailFragment newInstance(OnPageButtonClickListener listener, UserInfoResult.UserInfo userInfo, GasRecordModel model) {
        GasRecordDetailFragment fragment = new GasRecordDetailFragment();
        fragment.setListener(listener);
        fragment.setUserInfo(userInfo);
        fragment.setRecordModel(model);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_gas_user_detail;
    }

    @Override
    protected void initView() {
        setText(mStreetName, recordModel.jiedao);
        isCanChange = recordModel.getType() != 0;
        if (recordModel.getType() == 1) {
            setText(mRecordCreateTime, "建档日期：" + recordModel.jiandangriqi);
            initSpinnerView();
        } else {
            setText(mRecordUserName, recordModel.yonghuming);
            setText(mRecordUserAddress, recordModel.dizhi);
            setText(mRecordUser, recordModel.fuzeren);
            setText(mRecordUserTel, recordModel.dianhua);
            setText(mRecordLegal, recordModel.faren);
            if (recordModel.getType() == 0) {
                setText(mRecordCreateTime, "建档日期：" + Utils.dateFormat(recordModel.jiandangriqi));
                setText(mRecordSystem, recordModel.ranqiguanlizhidu);
                setText(mRecordLicense, recordModel.yingyezhizhao);
                setText(mRecordCompany, recordModel.gongqiqiye);
                setText(mRecordContract, recordModel.yongqihetong);
                setText(mRecordBjq, recordModel.ranqixieloubaojinqi);
                setText(mRecordCheck, recordModel.qiyeanjianjilu);
                setViewVisibility(mSubmit, false);
            } else {
                setText(mRecordCreateTime, "建档日期：" + recordModel.jiandangriqi);
                initSpinnerView();
            }

            if ("已签".equals(recordModel.yongqihetong)) {
                setText(mRecordSignedTime, Utils.dateFormat(recordModel.qiandingriqi));
                setViewVisibility(mRecordSignedInfo, true);
            } else {
                setViewVisibility(mRecordSignedInfo, false);
            }
            setChecked(mRecord_tyf_1, "可调节".equals(recordModel.tiaoyafa));
            if (!TextUtils.isEmpty(recordModel.tiaoyafa_geshu)) {
                setChecked(mRecord_tyf_2, true);
                setViewVisibility(mRecordTyfLayout, true);
                setText(mRecordTyfNumber, recordModel.tiaoyafa_geshu);
            }
            if (!TextUtils.isEmpty(recordModel.lianjieguan)) {
                setChecked(mRecordLjg1, recordModel.lianjieguan.contains("硬管连接"));
                setChecked(mRecordLjg2, recordModel.lianjieguan.contains("橡胶管"));
                setChecked(mRecordLjg3, recordModel.lianjieguan.contains("其他管"));
            }
            setText(mRecordRj1Number, recordModel.zaojuleixing_dafeng);
            setText(mRecordRj2Number, recordModel.zaojuleixing_gufeng);
            setChecked(mRecordXh_1, "否".equals(recordModel.xihuobaohu));
            if (!TextUtils.isEmpty(recordModel.tiaoyafa_geshu)) {
                setChecked(mRecordXh_2, true);
                setViewVisibility(mRecordXhLayout, true);
                setText(mRecordXhNumber, recordModel.xihuobaohu_geshu);
            }
            if ("有".equals(recordModel.qiyeanjianjilu)) {
                setViewVisibility(mRecordLastCheckLayout, true);
                setText(mRecordLastCheckTime, Utils.dateFormat(recordModel.anjianriqi));
            } else {
                setViewVisibility(mRecordLastCheckLayout, false);
            }
            setText(mRecordRemark, recordModel.beizhu);
        }
        addMapView();
    }

    private void initSpinnerView() {
        mRecordCheck.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                setViewVisibility(mRecordLastCheckLayout, position == 0);
            }
        });
        mRecordContract.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                setViewVisibility(mRecordSignedInfo, position == 0);
            }
        });
        initSpinner(mRecordLicense, type1List, recordModel.yingyezhizhao);
        initSpinner(mRecordSystem, type1List, recordModel.ranqiguanlizhidu);
        initSpinner(mRecordContract, Arrays.asList("已签", "未签"), recordModel.yongqihetong);
        initSpinner(mRecordBjq, Arrays.asList("已安装", "未安装"), recordModel.ranqixieloubaojinqi);
        initSpinner(mRecordCheck, type1List, recordModel.qiyeanjianjilu);
        getGasCompany();
    }

    private void addMapView() {
        if (recordModel.getType() == 1) {
            locationFragment = MapLocationFragment.newInstance(new MapLocationFragment.AddressQueryListener() {
                @Override
                public void onSuccess(String s, LatLng latLng) {
                    address = s;
                    mLocationAddress.setText(s);
                    currentLatLng = latLng;
                }

                @Override
                public void onClick() {
                    if (!address.contains("正在") && !address.contains("未")) {
                        showUserAddressTip();
                    }
                }
            });
        } else {
            if (!TextUtils.isEmpty(recordModel.location) && recordModel.location.contains(",")) {
                String[] strings = recordModel.location.split(",");
                currentLatLng = new LatLng(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
            }
            locationFragment = MapLocationFragment.newInstance(currentLatLng);
            setViewVisibility(mRecordLocationLayout, false);
        }
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.record_map, locationFragment)
                .commit();
    }

    @Override
    protected void initData() {
        if (recordModel.getType() == 0) {
            if (!TextUtils.isEmpty(recordModel.rquserid)) {
                setViewVisibility(mLinkLayout, true);
                mLinkName.setText(recordModel.rqyonghuming);
                mLinkAddress.setText(recordModel.rqdizhi);
                setViewVisibility(mUnlinkBtn, false);
            }
        } else {
            StyledDialog.init(getContext());
            setListener();
            initUserInfo();
        }
        initFileView();
    }

    private void initUserInfo() {
        if (userInfo == null) {
            setViewVisibility(mLinkLayout, false);
            setViewVisibility(mRecordLinkBtn, true);
        } else {
            setViewVisibility(mRecordLinkBtn, false);
            setViewVisibility(mLinkLayout, true);
            mLinkName.setText(userInfo.getUserName());
            mLinkAddress.setText(userInfo.getUserAddress());
            mRecordUserName.setText(userInfo.getUserName());
            mRecordUserAddress.setText(userInfo.getUserAddress());
            if (recordModel.getType() == 2) {
                mRecordUserAddress.setEnabled(false);
            }
        }
    }

    private void setListener() {
        mRecord_tyf_2.setOnCheckedChangeListener(this);
        mRecordXh_2.setOnCheckedChangeListener(this);
    }

    private void initSpinner(MaterialSpinner spinner, List<String> strings, String value) {
        spinner.setAdapter(new MaterialSpinnerAdapter<>(getContext(), strings));
        if (!TextUtils.isEmpty(value)) {
            spinner.setSelectedIndex(strings.indexOf(value));
        }
    }

    @OnClick({R.id.record_signed_time, R.id.record_last_check_time, R.id.unlink, R.id.record_submit, R.id.record_link_btn, R.id.record_link_info})
    public void onClick(View v) {
        if (recordModel.getType() == 0) {
            return;
        }
        switch (v.getId()) {
            default:
                break;
            case R.id.record_signed_time:
                currentTextView = mRecordSignedTime;
                createDatePicker();
                break;
            case R.id.record_last_check_time:
                currentTextView = mRecordLastCheckTime;
                createDatePicker();
                break;
            case R.id.unlink:
                showUnlinkTip();
                break;
            case R.id.record_submit:
                collectParams();
                if (verifyAllData()) {
                    listener.onNext(imageAdapter.getData());
                }
                break;
            case R.id.record_link_info:
            case R.id.record_link_btn:
                SearchGasUserActivity.toActivity(this);
                break;
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
        }
        if (!pickerDialog.isAdded()) {
            pickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        currentTextView.setText(String.format(Locale.CHINESE, "%s-%s-%s", year, monthOfYear + 1, dayOfMonth));
    }

    private void setViewVisibility(View v, boolean isShow) {
        v.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void showUnlinkTip() {
        StyledDialog.buildIosAlert("提示", "是否解除当前绑定？", new MyDialogListener() {
            @Override
            public void onFirst() {
                userInfo = null;
                initUserInfo();
            }

            @Override
            public void onSecond() {

            }
        }).setBtnText("是", "否").show();
    }

    private void showUserAddressTip() {
        StyledDialog.buildIosAlert("提示", "是否使用当前定位地址？", new MyDialogListener() {
            @Override
            public void onFirst() {
                mRecordUserAddress.setText(address);
            }

            @Override
            public void onSecond() {

            }
        }).setBtnText("是", "否").show();
    }

    private void setText(TextView view, String value) {
        view.setText(value);
        view.setEnabled(isCanChange);
    }

    private void setChecked(CheckBox box, boolean enable) {
        box.setChecked(enable);
        box.setClickable(isCanChange);
    }

    private String getText(TextView view) {
        return view.getText().toString();
    }

    private boolean verifyAllData() {
        return verify(mRecordUserName, recordModel.yonghuming, "请填写用户名称") &&
                verify(mRecordUserAddress, recordModel.dizhi, "请填写地址") &&
                verify(mRecordUser, recordModel.fuzeren, "请填写负责人") &&
                verify(mRecordUserTel, recordModel.dianhua, "请填写电话") &&
                //verify(mRecordLegal, recordModel.faren, "请填写法人") &&
                verify(mRecordCompany, recordModel.gongqiqiye, "请选择供气企业") &&
                (("已签".equals(recordModel.yongqihetong) &&
                        verify(mRecordSignedTime, recordModel.qiandingriqi, "请选择签订日期")
                ) || "未签".equals(recordModel.yongqihetong)) &&
                ((mRecord_tyf_2.isChecked() &&
                        verify(mRecordTyfNumber, recordModel.tiaoyafa_geshu, "请填写可调节调压阀个数(数量需大于0)", true)
                ) || !mRecord_tyf_2.isChecked()) &&
                verify(mRecordLjg1, recordModel.lianjieguan, "请选择连接管") &&
                verify(mRecordRj1Number, recordModel.zaojuleixing_dafeng, "请填写大气式燃具个数") &&
                verify(mRecordRj2Number, recordModel.zaojuleixing_gufeng, "请填写鼓风式燃具个数") &&
                ((mRecordXh_2.isChecked() &&
                        verify(mRecordXhNumber, recordModel.xihuobaohu_geshu, "请填写没有熄火保护装置的个数(数量需大于0))", true)
                ) || !mRecordXh_2.isChecked()) &&
                (("有".equals(recordModel.qiyeanjianjilu) &&
                        verify(mRecordLastCheckTime, recordModel.anjianriqi, "请选择最后安检日期")
                ) || "无".equals(recordModel.qiyeanjianjilu)) && verify(mFileInfo, "请添加3到5张照片");
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

    private boolean verify(View view, String value, String tip) {
        if (TextUtils.isEmpty(value)) {
            view.requestFocus();
            Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean verify(View view, String value, String tip, boolean verifyValue) {
        if (TextUtils.isEmpty(value)) {
            view.requestFocus();
            Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (verifyValue) {
            if (Integer.parseInt(value) == 0) {
                view.requestFocus();
                Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void collectParams() {
        recordModel.yonghuming = getText(mRecordUserName);
        recordModel.dizhi = getText(mRecordUserAddress);
        recordModel.fuzeren = getText(mRecordUser);
        recordModel.dianhua = getText(mRecordUserTel);
        recordModel.faren = getText(mRecordLegal);
        recordModel.ranqiguanlizhidu = getText(mRecordSystem);
        recordModel.yingyezhizhao = getText(mRecordLicense);
        recordModel.gongqiqiye = getText(mRecordCompany);
        recordModel.gongqiqiyeid = ((GasCompanyResult.GasCompany) mRecordCompany.getSelected()).getCid();
        recordModel.yongqihetong = getText(mRecordContract);
        recordModel.qiandingriqi = getText(mRecordSignedTime);
        recordModel.tiaoyafa_geshu = getText(mRecordTyfNumber);
        recordModel.lianjieguan = getText(mRecordUserAddress);
        recordModel.zaojuleixing_dafeng = getText(mRecordRj1Number);
        recordModel.zaojuleixing_gufeng = getText(mRecordRj2Number);
        recordModel.xihuobaohu_geshu = getText(mRecordXhNumber);
        recordModel.ranqixieloubaojinqi = getText(mRecordBjq);
        recordModel.anjianriqi = getText(mRecordLastCheckTime);
        recordModel.qiyeanjianjilu = getText(mRecordCheck);
        recordModel.beizhu = getText(mRecordRemark);

        if (mRecord_tyf_1.isChecked()) {
            recordModel.tiaoyafa = "不可调节";
        }
        if (mRecordXh_2.isChecked()) {
            recordModel.tiaoyafa = "是";
        }
        StringBuilder builder = new StringBuilder();
        if (mRecordLjg1.isChecked()) {
            builder.append("硬管连接");
        }
        if (mRecordLjg2.isChecked()) {
            builder.append(builder.length() > 0 ? "," : "").append("橡胶管");
        }
        if (mRecordLjg3.isChecked()) {
            builder.append(builder.length() > 0 ? "," : "").append("其他管");
        }
        recordModel.lianjieguan = builder.toString();
        if (currentLatLng != null) {
            recordModel.location = String.format(Locale.CHINESE, "%s,%s", currentLatLng.latitude, currentLatLng.longitude);
        }
        if (userInfo != null) {
            recordModel.rquserid = userInfo.getUserGuid();
            recordModel.rqyonghuming = userInfo.getUserName();
            recordModel.rqdizhi = userInfo.getUserAddress();
            recordModel.mbuid = userInfo.getMbu_id();
        }
    }

    private void getGasCompany() {
        disposable = NetworkApi.getService(GasService.class)
                .getComboList("B2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GasCompanyResult>() {
                    @Override
                    public void accept(GasCompanyResult result) throws Exception {
                        if (result.pushState == 200) {
                            GasCompanyAdapter adapter = new GasCompanyAdapter(context, result.getDatas());
                            mRecordCompany.setAdapter(adapter);
                            if (!TextUtils.isEmpty(recordModel.gongqiqiyeid)) {
                                for (int i = 0; i < result.getDatas().size(); i++) {
                                    if (recordModel.gongqiqiyeid.equals(result.getDatas().get(i).getCid())) {
                                        mRecordCompany.setSelectedIndex(i);
                                        break;
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(context, "获取供气企业失败，请稍后重试", Toast.LENGTH_SHORT).show();
                            mRecordCompany.setText("无法获取供气企业");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(context, "获取供气企业失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setViewVisibility(buttonView.getId() == R.id.record_tyf_2 ? mRecordTyfLayout : mRecordXhLayout, isChecked);
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
                if (view.getId() == R.id.iv_delete) {
                    imageAdapter.remove(position);
                    refreshFileCount();
                }
            }
        });
        if (!TextUtils.isEmpty(recordModel.phoneftp)) {
            String[] paths = recordModel.phoneftp.split(",");
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
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
                handleMedia(list);
            } else {
                userInfo = (UserInfoResult.UserInfo) data.getParcelableExtra("data");
                initUserInfo();
            }
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
                //.compressSavePath("/RQApp/GasImage/")
                .minimumCompressSize(300)
                .imageFormat(PictureMimeType.PNG)
                //.selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
}
