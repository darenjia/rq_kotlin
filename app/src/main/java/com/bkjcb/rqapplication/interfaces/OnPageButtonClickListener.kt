package com.bkjcb.rqapplication.interfaces

import com.bkjcb.rqapplication.model.MediaFile

/**
 * Created by DengShuai on 2020/4/29.
 * Description :
 */
interface OnPageButtonClickListener<T> {
    fun onClick(userInfo: T)
    fun onNext(list: List<MediaFile>?)
}