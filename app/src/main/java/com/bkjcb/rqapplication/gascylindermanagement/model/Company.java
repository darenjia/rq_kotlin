package com.bkjcb.rqapplication.gascylindermanagement.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DengShuai on 2021/3/4.
 * Description :
 */
public class Company {
    @SerializedName("unit_code")
    String id;
    @SerializedName("name_qc")
    String name;
    @SerializedName("name_jc")
    String subName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    @Override
    public String toString() {
        return name;
    }
}
