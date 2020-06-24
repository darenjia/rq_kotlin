package com.bkjcb.rqapplication.check;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.SimpleBaseActivity;
import com.bkjcb.rqapplication.check.fragment.ChooseApplianceCheckInfoFragment;
import com.bkjcb.rqapplication.check.fragment.ChooseApplianceCheckTypeFragment;
import com.bkjcb.rqapplication.check.fragment.ChooseCheckStationFragment;
import com.bkjcb.rqapplication.check.fragment.ChooseCheckTypeFragment;
import com.bkjcb.rqapplication.check.model.CheckItem;
import com.bkjcb.rqapplication.check.model.CheckStation;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

public class CreateApplianceCheckTaskActivity extends SimpleBaseActivity {

    private ChooseApplianceCheckTypeFragment checkTypeFragment;
    private int currentPage = 0;
    private ChooseCheckStationFragment stationFragment;
    private QMUITopBarLayout barLayout;
    private CheckItem checkItem;
    private ChooseApplianceCheckInfoFragment infoFragment;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_create_check_task;
    }

    @Override
    protected void initView() {
        barLayout = initTopbar("新建器具检查任务");
        updateTitle();
        checkTypeFragment = ChooseApplianceCheckTypeFragment.newInstance(new ChooseCheckTypeFragment.OnChooseListener() {
            @Override
            public void choose(String type) {
                checkItem.zhandianleixing = type;
                changeView(1);
            }
        }, new String[]{"维修检查企业", "报警器企业", "销售企业", ""});
        stationFragment = ChooseCheckStationFragment.newInstance(new ChooseCheckStationFragment.OnChooseListener() {
            @Override
            public void choose(CheckStation type) {
                checkItem.beijiandanweiid = type.getGuid();
                checkItem.beijiandanwei = type.getQiyemingcheng();
                changeView(2);
            }

            @Override
            public void back() {
                changeView(0);
            }
        }, 1);
        infoFragment = ChooseApplianceCheckInfoFragment.newInstance(new ChooseApplianceCheckInfoFragment.OnChooseListener() {
            @Override
            public void choose() {
                saveItem();
            }

            @Override
            public void back() {
                changeView(1);
            }
        });
    }

    protected static void ToActivity(Context context) {
        Intent intent = new Intent(context, CreateApplianceCheckTaskActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        replaceView(checkTypeFragment);
        checkItem = new CheckItem(1);
        checkTypeFragment.setCheckItem(checkItem);
        stationFragment.setCheckItem(checkItem);
        infoFragment.setCheckItem(checkItem);
    }

    private void replaceView(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.create_task_content, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (currentPage == 0) {
            super.onBackPressed();
        } else if (currentPage == 1) {
            changeView(0);
        } else {
            changeView(1);
        }
    }

    private void updateTitle() {
        String title = "新建检查任务";
        if (currentPage == 0) {
            title += "(1/3)";
        } else if (currentPage==1){
            title += "(2/3)";
        }else {
            title += "(2/3)";
        }
        barLayout.setTitle(title);
    }

    private void changeView(int page) {
        if (page == 0) {
            replaceView(checkTypeFragment);
        } else if (page==1){
            replaceView(stationFragment);
        }else {
            replaceView(infoFragment);
        }
        currentPage = page;
        updateTitle();
    }

    private void saveItem() {
        long id = CheckItem.getBox().put(checkItem);
        showSnackbar(barLayout, "创建检查任务成功！");
        ApplianceCheckResultDetailActivity.ToActivity(this, id);
        finish();
    }


}
