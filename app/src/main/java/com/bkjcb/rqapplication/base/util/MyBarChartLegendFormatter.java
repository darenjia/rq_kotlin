package com.bkjcb.rqapplication.base.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * Created by DengShuai on 2020/7/3.
 * Description :
 */
public class MyBarChartLegendFormatter implements IAxisValueFormatter {
    private List<String> names;

    public MyBarChartLegendFormatter(List<String> names) {
        this.names = names;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return names.get((int) value);
    }
}
