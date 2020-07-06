package com.bkjcb.rqapplication.contactBook.model;

import com.bkjcb.rqapplication.emergency.model.Emergency;

import java.util.List;

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
public class ContactModel {
    private List<Emergency> emergency;
    private List<Level> level;
    private List<User> user;
    private List<Unit> unit;

    public List<Emergency> getEmergency() {
        return emergency;
    }

    public void setEmergency(List<Emergency> emergency) {
        this.emergency = emergency;
    }

    public List<Level> getLevel() {
        return level;
    }

    public void setLevel(List<Level> level) {
        this.level = level;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public List<Unit> getUnit() {
        return unit;
    }

    public void setUnit(List<Unit> unit) {
        this.unit = unit;
    }
}
