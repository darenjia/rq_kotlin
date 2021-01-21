package com.bkjcb.rqapplication.Map.Bean;

import java.io.Serializable;

/**
 * Created by DengShuai on 2019/2/1.
 * Description :
 */
public class MapData implements Serializable {
    protected String name;
    protected String equimentCode;
    protected String location;
    private String equimnetType;
    protected boolean isChecked = false;
    protected int type;
    public static final int POINT = 0;
    public static final int LINE = 1;
    public static final int SURFACE = 2;

    public int getType() {
        return type;
    }

    public MapData() {
    }

    public MapData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquimentCode() {
        return equimentCode;
    }

    public void setEquimentCode(String equimentCode) {
        this.equimentCode = equimentCode;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEquimnetType() {
        return equimnetType;
    }

    public void setEquimnetType(String equimnetType) {
        this.equimnetType = equimnetType;
    }
}
