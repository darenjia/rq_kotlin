package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bkjcb.rqapplication.adapter.ViewPagerAdapter;
import com.bkjcb.rqapplication.fragment.BottleCheckFragment;
import com.bkjcb.rqapplication.fragment.BottleDataFragment;
import com.bkjcb.rqapplication.fragment.BottleOrderFragment;
import com.bkjcb.rqapplication.model.BottleResult;
import com.bkjcb.rqapplication.model.HttpResult;
import com.bkjcb.rqapplication.model.UserInfoResult;
import com.bkjcb.rqapplication.retrofit.DataService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.view.QestionCheckView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2019/10/22.
 * Description :
 */
public class UserDetailActivity extends SimpleBaseActivity {
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.userAddress)
    EditText mUserAddress;
    @BindView(R.id.btn_right)
    ImageView mRight;
    @BindView(R.id.btn_warning)
    ImageView mWarning;
    @BindView(R.id.userType)
    MaterialSpinner mUserType;
    @BindView(R.id.tabSegment)
    QMUITabSegment mTabSegment;
    @BindView(R.id.dataPager)
    ViewPager mDataPager;
    @BindView(R.id.submit_btn)
    Button mSubmitBtn;
    @BindView(R.id.bind_btn)
    LinearLayout mBindView;
    private UserInfoResult.UserInfo userInfo;
    //private List<String> UserTypes = Arrays.asList("居民", "非居民");
    private boolean editable = false;
    private List<Fragment> fragments;
    private QestionCheckView checkView;
    private QMUITipDialog tipDialog;
    private QMUIBottomSheet bottomSheet;

    public static void ToUserDetailActivity(Context context, UserInfoResult.UserInfo userInfo) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("data", userInfo);
        context.startActivity(intent);

    }

    @Override
    protected int setLayoutID() {
        return R.layout.activity_user;
    }

    @OnClick({R.id.bind_btn, R.id.submit_btn, R.id.btn_warning, R.id.btn_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                sendChangeInfo();
                //popCheckWindow();
                break;
            case R.id.bind_btn:
                ScanActivity.ToScanActivity(this, 2);
                break;
            case R.id.btn_warning:
                popCheckWindow(true);
                break;
            case R.id.btn_right:
                popCheckWindow(false);
                break;
            default:

        }
    }

    private void sendChangeInfo() {
        String name = mUsername.getText().toString();
        String address = mUserAddress.getText().toString();
        if (name.trim().length() == 0 || address.trim().length() == 0) {
            showSnackbar(mBindView, "用户名和地址不能为空");
            return;
        }
        if (name.trim().equals(userInfo.getUserName()) && address.trim().equals(userInfo.getUserAddress())) {
            showSnackbar(mBindView, "未修改任何信息！");
            return;
        }
        showLoading();
        disposable = NetworkApi.getService(DataService.class)
                .changeUserInfo(userInfo.getUserGuid(), name, address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult httpResult) throws Exception {
                        if (httpResult.pushState == 200) {
                            showSnackbar(mBindView, "提交成功");
                        } else {
                            showSnackbar(mBindView, httpResult.pushMsg);
                        }
                        tipDialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showSnackbar(mBindView, throwable.getMessage());
                        tipDialog.dismiss();
                    }
                });
    }

    @Override
    protected void initView() {
        super.initView();
        QMUITopBarLayout barLayout = initTopbar("用户详情");
        Button topBar = barLayout.addRightTextButton("编辑", R.id.top_right_button1);
        topBar.setTextColor(getResources().getColor(R.color.colorText));
        topBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editable) {
                    topBar.setText("编辑");
                    setInfoCanEdit(false);
                    editable = false;
                } else {
                    topBar.setText("取消");
                    setInfoCanEdit(true);
                    editable = true;
                }
            }
        });
        userInfo = (UserInfoResult.UserInfo) getIntent().getParcelableExtra("data");
        if (userInfo != null) {
            setInitData();
        }
        setInfoCanEdit(false);
        initTab();
        addTab("订单信息");
        addTab("安检信息");
        addTab("巡检信息");
        initViewPager();
    }

    private void initViewPager() {
        fragments = new ArrayList<>();
        fragments.add(new BottleOrderFragment());
        fragments.add(new BottleCheckFragment());
        fragments.add(new BottleDataFragment());
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        mDataPager.setAdapter(pagerAdapter);
    }

    private BottleDataFragment createFragment() {
        BottleDataFragment fragment = new BottleDataFragment();
        return fragment;
    }

    private void initTab() {
        int space = QMUIDisplayHelper.dp2px(this, 16);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setIndicatorWidthAdjustContent(false);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setupWithViewPager(mDataPager, false);
        mTabSegment.setPadding(0, 0, 0, 0);
    }

    private void addTab(String title) {
        QMUITabSegment.Tab tab = new QMUITabSegment.Tab(title);
        tab.setTextColor(getResources().getColor(R.color.colorSecondDrayText), getResources().getColor(R.color.colorText));
        mTabSegment.addTab(tab);
    }

    private void setInitData() {
        mUsername.setText(userInfo.getUserName());
        mUserAddress.setText(userInfo.getUserAddress());
        mUserType.setAdapter(new MaterialSpinnerAdapter<>(this, Arrays.asList(userInfo.getUserType())));
    }

    private void setInfoCanEdit(boolean isCanChange) {
        if (isCanChange) {
            mUsername.setEnabled(true);
            mUserAddress.setEnabled(true);
            //mUserType.setClickable(true);
            //mUserType.setAdapter(new MaterialSpinnerAdapter<>(this, UserTypes));
            mSubmitBtn.setVisibility(View.VISIBLE);
        } else {
            mUsername.setEnabled(false);
            mUserAddress.setEnabled(false);
            //mUserType.setClickable(false);
            mSubmitBtn.setVisibility(View.GONE);
            //mUserType.setAdapter(new MaterialSpinnerAdapter<>(this, UserTypes));
            setInitData();
        }

    }

    @Override
    protected void initData() {
        getBottleData();
    }
    private void popCheckWindow(boolean isChecked) {
        if (bottomSheet == null) {
            bottomSheet = new QMUIBottomSheet(this);
            checkView = new QestionCheckView(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheet.dismiss();
                    submitCheckResult(checkView.getRemark(), checkView.getStatus() ? "异常" : "正常");
                }
            }, isChecked);
            bottomSheet.setContentView(checkView.build());
            bottomSheet.setCancelable(true);
        }
        checkView.setStatus(isChecked);
        bottomSheet.show();
    }

    private void getBottleData() {
        Log.w("ds", userInfo.getUserGuid());
        disposable = NetworkApi.getService(DataService.class)
                .getBottleData(userInfo.getUserGuid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BottleResult>() {
                    @Override
                    public void accept(BottleResult result) throws Exception {
                        if (result.pushState == 200) {
                            ((BottleOrderFragment) fragments.get(0)).setBottleTourCheck(result.getDatas().getBottleSaleChecks());
                            ((BottleCheckFragment) fragments.get(1)).setCheckList(result.getDatas().getBottleSaleChecks());
                            ((BottleDataFragment) fragments.get(2)).setBottleTourCheck(result.getDatas().getBottleTourChecks());
                        } else {
                            ((BottleOrderFragment) fragments.get(0)).setEmptyView();
                            ((BottleCheckFragment) fragments.get(1)).setEmptyView();
                            ((BottleDataFragment) fragments.get(2)).setEmptyView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showSnackbar(mBindView, throwable.getMessage());
                        Log.w("ds", throwable.getMessage());
                        ((BottleOrderFragment) fragments.get(0)).setEmptyView();
                        ((BottleCheckFragment) fragments.get(1)).setEmptyView();
                        ((BottleDataFragment) fragments.get(2)).setEmptyView();
                    }
                });
    }

    private void submitCheckResult(String remark, String status) {
        showLoading();
        disposable = NetworkApi.getService(DataService.class)
                .saveTourCheck(userInfo.getUserGuid(), MyApplication.user.getReal_name(), status, remark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult httpResult) throws Exception {
                        if (httpResult.pushState == 200) {
                            showSnackbar(mBindView, "提交成功");
                        } else {
                            showSnackbar(mBindView, httpResult.pushMsg);
                        }
                        tipDialog.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showSnackbar(mBindView, throwable.getMessage());
                        tipDialog.dismiss();
                    }
                });
    }

    private void showLoading() {
        if (tipDialog == null) {
            tipDialog = new QMUITipDialog.Builder(this)
                    .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                    .setTipWord("正在提交")
                    .create();
            tipDialog.setCanceledOnTouchOutside(false);
            tipDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancelDisposable();
                }
            });
        }
        tipDialog.show();
    }
}
