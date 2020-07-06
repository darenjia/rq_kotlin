package com.bkjcb.rqapplication.contactBook.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.contactBook.adapter.ContactItemAdapter;
import com.bkjcb.rqapplication.contactBook.database.ContactDataUtil;
import com.bkjcb.rqapplication.contactBook.model.User;
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.QMUIEmptyView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
public class ContactMainFragment extends BaseSimpleFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.station_search)
    ImageView mStationSearch;
    @BindView(R.id.station_name)
    EditText mStationName;
    @BindView(R.id.station_search_close)
    ImageView mStationSearchClose;
    @BindView(R.id.contact_list)
    RecyclerView mContactList;
    @BindView(R.id.empty_view)
    QMUIEmptyView emptyView;
    private ContactItemAdapter adapter;
    OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (listener!=null){
            listener.onClick((User) adapter.getItem(position));
        }
    }

    public interface OnClickListener{
        void onClick(User user);
    }
    @Override
    public void setResId() {
        resId = R.layout.fragment_contact_main;
    }

    @Override
    protected void initView() {
        emptyView.show(true, "请稍后", "正在初始化数据", null, null);
        adapter = new ContactItemAdapter(R.layout.item_contact_view);
        mContactList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactList.setAdapter(adapter);
        adapter.bindToRecyclerView(mContactList);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        disposable = Observable.create(new ObservableOnSubscribe<List<User>>() {

            @Override
            public void subscribe(ObservableEmitter<List<User>> emitter) throws Exception {
                ContactDataUtil.init(getContext());
                emitter.onNext(ContactDataUtil.getAllUsers());
            }
        }).subscribeOn(Schedulers.computation())
                .map(new Function<List<User>, List<User>>() {
                    @Override
                    public List<User> apply(List<User> list) throws Exception {
                        for (User user : list) {
                            user.init();
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> list) throws Exception {
                        emptyView.hide();
                        adapter.setNewData(list);
                    }
                });
    }
}
