package com.bkjcb.rqapplication.Map.Bean;

import com.bkjcb.rqapplication.base.model.HttpResult;

/**
 * author : Young
 * description ：
 * date : 2019/7/4 13:59
 */
public class GuanxianResult extends HttpResult {
    private GuanxianDetail tongzhi;

    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public GuanxianDetail getTongzhi() {
        return tongzhi;
    }

    public void setTongzhi(GuanxianDetail tongzhi) {
        this.tongzhi = tongzhi;
    }
}
