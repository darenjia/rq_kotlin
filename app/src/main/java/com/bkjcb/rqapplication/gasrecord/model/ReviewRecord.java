package com.bkjcb.rqapplication.gasrecord.model;

import java.io.Serializable;

/**
 * Created by DengShuai on 2020/5/6.
 * Description :
 */
public class ReviewRecord implements Serializable {
   public String tiaoyaqi_zhenggai;
   public String tiaoyaqi_zhenggairiqi;
   public String lianjieguan_geshu;
   public String lianjieguan_zhenggai;
   public String lianjieguan_zhenggairiqi;
   public String ranju_geshu;
   public String ranju_zhenggai;
   public String ranju_zhenggairiqi;
   public String yehuaqi_shiyong;
   public String tuihuriqi;
   public String userId;
   public String yihuyidangid;
   public String jianchariqi;
   public String jianchadanwei;
   public String jianchadanweiid;
   public String tiaoyaqi_geshu;
   public String jianchajieguo;
   public String phoneftp;
   public String yonghuming;

    public int status;

    public ReviewRecord(int status) {
        this.status = status;
    }
    public ReviewRecord() {
    }
}
