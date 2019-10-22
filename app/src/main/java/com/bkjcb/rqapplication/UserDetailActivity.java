package com.bkjcb.rqapplication;

/**
 * Created by DengShuai on 2019/10/22.
 * Description :
 */
public class UserDetailActivity extends SimpleBaseActivity {
    @Override
    protected int setLayoutID() {
        return R.layout.activity_user;
    }

    @Override
    protected void initView() {
        super.initView();
        initTopbar("用户详情");
    }
}
