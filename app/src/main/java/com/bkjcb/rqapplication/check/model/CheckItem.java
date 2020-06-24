package com.bkjcb.rqapplication.check.model;

import com.bkjcb.rqapplication.datebase.ObjectBox;

import java.io.Serializable;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by DengShuai on 2019/12/23.
 * Description :
 */
@Entity
public class CheckItem implements Serializable {
    @Id(assignable = true)
    public long id;
    public String c_id;
    public String zhandianleixing;
    public String beijiandanweiid;
    public String beijiandanwei;
    public String jianchariqi;
    public String jianchajieguo;
    public String beizhu;
    public String filePath;
    public String checkMan;
    public String year;
    public long systime;
    public int status;
    public int type;//0站点检查 1器具检查


    public CheckItem() {
    }

    public CheckItem(int type) {
        this.type = type;
    }

    public static Box<CheckItem> getBox() {
        return ObjectBox.get().boxFor(CheckItem.class);
    }
}
