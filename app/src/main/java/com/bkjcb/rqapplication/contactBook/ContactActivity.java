package com.bkjcb.rqapplication.contactBook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.contactBook.database.ContactDataUtil;
import com.bkjcb.rqapplication.contactBook.fragment.ContactFirstFragment;
import com.bkjcb.rqapplication.contactBook.model.ChatMessage;
import com.bkjcb.rqapplication.contactBook.model.User;
import com.bkjcb.rqapplication.base.util.Utils;
import com.hss01248.dialog.StyledDialog;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
public class ContactActivity extends SimpleBaseActivity {


    private ViewHolder viewHolder;
    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_contact;
    }

    @Override
    protected void initView() {
        StyledDialog.init(this);
        QMUITopBarLayout barLayout = initTopbar("应急通讯录");
        /* ContactMainFragment mainFragment = new ContactMainFragment();
       mainFragment.setListener(new ContactMainFragment.OnClickListener() {
            @Override
            public void onClick(User user) {
                alertUserInfo(user);
            }
        });*/
        barLayout.addRightImageButton(R.drawable.vector_drawable_search, R.id.top_right_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContactSearchActivity.ToActivity(ContactActivity.this);
                    }
                });
        barLayout.addRightImageButton(R.drawable.vector_drawable_weixin, R.id.top_right_button1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ContactActivity.this,ChatActivity.class));
                    }
                });
    }
    @Override
    protected void initData() {
        disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                ContactDataUtil.init(ContactActivity.this);
                emitter.onNext(true);
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean bool) throws Exception {
                        if (bool) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.contact_content, ContactFirstFragment.newInstance(new ContactFirstFragment.OnClickListener() {
                                        @Override
                                        public void onClick(String tel) {
                                            actionCall(tel);
                                        }
                                    }))
                                    .commit();
                        }
                    }
                });
    }

    public static void ToActivity(Context context) {
        Intent intent = new Intent(context, ContactActivity.class);
        context.startActivity(intent);
    }

    protected void alertUserInfo(User user) {
        StyledDialog.buildCustomBottomSheet(initDialogView(user))
                .setBackground(getResources().getColor(R.color.colorAlpha))
                .setBottomSheetDialogMaxHeightPercent(0.4f).show();
    }

    private View initDialogView(final User user) {
        if (viewHolder==null){
            viewHolder=new ViewHolder();
        }
        viewHolder.imageView.setTextAndColor(user.getUsername().substring(0, 1), Utils.getRandomColor(this));
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMActivity.ToActivity(ContactActivity.this,user);
            }
        });
        viewHolder.name.setText(user.getUsername());
        String s1 = user.getTel();
        String s2 = user.getU_tel();
        String s3 = user.getUnit().getTel();
        if (!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)) {
            viewHolder.tel2.setVisibility(View.VISIBLE);
            viewHolder.tel1.setLeftBottomString(s1);
            viewHolder.tel2.setLeftBottomString(s2);
        } else {
            if (TextUtils.isEmpty(s1)) {
                viewHolder.tel1.setLeftBottomString(s2);
            } else {
                viewHolder.tel1.setLeftBottomString(s1);
            }
        }
        final String unitName = user.getLevel().getDepartmentnamea();
        if (unitName.length() > 16) {
            viewHolder.department.setRightString(unitName.substring(16, unitName.length()));
            viewHolder.department.setRightTopString(unitName.substring(0, 16));
        } else {
            viewHolder.department.setRightString(unitName);
        }
//        department.setRightBottomString(user.getDepartmentNameA());
        String address = user.getUnit().getAddress();
        /*if (address.length() > 16) {
            unit_address.setRightString(address.substring(16, address.length()));
            unit_address.setRightTopString(address.substring(0, 16));
        } else {
            unit_address.setRightString(user.getUnit().getAddress());
        }*/
        viewHolder.unit_address.setRightString(address);
        if (!TextUtils.isEmpty(user.getDuty())) {
            viewHolder.zhiwu.setRightBottomString(user.getDuty());
            viewHolder.zhiwu.setVisibility(View.VISIBLE);
        }
        final String qxName = user.getUnit().getDistrictName();
        if (!TextUtils.isEmpty(qxName)) {
            viewHolder.quxian.setRightBottomString(qxName);
            viewHolder.quxian.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(s3)) {
            String[] numbers = s3.split("、");
            if (numbers.length > 1) {
                viewHolder.unit_phone.setCenterBottomString(numbers[1]);
            }
            viewHolder.unit_phone.setRightBottomString(numbers[0]);
            viewHolder.unit_phone.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(user.getUnit().getFax())) {
            viewHolder.unit_fax.setRightBottomString(user.getUnit().getFax());
            viewHolder.unit_fax.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(user.getUnit().getZipcode())) {
            viewHolder.unit_zipcode.setRightBottomString(user.getUnit().getZipcode());
            viewHolder.unit_zipcode.setVisibility(View.VISIBLE);
        }
        viewHolder.tel1.setRightImageViewClickListener(new SuperTextView.OnRightImageViewClickListener() {
            @Override
            public void onClickListener(ImageView imageView) {
                String number =viewHolder.tel1.getLeftBottomString();
                actionCall(number);
            }
        });
        //tel1.setOnClickListener(listener);
        viewHolder.tel2.setRightImageViewClickListener(new SuperTextView.OnRightImageViewClickListener() {
            @Override
            public void onClickListener(ImageView imageView) {
                String number = viewHolder.tel2.getLeftBottomString();
                actionCall(number);
            }
        });
        //tel1.setOnLongClickListener(longClickListener);
        //tel2.setOnLongClickListener(longClickListener);
        viewHolder.unit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = ((SuperTextView) v).getRightBottomString();
                actionCall(number);
            }
        });
        viewHolder.department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* viewPager.setCurrentItem(0);
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
        });
        return viewHolder.view;
    }

    public void actionCall(String number) {
        number = number.trim();
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher m = pattern.matcher(number);
        if (!m.matches()) {
            int end = isNumber(number);
            if (end > 0) {
                number = number.substring(0, end);
            }
        }

        if (number.length() == 11) {
            number = "+86" + number;
        } else {
            number = "021" + number;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(ContactActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 0);
            }
            return;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    private int isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            int flag = s.charAt(i);
            if (flag < 48 || flag > 57) {
                return i;
            }
        }
        return -1;
    }

    class ViewHolder{
        TextView name;
        AvatarImageView imageView;
        SuperTextView tel1;
        SuperTextView tel2;
        SuperTextView quxian;
        SuperTextView department;
        SuperTextView zhiwu;
        SuperTextView unit_address;
        SuperTextView unit_phone;
        SuperTextView unit_fax;
        SuperTextView unit_zipcode;
        View view;

        ViewHolder(){
            view = getLayoutInflater().inflate(R.layout.alert_view, null);
            name = (TextView) view.findViewById(R.id.name);
            imageView = (AvatarImageView) view.findViewById(R.id.item_avatar);
            tel1 = (SuperTextView) view.findViewById(R.id.phoneNumber1);
            tel2 = (SuperTextView) view.findViewById(R.id.phoneNumber2);
            quxian = (SuperTextView) view.findViewById(R.id.quxian);
            department = (SuperTextView) view.findViewById(R.id.bumen);
            zhiwu = (SuperTextView) view.findViewById(R.id.zhiwu);
            unit_address = (SuperTextView) view.findViewById(R.id.dizhi_danwei);
            unit_phone = (SuperTextView) view.findViewById(R.id.dianha_danwei);
            unit_fax = (SuperTextView) view.findViewById(R.id.chuanzhen_danwei);
            unit_zipcode = (SuperTextView) view.findViewById(R.id.youbian_danwei);
        }
    }
}
