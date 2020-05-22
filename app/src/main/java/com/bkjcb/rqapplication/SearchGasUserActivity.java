package com.bkjcb.rqapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bkjcb.rqapplication.fragment.GasUserSearchFragment;
import com.bkjcb.rqapplication.interfaces.OnPageButtonClickListener;
import com.bkjcb.rqapplication.model.UserInfoResult;

import java.util.List;

/**
 * Created by DengShuai on 2020/5/12.
 * Description :
 */
public class SearchGasUserActivity extends SimpleBaseActivity {
    public static int CHOOSE_USER = 123;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_gas_user_add;
    }

    @Override
    protected void initView() {
        initTopbar("选择燃气用户");
    }

    @Override
    protected void initData() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout, GasUserSearchFragment.newInstance(new OnPageButtonClickListener<UserInfoResult.UserInfo>() {
                    @Override
                    public void onClick(UserInfoResult.UserInfo userInfo) {
                        Intent intent = new Intent();
                        intent.putExtra("data", userInfo);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onNext(List list) {

                    }
                },true))
                .commit();
    }

    public static void toActivity(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), SearchGasUserActivity.class);
        fragment.startActivityForResult(intent, CHOOSE_USER);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
