package com.bkjcb.rqapplication.check.model;

import com.bkjcb.rqapplication.datebase.ObjectBox;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by DengShuai on 2019/12/24.
 * Description :
 */
@Entity
public class CheckResultItem {
    @Id(assignable = true)
    public long id;
    public String jianchajilu;
    public String jianchaid;
    public String jianchaxiangid;

    public CheckResultItem() {
    }

    public CheckResultItem(String jianchaid, String jianchaxiangid) {
        this.jianchaid = jianchaid;
        this.jianchaxiangid = jianchaxiangid;
    }

    public static Box<CheckResultItem> getBox(){
        return ObjectBox.INSTANCE.getBoxStore().boxFor(CheckResultItem.class);
    }
}
