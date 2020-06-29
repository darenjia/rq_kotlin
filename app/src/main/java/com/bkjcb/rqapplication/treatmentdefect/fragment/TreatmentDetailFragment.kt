package com.bkjcb.rqapplication.treatmentdefect.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.fragment.BaseSimpleFragment
import com.bkjcb.rqapplication.retrofit.DataService
import com.bkjcb.rqapplication.retrofit.NetworkApi
import com.bkjcb.rqapplication.treatmentdefect.TreatmentDefectActivity
import com.bkjcb.rqapplication.treatmentdefect.adapter.OrderListAdapter
import com.bkjcb.rqapplication.treatmentdefect.adapter.SecurityCheckListAdapter
import com.bkjcb.rqapplication.treatmentdefect.model.BottleSaleCheck
import com.bkjcb.rqapplication.treatmentdefect.model.DefectTreatmentModel
import com.bkjcb.rqapplication.view.MyDialogViewHolder
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hss01248.dialog.StyledDialog
import com.hss01248.dialog.config.ConfigBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_treatment_detail.*

/**
 * Created by DengShuai on 2020/6/12.
 * Description :
 */
class TreatmentDetailFragment : BaseSimpleFragment() {

    private var model: DefectTreatmentModel? = null
    private lateinit var checkAdapter: SecurityCheckListAdapter
    private lateinit var orderAdapter: OrderListAdapter
    private var configBean1: ConfigBean? = null
    private var configBean2: ConfigBean? = null
    fun setModel(model: DefectTreatmentModel?) {
        this.model = model
    }

    override fun initView() {
        StyledDialog.init(context)
        if (model != null) {
            info_result.text = model!!.processTime
            info_accident_type.text = model!!.casesType
            info_opinion.text = model!!.opinions
            info_type.text = model!!.userCode
            info_station.text = model!!.userName
            info_year.text = if (model!!.userType == 0) "居民" else "非居民"
            info_name.text = model!!.qu
            info_date.text = model!!.userAddress
            info_check_list.layoutManager = LinearLayoutManager(context)
            checkAdapter = SecurityCheckListAdapter(R.layout.item_security_check_view)
            info_check_list.adapter = checkAdapter
            checkAdapter.bindToRecyclerView(info_check_list)
            checkAdapter.showLoading()
            checkAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> showDetail(adapter.getItem(position) as BottleSaleCheck?, 1) }
            info_distribution_list.layoutManager = LinearLayoutManager(context)
            orderAdapter = OrderListAdapter(R.layout.item_order_view)
            info_distribution_list.adapter = orderAdapter
            orderAdapter.bindToRecyclerView(info_distribution_list)
            orderAdapter.showLoading()
            orderAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position -> showDetail(adapter.getItem(position) as BottleSaleCheck?, 0) }
            if (model!!.flag > 0) {
                showFinishBtn()
            }
        }
    }

    override fun initData() {
        obtainBaseInfo()
    }

    private fun obtainBaseInfo() {
            disposable = NetworkApi.getService(DataService::class.java)
                    .getBottleData(model!!.mbuGuid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result ->
                        if (result.pushState == 200) {
                            setCheckData(result.datas.bottleSaleChecks)
                            //setOrderData(result.datas.bottleSaleChecks)
                        } else {
                            showError()
                        }
                    }) { showError() }
        }

    private fun setOrderData(list: List<BottleSaleCheck>?) {
        if (list != null && list.isNotEmpty()) {
            orderAdapter.setNewData(list)
        } else {
            orderAdapter.showEmpty()
        }
    }

    private fun setCheckData(list: List<BottleSaleCheck>?) {
        if (list != null && list.isNotEmpty()) {
            checkAdapter.setNewData(list)
            orderAdapter.setNewData(list)
        } else {
            checkAdapter.showEmpty()
            orderAdapter.showEmpty()
        }
    }

    private fun showError() {
        checkAdapter.showError()
        orderAdapter.showError()
    }

    private fun showFinishBtn() {
        defect_detail.visibility = View.VISIBLE
        defect_detail.setOnClickListener { TreatmentDefectActivity.toActivity(activity, model) }
    }

    private fun showDetail(check: BottleSaleCheck?, type: Int) {
        if (type == 0) {
            if (configBean1 == null) {
                val holder1 = MyDialogViewHolder(context, 0)
                configBean1 = StyledDialog.buildCustomInIos(holder1, null).setBtnText("确定")
            }
            (configBean1!!.customContentHolder as MyDialogViewHolder).assingDatasAndEvents(context, check)
            configBean1!!.show()
        } else {
            if (configBean2 == null) {
                val holder2 = MyDialogViewHolder(context, 1)
                configBean2 = StyledDialog.buildCustomInIos(holder2, null).setBtnText("确定")
            }
            (configBean2!!.customContentHolder as MyDialogViewHolder).assingDatasAndEvents(context, check)
            configBean2!!.show()
        }
    }

    override fun initResID(): Int {
        return R.layout.fragment_treatment_detail
    }

    companion object {
        fun newInstance(model: DefectTreatmentModel?): TreatmentDetailFragment {
            val fragment = TreatmentDetailFragment()
            fragment.setModel(model)
            return fragment
        }
    }
}