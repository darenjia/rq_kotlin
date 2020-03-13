package com.bkjcb.rqapplication;

import android.content.Context;
import android.content.Intent;

import com.bkjcb.rqapplication.fragment.ContactChildFragment;
import com.bkjcb.rqapplication.model.User;
import com.hss01248.dialog.StyledDialog;

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
public class ContactDetailActivity extends ContactActivity {

    @Override
    protected void initView() {
        StyledDialog.init(this);
        initTopbar("应急通讯录");
        ContactChildFragment mainFragment = ContactChildFragment.newInstance(getType(), new ContactChildFragment.OnClickListener() {
            @Override
            public void onClick(User user) {
                alertUserInfo(user);
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.contact_content, mainFragment)
                .commit();
    }

    @Override
    protected void initData() {
    }

    public static void ToActivity(Context context, int type) {
        Intent intent = new Intent(context, ContactDetailActivity.class);
        intent.putExtra("data", type);
        context.startActivity(intent);
    }

    private int getType() {
        return getIntent().getIntExtra("data", 0);
    }

}
