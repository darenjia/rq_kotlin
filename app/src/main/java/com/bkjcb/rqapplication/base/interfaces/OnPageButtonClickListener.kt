package com.bkjcb.rqapplication.base.interfaces

import com.bkjcb.rqapplication.base.model.MediaFile

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
interface OnPageButtonClickListener<T> {
    fun onClick(userInfo: T)
    fun onNext(list: List<MediaFile>?)
}