package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.bkjcb.rqapplication.adapter.ViewPagerAdapter;
import com.bkjcb.rqapplication.fragment.ApplianceCheckItemDetailFragment;
import com.bkjcb.rqapplication.model.ApplianceCheckContentItem;
import com.bkjcb.rqapplication.model.ApplianceCheckResult;
import com.bkjcb.rqapplication.model.ApplianceCheckResultItem;
import com.bkjcb.rqapplication.model.ApplianceCheckResultItem_;
import com.bkjcb.rqapplication.model.CheckItem;
import com.bkjcb.rqapplication.retrofit.ApplianceCheckService;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class ApplianceCheckDetailActivity extends CheckDetailActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StyledDialog
                .init(this);
    }

    protected Box<ApplianceCheckResultItem> checkResultItemBox;

    protected void saveResult() {
        /*if (TextUtils.isEmpty(checkItem.jianchajieguo)) {

        } else {

        }*/
        createRemarkDialog();
        /* */
    }

    @Override
    protected void getCheckContent() {
        disposable = retrofit.create(ApplianceCheckService.class)
                .getFixCheckItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApplianceCheckResult>() {
                    @Override
                    public void accept(ApplianceCheckResult result) throws Exception {
                        if (result.pushState == 200 && result.getDatas() != null && result.getDatas().size() > 0) {
                            initCheckData(result.getDatas());
                            initImageListView();
                            hideEmptyView();
                        } else {
                            getDateFail(result.pushMsg);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getDateFail(throwable.getMessage());
                    }
                });
    }

    protected void initCheckData(List<ApplianceCheckContentItem> datas) {
        fragmentList = new ArrayList<>();
        contents = new ArrayList<>();
        checkResultItemBox = ApplianceCheckResultItem.getBox();
        for (ApplianceCheckContentItem item : datas) {
            fragmentList.add(createFragment(item, checkItem.c_id));
            saveResultItem(checkItem.c_id, item.getGuid());
            contents.add(item.getXuhao() + "." + item.getCheakname());
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(adapter);
        updateCurrentPageNumber();
    }

    protected Fragment createFragment(ApplianceCheckContentItem item, String id) {
        return ApplianceCheckItemDetailFragment.newInstances(item, id, checkItem.status == 3);
    }

    protected void saveResultItem(String cid, String uid) {
        if (checkResultItemBox.query().equal(ApplianceCheckResultItem_.jianchaid, cid).and().equal(ApplianceCheckResultItem_.jianchaxiangid, uid).build().count() == 0) {
            ApplianceCheckResultItem resultItem = new ApplianceCheckResultItem(cid, uid);
            checkResultItemBox.put(resultItem);
        }
    }


    protected static void ToActivity(Context context, CheckItem checkItem) {
        Intent intent = new Intent(context, ApplianceCheckDetailActivity.class);
        intent.putExtra("data", checkItem);
        context.startActivity(intent);
    }

    protected static void ToActivity(Context context, long id) {
        Intent intent = new Intent(context, ApplianceCheckDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    private void createRemarkDialog() {
        StyledDialog.buildNormalInput("检查综合意见", "请输入", null, "", null, new MyDialogListener() {
            @Override
            public void onFirst() {

            }

            @Override
            public void onSecond() {

            }

            @Override
            public void onGetInput(CharSequence input1, CharSequence input2) {
                super.onGetInput(input1, input2);
                saveData(input1.toString());
            }
        }).setBtnText("保存")
                .show();
    }

    private void saveData(String data) {
        checkItem.status = 2;
        if (data != null) {
            checkItem.beizhu = data;
        }
        saveDate();
        Toast.makeText(this, "检查完成！", Toast.LENGTH_SHORT).show();
        finish();
    }

}
