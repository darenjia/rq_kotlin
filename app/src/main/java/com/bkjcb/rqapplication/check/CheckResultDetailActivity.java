package com.bkjcb.rqapplication.check;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.bkjcb.rqapplication.Constants;
import com.bkjcb.rqapplication.MyApplication;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.SimpleBaseActivity;
import com.bkjcb.rqapplication.check.fragment.ApplianceCheckResultFragment;
import com.bkjcb.rqapplication.check.fragment.CheckResultFragment;
import com.bkjcb.rqapplication.check.model.CheckItem;
import com.bkjcb.rqapplication.check.model.CheckResultItem;
import com.bkjcb.rqapplication.check.model.CheckResultItem_;
import com.bkjcb.rqapplication.check.retrofit.CheckService;
import com.bkjcb.rqapplication.ftp.FtpUtils;
import com.bkjcb.rqapplication.ftp.UploadTask;
import com.bkjcb.rqapplication.model.ExportFilePathResult;
import com.bkjcb.rqapplication.model.HttpResult;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.bkjcb.rqapplication.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class CheckResultDetailActivity extends SimpleBaseActivity {

    @BindView(R.id.info_operation)
    Button mInfoOperation;
    @BindView(R.id.info_export)
    Button mInfoExport;
    protected CheckItem checkItem;
    private CheckResultFragment fragment;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_detail_result_check;
    }

    @Override
    protected void initView() {
        initTopbar("检查信息", 0);
    }

    @Override
    protected void initData() {
        checkItem = (CheckItem) getIntent().getSerializableExtra("data");
        if (checkItem == null) {
            long id = getIntent().getLongExtra("id", 0);
            checkItem = CheckItem.getBox().get(id);
            if (checkItem == null) {
                return;
            }
        }
        if (checkItem.type == 0) {
            fragment = CheckResultFragment.newInstance(checkItem);
        } else {
            fragment = ApplianceCheckResultFragment.newInstance(checkItem);
        }
        showCheckDetail();
        //initTextValue();
        mInfoExport.setVisibility(checkItem.status == 3 ? View.VISIBLE : View.GONE);
        mInfoOperation.setText(getOperation(checkItem.status));
    }

    private void showCheckDetail() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, fragment)
                .commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initTextValue();
        fragment.updateData(checkItem);
    }

    private void initTextValue() {
        checkItem = CheckItem.getBox().get(checkItem.id);
        mInfoOperation.setText(getOperation(checkItem.status));
    }

    @OnClick({R.id.info_operation, R.id.info_export})
    public void onClick(View v) {
        if (v.getId() == R.id.info_operation) {
            if (checkItem.status == 2) {
                submitResult();
            } else {
                toCheckContentActivity();
            }
        } else if (v.getId() == R.id.info_export) {
            getExportFilePath();
        }

    }

    protected void toCheckContentActivity() {
        CheckDetailActivity.ToActivity(this, checkItem.id);
    }

    protected static void ToActivity(Context context, CheckItem checkItem) {
        Intent intent = new Intent(context, CheckResultDetailActivity.class);
        intent.putExtra("data", checkItem);
        context.startActivity(intent);
    }

    protected static void ToActivity(Context context, long id) {
        Intent intent = new Intent(context, CheckResultDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    private String getOperation(int status) {
        String retString = "";
        switch (status) {
            case 0:
                retString = "开始检查";
                break;
            case 1:
                retString = "继续检查";
                break;
            case 2:
                retString = "上传提交";
                break;
            case 3:
                retString = "查看详情";
            default:
        }
        return retString;
    }

    private List<String> getFiles() {
        if (TextUtils.isEmpty(checkItem.filePath)) {
            return new ArrayList<>();
        }
        return Arrays.asList(checkItem.filePath.split(","));
    }

    protected void submitResult() {
        showLoading(true);
        List<CheckResultItem> list = queryResult();
        disposable = UploadTask.createUploadTask(getFiles(), Utils.getFTPPath(checkItem), new FtpUtils.UploadProgressListener() {
            @Override
            public void onUploadProgress(String currentStep, long uploadSize, long size, File file) {

            }
        }).subscribeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<HttpResult>>() {
                    @Override
                    public ObservableSource<HttpResult> apply(Boolean aBoolean) throws Exception {
                        return aBoolean ? NetworkApi.getService(CheckService.class).saveCheckItem(
                                MyApplication.getUser().getUserId(),
                                checkItem.year,
                                checkItem.zhandianleixing,
                                checkItem.beijiandanweiid,
                                checkItem.beizhu,
                                checkItem.jianchariqi,
                                checkItem.jianchajieguo,
                                getItemsID(list),
                                getItemsResult(list),
                                Utils.getFTPPath(checkItem),
                                checkItem.c_id
                        ) : null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpResult>() {
                    @Override
                    public void accept(HttpResult result) throws Exception {
                        if (result.pushState == 200) {
                            updateTaskStatus();
                        }
                        showTipInfo(result.pushMsg);
                        showLoading(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showLoading(false);
                        showTipInfo("上传失败！" + throwable.getMessage());
                    }
                });
    }

    protected void updateTaskStatus() {
        checkItem.status = 3;
        CheckItem.getBox().put(checkItem);
        initTextValue();
    }

    protected void showTipInfo(String info) {
        showSnackbar(mInfoOperation, info);
    }

    private String[] getItemsID(List<CheckResultItem> list) {
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strings[i] = list.get(i).jianchaxiangid;
        }
        return strings;
    }

    private String[] getItemsResult(List<CheckResultItem> list) {
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strings[i] = TextUtils.isEmpty(list.get(i).jianchajilu) ? "" : list.get(i).jianchajilu;
        }
        return strings;
    }

    private List<CheckResultItem> queryResult() {
        return CheckResultItem.getBox().query().equal(CheckResultItem_.jianchaid, checkItem.c_id).build().find();
    }

    private void getExportFilePath() {
        showLoading(true);
        disposable = NetworkApi.getService(CheckService.class)
                .getExportPath(checkItem.c_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ExportFilePathResult>() {
                    @Override
                    public void accept(ExportFilePathResult exportFilePathResult) throws Exception {
                        showLoading(false);
                        if (exportFilePathResult.pushState == 200) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(Constants.BASE_URL + "/rq/push/downloadfile?downloadfile=" + exportFilePathResult.getDatas()));
                            startActivity(intent);
                        } else {
                            showSnackbar(mInfoExport, "生成文件失败！" + exportFilePathResult.getPushMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showLoading(false);
                        showSnackbar(mInfoExport, "获取文件地址失败！" + throwable.getMessage());
                    }
                });
    }
}
