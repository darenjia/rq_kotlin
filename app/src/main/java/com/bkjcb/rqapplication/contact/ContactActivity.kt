package com.bkjcb.rqapplication.contact

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.TextView
import cn.carbs.android.avatarimageview.library.AvatarImageView
import com.allen.library.SuperTextView
import com.allen.library.SuperTextView.OnRightImageViewClickListener
import com.bkjcb.rqapplication.BaseSimpleActivity
import com.bkjcb.rqapplication.R
import com.bkjcb.rqapplication.contact.fragment.ContactFirstFragment
import com.bkjcb.rqapplication.contact.model.User
import com.bkjcb.rqapplication.datebase.ContactDataUtil
import com.bkjcb.rqapplication.util.Utils
import com.hss01248.dialog.StyledDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.with_bg_top_bar.*
import java.util.regex.Pattern

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
open class ContactActivity : BaseSimpleActivity() {
    override fun setLayoutID(): Int {
        return R.layout.activity_main_contact
    }

    override fun initView() {
        StyledDialog.init(this)
        initTopBar("应急通讯录", appbar)
        /* ContactMainFragment mainFragment = new ContactMainFragment();
       mainFragment.setListener(new ContactMainFragment.OnClickListener() {
            @Override
            public void onClick(User user) {
                alertUserInfo(user);
            }
        });*/appbar.addRightImageButton(R.drawable.vector_drawable_search, R.id.top_right_button)
                .setOnClickListener { ContactSearchActivity.toActivity(this@ContactActivity) }
    }

    override fun initData() {
        disposable = Observable.create<Boolean> { emitter ->
            ContactDataUtil.init(this@ContactActivity)
            emitter.onNext(true)
        }.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { bool ->
                    if (bool) {
                        supportFragmentManager
                                .beginTransaction()
                                .add(R.id.contact_content, ContactFirstFragment.newInstance(object : ContactFirstFragment.OnClickListener {
                                    override fun onClick(tel: String?) {
                                        if (tel != null) {
                                            actionCall(tel)
                                        }
                                    }

                                }))
                                .commit()
                    }
                }
    }

    protected fun alertUserInfo(user: User) {
        StyledDialog.buildCustomBottomSheet(initDialogView(user))
                .setBackground(resources.getColor(R.color.colorAlpha))
                .setBottomSheetDialogMaxHeightPercent(0.4f).show()
    }

    private fun initDialogView(user: User): View {
        val view = layoutInflater.inflate(R.layout.alert_view, null)
        val name = view.findViewById<View>(R.id.name) as TextView
        val imageView = view.findViewById<View>(R.id.item_avatar) as AvatarImageView
        imageView.setTextAndColor(user.username!!.substring(0, 1), Utils.getRandomColor(this))
        val tel1 = view.findViewById<View>(R.id.phoneNumber1) as SuperTextView
        val tel2 = view.findViewById<View>(R.id.phoneNumber2) as SuperTextView
        val quxian = view.findViewById<View>(R.id.quxian) as SuperTextView
        val department = view.findViewById<View>(R.id.bumen) as SuperTextView
        val zhiwu = view.findViewById<View>(R.id.zhiwu) as SuperTextView
        val unit_address = view.findViewById<View>(R.id.dizhi_danwei) as SuperTextView
        val unit_phone = view.findViewById<View>(R.id.dianha_danwei) as SuperTextView
        val unit_fax = view.findViewById<View>(R.id.chuanzhen_danwei) as SuperTextView
        val unit_zipcode = view.findViewById<View>(R.id.youbian_danwei) as SuperTextView
        name.text = user.username
        val s1 = user.tel
        val s2 = user.u_tel
        val s3 = user.unit!!.tel
        if (!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)) {
            tel2.visibility = View.VISIBLE
            tel1.setLeftBottomString(s1)
            tel2.setLeftBottomString(s2)
        } else {
            if (TextUtils.isEmpty(s1)) {
                tel1.setLeftBottomString(s2)
            } else {
                tel1.setLeftBottomString(s1)
            }
        }
        val unitName = user.level!!.departmentnamea
        if (unitName!!.length > 16) {
            department.setRightString(unitName.substring(16, unitName.length))
            department.setRightTopString(unitName.substring(0, 16))
        } else {
            department.setRightString(unitName)
        }
        //        department.setRightBottomString(user.getDepartmentNameA());
        val address = user.unit!!.address
        /*if (address.length() > 16) {
            unit_address.setRightString(address.substring(16, address.length()));
            unit_address.setRightTopString(address.substring(0, 16));
        } else {
            unit_address.setRightString(user.getUnit().getAddress());
        }*/unit_address.setRightString(address)
        if (!TextUtils.isEmpty(user.duty)) {
            zhiwu.setRightBottomString(user.duty)
            zhiwu.visibility = View.VISIBLE
        }
        val qxName = user.unit!!.districtName
        if (!TextUtils.isEmpty(qxName)) {
            quxian.setRightBottomString(qxName)
            quxian.visibility = View.VISIBLE
        }
        if (!TextUtils.isEmpty(s3)) {
            val numbers = s3!!.split("、".toRegex()).toTypedArray()
            if (numbers.size > 1) {
                unit_phone.setCenterBottomString(numbers[1])
            }
            unit_phone.setRightBottomString(numbers[0])
            unit_phone.visibility = View.VISIBLE
        }
        if (!TextUtils.isEmpty(user.unit!!.fax)) {
            unit_fax.setRightBottomString(user.unit!!.fax)
            unit_fax.visibility = View.VISIBLE
        }
        if (!TextUtils.isEmpty(user.unit!!.zipcode)) {
            unit_zipcode.setRightBottomString(user.unit!!.zipcode)
            unit_zipcode.visibility = View.VISIBLE
        }
        val listener = OnRightImageViewClickListener {
            val number = (view as SuperTextView).leftBottomString
            actionCall(number)
        }
        val longClickListener = OnLongClickListener { //showPopBottom(v);
            true
        }
        tel1.setRightImageViewClickListener {
            val number = tel1.leftBottomString
            actionCall(number)
        }
        //tel1.setOnClickListener(listener);
        tel2.setRightImageViewClickListener {
            val number = tel2.leftBottomString
            actionCall(number)
        }
        //tel1.setOnLongClickListener(longClickListener);
        //tel2.setOnLongClickListener(longClickListener);
        unit_phone.setOnClickListener { v ->
            val number = (v as SuperTextView).rightBottomString
            actionCall(number)
        }
        department.setOnClickListener { /* viewPager.setCurrentItem(0);
                FirstFragment firstFragment = (FirstFragment) fragments.get(0);*/
            /*  Level level = new Level();
                level.setDepartmentNameA(unitName);
                level.setLevel(user.getLevel());
                level.setKind1(user.getKind1());
                level.setKind2(user.getKind2());
                level.setKind3(user.getKind3());
                level.setQuxian(user.getQuxin());
                AboutActivity.comeIn(level, MainActivity.this, 0, qxName);*/
        }
        return view
    }

    fun actionCall(number: String) {
        var phoneNumber = number.trim { it <= ' ' }
        val pattern = Pattern.compile("[0-9]*")
        val m = pattern.matcher(phoneNumber)
        if (!m.matches()) {
            val end = isNumber(phoneNumber)
            if (end > 0) {
                phoneNumber = phoneNumber.substring(0, end)
            }
        }
        phoneNumber = if (phoneNumber.length == 11) {
            "+86$phoneNumber"
        } else {
            "021$phoneNumber"
        }
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        if (ActivityCompat.checkSelfPermission(this@ContactActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 0)
            }
            return
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun isNumber(s: String): Int {
        for (i in s.indices) {
            val flag = s[i].toInt()
            if (flag < 48 || flag > 57) {
                return i
            }
        }
        return -1
    }

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, ContactActivity::class.java)
            context.startActivity(intent)
        }
    }
}