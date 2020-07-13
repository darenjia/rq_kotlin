package com.bkjcb.rqapplication.emergency.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.base.util.RxJavaUtil;
import com.bkjcb.rqapplication.emergency.CreateEmergencyActivity;
import com.bkjcb.rqapplication.emergency.adapter.EmergencyAdapter;
import com.bkjcb.rqapplication.emergency.model.EmergencyModel;
import com.bkjcb.rqapplication.emergency.retrofit.EmergencyService;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Created by DengShuai on 2020/6/10.
 * Description :
 */
public class EmergencyFragment extends BaseSimpleFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.content_layout)
    RecyclerView mContentLayout;
    private EmergencyAdapter adapter;
    private String type = "";

    public void setType(String type) {
        this.type = type;
    }

    public static EmergencyFragment newInstance(String type) {
        EmergencyFragment fragment = new EmergencyFragment();
        fragment.setType(type);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_emergency;
    }

    @Override
    protected void initView() {
        adapter = new EmergencyAdapter(R.layout.item_emergency_detail);
        adapter.bindToRecyclerView(mContentLayout);
        mContentLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentLayout.setAdapter(adapter);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    protected void initData() {
        adapter.setOnItemClickListener(this);
        getData();
    }

    private void getData() {
        refreshLayout.setRefreshing(true);
        disposable = NetworkApi.getService(EmergencyService.class).getGasAccident(type)
                .compose(RxJavaUtil.getObservableTransformer())
                .subscribe(new Consumer<SimpleHttpResult<List<EmergencyModel>>>() {
                    @Override
                    public void accept(SimpleHttpResult<List<EmergencyModel>> result) throws Exception {
                        refreshLayout.setRefreshing(false);
                        if (result.pushState == 200) {
                            showResultList(result.getDatas());
                        } else {
                            showErrorView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.setRefreshing(false);
                        Toast.makeText(context, "获取数据错误：" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        showErrorView();
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CreateEmergencyActivity.ToActivity(getContext(), (EmergencyModel) adapter.getItem(position));
    }


    protected void showResultList(List<EmergencyModel> list) {
        if (list != null && list.size() > 0) {
            adapter.setNewData(list);
        } else {
            adapter.showEmptyView();
        }
    }


    protected void showErrorView() {
        adapter.showErrorView();
    }

}
