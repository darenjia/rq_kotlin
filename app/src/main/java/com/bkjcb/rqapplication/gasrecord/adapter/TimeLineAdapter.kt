package com.bkjcb.rqapplication.gasrecord.adapter

import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.gasrecord.model.ReviewRecord
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.github.vipulasri.timelineview.TimelineView

class TimeLineAdapter(layoutResId: Int) : BaseQuickAdapter<ReviewRecord, BaseViewHolder>(layoutResId) {
    override fun convert(helper: BaseViewHolder, item: ReviewRecord) {
        helper.setText(R.id.timeline_time, item.jianchariqi)
                .setText(R.id.timeline_people, item.jianchadanwei)
                .setText(R.id.timeline_status, item.jianchajieguo)
        val mTimelineView = helper.getView<TimelineView>(R.id.timeline)
        mTimelineView.initLine(getItemViewType(helper.layoutPosition))
    }

    override fun getItemViewType(position: Int): Int {
        return if (data.size > 0) TimelineView.getTimeLineViewType(position, itemCount) else super.getItemViewType(position)
    }
}