package com.bkjcb.rqapplication.gascylindermanagement.adapter;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.gascylindermanagement.model.CirculationInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.vipulasri.timelineview.TimelineView;

public class CirculationAdapter extends BaseQuickAdapter<CirculationInfo.ReturnValueBean, BaseViewHolder> {
    //private Drawable drawable;

    public CirculationAdapter(int layoutResId) {
        super(layoutResId);
        // drawable = MyApplication.getContext().getDrawable(R.drawable.ic_marker_active);
    }

    @Override
    protected void convert(BaseViewHolder helper, CirculationInfo.ReturnValueBean item) {
            helper.setText(R.id.timeline_time, item.getCDateTime())
                    .setText(R.id.timeline_status, getTypeString(item.getCNumber()))
                    .setText(R.id.timeline_people, item.toString());
            TimelineView mTimelineView = helper.getView(R.id.timeline);
            mTimelineView.initLine(getItemViewType(helper.getLayoutPosition()));
    }

    @Override
    public int getItemViewType(int position) {
        return getData().size() > 0 ? TimelineView.getTimeLineViewType(position, getItemCount()) : super.getItemViewType(position);
    }

    private String getTypeString(int i){
        String typeStr="";
        switch (i){
            case 1:
                typeStr="充装环节-收瓶信息";
                break;
            case 2:
                typeStr="充装环节-充前检查";
                break;
            case 3:
                typeStr="充装环节-充装及充后";
                break;
            case 4:
                typeStr="充装环节-发瓶信息";
                break;
            case 5:
                typeStr="供应站环节-满瓶入库";
                break;
            case 6:
                typeStr="供应站环节-满瓶出库";
                break;
            case 7:
                typeStr="销售环节-满瓶销售";
                break;
            case 8:
                typeStr="销售环节-空瓶回收";
                break;
            case 9:
                typeStr="供应站环节-空瓶入库";
                break;
            case 10:
                typeStr="供应站环节-空瓶出库";
                break;
            default:
        }
        return typeStr;
    }
}
