package com.bkjcb.rqapplication.userRecord.adapter;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.userRecord.model.ReviewRecord;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.vipulasri.timelineview.TimelineView;

public class TimeLineAdapter extends BaseQuickAdapter<ReviewRecord, BaseViewHolder> {
    //private Drawable drawable;

    public TimeLineAdapter(int layoutResId) {
        super(layoutResId);
        // drawable = MyApplication.getContext().getDrawable(R.drawable.ic_marker_active);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReviewRecord item) {
            helper.setText(R.id.timeline_time, item.jianchariqi)
                    .setText(R.id.timeline_people, item.jianchadanwei)
                    .setText(R.id.timeline_status, item.jianchajieguo);
            TimelineView mTimelineView = helper.getView(R.id.timeline);
            mTimelineView.initLine(getItemViewType(helper.getLayoutPosition()));
    }

    @Override
    public int getItemViewType(int position) {
        return getData().size() > 0 ? TimelineView.getTimeLineViewType(position, getItemCount()) : super.getItemViewType(position);
    }
}
