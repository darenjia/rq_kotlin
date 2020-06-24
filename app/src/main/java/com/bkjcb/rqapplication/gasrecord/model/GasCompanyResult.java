package com.bkjcb.rqapplication.gasrecord.model;

import com.bkjcb.rqapplication.model.HttpResult;

import java.util.List;

/**
 * Created by DengShuai on 2020/4/30.
 * Description :
 */
public class GasCompanyResult extends HttpResult {

    private List<GasCompany> datas;

    public List<GasCompany> getDatas() {
        return datas;
    }

    public void setDatas(List<GasCompany> datas) {
        this.datas = datas;
    }

   public static class GasCompany {
        /**
         * cid : 2c9e66f371728bd20171728c40890006
         * name : 上海恒申燃气发展有限公司
         * ext : 40286f816f467f63016f467fcf930005
         */

        private String cid;
        private String name;
        private String ext;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }
    }
}
