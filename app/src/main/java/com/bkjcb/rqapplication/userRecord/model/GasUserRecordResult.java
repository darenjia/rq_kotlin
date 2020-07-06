package com.bkjcb.rqapplication.userRecord.model;

import com.bkjcb.rqapplication.base.model.HttpResult;

import java.util.List;

/**
 * Created by DengShuai on 2020/5/6.
 * Description :
 */
public class GasUserRecordResult extends HttpResult {
    private List<GasUserRecord> datas;
    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<GasUserRecord> getDatas() {
        return datas;
    }

    public void setDatas(List<GasUserRecord> datas) {
        this.datas = datas;
    }

    public static class GasUserRecord {

        /**
         * yonghuming : yonghuming3
         * yihuyidangid : 2c9dab9771c9b29d0171c9b793eb0493
         * rquserid : null
         * dizhi : dizhi
         * systime : 2020-04-30 14:13:03
         * state : 0
         * jiandangriqi : 2020-04-30
         */

        private String yonghuming;
        private String yihuyidangid;
        private String rquserid;
        private String dizhi;
        private long systime;
        private String jiandangriqi;
        private String yihuyidangfuchaid;
        private String mbuid;

        public String getMbuid() {
            return mbuid;
        }

        public void setMbuid(String mbuid) {
            this.mbuid = mbuid;
        }

        public String getYonghuming() {
            return yonghuming;
        }

        public String getRquserid() {
            return rquserid;
        }

        public void setRquserid(String rquserid) {
            this.rquserid = rquserid;
        }

        public long getSystime() {
            return systime;
        }

        public void setSystime(long systime) {
            this.systime = systime;
        }

        public void setYonghuming(String yonghuming) {
            this.yonghuming = yonghuming;
        }

        public String getYihuyidangid() {
            return yihuyidangid;
        }

        public void setYihuyidangid(String yihuyidangid) {
            this.yihuyidangid = yihuyidangid;
        }



        public String getDizhi() {
            return dizhi;
        }

        public void setDizhi(String dizhi) {
            this.dizhi = dizhi;
        }

        public String getJiandangriqi() {
            return jiandangriqi;
        }

        public String getYihuyidangfuchaid() {
            return yihuyidangfuchaid;
        }

        public void setYihuyidangfuchaid(String yihuyidangfuchaid) {
            this.yihuyidangfuchaid = yihuyidangfuchaid;
        }

        public void setJiandangriqi(String jiandangriqi) {
            this.jiandangriqi = jiandangriqi;
        }
    }
}
