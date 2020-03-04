package com.bkjcb.rqapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bkjcb.rqapplication.adapter.LocalImageHolderView;
import com.bkjcb.rqapplication.adapter.MenuGridAdapter;
import com.bkjcb.rqapplication.model.MenuItem;
import com.bkjcb.rqapplication.view.MyGridView;

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
    ConvenientBanner mConvenientBanner;
    @BindView(R.id.message_more)
    ImageView mMessageMore;
    @BindView(R.id.main_message_list)
    RecyclerView mMainMessageList;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_mian;
    }

    @Override
    protected void initData() {
        initMenu();
        initBanner();
        initMessage();
    }

    private void initMessage() {

    }


    private void initMenu() {
        MenuGridAdapter adapter = new MenuGridAdapter(MenuItem.getAllMenu());
        mMainMenuGrid.setAdapter(adapter);
        mMainMenuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        GasMainActivity.ToActivity(MainActivity.this);
                        break;
                    case 1:
                        CheckMainActivity.ToActivity(MainActivity.this,0);
                        break;
                    case 2:
                        CheckMainActivity.ToActivity(MainActivity.this,1);
                        break;
                    case 3:
                        ContactActivity.ToActivity(MainActivity.this);
                        break;
                    default:
                }
            }
        });
    }

    private void initBanner() {
        List<String> list = new ArrayList<>();
        list.add("https://bucket-shgas.oss-cn-shanghai.aliyuncs.com/portalWebSite/static/home/home3.jpg");
        list.add("https://bucket-shgas.oss-cn-shanghai.aliyuncs.com/portalWebSite/static/home/home2.jpg");
        list.add("https://bucket-shgas.oss-cn-shanghai.aliyuncs.com/portalWebSite/static/home9.jpg");
        mConvenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
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

}
