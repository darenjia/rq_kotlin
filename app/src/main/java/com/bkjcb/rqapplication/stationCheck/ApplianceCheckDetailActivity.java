package com.bkjcb.rqapplication.stationCheck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.bkjcb.rqapplication.base.adapter.ViewPagerAdapter;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.stationCheck.fragment.AlarmCheckItemResultFragment;
import com.bkjcb.rqapplication.stationCheck.fragment.ApplianceCheckItemDetailFragment;
import com.bkjcb.rqapplication.stationCheck.model.ApplianceCheckContentItem;
import com.bkjcb.rqapplication.stationCheck.model.ApplianceCheckResult;
import com.bkjcb.rqapplication.stationCheck.model.ApplianceCheckResultItem;
import com.bkjcb.rqapplication.stationCheck.model.ApplianceCheckResultItem_;
import com.bkjcb.rqapplication.stationCheck.model.CheckItem;
import com.bkjcb.rqapplication.stationCheck.retrofit.ApplianceCheckService;
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
        StyledDialog.init(this);
    }

    protected Box<ApplianceCheckResultItem> checkResultItemBox;

    protected void saveResult() {
        /*if (TextUtils.isEmpty(checkItem.jianchajieguo)) {

        } else {

        }*/
        ApplianceCheckItemDetailFragment fragment;
        if (fragmentList.get(mViewPager.getCurrentItem()) instanceof ApplianceCheckItemDetailFragment) {
            fragment = (ApplianceCheckItemDetailFragment) fragmentList.get(mViewPager.getCurrentItem());
            fragment.saveData();
        }
        int pager = 0;
        if ((pager = verify()) == -1) {
            if (TextUtils.isEmpty(checkItem.tijiaobaogao)) {
                if (mViewPager.getCurrentItem() == fragmentList.size() - 1) {
                    showSnackbar(mPagerNumber, "请选择整改要求");
                } else {
                    mViewPager.setCurrentItem(fragmentList.size() - 1, true);
                }
            } else {
                showFinishTipDialog();
//                createRemarkDialog();
            }
        } else {
            mViewPager.setCurrentItem(pager, true);
            fragment = (ApplianceCheckItemDetailFragment) fragmentList.get(pager);
            fragment.scrollToBottom();
            showSnackbar(mViewPager, "请填写检查内容记录");
        }
        /* */
    }

    @Override
    protected void getCheckContent() {
        List<ApplianceCheckContentItem> list = ApplianceCheckContentItem.getContentItems(checkItem.c_id);
        if (list.size() > 0) {
            initCheckData(list);
            initImageListView();
            hideEmptyView();
        } else {
            getDataFromNet();
        }

    }

    protected void getDataFromNet() {
        disposable = NetworkApi.getService(ApplianceCheckService.class)
                .getFixCheckItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApplianceCheckResult>() {
                    @Override
                    public void accept(ApplianceCheckResult result) throws Exception {
                        if (result.pushState == 200 && result.getDatas() != null && result.getDatas().size() > 0) {
                            saveCheckContent(result.getDatas());
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

    protected void saveCheckContent(List<ApplianceCheckContentItem> datas) {
        for (ApplianceCheckContentItem item : datas) {
            item.setCid(checkItem.c_id);
        }
        ApplianceCheckContentItem.getBox().put(datas);
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
        contents.add((datas.size()+1) + ".检查综合意见及整改要求");
        fragmentList.add(AlarmCheckItemResultFragment.newInstance(checkItem));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(adapter);
        updateCurrentPageNumber();
    }

    protected Fragment createFragment(ApplianceCheckContentItem item, String id) {
        return ApplianceCheckItemDetailFragment.newInstance(item, id, checkItem.status == 3);
    }

    protected void saveResultItem(String cid, String uid) {
        if (checkResultItemBox.query().equal(ApplianceCheckResultItem_.jianchaid, cid).and().equal(ApplianceCheckResultItem_.jianchaxiangid, uid).build().count() == 0) {
            ApplianceCheckResultItem resultItem = new ApplianceCheckResultItem(cid, uid);
            resultItem.content = "";
            resultItem.remark = "";
            resultItem.ischeck = "1";
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
        String content = "";
        if (!TextUtils.isEmpty(checkItem.beizhu)) {
            content = checkItem.beizhu;
        }
        StyledDialog.buildNormalInput("检查综合意见", "请输入", null, content, null, new MyDialogListener() {
            @Override
            public void onFirst() {
                //closeActivity(true);
                showFinishTipDialog();
            }

            @Override
            public void onSecond() {
                closeActivity(false);
            }

            @Override
            public void onGetInput(CharSequence input1, CharSequence input2) {
                super.onGetInput(input1, input2);
                saveData(input1.toString());
            }
        }).setBtnText("保存并提交", "保存")
                .show();
    }

    private void saveData(String data) {
        if (data != null) {
            checkItem.beizhu = data;
        }
    }

    @Override
    protected void closeActivity(boolean isSubmit) {
        if (isSubmit) {
            checkItem.status = 2;
        }
        if (currentPage == fragmentList.size() - 1) {
            AlarmCheckItemResultFragment resultFragment = (AlarmCheckItemResultFragment) fragmentList.get(fragmentList.size() - 1);
            checkItem.beizhu = resultFragment.getRemark();
        }
        saveDate();
        Toast.makeText(this, "检查完成！", Toast.LENGTH_SHORT).show();
        finish();
    }

    private int verify() {
        ApplianceCheckResultItem resultItem;
        List<ApplianceCheckContentItem> contentItems = ApplianceCheckContentItem.getContentItems(checkItem.c_id);
        for (int i = 0; i < contentItems.size() - 1; i++) {
            ApplianceCheckContentItem item = contentItems.get(i);
            if ((resultItem = queryResult(item.getGuid())) != null) {
                if (resultItem.ischeck.equals("0")) {
                    if ((!TextUtils.isEmpty(item.getCheakrecord()) || !TextUtils.isEmpty(item.getCheakrecord2())) && (TextUtils.isEmpty(resultItem.content)) && TextUtils.isEmpty(resultItem.remark)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private ApplianceCheckResultItem queryResult(String id) {
        return ApplianceCheckResultItem.getBox().query().equal(ApplianceCheckResultItem_.jianchaid, checkItem.c_id).equal(ApplianceCheckResultItem_.jianchaxiangid, id).build().findUnique();
    }
}
