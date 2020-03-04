package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bkjcb.rqapplication.fragment.ChooseCheckInfoFragment;
import com.bkjcb.rqapplication.fragment.ChooseCheckStationFragment;
import com.bkjcb.rqapplication.fragment.ChooseCheckTypeFragment;
import com.bkjcb.rqapplication.model.CheckItem;
import com.bkjcb.rqapplication.model.CheckStation;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

public class CreateCheckTaskActivity extends SimpleBaseActivity {

    private ChooseCheckTypeFragment checkTypeFragment;
    private int currentPage = 0;
    private ChooseCheckStationFragment stationFragment;
    private QMUITopBarLayout barLayout;
    private ChooseCheckInfoFragment infoFragment;
    private CheckItem checkItem;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_create_check_task;
    }

    @Override
    protected void initView() {
        barLayout = initTopbar("新建检查任务");
        updateTitle();
        checkTypeFragment = ChooseCheckTypeFragment.newInstance(new ChooseCheckTypeFragment.OnChooseListener() {
            @Override
            public void choose(String type) {
                checkItem.zhandianleixing = type;
                changeView(1);
            }
        }, new String[]{"气化站", "储配站", "供应站", "加气站"});
        stationFragment = ChooseCheckStationFragment.newInstance(new ChooseCheckStationFragment.OnChooseListener() {
            @Override
            public void choose(CheckStation type) {
                checkItem.beijiandanweiid = type.getGuid();
                checkItem.beijiandanwei = type.getGas_station();
                changeView(2);
            }

            @Override
            public void back() {
                changeView(0);
            }
        });
        infoFragment = ChooseCheckInfoFragment.newInstance(new ChooseCheckInfoFragment.OnChooseListener() {
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
        Intent intent = new Intent(context, CreateCheckTaskActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        initRetrofit();
        replaceView(checkTypeFragment);
        checkItem = new CheckItem();
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
        } else if (currentPage == 1) {
            title += "(2/3)";
        } else {
            title += "(3/3)";
        }
        barLayout.setTitle(title);
    }

    private void changeView(int page) {
        if (page == 0) {
            replaceView(checkTypeFragment);
        } else if (page == 1) {
            replaceView(stationFragment);
        } else {
            replaceView(infoFragment);
        }
        currentPage = page;
        updateTitle();
    }

    private void saveItem() {
        long id = CheckItem.getBox().put(checkItem);
        showSnackbar(barLayout, "创建检查任务成功！");
        //CheckDetailActivity.ToActivity(this,id);
        CheckResultDetailActivity.ToActivity(this, id);
        finish();
    }


}
