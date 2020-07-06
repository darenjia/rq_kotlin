package com.bkjcb.rqapplication.base.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.model.BottleSaleCheck;
import com.hss01248.dialog.adapter.SuperLvHolder;

/**
 * Created by DengShuai on 2020/6/23.
 * Description :
 */
public class MyDialogViewHolder extends SuperLvHolder<BottleSaleCheck> {
    int type = 0;
    TextView infoGas;
    TextView infoTime;
    TextView infoPerson;
    TextView infoId;
    TextView checkTime;
    TextView checkResult;
    TextView checkPerson;
    TextView checkRemark;
    View type1;
    View type2;

    public MyDialogViewHolder(Context context) {
        super(context);
    }

    public MyDialogViewHolder(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    protected void findViews() {
        infoGas = rootView.findViewById(R.id.info_gas);
        infoTime = rootView.findViewById(R.id.info_time);
        infoPerson = rootView.findViewById(R.id.info_person);
        infoId = rootView.findViewById(R.id.info_id);
        checkTime = rootView.findViewById(R.id.info_check_time);
        checkPerson = rootView.findViewById(R.id.info_check_person);
        checkRemark = rootView.findViewById(R.id.info_check_remark);
        checkResult = rootView.findViewById(R.id.info_check_result);
        type1 = rootView.findViewById(R.id.layout_type1);
        type2 = rootView.findViewById(R.id.layout_type2);
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.view_alert_gas_info;
    }

    @Override
    public void assingDatasAndEvents(Context context, @Nullable BottleSaleCheck bean) {
        if (type == 0) {
            type2.setVisibility(View.GONE);
            infoGas.setText(bean.getUnitName());
            infoTime.setText(bean.getSaleTime());
            infoPerson.setText(bean.getCheckPeople());
            infoId.setText(bean.getBottleCode());
        } else {
            type1.setVisibility(View.GONE);
            checkResult.setText(bean.getCheckState());
            checkRemark.setText(bean.getCheckRemark());
            checkPerson.setText(bean.getCheckPeople());
            checkTime.setText(bean.getSaleTime());
        }
    }
}
