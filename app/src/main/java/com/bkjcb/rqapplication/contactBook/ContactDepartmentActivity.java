package com.bkjcb.rqapplication.contactBook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.contactBook.adapter.ContactItemAdapter;
import com.bkjcb.rqapplication.contactBook.database.ContactDataUtil;
import com.bkjcb.rqapplication.contactBook.model.User;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.dialog.StyledDialog;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/3/31.
 * Description :
 */
public class ContactDepartmentActivity extends ContactActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.search_result_list)
    RecyclerView mResultView;
    private String keyWord;
    private ContactItemAdapter itemAdapter;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_contact_department;
    }

    @Override
    protected void initView() {
        StyledDialog.init(this);
        keyWord = getIntent().getStringExtra("Key");
        initTopbar(keyWord);
        mResultView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ContactItemAdapter(R.layout.item_contact_view);
        mResultView.setAdapter(itemAdapter);
    }

    @Override
    protected void initData() {
        itemAdapter.setOnItemClickListener(this);
        itemAdapter.setNewData(ContactDataUtil.queryUserByDepartment(keyWord));
    }

    public static void ToActivity(Context context, String key) {
        Intent intent = new Intent(context, ContactDepartmentActivity.class);
        intent.putExtra("Key", key);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        User user= (User) adapter.getItem(position);
        alertUserInfo(user);
    }
}
