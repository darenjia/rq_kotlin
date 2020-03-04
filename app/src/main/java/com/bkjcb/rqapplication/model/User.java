package com.bkjcb.rqapplication.model;

import com.bkjcb.rqapplication.datebase.ObjectBox;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

@Entity
public class User {
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
     * unitid : 1
     * username : 朱成宇
     * loginname : 朱成宇
     * password : null
     * u_tel : 23113121
     * tel : 18916879517
     * duty : 设施管理处/处长
     * role_a : 1
     * role_b : 0
     * role_c : 0
     * role_d : 0
     * flag : 0
     */

    private int uid;
    private int unitid;
    private String username;
    private String loginname;
    private String password;
    private String u_tel;
    private String tel;
    private String duty;
    private int role_a;
    private int role_b;
    private int role_c;
    private int role_d;
    private int flag;
    @Transient
    private Unit unit;
    @Transient
    private Level level;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Unit getUnit() {
        if (unit == null) {
            unit = ObjectBox.getUnitBox().query().equal(Unit_.uid, unitid).build().findFirst();
        }
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Level getLevel() {
        if (level == null) {
            level = ObjectBox.getLevelBox().query().equal(Level_.uid, getUnit().getLevelid()).build().findFirst();
        }
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getUnitid() {
        return unitid;
    }

    public void setUnitid(int unitid) {
        this.unitid = unitid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getU_tel() {
        return u_tel;
    }

    public void setU_tel(String u_tel) {
        this.u_tel = u_tel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public int getRole_a() {
        return role_a;
    }

    public void setRole_a(int role_a) {
        this.role_a = role_a;
    }

    public int getRole_b() {
        return role_b;
    }

    public void setRole_b(int role_b) {
        this.role_b = role_b;
    }

    public int getRole_c() {
        return role_c;
    }

    public void setRole_c(int role_c) {
        this.role_c = role_c;
    }

    public int getRole_d() {
        return role_d;
    }

    public void setRole_d(int role_d) {
        this.role_d = role_d;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void init() {
        if (unit == null) {
            unit = ObjectBox.getUnitBox().query().equal(Unit_.uid, unitid).build().findFirst();
        }
        if (level == null && unit != null) {
            level = ObjectBox.getLevelBox().query().equal(Level_.uid, getUnit().getLevelid()).build().findFirst();
        }
    }
}