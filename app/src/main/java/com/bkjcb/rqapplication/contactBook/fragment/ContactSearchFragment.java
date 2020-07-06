package com.bkjcb.rqapplication.contactBook.fragment;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bkjcb.rqapplication.contactBook.ContactDepartmentActivity;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.contactBook.adapter.ContactSearchResultAdapter;
import com.bkjcb.rqapplication.contactBook.database.ContactDataUtil;
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.stationCheck.model.ContactBaseModel;
import com.bkjcb.rqapplication.contactBook.model.Level;
import com.bkjcb.rqapplication.base.model.SearchKeyWord;
import com.bkjcb.rqapplication.contactBook.model.User;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
public class ContactSearchFragment extends BaseSimpleFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.station_search_type)
    MaterialSpinner mStationSearchType;
    @BindView(R.id.station_name)
    EditText mStationName;
    @BindView(R.id.station_search)
    ImageView mStationSearch;
    @BindView(R.id.contact_list)
    RecyclerView mContactList;
    @BindView(R.id.search_history_tag)
    TagContainerLayout mSearchTag;
    @BindView(R.id.search_history)
    LinearLayout mSearchHistory;
    @BindView(R.id.search_title)
    TextView mSearchTitle;
    private ContactSearchResultAdapter adapter;
    OnClickListener listener;
    private boolean isSearchName = false;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (listener != null) {
            if (isSearchName) {
                listener.onClick((User) adapter.getItem(position));
            } else {
                ContactDepartmentActivity.ToActivity(getContext(), ((Level) adapter.getItem(position)).getDepartmentname());
            }
        }
    }

    public interface OnClickListener {
        void onClick(User user);
    }

    @Override

    public void setResId() {
        resId = R.layout.fragment_contact_main;
    }

    @Override
    protected void initView() {
        adapter = new ContactSearchResultAdapter(R.layout.item_contact_view);
        mContactList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactList.setAdapter(adapter);
        adapter.bindToRecyclerView(mContactList);
        adapter.setOnItemClickListener(this);
        mStationSearchType.setAdapter(new MaterialSpinnerAdapter<>(getContext(), Arrays.asList("单位", "人名")));
        showTag();
    }


    private void showTag() {
        List<String> strings = new ArrayList<>();
        List<SearchKeyWord> words = SearchKeyWord.getAll();
        for (SearchKeyWord s :
                words) {
            strings.add(s.getKey());
        }
        mSearchTag.setTags(strings);
    }

    @Override
    protected void initData() {
        mStationSearchType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                isSearchName = !item.toString().equals("单位");
            }
        });
        disposable = Observable.merge(Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mStationSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emitter.onNext(mStationName.getText().toString());
                    }
                });
                mSearchTag.setOnTagClickListener(new TagView.OnTagClickListener() {
                    @Override
                    public void onTagClick(int position, String text) {
                        emitter.onNext(text);
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
        }), Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                mStationName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (v.getId() == R.id.station_name && actionId == EditorInfo.IME_ACTION_SEARCH) {
                            emitter.onNext(mStationName.getText().toString());
                        }
                        return false;
                    }
                });
            }
        }))
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.trim().length() > 0;
                    }
                }).debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .map(new Function<String, List<ContactBaseModel>>() {
                    @Override
                    public List<ContactBaseModel> apply(String s) throws Exception {
                        return filter(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ContactBaseModel>>() {
                    @Override
                    public void accept(List<ContactBaseModel> list) throws Exception {
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        showResult(list);
                    }
                });
    }

    private List<ContactBaseModel> filter(String s) {
        SearchKeyWord.save(s);
        List<ContactBaseModel> list = new ArrayList<>();
        if (isSearchName) {
            list.addAll(ContactDataUtil.queryUser(s));
        } else {
            list.addAll(ContactDataUtil.queryLevel(s));
        }
        return list;
    }

    private void showResult(List<ContactBaseModel> list) {
        mSearchTitle.setVisibility(View.VISIBLE);
        adapter.setNewData(list);
        mContactList.setVisibility(View.VISIBLE);
        mSearchHistory.setVisibility(View.GONE);
    }

}
