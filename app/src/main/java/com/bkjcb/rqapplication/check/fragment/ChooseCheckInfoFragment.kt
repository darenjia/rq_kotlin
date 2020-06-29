package com.bkjcb.rqapplication.check.fragment

import android.text.TextUtils
import android.view.View
import butterknife.OnClick
import com.bkjcb.rqapplication.MyApplication
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.check.model.CheckItem
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment
import com.bkjcb.rqapplication.base.util.Utils.dateFormat
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.fragment_check_info_view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by DengShuai on 2019/12/30.
 * Description :
 */
open class ChooseCheckInfoFragment : BaseSimpleFragment(), DatePickerDialog.OnDateSetListener, View.OnClickListener {


    var checkItem: CheckItem? = null
    private var pickerDialog: DatePickerDialog? = null
    private lateinit var calendar: Calendar
    private lateinit var strings: ArrayList<String>

    override fun onClick(v: View) {
        when (v.id) {
            R.id.info_date -> createDatePicker()
            R.id.info_confirm -> {
                /* if (mInfoName.getText().length() == 0) {
                    Toast.makeText(context, "请填写检查人员姓名", Toast.LENGTH_SHORT).show();
                    return;
                }*/checkItem!!.checkMan = info_name.text.toString()
                checkItem!!.jianchariqi = info_date.text.toString()
                checkItem!!.year = strings[info_year.selectedIndex]
                checkItem!!.systime = calendar.timeInMillis
                checkItem!!.c_id = UUID.randomUUID().toString().replace("-", "")
                listener?.choose()
            }
            R.id.info_back -> listener!!.back()
            else -> {
            }
        }
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        info_date.text = "$year-${monthOfYear + 1}-$dayOfMonth"
    }

    interface OnChooseListener {
        fun choose()
        fun back()
    }

    private var listener: OnChooseListener? = null
    fun setListener(listener: OnChooseListener?) {
        this.listener = listener
    }


    override fun initResID() = R.layout.fragment_check_info_view

    override fun initView() {
        strings = ArrayList()
        strings.add("2020")
        strings.add("2019")
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))
        info_year.setAdapter(MaterialSpinnerAdapter(context, strings))
        info_year.text = dateFormat("", calendar.time)
        info_name.text = MyApplication.getUser().real_name
        if (checkItem != null) {
            if (!TextUtils.isEmpty(checkItem!!.jianchariqi)) {
                info_date.text = checkItem!!.jianchariqi
                try {
                    calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE).parse(checkItem!!.jianchariqi)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
            if (!TextUtils.isEmpty(checkItem!!.checkMan)) {
                info_name.text = checkItem!!.checkMan
            }
            if (!TextUtils.isEmpty(checkItem!!.year)) {
                var positon = 0
                for (i in strings.indices) {
                    if (strings[i] == checkItem!!.year) {
                        positon = i
                        break
                    }
                }
                info_year.selectedIndex = positon
            }
        }
        //@OnClick(R.id.info_date, R.id.info_confirm, R.id.info_back)
        info_date.setOnClickListener(this)
        info_confirm.setOnClickListener(this)
        info_back.setOnClickListener(this)
    }

    override fun initData() {}

    override fun onStart() {
        super.onStart()
        updateInfo()
    }

    private fun updateInfo() {
        if (checkItem != null) {
            info_type.text = checkItem!!.zhandianleixing
            info_station.text = checkItem!!.beijiandanwei
            if (checkItem!!.type == 1) {
                info_type_title.text = "企业类型"
                info_name_title.text = "企业名称"
                info_year_layout.visibility = View.GONE
            }
        }
    }

    private fun createDatePicker() {
        if (pickerDialog == null) {
            pickerDialog = DatePickerDialog.newInstance(
                    this,
                    calendar[Calendar.YEAR],  // Initial year selection
                    calendar[Calendar.MONTH],  // Initial month selection
                    calendar[Calendar.DAY_OF_MONTH] // Inital day selection
            )
        }
        if (!pickerDialog!!.isAdded) {
            pickerDialog!!.show(activity!!.fragmentManager, "DatePickerDialog")
        }
    }

    companion object {
        fun newInstance(listener: OnChooseListener?): ChooseCheckInfoFragment {
            val fragment = ChooseCheckInfoFragment()
            fragment.setListener(listener)
            return fragment
        }
    }
}