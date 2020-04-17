package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.bkjcb.rqapplication.adapter.AddressItemAdapter;
import com.bkjcb.rqapplication.fragment.MapFragment;
import com.bkjcb.rqapplication.model.ConvertResult;
import com.bkjcb.rqapplication.model.UserInfoResult;
import com.bkjcb.rqapplication.retrofit.ConvertService;
import com.bkjcb.rqapplication.retrofit.DataService;
import com.bkjcb.rqapplication.retrofit.NetworkApi;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GasMainActivity extends SimpleBaseActivity {

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
    private AddressItemAdapter adapter;
    private List<UserInfoResult.UserInfo> userInfoList;
    private Disposable filterDisposable;
    private MapFragment fragment;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_gas;
    }

    @OnClick(R.id.clear_btn)
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.clear_btn:
                mSearchView.setText("");
                mClearBtn.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        barLayout.setTitle(MyApplication.user.getReal_name());
        barLayout.addRightImageButton(R.drawable.vector_drawable_setting, R.id.top_right_button1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GasMainActivity.this, SettingActivity.class);
                        startActivityForResult(intent, 1);
                    }
                });
        barLayout.addRightImageButton(R.drawable.vector_drawable_scan_white, R.id.top_right_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GasMainActivity.this, ScanActivity.class);
                        startActivityForResult(intent, 1);
                    }
                });
        fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.map_layout, fragment).commit();
        initSwipeRefreshLayout(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        mAddressList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressItemAdapter(R.layout.item_address_view);
        adapter.bindToRecyclerView(mAddressList);
        mAddressList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserDetailActivity.ToUserDetailActivity(GasMainActivity.this, (UserInfoResult.UserInfo) adapter.getData().get(position));
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        getData();
    }

    private void getData() {
        disposable = NetworkApi.getService(DataService.class)
                .getUserInfos(MyApplication.user.getAreacode().getArea_code())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserInfoResult>() {
                    @Override
                    public void accept(UserInfoResult result) throws Exception {
                        if (result.getPushState() == 200) {
                            userInfoList = result.getDatas();
                            adapter.setNewData(userInfoList);
                            transformCoordinate(userInfoList);
                        } else {
                            Toast.makeText(getApplicationContext(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                        }
                        showRefreshLayout(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showRefreshLayout(false);
                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }



    private void filter() {
        filterDisposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mSearchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        emitter.onNext(s.toString());
                        mClearBtn.setVisibility(View.VISIBLE);
                    }
                });

            }
        }).debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .map(new Function<String, List<UserInfoResult.UserInfo>>() {
                    @Override
                    public List<UserInfoResult.UserInfo> apply(String s) throws Exception {
                        List<UserInfoResult.UserInfo> list = new ArrayList<>();
                        if (s.trim().length() > 0) {
                            for (UserInfoResult.UserInfo user : userInfoList) {
                                if (user.getUserName().contains(s) || user.getUserAddress().contains(s)) {
                                    list.add(user);
                                }
                            }
                        } else {
                            return userInfoList;
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UserInfoResult.UserInfo>>() {
                    @Override
                    public void accept(List<UserInfoResult.UserInfo> userInfos) throws Exception {
                        if (userInfos.size() > 0) {
                            adapter.setNewData(userInfos);
                        } else {
                            setEmptyList();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        setEmptyList();
                    }
                });
    }

    private void setEmptyList() {
        adapter.setNewData(null);
        adapter.setEmptyView(R.layout.empty_textview, (ViewGroup) mAddressList.getParent());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (filterDisposable != null && !filterDisposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        filter();
    }

    private void transformCoordinate(List<UserInfoResult.UserInfo> list) {
        StringBuilder builder = new StringBuilder();
        for (UserInfoResult.UserInfo info : list) {
            if (info.getX() == null || info.getY() == null) {
                continue;
            }
            builder.append(info.getX()).append(",").append(info.getY()).append("|");
        }
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://47.103.63.36:8084/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        disposable = retrofit.create(ConvertService.class)
                .convert("sh", "gcj02", builder.substring(0, builder.length() - 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ConvertResult>() {
                    @Override
                    public void accept(ConvertResult convertResult) throws Exception {
                        if (convertResult.isSuccess()) {
                            Log.w("ds", convertResult.getData().size() + "");
                            locationToLatlng(convertResult.getData());
                            fragment.setLatLngs(userInfoList);
                        } else {
                            showSnackbar(mMapLayout, convertResult.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showSnackbar(mMapLayout, throwable.getMessage());
                    }
                });
    }

    private void locationToLatlng(List<ConvertResult.LocationPosition> locationPositions) {
        for (int i = 0; i < locationPositions.size(); i++) {
            ConvertResult.LocationPosition position = locationPositions.get(i);
            userInfoList.get(i).setLatLng(new LatLng(position.getLatitude(), position.getLongitude()));
        }
    }

    public static void ToActivity(Context context) {
        Intent intent = new Intent(context, GasMainActivity.class);
        context.startActivity(intent);
    }
}
