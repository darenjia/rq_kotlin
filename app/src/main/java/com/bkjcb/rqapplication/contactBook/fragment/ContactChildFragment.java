package com.bkjcb.rqapplication.contactBook.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.contactBook.adapter.ContactItemAdapter;
import com.bkjcb.rqapplication.contactBook.adapter.UnitAdapter;
import com.bkjcb.rqapplication.contactBook.database.ContactDataUtil;
import com.bkjcb.rqapplication.contactBook.model.Level;
import com.bkjcb.rqapplication.contactBook.model.User;
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2017/9/15.
 */

public class ContactChildFragment extends BaseSimpleFragment {

    @BindView(R.id.tag_layout)
    TagContainerLayout mTagLayout;
    @BindView(R.id.departmentTitle)
    TextView mDepartmentTitle;
    @BindView(R.id.departmentListView)
    RecyclerView mDepartmentListView;
    @BindView(R.id.header)
    SuperTextView mHeader;
    @BindView(R.id.recycler_list)
    RecyclerView mRecyclerList;
    @BindView(R.id.result_view)
    FrameLayout mResultView;

    private int fragmentType;
    private UnitAdapter unitAdapter;
    private ContactItemAdapter itemAdapter;

    private ArrayList<String> strings;
    private OnClickListener listener;
    private HashMap<String, Level> map;

    public interface OnClickListener {
        void onClick(User user);
    }

    public void setFragmentType(int fragmentType) {
        this.fragmentType = fragmentType;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public static ContactChildFragment newInstance(int type, OnClickListener listener) {
        ContactChildFragment fragment = new ContactChildFragment();
        fragment.setFragmentType(type);
        fragment.setListener(listener);
        return fragment;
    }


    @Override
    public void setResId() {
        this.resId = R.layout.fragment_contact_child;
    }

    @Override
    protected void initView() {
        FlexboxLayoutManager layoutManager1 = new FlexboxLayoutManager(getContext());
        layoutManager1.setFlexDirection(FlexDirection.ROW);
        layoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        mDepartmentListView.setLayoutManager(layoutManager1);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void initData() {
        init();
        unitAdapter = new UnitAdapter(R.layout.item_unit_adapter);
        switch (fragmentType) {
            case 0://市级
                unitAdapter.setNewData(getData(0, -1, -1, -1));
                mDepartmentTitle.setText("市级单位");
                mDepartmentTitle.setBackgroundColor(getResources().getColor(R.color.color_type_1));
                break;
            case 1://区级
                unitAdapter.setNewData(getData());
                mDepartmentTitle.setText("区级单位");
                mDepartmentTitle.setBackgroundColor(getResources().getColor(R.color.color_type_2));
                break;
            case 2://企业
                unitAdapter.setNewData(getData(2, -1, -1, -1));
                mDepartmentTitle.setText("企业单位");
                mDepartmentTitle.setBackgroundColor(getResources().getColor(R.color.color_type_0));
                break;
        }
        itemAdapter = new ContactItemAdapter(R.layout.item_contact_view);
        mRecyclerList.setAdapter(itemAdapter);
        itemAdapter.bindToRecyclerView(mRecyclerList);
        mDepartmentListView.setAdapter(unitAdapter);
        setListener();
    }


    private void init() {
        map = new HashMap<>(5);
        mTagLayout.removeAllTags();
        setGone(mResultView);
        strings = new ArrayList<>();
        mTagLayout.setTags(strings);
        setVisible(mDepartmentTitle);
        setVisible(mDepartmentListView);
    }

    protected void setListener() {
        itemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                listener.onClick((User) adapter.getItem(position));
            }
        });
        setDisposable();
    }

    private void filterData(Level level) {
        if (level != null && level.getLevel() != -1) {
            if (level.getKind1() == 0) {
                unitAdapter.setNewData(getData(fragmentType, -1, -1, -1, level.getQuxian()));
            } else if (level.getKind2() == 0) {
                unitAdapter.setNewData(getData(fragmentType, level.getKind1(), -1, -1, level.getQuxian()));
            } else if (level.getKind3() == 0) {
                unitAdapter.setNewData(getData(fragmentType, level.getKind1(), level.getKind2(), -1, level.getQuxian()));
            } else {
                unitAdapter.setNewData(getData(fragmentType, level.getKind1(), level.getKind2(), level.getKind3(), level.getQuxian()));
            }
            if (!map.containsKey(level.getDepartmentnamea())) {
                map.put(level.getDepartmentnamea(), level);
                setStrings(level.getDepartmentnamea());
            } else {
                int index = strings.indexOf(level.getDepartmentnamea())+1;
                    for (int i = index; i < strings.size(); i++) {
                        map.remove(strings.get(i));
                    }
                setStrings(null);

            }
            mHeader.setLeftString(level.getDepartmentnamea());
            mDepartmentTitle.setText(level.getDepartmentnamea());
        } else {
            switch (fragmentType) {
                case 0:
                    unitAdapter.setNewData(getData(0, -1, -1, -1));
                    mDepartmentTitle.setText("市级单位");
                    break;
                case 1:
                    unitAdapter.setNewData(getData());
                    mDepartmentTitle.setText("区级单位");
                    break;
                case 2:
                    unitAdapter.setNewData(getData(2, -1, -1, -1));
                    mDepartmentTitle.setText("企业单位");
                    break;
            }
            mDepartmentTitle.setVisibility(View.VISIBLE);
            strings.clear();
            mTagLayout.removeAllTags();
            map.clear();
        }
    }

    private void setDataAndView(List<User> users) {
        if (users.size() > 0 || strings.size() > 0) {
            setVisible(mResultView);
            itemAdapter.setNewData(users);
//            changeList(model.getDepartmentNameA());
            mHeader.setRightString(itemAdapter.getData().size() + "");
        } else {
            setGone(mResultView);
        }
    }

    private void setDisposable() {
        disposable = Observable.create(new ObservableOnSubscribe<Level>() {
            @Override
            public void subscribe(ObservableEmitter<Level> emitter) throws Exception {
                unitAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        Level level = (Level) adapter.getItem(position);
                        emitter.onNext(level);

                    }
                });
                mTagLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
                    @Override
                    public void onTagClick(int position, String text) {
                        if (position != 0) {
                            if (position != strings.size() - 1) {
                                emitter.onNext(map.get(text));
                            }
                        } else {
                            emitter.onNext(new Level(-1));
                        }
                    }

                    @Override
                    public void onTagLongClick(int position, String text) {

                    }

                    @Override
                    public void onSelectedTagDrag(int position, String text) {

                    }

                    @Override
                    public void onTagCrossClick(int position) {

                    }
                });
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Level>() {
                    @Override
                    public void accept(Level level) throws Exception {
                        filterData(level);
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<Level, List<User>>() {
                    @Override
                    public List<User> apply(Level level) throws Exception {
                        return getUserData(level);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        setDataAndView(users);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private List<Level> getData(int level, int kind1, int kind2, int kind3, String quxian) {
        if (quxian.equals("0")) {
            quxian = "";
        }
        return ContactDataUtil.queryLevel(level, kind1, kind2, kind3, quxian);
    }

    private List<Level> getData(int level, int kind1, int kind2, int kind3) {
        return ContactDataUtil.queryLevel(level, kind1, kind2, kind3, "");
    }

    private List<Level> getData() {
        return ContactDataUtil.getAllDistractName();
    }

    private List<User> getUserData(Level level) {
        return ContactDataUtil.queryUser(level);
    }

    private void changeList(String s) {
        if (s != null) {
            setHeaderView(s, String.valueOf(itemAdapter.getItemCount()));
        }
    }

    private void setStrings(String s) {
        if (s == null) {
            if (strings.size() > 0) {
                strings.remove(strings.size() - 1);
            }
        } else {
            if (strings.size() == 0) {
                strings.add(mDepartmentTitle.getText().toString());
                mDepartmentTitle.setVisibility(View.GONE);
            }
            strings.add(s);
        }
        mTagLayout.setTags(strings);
    }

    private void setGone(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }

    }

    private void setVisible(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    private void setHeaderView(String name, String num) {
        mHeader.setLeftString(name);
        mHeader.setRightString(num);
    }

}
