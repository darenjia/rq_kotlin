package com.bkjcb.rqapplication.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by DengShuai on 2020/6/30.
 * Description :
 */
public class GasStatisticData extends AbstractExpandableItem<GasStatisticData> implements MultiItemEntity {

    public static final int DISTRICT_TYPE = 1;
    public static final int STREET_TYPE = 2;
    /**
     * name : 马桥镇
     * gs :
     * tiaoyafa_geshu :
     * tiaoyafa :
     * xihuobaohu :
     * childrens : null
     */

    private String name;
    private String gs;
    private String tiaoyafa_geshu;
    private String tiaoyafa;
    private String xihuobaohu;
    private String jrgs;
    private List<GasStatisticData> childrens;

    public String getJrgs() {
        return jrgs;
    }

    public void setJrgs(String jrgs) {
        this.jrgs = jrgs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGs() {
        return gs;
    }

    public void setGs(String gs) {
        this.gs = gs;
    }

    public String getTiaoyafa_geshu() {
        return tiaoyafa_geshu;
    }

    public void setTiaoyafa_geshu(String tiaoyafa_geshu) {
        this.tiaoyafa_geshu = tiaoyafa_geshu;
    }

    public String getTiaoyafa() {
        return tiaoyafa;
    }

    public void setTiaoyafa(String tiaoyafa) {
        this.tiaoyafa = tiaoyafa;
    }

    public String getXihuobaohu() {
        return xihuobaohu;
    }

    public void setXihuobaohu(String xihuobaohu) {
        this.xihuobaohu = xihuobaohu;
    }

    public List<GasStatisticData> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<GasStatisticData> childrens) {
        this.childrens = childrens;
      /*  if (childrens != null && childrens.size() > 0) {
            setSubItems(childrens);
        }*/
    }

    public void init() {
        setSubItems(getChildrens());
    }

    @Override
    public int getLevel() {
        return childrens == null ? STREET_TYPE : DISTRICT_TYPE;
    }

    @Override
    public int getItemType() {
        return childrens == null ? STREET_TYPE : DISTRICT_TYPE;
    }
}
