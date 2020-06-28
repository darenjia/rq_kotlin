package com.bkjcb.rqapplication.view

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.util.Utils

object FooterView {
    fun createFooter(listener: View.OnClickListener): View {
        val view = ImageView(MyApplication.getContext())
        val width:Int =Utils.dip2px(MyApplication.getContext(), 120f)
        view.layoutParams = ViewGroup.LayoutParams(width, width)
        val padding = Utils.dip2px(MyApplication.getContext(), 5f)
        view.setPadding(padding, padding, padding, padding)
        view.setImageResource(R.drawable.icon_add_pic)
        view.setOnClickListener(listener)
        return view
    }
}