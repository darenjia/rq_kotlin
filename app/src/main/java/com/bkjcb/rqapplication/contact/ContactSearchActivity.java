package com.bkjcb.rqapplication.contact;

import android.content.Context;
import android.content.Intent;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.contact.fragment.ContactSearchFragment;
import com.bkjcb.rqapplication.contact.model.User;
import com.hss01248.dialog.StyledDialog;

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
public class ContactSearchActivity extends ContactActivity {

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_contact;
    }

    @Override
    protected void initView() {
        StyledDialog.init(this);
        initTopbar("搜索");
    }

    @Override
    protected void initData() {
        ContactSearchFragment mainFragment = new ContactSearchFragment();
        mainFragment.setListener(new ContactSearchFragment.OnClickListener() {
            @Override
            public void onClick(User user) {
                alertUserInfo(user);
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.contact_content, mainFragment).commit();
    }

    public static void ToActivity(Context context) {
        Intent intent = new Intent(context, ContactSearchActivity.class);
        context.startActivity(intent);
    }

}
