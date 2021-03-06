package com.bkjcb.rqapplication.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
abstract class BaseSimpleFragment : BaseFragment() {
    var contentView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (view == null) {
            contentView = View.inflate(activity, initResID(), null)
        }
        initView()
        initData()
        return view
    }

    abstract fun initResID(): Int
    open fun initView() {}
    open fun initData() {}
}