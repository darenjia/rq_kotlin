package com.bkjcb.rqapplication.stationCheck.model;

import com.bkjcb.rqapplication.base.datebase.ObjectBox;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by DengShuai on 2020/12/31.
 * Description :
 */
@Entity
public class ModifyNotificationModel {
    @Id(assignable = true)
    public long id;
    public String guid;//检查ID
    public String zi;//字
    public String name;//单位名称
    public String time;//发生时间
    public String address;//发生地点
    public String des;//行为描述
    public String provision;//规定名称
    public String provision1;//规定第几条
    public String provision2;//第几款
    public String provision3;//第几项
    public String provisionName;//规定名称
    public String provision11;//第几条
    public String provision12;//第几款
    public String provision13;//第几项
    public int type;//改正期限
    public String typeTime;//改正期限
    public String content;//修改具体

    public static Box<ModifyNotificationModel> getBox() {
        return ObjectBox.get().boxFor(ModifyNotificationModel.class);
    }

    public static void save(ModifyNotificationModel model) {
        getBox().put(model);
    }

    public static ModifyNotificationModel query(String guid) {
        return getBox().query().equal(ModifyNotificationModel_.guid, guid).build().findFirst();
    }
}
