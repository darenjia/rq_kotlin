package com.bkjcb.rqapplication.gascylindermanagement.model;

import java.util.List;

/**
 * Created by DengShuai on 2021/3/9.
 * Description :
 */
public class CompanyResult {
    private boolean success;
    private List<Company> returnValue;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Company> getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(List<Company> returnValue) {
        this.returnValue = returnValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
