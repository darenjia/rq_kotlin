package com.bkjcb.rqapplication.gascylindermanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.gascylindermanagement.model.Company;
import com.bkjcb.rqapplication.gascylindermanagement.model.CompanyResult;
import com.bkjcb.rqapplication.gascylindermanagement.retrofit.QueryService;
import com.bkjcb.rqapplication.gascylindermanagement.util.NfcUtils;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2021/2/26.
 * Description :
 */
public class NFCActivity extends SimpleBaseActivity {
    @BindView(R.id.nfc_id)
    EditText mNfcIDView;
    @BindView(R.id.nfc_query)
    Button mNfcSearchButton;
    @BindView(R.id.nfc_tip)
    TextView mNfcTipView;
    @BindView(R.id.company_type)
    MaterialSpinner materialSpinner;
    List<Company> companyList;

    private static final String TAG = "NFCActivity";

    private NfcUtils nfcUtils;
    private MaterialSpinnerAdapter<Company> adapter;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_gas_cyliner;
    }

    @Override
    protected void initView() {
        super.initView();
        initTopbar("气瓶溯源");
        mNfcIDView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == 6) {
                searchCardID();
                return true;
            }
            return false;
        });
    }

    private String getCardID() {
        return mNfcIDView.getText().toString().trim();
    }

    @OnClick({R.id.nfc_query})
    public void onClick(View v) {
        if (v.getId() == R.id.nfc_query) {
            searchCardID();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开启前台调度系统
        nfcUtils.enableForegroundDispatch();
    }

    @Override
    protected void onStart() {
        super.onStart();
        nfcUtils.onStart();
        mNfcTipView.setOnClickListener(null);
        if (nfcUtils.isNfcEnable()) {
            mNfcTipView.setVisibility(View.GONE);
        } else if (nfcUtils.isNfcExits()) {
            mNfcTipView.setText("请打开手机NFC功能");
            mNfcTipView.setVisibility(View.VISIBLE);
            mNfcTipView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nfcUtils.intentToNfcSetting();
                }
            });
        } else {
            mNfcTipView.setText("当前手机不支持NFC功能，请输入编号查询");
            mNfcTipView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        //关闭前台调度系统
        nfcUtils.disableForegroundDispatch();
    }

    @Override
    protected void initData() {
        super.initData();
        nfcUtils = new NfcUtils(this);
        companyList = new ArrayList<>();
        adapter = new MaterialSpinnerAdapter<>(this, companyList);
        adapter.setHintEnabled(true);
        materialSpinner.setAdapter(adapter);
        getCompanyList();
    }

    private void getCompanyList() {
        NetworkApi.getService(QueryService.class)
                .queryCompanyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompanyResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull CompanyResult result) {
                        if (result.isSuccess()) {
                            companyList.addAll(result.getReturnValue());
                            adapter.notifyDataSetChanged();
                        }else {
                            showSnackbar(mNfcIDView, result.getMessage());
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showSnackbar(mNfcIDView, "获取供气单位失败，请稍后再试！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void ToActivity(Context context) {
        context.startActivity(new Intent(context, NFCActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String[] strs = nfcUtils.readNFC(intent);
        verifyData(strs);
    }

    private void verifyData(String[] strings) {
        if (strings == null || strings.length < 2 || TextUtils.isEmpty(strings[0]) || TextUtils.isEmpty(strings[1])) {
            showTipString();
        } else {
//            ToDetailActivity();
            parseID(strings[1], strings[0],1);
        }
    }

    private boolean verifyID(String s) {
        return !TextUtils.isEmpty(s) && s.length() > 5;
    }

    private void showTipString() {
        mNfcTipView.setText("读取数据失败,请贴近卡片再试一次!");
        mNfcTipView.setVisibility(View.VISIBLE);
    }

    private void searchCardID() {
        hideSoftInput();
        String id = getCardID();
       if(materialSpinner.getSelectedIndex()>0){
           if (verifyID(id)) {
               String cId = ((Company) (materialSpinner.getSelected())).getId();
               parseID(id, cId,2);
           } else {
               showSnackbar(mNfcIDView, "请输入正确的ID格式");
           }
       }else {
           showSnackbar(mNfcIDView, "请选择产权单位");
       }
    }

    private void parseID(String id, String content,int type) {
        GasCylinderDetailActivity.ToGasCylinderDetailActivity(this, id, content,type);
    }

    private void hideSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(mNfcIDView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
