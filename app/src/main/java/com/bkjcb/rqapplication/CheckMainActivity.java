package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bkjcb.rqapplication.adapter.CheckItemAdapter;
import com.bkjcb.rqapplication.model.CheckItem;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class CheckMainActivity extends SimpleBaseActivity {
    @BindView(R.id.appbar)
    QMUITopBarLayout mAppbar;
    @BindView(R.id.check_list)
    RecyclerView mCheckList;
    private CheckItemAdapter adapter;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_check;
    }

    @Override
    protected void initView() {
        mAppbar.setTitle("检查");
        mAppbar.addRightImageButton(R.drawable.vector_drawable_add, R.id.top_right_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CreateCheckTaskActivity.ToActivity(CheckMainActivity.this);
                    }
                });
        adapter = new CheckItemAdapter(R.layout.item_checkadapter_view);
        mCheckList.setLayoutManager(new LinearLayoutManager(this));
        mCheckList.setAdapter(adapter);
        adapter.bindToRecyclerView(mCheckList);
    }

    @Override
    protected void initData() {
        adapter.addData(new CheckItem(System.currentTimeMillis()));
        adapter.addData(new CheckItem(System.currentTimeMillis()));
        adapter.addData(new CheckItem(System.currentTimeMillis() - 24 * 3600*1000));
    }

    private void queryLocalData() {

    }

    private void queryRemateDta() {

    }

    protected static void ToActivity(Context context) {
        Intent intent = new Intent(context, CheckMainActivity.class);
        context.startActivity(intent);
    }
}
