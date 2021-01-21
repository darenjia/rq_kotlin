package com.bkjcb.rqapplication.base.model;

import com.bkjcb.rqapplication.Map.Bean.LocationPosition;

import java.util.List;

/**
 * Created by DengShuai on 2019/3/26.
 * Description :
 */
public class ConvertResult {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private List<LocationPosition> data;

    public List<LocationPosition> getData() {
        return data;
    }

    public void setData(List<LocationPosition> data) {
        this.data = data;
    }

}
