package com.bkjcb.rqapplication.model;

/**
 * Created by DengShuai on 2019/10/24.
 * Description :
 */
public class HttpResult {

    /**
     * pushState : 200
     * pushMsg : 推送成功
     * pushTime : 2019-10-24 14:59:11
     */

    public int pushState;
    public String pushMsg;
    public String pushTime;

    public int getPushState() {
        return pushState;
    }

    public void setPushState(int pushState) {
        this.pushState = pushState;
    }

    public String getPushMsg() {
        return pushMsg;
    }

    public void setPushMsg(String pushMsg) {
        this.pushMsg = pushMsg;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }
}
