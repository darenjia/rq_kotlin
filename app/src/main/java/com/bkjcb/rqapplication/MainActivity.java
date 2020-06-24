package com.bkjcb.rqapplication;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bkjcb.rqapplication.actionregister.ActionRegisterActivity;
import com.bkjcb.rqapplication.adapter.LocalImageHolderView;
import com.bkjcb.rqapplication.adapter.MenuGridAdapter;
import com.bkjcb.rqapplication.check.CheckMainActivity;
import com.bkjcb.rqapplication.contact.ContactActivity;
import com.bkjcb.rqapplication.emergency.EmergencyActivity;
import com.bkjcb.rqapplication.gasrecord.GasUserRecordActivity;
import com.bkjcb.rqapplication.model.MenuItem;
import com.bkjcb.rqapplication.treatmentdefect.DefectTreatmentMainActivity;
import com.bkjcb.rqapplication.view.MyGridView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class MainActivity extends SimpleBaseActivity {

    @BindView(R.id.main_menu_grid)
    MyGridView mMainMenuGrid;
    @BindView(R.id.convenientBanner)
    ConvenientBanner<String> mConvenientBanner;
    @BindView(R.id.message_more)
    ImageView mMessageMore;
    private int user_type = 0;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_mian;
    }

    @Override
    protected void initData() {

        initUserType();
        initMenu();
        initBanner();
        initMessage();
        //getIPConfig();
    }

    private void initUserType() {
        if ("市用户".equals(MyApplication.getUser().getUserleixing())) {
            user_type = 0;
        } else if ("区用户".equals(MyApplication.getUser().getUserleixing())) {
            user_type = 1;
        } else {
            user_type = 2;
        }
    }

    private void initMessage() {

    }

    private void getIPConfig() {
        Constants.BASE_URL = getSharedPreferences().getString("ip", Constants.BASE_URL);
    }

    private void initMenu() {
        MenuGridAdapter adapter = new MenuGridAdapter(getMenuItems());
        mMainMenuGrid.setAdapter(adapter);
        mMainMenuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item = (MenuItem) adapter.getItem(position);
                if (item.purview) {
                    switch (position) {
                        case 0:
                            GasUserRecordActivity.ToActivity(MainActivity.this);
                            break;
                        case 1:
                            CheckMainActivity.ToActivity(MainActivity.this, 0);
                            break;
                        case 2:
                            CheckMainActivity.ToActivity(MainActivity.this, 1);
                            break;
                        case 3:
                            ContactActivity.ToActivity(MainActivity.this);
                            break;
                        case 4:
                            ActionRegisterActivity.Companion.toActivity(MainActivity.this);
                            break;
                        case 5:
                            EmergencyActivity.Companion.toActivity(MainActivity.this);
                            break;
                        case 6:
                            DefectTreatmentMainActivity.ToActivity(MainActivity.this);
                            break;
                        case 7:
                            SettingActivity.ToActivity(MainActivity.this);
                            break;
                        default:
                    }
                } else {
                    showSnackbar(mMainMenuGrid, "该功能暂未开放！");
                }
            }
        });
    }

    private List<MenuItem> getMenuItems() {
        if (user_type == 0) {
            return MenuItem.getMunicipalMenu();
        } else if (user_type == 1) {
            return MenuItem.getDistrictMenu();
        } else {
            return MenuItem.getStreetMenu();
        }
    }

    private void initBanner() {
        List<String> list = new ArrayList<>();
        list.add("https://bucket-shgas.oss-cn-shanghai.aliyuncs.com/portalWebSite/static/home/home3.jpg");
        list.add("https://bucket-shgas.oss-cn-shanghai.aliyuncs.com/portalWebSite/static/home/home2.jpg");
        list.add("https://bucket-shgas.oss-cn-shanghai.aliyuncs.com/portalWebSite/static/home9.jpg");
        mConvenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Holder<String> createHolder(View itemView) {
                return new LocalImageHolderView(itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_banner_view;
            }
        }, list)
                .startTurning(5000)
                .setPageIndicator(new int[]{R.drawable.vector_drawable_dot_normal, R.drawable.vector_drawable_dot_focus});
    }

    private void showDialogChangeIP() {
        StyledDialog.init(this);
        StyledDialog.buildNormalInput("IP", "ip", "", Constants.BASE_URL, "", new MyDialogListener() {
            @Override
            public void onFirst() {

            }

            @Override
            public void onSecond() {

            }

            @Override
            public void onGetInput(CharSequence input1, CharSequence input2) {
                super.onGetInput(input1, input2);
                Constants.BASE_URL = input1.toString();
                getSharedPreferences().edit().putString("ip", Constants.BASE_URL).apply();
            }
        }).setBtnText("修改").show();
    }

}
