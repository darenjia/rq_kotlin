package com.bkjcb.rqapplication.base.fragment

import android.support.v4.app.Fragment
import io.reactivex.disposables.Disposable

/**
 * Created by DengShuai on 2019/2/19.
 * Description :
 */
open class BaseFragment : Fragment() {
    @JvmField
    protected var disposable: Disposable? = null
    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable != null && !disposable!!.isDisposed) {
            disposable?.dispose()
        }
    }
}