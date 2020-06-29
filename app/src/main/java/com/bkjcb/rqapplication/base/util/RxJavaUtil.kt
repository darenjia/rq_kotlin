package com.bkjcb.rqapplication.base.util

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by DengShuai on 2020/5/28.
 * Description :
 */
object RxJavaUtil {
    fun <UD> getObservableTransformer(): ObservableTransformer<UD, UD> {
        return ObservableTransformer { upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }
}