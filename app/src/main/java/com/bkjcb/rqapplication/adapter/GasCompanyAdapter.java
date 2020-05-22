package com.bkjcb.rqapplication.adapter;

import android.content.Context;

import com.bkjcb.rqapplication.model.GasCompanyResult;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;

import java.util.List;

/**
 * Created by DengShuai on 2020/4/30.
 * Description :
 */
public class GasCompanyAdapter extends MaterialSpinnerAdapter<GasCompanyResult.GasCompany> {
    public GasCompanyAdapter(Context context, List<GasCompanyResult.GasCompany> items) {
        super(context, items);
    }

    @Override
    public String getItemText(int position) {
        return getItem(position).getName();
    }

    @Override
    public String getShowText(int position) {
        return get(position).getName();
    }
}
