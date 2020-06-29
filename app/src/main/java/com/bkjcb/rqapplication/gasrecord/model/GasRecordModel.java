package com.bkjcb.rqapplication.gasrecord.model;

import com.bkjcb.rqapplication.datebase.ObjectBox;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by DengShuai on 2020/4/30.
 * Description :
 */
@Entity
public class GasRecordModel implements Serializable {
    @SerializedName("uid")
    @Id(assignable = true)
    public long id;
    public String anjianriqi;
    public String qiyeanjianjilu;
    public String ranqixieloubaojinqi;
    public String xihuobaohu_geshu;
    public String xihuobaohu;
    public String zaojuleixing_gufeng;
    public String zaojuleixing_dafeng;
    public String lianjieguan;
    public String tiaoyafa_geshu;
    public String tiaoyafa;
    public String qiandingriqi;
    public String yongqihetong;
    public String gongqiqiyeid;
    public String gongqiqiye;
    public String ranqiguanlizhidu;
    public String faren;
    public String yingyezhizhao;
    public String dianhua;
    public String dizhi;
    public String fuzeren;
    public String yonghuming;
    public String jiandangriqi;
    public String jiedao;
    public String suoshuqu;
    public String userId;
    //public String userid;
    public String rqdizhi;
    public String rqyonghuming;
    public String rquserid;
    public String location;
    public String beizhu;
    public String mbuid;
    public String phoneftp;
    public String year;
    public String yihuyidangid;
    @SerializedName("id")
    public String uid;
    public int type = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GasRecordModel(int type) {
        this.type = type;
    }

    public GasRecordModel() {
    }

    public static Box<GasRecordModel> getBox() {
        return ObjectBox.INSTANCE.getBoxStore().boxFor(GasRecordModel.class);
    }

    public static long save(GasRecordModel model) {
        return getBox().put(model);
    }

    public static void remove(GasRecordModel model) {
        getBox().remove(model);
    }

    public static List<GasRecordModel> all() {
        return getBox().getAll();
    }

}
