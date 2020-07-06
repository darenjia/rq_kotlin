package com.bkjcb.rqapplication.base.util;

import com.bkjcb.rqapplication.userRecord.model.GasStatisticData;

import java.util.List;

/**
 * Created by DengShuai on 2020/7/2.
 * Description :
 */
public class DataUtil {
    private static DataUtil dataUtil;
    private List<GasStatisticData> list;

    public static DataUtil getInstance() {
        if (dataUtil == null) {
            dataUtil = new DataUtil();
        }
        return dataUtil;
    }

    public List<GasStatisticData> getList() {
        return list;
    }

    public void setList(List<GasStatisticData> list) {
        this.list = initStatisticData(list);
    }

    private List<GasStatisticData> initStatisticData(List<GasStatisticData> data) {
        if (data != null) {
            for (GasStatisticData d : data) {
                d.init();
            }
        }
        return data;
    }
}
