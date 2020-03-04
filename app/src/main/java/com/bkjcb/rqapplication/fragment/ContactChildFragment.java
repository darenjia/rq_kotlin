package com.bkjcb.rqapplication.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.adapter.ContactItemAdapter;
import com.bkjcb.rqapplication.adapter.UnitAdapter;
import com.bkjcb.rqapplication.datebase.ContactDataUtil;
import com.bkjcb.rqapplication.model.Level;
import com.bkjcb.rqapplication.model.User;
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

/**
 * Created by DengShuai on 2017/9/15.
 */

public class ContactChildFragment extends BaseSimpleFragment implements View.OnClickListener {

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

    private int layer;
    private ArrayList<String> strings;
    private List<User> userList;
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
        mDepartmentListView.setAdapter(unitAdapter);
        setListener();
    }


    private void init() {
        map = new HashMap<>(5);
        mTagLayout.removeAllTags();
        setGone(mResultView);
        userList = null;
        layer = 0;
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
        unitAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Level level = (Level) adapter.getItem(position);
                filterData(level);
            }
        });
        mTagLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                if (position != strings.size() - 1) {
                    filterData(map.get(text));
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

    private void filterData(Level level) {
        if (level != null) {
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
                setStrings(null);
            }
            setDataAndView(level);

        }
    }

    private void setDataAndView(final Level model) {
        userList = getUserData(model);
        if (userList.size() > 0 || unitAdapter.getItemCount() == 0) {
            setVisible(mResultView);
            itemAdapter.setNewData(userList);
//            changeList(model.getDepartmentNameA());
            mHeader.setLeftString(model.getDepartmentnamea());
            mHeader.setRightString(itemAdapter.getItemCount() + "");
        } else {
            setGone(mResultView);
        }
        if (unitAdapter.getItemCount() == 0) {
            setGone(mDepartmentListView, mDepartmentTitle);
        } else {
            mDepartmentTitle.setText(model.getDepartmentnamea());
            setVisible(mDepartmentListView, mDepartmentTitle);
        }
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


    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.ret_btn:
                if (layer == 0) {
                    return;
                } else if (layer == 1) {
                    if (recyclerId == 2) {
                        adapter.setData(getData(2, -1, -1, -1, -1));
                    } else if (recyclerId == 0) {
                        adapter.setData(getData(0, -1, -1, -1, -1));
                    } else {
                        adapter.setData(getData());
                    }
                    layer = 0;
                } else if (layer == 2) {
                    if (recyclerId == 2) {
                        adapter.setData(getData(2, flagLevel.getKind1(), -1, -1, -1));
                    } else if (recyclerId == 0) {
                        adapter.setData(getData(0, flagLevel.getKind1(), -1, -1, -1));
                    } else {
                        adapter.setData(getData(1, -1, -1, -1, flagLevel.getQuxian()));
                    }
                    layer = 1;
                } else if (layer == 3) {
                    if (recyclerId == 1) {
                        adapter.setData(getData(1, lastLevel.getKind1(), -1, -1, lastLevel.getQuxian()));
                    }
                    layer = 2;
                } else if (layer == 4) {

                    adapter.setData(getData(1, lastLevel.getKind1(), lastLevel.getKind2(), -1, lastLevel.getQuxian()));
                    layer = 3;

                }
                if (flagLevel.getId() != lastLevel.getId()) {
                    flagLevel = lastLevel;
                } else {
                    lastLevel.setId(-1);
                }
                setDataAndView(lastLevel);
                if (layer == 0) {
                    btn_return.setBackgroundResource(R.color.black_3);
                }
                setStrings(null);
                break;
        }*/
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
            if (strings.size() > 0) {
                String newTitle = strings.get(strings.size() - 1);
                mDepartmentTitle.setText(newTitle);
            } else {
                switch (fragmentType) {
                    case 0:
                        mDepartmentTitle.setText("市级单位");
                        break;
                    case 1:
                        mDepartmentTitle.setText("区级单位");
                        break;
                    case 2:
                        mDepartmentTitle.setText("企业单位");
                        break;
                }
            }
            mTagLayout.removeTag(mTagLayout.getTags().size() - 1);
        } else {
            strings.add(s);
            mTagLayout.addTag(s);
        }
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
