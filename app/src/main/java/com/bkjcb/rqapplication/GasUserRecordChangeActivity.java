package com.bkjcb.rqapplication;

import android.app.Activity;
import android.content.Intent;

import com.bkjcb.rqapplication.fragment.GasRecordDetailFragment;
import com.bkjcb.rqapplication.interfaces.OnPageButtonClickListener;
import com.bkjcb.rqapplication.model.GasRecordModel;
import com.bkjcb.rqapplication.model.HttpResult;
import com.bkjcb.rqapplication.model.MediaFile;
import com.bkjcb.rqapplication.model.UserInfoResult;
import com.bkjcb.rqapplication.retrofit.GasService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.util.Utils;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by DengShuai on 2020/5/13.
 * Description :
 */
public class GasUserRecordChangeActivity extends AddNewGasUserActivity {
    public static final int CHANGE_RECORD = 110;


    protected int setLayoutID() {
        return R.layout.activity_gas_user_add;
    }

    @Override
    protected void initView() {
        initTopbar("修改一户一档");
    }

    @Override
    protected void initData() {
        model = (GasRecordModel) getIntent().getSerializableExtra("data");
        model.setType(2);
        model.phoneftp = "";
        model.jiandangriqi = Utils.getCurrentTime();
        model.yihuyidangid = model.id;
        model.userId = MyApplication.user.getUserId();
        remoteAddress = "yihuyidang/xinzeng/" + model.yihuyidangid + "/";
        UserInfoResult.UserInfo userInfo = new UserInfoResult.UserInfo();
        userInfo.setUserGuid(model.rquserid);
        userInfo.setUserName(model.rqyonghuming);
        userInfo.setUserAddress(model.rqdizhi);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout, GasRecordDetailFragment.newInstance(new OnPageButtonClickListener<String>() {
                    @Override
                    public void onClick(String userInfo) {

                    }

                    @Override
                    public void onNext(List<MediaFile> list) {
                        submitData(list);
                    }
                }, userInfo, model))
                .commit();
    }

    @Override
    protected Observable<HttpResult> createSubmitObservable() {
        return NetworkApi.getService(GasService.class)
                .updateUserInfo(getFiledMap(model));
    }

    @Override
    protected void submitSuccess() {
        setResult(RESULT_OK);
        super.submitSuccess();
    }

    public static void toActivity(Activity context, GasRecordModel recordModel) {
        Intent intent = new Intent(context, GasUserRecordChangeActivity.class);
        intent.putExtra("data", recordModel);
        context.startActivityForResult(intent, CHANGE_RECORD);
    }

}
