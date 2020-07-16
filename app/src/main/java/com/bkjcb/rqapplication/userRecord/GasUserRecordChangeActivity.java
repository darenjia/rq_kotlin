package com.bkjcb.rqapplication.userRecord;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.bkjcb.rqapplication.base.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.userRecord.fragment.GasRecordDetailFragment;
import com.bkjcb.rqapplication.base.interfaces.OnPageButtonClickListener;
import com.bkjcb.rqapplication.userRecord.model.GasRecordModel;
import com.bkjcb.rqapplication.base.model.HttpResult;
import com.bkjcb.rqapplication.base.model.MediaFile;
import com.bkjcb.rqapplication.userRecord.model.UserInfoResult;
import com.bkjcb.rqapplication.userRecord.retrofit.GasService;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.base.util.Utils;

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
        if (model==null){
            finish();
        }
        model.setType(2);
        //model.phoneftp = "";
        model.jiandangriqi = Utils.getCurrentTime();
        if (!TextUtils.isEmpty(model.uid)) {
            model.yihuyidangid = model.uid;
            remoteAddress = "yihuyidang/xinzeng/" + model.yihuyidangid + "/";
        }
        model.userId = MyApplication.getUser().getUserId();
        UserInfoResult.UserInfo userInfo = null;
        if (!TextUtils.isEmpty(model.rquserid)) {
            userInfo = new UserInfoResult.UserInfo();
            userInfo.setUserGuid(model.rquserid);
            userInfo.setUserName(model.rqyonghuming);
            userInfo.setUserAddress(model.rqdizhi);
        }
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
        return model.id > 0 ?
                NetworkApi.getService(GasService.class).saveUserInfo(getFiledMap(model))
                : NetworkApi.getService(GasService.class).updateUserInfo(getFiledMap(model));
    }

    @Override
    protected void submitSuccess() {
        if (model.id > 0) {
            GasRecordModel.getBox().remove(model);
        }else {
            setResult(Activity.RESULT_OK);
        }
        finish();
    }

    public static void toActivity(Activity context, GasRecordModel recordModel) {
        Intent intent = new Intent(context, GasUserRecordChangeActivity.class);
        intent.putExtra("data", recordModel);
        context.startActivityForResult(intent, CHANGE_RECORD);
    }

}
