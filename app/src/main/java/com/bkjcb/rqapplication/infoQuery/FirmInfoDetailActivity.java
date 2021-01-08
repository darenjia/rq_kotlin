package com.bkjcb.rqapplication.infoQuery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.infoQuery.fragment.AlarmFirmInfoFragment;
import com.bkjcb.rqapplication.infoQuery.fragment.BusinessFirmInfoFragment;
import com.bkjcb.rqapplication.infoQuery.fragment.InstallationFirmInfoFragment;
import com.bkjcb.rqapplication.infoQuery.fragment.SellFirmInfoFragment;
import com.bkjcb.rqapplication.infoQuery.model.BaseFirmModel;

/**
 * Created by DengShuai on 2020/12/30.
 * Description :
 */
public class FirmInfoDetailActivity extends SimpleBaseActivity {

    @Override
    protected int setLayoutID() {
        return R.layout.activity_create_check_task;
    }

    @Override
    protected void initView() {

        BaseFirmModel model = (BaseFirmModel) getIntent().getSerializableExtra("code");
        initTopbar(model.name);
        Fragment fragment = null;
        switch (model.type) {
            case 0:
                fragment= BusinessFirmInfoFragment.newInstance(model.cid);
                break;
            case 1:
                fragment= InstallationFirmInfoFragment.newInstance(model.cid);
                break;

            case 2:
                fragment= AlarmFirmInfoFragment.newInstance(model.cid);
                break;
            default:
                fragment= SellFirmInfoFragment.newInstance(model.cid);
        }
        initFragment(fragment);


    }

    private void initFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.create_task_content, fragment)
                .commit();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    public static void ToActivity(Context context, BaseFirmModel model) {
        Intent intent = new Intent(context, FirmInfoDetailActivity.class);
        intent.putExtra("code", model);
        context.startActivity(intent);
    }
}
