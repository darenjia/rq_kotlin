package com.bkjcb.rqapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DengShuai on 2019/10/28.
 * Description :
 */
public class UserResult extends HttpResult{
    @SerializedName("datas")
    private User datas;

    public User getDatas() {
        return datas;
    }

    public void setDatas(User datas) {
        this.datas = datas;
    }

    public static class User {


        /**
         * user_name : 1
         * password :
         * real_name : song
         * contact_phone : 18173966842
         * danweileixing :
         * userId : F7EE86DEA39B44BB95FDFE0A6A6B3319
         * areaId : 17
         * userleixing : 市用户
         * sc_id : 5304268
         * email :
         * is_use : false
         * usercomp : 全市
         * area : {"id":"","resultCount":0,"lastModifiedDate":null,"markAsDeleted":false,"area_name":"全市","record_name":"沪","record_code":"310","jane_spell":"QS"}
         * areacode :
         * roles : ["管道气企业报表管理_液化气_审批","市用户","供求监测_月度计划_市审批","系统配置_市_用户管理","企业日常检查_报警器企业_新增","管道气企业报表管理_管道气_审批","站点检查_审核人","市场检查管理_市_审批","办公室","站点检查_经办人","市场检查管理_市_新增","第三方_市_新增","企业日常检查_维修检查企业_新增","行政执法_市_查看","事件管理_市_查看","站点检查_查看人","企业日常检查_销售企业_新增"]
         */

        private String user_name;
        private String password;
        private String real_name;
        private String contact_phone;
        private String danweileixing;
        private String userId;
        private String areaId;
        private String userleixing;
        private String sc_id;
        private String email;
        private boolean is_use;
        private String usercomp;
        private AreaBean area;
        private AreaCodeBean areacode;
        private List<String> roles;

        public AreaCodeBean getAreacode() {
            return areacode;
        }

        public void setAreacode(AreaCodeBean areacode) {
            this.areacode = areacode;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getContact_phone() {
            return contact_phone;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }

        public String getDanweileixing() {
            return danweileixing;
        }

        public void setDanweileixing(String danweileixing) {
            this.danweileixing = danweileixing;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getUserleixing() {
            return userleixing;
        }

        public void setUserleixing(String userleixing) {
            this.userleixing = userleixing;
        }

        public String getSc_id() {
            return sc_id;
        }

        public void setSc_id(String sc_id) {
            this.sc_id = sc_id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isIs_use() {
            return is_use;
        }

        public void setIs_use(boolean is_use) {
            this.is_use = is_use;
        }

        public String getUsercomp() {
            return usercomp;
        }

        public void setUsercomp(String usercomp) {
            this.usercomp = usercomp;
        }

        public AreaBean getArea() {
            return area;
        }

        public void setArea(AreaBean area) {
            this.area = area;
        }


        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public static class AreaBean {
            /**
             * id :
             * resultCount : 0
             * lastModifiedDate : null
             * markAsDeleted : false
             * area_name : 全市
             * record_name : 沪
             * record_code : 310
             * jane_spell : QS
             */

            private String id;
            private int resultCount;
            private String area_name;
            private String record_name;
            private String record_code;
            private String jane_spell;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getResultCount() {
                return resultCount;
            }

            public void setResultCount(int resultCount) {
                this.resultCount = resultCount;
            }

            public String getArea_name() {
                return area_name;
            }

            public void setArea_name(String area_name) {
                this.area_name = area_name;
            }

            public String getRecord_name() {
                return record_name;
            }

            public void setRecord_name(String record_name) {
                this.record_name = record_name;
            }

            public String getRecord_code() {
                return record_code;
            }

            public void setRecord_code(String record_code) {
                this.record_code = record_code;
            }

            public String getJane_spell() {
                return jane_spell;
            }

            public void setJane_spell(String jane_spell) {
                this.jane_spell = jane_spell;
            }
        }
        public static class AreaCodeBean{

            /**
             * id : null
             * resultCount : 0
             * lastModifiedDate : null
             * markAsDeleted : false
             * area_code : 310101002
             * area_street : 上海/黄浦/南京东路街道
             * add_time : 1559632248670
             * street_jc : 南京东路
             * area_jc : 黄浦
             */

            private Object id;
            private int resultCount;
            private String area_code;
            private String area_street;
            private long add_time;
            private String street_jc;
            private String area_jc;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public int getResultCount() {
                return resultCount;
            }

            public void setResultCount(int resultCount) {
                this.resultCount = resultCount;
            }

            public String getArea_code() {
                return area_code;
            }

            public void setArea_code(String area_code) {
                this.area_code = area_code;
            }

            public String getArea_street() {
                return area_street;
            }

            public void setArea_street(String area_street) {
                this.area_street = area_street;
            }

            public long getAdd_time() {
                return add_time;
            }

            public void setAdd_time(long add_time) {
                this.add_time = add_time;
            }

            public String getStreet_jc() {
                return street_jc;
            }

            public void setStreet_jc(String street_jc) {
                this.street_jc = street_jc;
            }

            public String getArea_jc() {
                return area_jc;
            }

            public void setArea_jc(String area_jc) {
                this.area_jc = area_jc;
            }
        }
    }
}
