package com.bkjcb.rqapplication.stationCheck;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.stationCheck.adapter.CheckItemAdapter;
import com.bkjcb.rqapplication.stationCheck.model.CheckItem;
import com.bkjcb.rqapplication.stationCheck.model.CheckItem_;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.objectbox.query.QueryBuilder;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class CheckMainActivity extends SimpleBaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.check_list)
    RecyclerView mCheckList;
    @BindView(R.id.station_search_close)
    Button mSearchButton;
    @BindView(R.id.station_name)
    EditText mSearchKey;
    private CheckItemAdapter adapter;
    private boolean isShowAll = false;
    private int type;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_check_with_refresh;
    }

    @Override
    protected void initView() {
        QMUITopBarLayout mAppbar = initTopbar("检查列表");
        mAppbar.addRightImageButton(R.drawable.vector_drawable_create, R.id.top_right_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == 0) {
                            CreateCheckTaskActivity.ToActivity(CheckMainActivity.this);
                        } else {
                            CreateApplianceCheckTaskActivity.ToActivity(CheckMainActivity.this);
                        }
                    }
                });
        mAppbar.addRightImageButton(getButtonDrawableResId(), R.id.top_right_button1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QMUIAlphaImageButton button = (QMUIAlphaImageButton) v;
                        isShowAll = !isShowAll;
                        button.setImageResource(getButtonDrawableResId());
                        Toast.makeText(CheckMainActivity.this, isShowAll ?  "显示全部":"仅显示未完成", Toast.LENGTH_SHORT).show();
                        showCheckList();
                    }
                });
        //mAppbar.setBackgroundAlpha(0);
        adapter = new CheckItemAdapter(R.layout.item_checkadapter_view);
        mCheckList.setLayoutManager(new LinearLayoutManager(this));
        mCheckList.setAdapter(adapter);
        initSwipeRefreshLayout(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.replaceData(queryLocalData());
                showRefreshLayout(false);
            }
        });
        adapter.bindToRecyclerView(mCheckList);

    }

    private int getButtonDrawableResId() {
        return isShowAll ? R.drawable.vector_drawable_all : R.drawable.vector_drawable_sub;
    }

    @OnClick(R.id.station_search_close)
    public void onClick(View v) {
        adapter.replaceData(queryLocalData());
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("Type", 0);
        adapter.setOnItemClickListener(this);
        getHideSetting();
        showCheckList();
    }

    private List<CheckItem> queryLocalData() {
        String key = mSearchKey.getText().toString();
        QueryBuilder<CheckItem> builder = CheckItem.getBox().query();
        builder.equal(CheckItem_.type, type);
        if (!TextUtils.isEmpty(key)) {
            builder.contains(CheckItem_.beijiandanwei, key);
        }
        if (!isShowAll) {
            builder.notEqual(CheckItem_.status, 3);
        }
        return builder.build().find();
    }

    private void queryRemoteDta() {

    }

    protected void getHideSetting() {
        isShowAll = !getSharedPreferences().getBoolean("hide_finished", false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showCheckList();
    }

    protected void showCheckList() {
        List<CheckItem> list = queryLocalData();
        if (list.size() > 0) {
            adapter.setNewData(list);
        } else {
            adapter.setEmptyView(createEmptyView(createClickListener()));
        }
    }

    protected View.OnClickListener createClickListener() {
        if (type == 0) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateCheckTaskActivity.ToActivity(CheckMainActivity.this);
                }
            };
        } else {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateApplianceCheckTaskActivity.ToActivity(CheckMainActivity.this);
                }
            };
        }
    }

    protected static void ToActivity(Context context) {
        Intent intent = new Intent(context, CheckMainActivity.class);
        context.startActivity(intent);
    }

    public static void ToActivity(Context context, int type) {
        Intent intent = new Intent(context, CheckMainActivity.class);
        intent.putExtra("Type", type);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (type == 0) {
            CheckResultDetailActivity.ToActivity(this, (CheckItem) adapter.getItem(position));
        } else {
            ApplianceCheckResultDetailActivity.ToActivity(this, (CheckItem) adapter.getItem(position));
        }
    }

    private View createEmptyView(View.OnClickListener listener) {
        View view = getLayoutInflater().inflate(R.layout.empty_textview_with_button, null);
        view.findViewById(R.id.empty_button).setOnClickListener(listener);
        return view;
    }
}
