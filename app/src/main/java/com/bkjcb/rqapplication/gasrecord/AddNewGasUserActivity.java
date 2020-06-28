package com.bkjcb.rqapplication.gasrecord;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.bkjcb.rqapplication.Constants;
import com.bkjcb.rqapplication.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.SimpleBaseActivity;
import com.bkjcb.rqapplication.gasrecord.fragment.GasRecordDetailFragment;
import com.bkjcb.rqapplication.gasrecord.fragment.GasUserSearchFragment;
import com.bkjcb.rqapplication.ftp.UploadTask;
import com.bkjcb.rqapplication.interfaces.OnPageButtonClickListener;
import com.bkjcb.rqapplication.gasrecord.model.GasRecordModel;
import com.bkjcb.rqapplication.model.HttpResult;
import com.bkjcb.rqapplication.model.MediaFile;
import com.bkjcb.rqapplication.treatmentdefect.model.UserInfoResult;
import com.bkjcb.rqapplication.gasrecord.retrofit.GasService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.util.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public class AddNewGasUserActivity extends SimpleBaseActivity {
    protected GasRecordModel model;
    protected String remoteAddress = "yihuyidang/xinzeng/" + Utils.getUUID() + "/";


    @Override
    protected int setLayoutID() {
        return R.layout.activity_gas_user_add;
    }

    @Override
    protected void initView() {
        initTopbar("添加一户一档");
    }

    @Override
    protected void initData() {
        super.initData();
            model = new GasRecordModel(1);
            model.userId = MyApplication.getUser().getUserId();
            model.suoshuqu = MyApplication.getUser().getAreacode().getArea_jc();
            model.jiedao = MyApplication.getUser().getAreacode().getStreet_jc();
            model.jiandangriqi = Utils.getCurrentTime();
            model.year = "2020";
            showUserListView();
    }

    protected void showUserListView() {
        GasUserSearchFragment fragment = GasUserSearchFragment.newInstance(new OnPageButtonClickListener<UserInfoResult.UserInfo>() {
            @Override
            public void onClick(UserInfoResult.UserInfo userInfo) {
                showUserInfoDetail(createGasRecordDetailFragment(userInfo, model));
            }

            @Override
            public void onNext(List<MediaFile> list) {
                showUserInfoDetail(createGasRecordDetailFragment(null, model));
            }
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_layout, fragment)
                .commit();
    }

    protected void showUserInfoDetail(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_layout, fragment)
                .commit();
    }

    public static void ToActivity(Context context) {
        Intent intent = new Intent(context, AddNewGasUserActivity.class);
        context.startActivity(intent);
    }

    protected GasRecordDetailFragment createGasRecordDetailFragment(UserInfoResult.UserInfo userInfo, GasRecordModel recordModel) {
        return GasRecordDetailFragment.newInstance(new OnPageButtonClickListener<UserInfoResult.UserInfo>() {
            @Override
            public void onClick(UserInfoResult.UserInfo userInfo) {

            }

            @Override
            public void onNext(List<MediaFile> list) {
                submitData(list);
            }
        }, userInfo, recordModel);
    }

    protected void submitData(List<MediaFile> list) {
        showLoading(true);
        if (list != null && list.size() > 0) {
            List<String> path = new ArrayList<>();
            List<String> fileName = new ArrayList<>();
            for (MediaFile file : list) {
                if (file.isLocal()) {
                    path.add(file.getPath());
                    fileName.add(remoteAddress + Utils.getFileName(file.getPath()));
                } else {
                    fileName.add(file.getPath().replace(Constants.IMAGE_URL, ""));
                }

            }
            model.phoneftp = Utils.listToString(fileName);
            disposable = UploadTask.createUploadTask(path, remoteAddress)
                    .subscribeOn(Schedulers.io())
                    .flatMap(new Function<Boolean, ObservableSource<HttpResult>>() {
                        @Override
                        public ObservableSource<HttpResult> apply(Boolean aBoolean) throws Exception {
                            return aBoolean ? createSubmitObservable() : null;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<HttpResult>() {
                        @Override
                        public void accept(HttpResult httpResult) throws Exception {
                            showLoading(false);
                            if (httpResult != null) {
                                if (httpResult.pushState == 200) {
                                    submitSuccess();
                                    Toast.makeText(AddNewGasUserActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddNewGasUserActivity.this, "提交失败！" + httpResult.pushMsg, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddNewGasUserActivity.this, "文件上传失败，稍后再试！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            showLoading(false);
                            Toast.makeText(AddNewGasUserActivity.this, "提交错误！" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    protected Observable<HttpResult> createSubmitObservable() {
        return NetworkApi.getService(GasService.class)
                .saveUserInfo(getFiledMap(model));
    }

    protected void submitSuccess() {
        finish();
    }

    protected HashMap<String, String> getFiledMap(GasRecordModel model) {
        HashMap<String, String> map = new HashMap<>();
        Class<? extends GasRecordModel> aClass = model.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field filed : declaredFields) {
            try {
                map.put(filed.getName(), valueOf(filed.get(model)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                break;
            }
        }
        return map;
    }

    public String valueOf(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

}
