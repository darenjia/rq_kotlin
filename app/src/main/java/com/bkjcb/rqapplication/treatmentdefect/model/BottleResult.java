package com.bkjcb.rqapplication.treatmentdefect.model;

import com.bkjcb.rqapplication.base.model.HttpResult;

import java.util.List;

/**
 * Created by DengShuai on 2019/10/31.
 * Description :
 */
public class BottleResult extends HttpResult {
    private Bottle datas;

    public Bottle getDatas() {
        return datas;
    }

    public void setDatas(Bottle datas) {
        this.datas = datas;
    }

    public class Bottle {
        private List<BottleSaleCheck> bottleSaleChecks;
        private List<BottleTourCheck> bottleTourChecks;
        private BottleUser bottleUser;

        public List<BottleSaleCheck> getBottleSaleChecks() {
            return bottleSaleChecks;
        }

        public void setBottleSaleChecks(List<BottleSaleCheck> bottleSaleChecks) {
            this.bottleSaleChecks = bottleSaleChecks;
        }

        public List<BottleTourCheck> getBottleTourChecks() {
            return bottleTourChecks;
        }

        public void setBottleTourChecks(List<BottleTourCheck> bottleTourChecks) {
            this.bottleTourChecks = bottleTourChecks;
        }

        public BottleUser getBottleUser() {
            return bottleUser;
        }

        public void setBottleUser(BottleUser bottleUser) {
            this.bottleUser = bottleUser;
        }
    }
}
