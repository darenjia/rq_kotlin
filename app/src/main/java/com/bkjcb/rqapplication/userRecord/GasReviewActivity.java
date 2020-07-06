package com.bkjcb.rqapplication.userRecord;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.bkjcb.rqapplication.base.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.userRecord.fragment.GasReviewFragment;
import com.bkjcb.rqapplication.userRecord.fragment.GasUserSearch2Fragment;
import com.bkjcb.rqapplication.base.ftp.UploadTask;
import com.bkjcb.rqapplication.base.interfaces.OnPageButtonClickListener;
import com.bkjcb.rqapplication.userRecord.model.GasUserRecordResult;
import com.bkjcb.rqapplication.base.model.HttpResult;
import com.bkjcb.rqapplication.base.model.MediaFile;
import com.bkjcb.rqapplication.userRecord.model.ReviewRecord;
import com.bkjcb.rqapplication.userRecord.model.ReviewRecordResult;
import com.bkjcb.rqapplication.userRecord.retrofit.GasService;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.base.util.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
public class GasReviewActivity extends AddNewGasUserActivity {
    private ReviewRecord record;
    private String id;
    protected String remoteAddress = "yihuyidang/fucha/" + Utils.getUUID() + "/";

    @Override
    protected void initView() {
        initEmptyView();
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("ID");
        int type = getIntent().getIntExtra("Type", 0);
        String name = getIntent().getStringExtra("Name");
        record = (ReviewRecord) getIntent().getSerializableExtra("data");
        if (type != 0 || (TextUtils.isEmpty(id) && record == null)) {
            initTopbar("新建复查记录");
            record = new ReviewRecord(1);
            record.userId = MyApplication.getUser().getUserId();
            record.yonghuming = name;
            if (TextUtils.isEmpty(id)) {
                showUserListView();
            } else {
                record.yihuyidangid = id;
                showUserInfoDetail(createFragment());
            }

        } else {
            initTopbar("复查记录详情");
            if (record == null) {
                getRecordData();
            } else {
                showUserInfoDetail(createFragment());
            }
        }
    }

    private Fragment createFragment() {
        return GasReviewFragment.newInstance(record, new OnPageButtonClickListener<String>() {
            @Override
            public void onClick(String userInfo) {

            }

            @Override
            public void onNext(List<MediaFile> list) {
                submitData(list);
            }
        });
    }

    @Override
    protected void showUserListView() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_layout, GasUserSearch2Fragment.newInstance(new OnPageButtonClickListener<GasUserRecordResult.GasUserRecord>() {

                    @Override
                    public void onClick(GasUserRecordResult.GasUserRecord userInfo) {
                        record.yihuyidangid = userInfo.getYihuyidangid();
                        showUserInfoDetail(createFragment());
                    }

                    @Override
                    public void onNext(List<MediaFile> list) {

                    }
                }))
                .commit();
    }

    protected void submitData(List<MediaFile> list) {
        showLoading(true);
        if (list != null && list.size() > 0) {
            List<String> path = new ArrayList<>();
            List<String> fileName = new ArrayList<>();
            for (MediaFile file : list) {
                path.add(file.getPath());
                fileName.add(remoteAddress + Utils.getFileName(file.getPath()));
            }
            record.phoneftp = Utils.listToString(fileName);
            disposable = UploadTask.createUploadTask(path, remoteAddress)
                    .subscribeOn(Schedulers.io())
                    .flatMap(new Function<Boolean, ObservableSource<HttpResult>>() {
                        @Override
                        public ObservableSource<HttpResult> apply(Boolean aBoolean) throws Exception {
                            return aBoolean ? NetworkApi.getService(GasService.class)
                                    .saveReviewInfo(getFiledMap(record)) : null;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<HttpResult>() {
                        @Override
                        public void accept(HttpResult httpResult) throws Exception {
                            showLoading(false);
                            if (httpResult != null) {
                                if (httpResult.pushState == 200) {
                                    finish();
                                    Toast.makeText(GasReviewActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(GasReviewActivity.this, "提交失败！" + httpResult.pushMsg, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(GasReviewActivity.this, "文件上传失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            showLoading(false);
                            Toast.makeText(GasReviewActivity.this, "提交失败！" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public static void ToActivity(Context context) {
        Intent intent = new Intent(context, GasReviewActivity.class);
        context.startActivity(intent);
    }

    public static void ToActivity(Context context, ReviewRecord record) {
        Intent intent = new Intent(context, GasReviewActivity.class);
        intent.putExtra("data", record);
        context.startActivity(intent);
    }

    public static void ToActivity(Context context, String record) {
        Intent intent = new Intent(context, GasReviewActivity.class);
        intent.putExtra("ID", record);
        context.startActivity(intent);
    }

    public static void ToActivity(Context context, String record, int type, String name) {
        Intent intent = new Intent(context, GasReviewActivity.class);
        intent.putExtra("ID", record);
        intent.putExtra("Type", type);
        intent.putExtra("Name", name);
        context.startActivity(intent);
    }

    private HashMap<String, String> getFiledMap(ReviewRecord model) {
        HashMap<String, String> map = new HashMap<>();
        Class<? extends ReviewRecord> aClass = model.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field filed : declaredFields) {
            try {
                map.put(filed.getName(), valueOf(filed.get(model)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private void getRecordData() {
        emptyView.show(true);
        disposable = NetworkApi.getService(GasService.class)
                .getRecordList("", id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReviewRecordResult>() {
                    @Override
                    public void accept(ReviewRecordResult result) throws Exception {
                        if (result.pushState == 200 && result.getData().size() > 0) {
                            record = result.getData().get(0);
                            if (record != null) {
                                showUserInfoDetail(createFragment());
                                hideEmptyView();
                            } else {
                                showErrorView(null);
                            }
                        } else {
                            showErrorView(null);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showErrorView(null);
                    }
                });
    }
}
