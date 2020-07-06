package com.bkjcb.rqapplication.adapter;

import android.text.TextUtils;
import android.view.View;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.model.GasStatisticData;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.zagum.expandicon.ExpandIconView;

import java.util.List;

/**
 * Created by DengShuai on 2020/6/30.
 * Description :
 */
public class GasStatisticAdapter extends BaseMultiItemQuickAdapter<GasStatisticData, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public GasStatisticAdapter(List<GasStatisticData> data) {
        super(data);
        addItemType(GasStatisticData.DISTRICT_TYPE, R.layout.item_gas_statistic_district);
        addItemType(GasStatisticData.STREET_TYPE, R.layout.item_gas_statistic_street);
    }

    @Override
    protected void convert(BaseViewHolder helper, GasStatisticData item) {
        switch (helper.getItemViewType()) {
            case GasStatisticData.DISTRICT_TYPE:
                helper.setText(R.id.district_name, item.getName())
                        .setText(R.id.number_all, getText(item.getGs()))
                        .setText(R.id.number_type1, getText(item.getTiaoyafa()))
                        .setText(R.id.number_type2, getText(item.getXihuobaohu()));
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        ExpandIconView view = helper.getView(R.id.expand_icon);
                        if (item.isExpanded()) {
                            collapse(pos);
                            view.setState(ExpandIconView.LESS, true);
                        } else {
                            expand(pos);
                            view.setState(ExpandIconView.MORE, true);
                        }
                    }
                });
                break;
            case GasStatisticData.STREET_TYPE:
                helper.setText(R.id.street_name, item.getName())
                        .setText(R.id.number_all, getText(item.getGs()))
                        .setText(R.id.number_type1, getText(item.getTiaoyafa()))
                        .setText(R.id.number_type2, getText(item.getXihuobaohu()));
                break;
            default:
        }
    }

    private String getText(String s) {
        return TextUtils.isEmpty(s) ? "0" : s;
    }
}
