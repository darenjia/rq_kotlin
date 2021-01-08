package com.bkjcb.rqapplication.base;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bkjcb.rqapplication.Map.MapMainActivity;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.actionRegister.ActionRegisterActivity;
import com.bkjcb.rqapplication.base.adapter.LocalImageHolderView;
import com.bkjcb.rqapplication.base.adapter.MenuGridAdapter;
import com.bkjcb.rqapplication.base.model.MenuItem;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.bkjcb.rqapplication.base.retrofit.NetworkApi;
import com.bkjcb.rqapplication.base.util.DataUtil;
import com.bkjcb.rqapplication.base.util.MyBarChartLegendFormatter;
import com.bkjcb.rqapplication.base.util.RxJavaUtil;
import com.bkjcb.rqapplication.base.view.MyGridView;
import com.bkjcb.rqapplication.contactBook.ContactActivity;
import com.bkjcb.rqapplication.emergency.EmergencyMainActivity;
import com.bkjcb.rqapplication.infoQuery.FirmQueryActivity;
import com.bkjcb.rqapplication.stationCheck.CheckMainActivity;
import com.bkjcb.rqapplication.treatment.DefectTreatmentMainActivity;
import com.bkjcb.rqapplication.userRecord.GasUserRecordActivity;
import com.bkjcb.rqapplication.userRecord.GasUserStatisticActivity;
import com.bkjcb.rqapplication.userRecord.model.GasStatisticData;
import com.bkjcb.rqapplication.userRecord.retrofit.GasService;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

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
    @BindView(R.id.gas_chart)
    PieChart chart;
    @BindView(R.id.gas_chart2)
    BarChart chart2;
    private int user_type = 0;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

        initUserType();
        initMenu();
        initBanner();
        initMessage();
        //getIPConfig();
        //initChat();
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
        String name = "";
        if (user_type != 0) {
            name = MyApplication.getUser().getArea().getArea_name();
        }
        getListData(name);
    }

    private void initMenu() {
        MenuGridAdapter adapter = new MenuGridAdapter(getMenuItems());
        mMainMenuGrid.setAdapter(adapter);
        mMainMenuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem item = (MenuItem) adapter.getItem(position);
                if (item.purview) {
                    switch (item.type) {
                        case 1:
                            if (user_type == 2) {
                                GasUserRecordActivity.ToActivity(MainActivity.this);
                            } else {
                                GasUserStatisticActivity.toActivity(MainActivity.this);
                            }
                            break;
                        case 2:
                            CheckMainActivity.ToActivity(MainActivity.this, 0);
                            break;
                        case 3:
                            CheckMainActivity.ToActivity(MainActivity.this, 1);
                            break;
                        case 4:
                            ContactActivity.ToActivity(MainActivity.this);
                            break;
                        case 5:
                            ActionRegisterActivity.ToActivity(MainActivity.this);
                            break;
                        case 6:
                            //EmergencyActivity.ToActivity(MainActivity.this);
                            EmergencyMainActivity.ToActivity(MainActivity.this);
                            break;
                        case 7:
                            DefectTreatmentMainActivity.ToActivity(MainActivity.this);
                            break;
                        case 8:
//                            SettingActivity.ToActivity(MainActivity.this);
                            MapMainActivity.ToActivity(MainActivity.this);
                            break;
                        case 9:
                            FirmQueryActivity.ToActivity(MainActivity.this);
                            break;
                        case 10:
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
//        list.add("https://bucket-shgas.oss-cn-shanghai.aliyuncs.com/portalWebSite/static/home/home3.jpg");
//        list.add("https://bucket-shgas.oss-cn-shanghai.aliyuncs.com/portalWebSite/static/home/home2.jpg");
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


    private void getListData(String name) {
        disposable = NetworkApi.getService(GasService.class)
                .getStatisticData(name)
                .compose(RxJavaUtil.getObservableTransformer())
                .subscribe(new Consumer<SimpleHttpResult<List<GasStatisticData>>>() {
                    @Override
                    public void accept(SimpleHttpResult<List<GasStatisticData>> result) throws Exception {
                        if (result.pushState == 200) {
                            DataUtil.getInstance().setList(result.getDatas());
                            handleData(DataUtil.getInstance().getList());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    private void initChat(List<GasStatisticData> list) {
        chart.setBackgroundColor(Color.WHITE);

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

       /* chart.setMaxAngle(180f); // HALF CHART
        chart.setRotationAngle(180f);*/
        chart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setWordWrapEnabled(true);
        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
        chart.setUsePercentValues(false);
        setData(list);
        chart.invalidate();
        initBarChat(list);
    }

    private void initBarChat(List<GasStatisticData> list) {
        chart2.setDrawBarShadow(false);
        chart2.setDrawValueAboveBar(true);

        chart2.getDescription().setEnabled(false);
        // scaling can now only be done on x- and y-axis separately
        chart2.setPinchZoom(false);

        chart2.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        XAxis xAxis = chart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);


        YAxis leftAxis = chart2.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(new DefaultAxisValueFormatter(0));
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart2.getAxisRight();
        rightAxis.setEnabled(false);

        Legend l = chart2.getLegend();
        l.setEnabled(false);
      /*  l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);*/
        setBarChartData(list);
        chart2.invalidate();
    }

    private void setBarChartData(List<GasStatisticData> list) {
        ArrayList<BarEntry> values = new ArrayList<>();
        List<String> legendNames = new ArrayList<>();
        int number = 0;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                GasStatisticData data = list.get(i);
                if (!TextUtils.isEmpty(data.getGs())) {
                    float count = Float.parseFloat(data.getGs());
                    if (count > 0) {
                        legendNames.add(data.getName());
                        values.add(new BarEntry(number++, count));
                    }
                }
            /*    legendNames.add(data.getName());
                float count = TextUtils.isEmpty(data.getGs()) ? 0 : Float.parseFloat(data.getGs());
                values.add(new BarEntry(i, count));*/
            }
        }
        BarDataSet set = new BarDataSet(values, "");
        set.setColors(getColors());
        set.setDrawValues(true);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        BarData barData = new BarData(dataSets);
        chart2.setData(barData);
        chart2.setFitBars(true);
        XAxis xAxis = chart2.getXAxis();
        xAxis.setValueFormatter(new MyBarChartLegendFormatter(legendNames));

    }

    private void setData(List<GasStatisticData> list) {
        ArrayList<PieEntry> values = new ArrayList<>();
        float count = 0;
        float allCount = 0;
        if (list != null && list.size() > 0) {
            for (GasStatisticData data : list) {
                if (!TextUtils.isEmpty(data.getGs())) {
//                    count = TextUtils.isEmpty(data.getGs()) ? 0 : Float.parseFloat(data.getGs());
                    count = Float.parseFloat(data.getGs());
                    if (count > 0) {
                        values.add(new PieEntry(count, data.getName()));
                        allCount += count;
                    }
                }
            }
        }

        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(getColors());
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);

      /*  dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);*/

        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        chart.setCenterText(generateCenterSpannableText((int) allCount));
        chart.setCenterTextOffset(0, -20);

    }

    private ArrayList<Integer> getColors() {
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }

        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }

        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }

        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        return colors;
    }

    private SpannableString generateCenterSpannableText(int count) {

        SpannableString s = new SpannableString("总户数\n" + count);
        s.setSpan(new RelativeSizeSpan(1.0f), 0, 3, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 3, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(2.4f), 3, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 3, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 3, s.length(), 0);
        return s;
    }

    private void handleData(List<GasStatisticData> list) {
        if (list != null && list.size() > 0) {
            if (list.size() == 1) {
                List<GasStatisticData> dataList = list.get(0).getChildrens();
                initChat(dataList);
            } else {
                initChat(list);
            }
        }
    }
}

