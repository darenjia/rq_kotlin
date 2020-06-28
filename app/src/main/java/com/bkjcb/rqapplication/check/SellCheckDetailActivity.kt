package com.bkjcb.rqapplication.check

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.bkjcb.rqapplication.check.model.ApplianceCheckContentItem
import com.bkjcb.rqapplication.check.model.ApplianceCheckResult
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.check.retrofit.ApplianceCheckService
import com.bkjcb.rqapplication.retrofit.NetworkApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
class SellCheckDetailActivity : ApplianceCheckDetailActivity() {
     override fun initDataFromNet() {
            disposable = NetworkApi.getService(ApplianceCheckService::class.java)
                    .saleCheckItem()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        if (result.pushState == 200 && result.datas != null && result.datas!!.size > 0) {
                            saveCheckContent(result.datas!!)
                            initCheckData(result.datas!!)
                            initImageListView()
                            hideEmptyView()
                        } else {
                            getDateFail(result.pushMsg)
                        }
                    }, { throwable -> getDateFail(throwable.message) })
        }

    override fun createFragment(item: ApplianceCheckContentItem?, id: String?): Fragment? {
        return super.createFragment(item, id)
    }

    companion object {
         fun toActivity(context: Context, checkItem: CheckItem?) {
            val intent = Intent(context, SellCheckDetailActivity::class.java)
            intent.putExtra("data", checkItem)
            context.startActivity(intent)
        }

         fun toActivity(context: Context, id: Long) {
            val intent = Intent(context, SellCheckDetailActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }
}