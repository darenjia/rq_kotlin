package com.bkjcb.rqapplication;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bkjcb.rqapplication.adapter.AddressItemAdapter;
import com.bkjcb.rqapplication.fragment.MapFragment;
import com.bkjcb.rqapplication.model.Test;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends SimpleBaseActivity {

    @BindView(R.id.map_layout)
    FrameLayout mMapLayout;
    @BindView(R.id.search_view)
    EditText mSearchView;
    @BindView(R.id.clear_btn)
    ImageView mClearBtn;
    @BindView(R.id.address_list)
    RecyclerView mAddressList;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.appbar)
    QMUITopBarLayout barLayout;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.clear_btn)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.clear_btn:
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        barLayout.setTitle("首页");
        barLayout.addRightImageButton(R.drawable.vector_drawable_scan, R.id.top_right_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                        startActivityForResult(intent, 1);
                    }
                });
        MapFragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.map_layout, fragment).commit();
        initSwipeRefreshLayout(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        mAddressList.setLayoutManager(new LinearLayoutManager(this));
        AddressItemAdapter adapter = new AddressItemAdapter(R.layout.item_address_view);
        mAddressList.setAdapter(adapter);
        for (int i = 0; i < 10; i++) {
            adapter.addData(new Test());
        }
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
