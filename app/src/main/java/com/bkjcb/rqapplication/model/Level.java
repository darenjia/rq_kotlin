package com.bkjcb.rqapplication.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Level {
    @Id(assignable = true)
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * id : 1
     * quxian : 10
     * departmentname : 浦东新区环保局
     * departmentnamea : 浦东新区环保局
     * level : 1
     * kind1 : 1
     * kind2 : 0
     * kind3 : 0
     * flag : 0
     * districtname : 浦东新区
     */
    public Level() {

    }

    public Level(String quxian, String departmentnamea, int level, String districtname) {
        this.quxian = quxian;
        this.departmentnamea = departmentnamea;
        this.level = level;
        this.districtname = districtname;
    }

    private int uid;
    private String quxian;
    private String departmentname;
    private String departmentnamea;
    private int level;
    private int kind1;
    private int kind2;
    private int kind3;
    private int flag;
    private String districtname;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getQuxian() {
        return quxian;
    }

    public void setQuxian(String quxian) {
        this.quxian = quxian;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getDepartmentnamea() {
        return departmentnamea;
    }

    public void setDepartmentnamea(String departmentnamea) {
        this.departmentnamea = departmentnamea;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getKind1() {
        return kind1;
    }

    public void setKind1(int kind1) {
        this.kind1 = kind1;
    }

    public int getKind2() {
        return kind2;
    }

    public void setKind2(int kind2) {
        this.kind2 = kind2;
    }

    public int getKind3() {
        return kind3;
    }

    public void setKind3(int kind3) {
        this.kind3 = kind3;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }
}
