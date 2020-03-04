package com.bkjcb.rqapplication.model;

import com.bkjcb.rqapplication.datebase.ObjectBox;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by DengShuai on 2019/12/24.
 * Description :
 */
@Entity
public class ApplianceCheckResultItem {
    @Id(assignable = true)
    public long id;
    public String ischeck;
    public String remark;
    public String content;
    public String jianchaxiangid;
    public String jianchaid;

    public ApplianceCheckResultItem() {
    }

    public ApplianceCheckResultItem(String jianchaid, String jianchaxiangid) {
        this.jianchaid = jianchaid;
        this.jianchaxiangid = jianchaxiangid;
    }

    public static Box<ApplianceCheckResultItem> getBox(){
        return ObjectBox.get().boxFor(ApplianceCheckResultItem.class);
    }
}
