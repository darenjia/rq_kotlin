package com.bkjcb.rqapplication.emergency;

import android.content.Context;
import android.content.Intent;

import com.bkjcb.rqapplication.emergency.fragment.EmergencyFragment;
import com.bkjcb.rqapplication.treatment.DefectTreatmentMainActivity;

/**
 * Created by DengShuai on 2020/7/8.
 * Description :
 */
public class EmergencyMainActivity extends DefectTreatmentMainActivity {

    @Override
    protected void initView() {
        initTopbar("事故现场");
    }

    @Override
    public void addFragment() {
        fragments.add(EmergencyFragment.newInstance("处理中"));
        fragments.add(EmergencyFragment.newInstance("已销根"));
    }

    @Override
    public void initTabString() {
        addTab("处理中");
        addTab("已销根");
    }

    public static void ToActivity(Context context) {
        Intent intent = new Intent(context, EmergencyMainActivity.class);
        context.startActivity(intent);
    }
}
