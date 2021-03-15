package com.bkjcb.rqapplication.base.model;


import com.bkjcb.rqapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DengShuai on 2019/5/15.
 * Description :
 */
public class MenuItem {
    public String text;
    public int imgUrl;
    public int type;
    public int messageCount;
    public boolean purview = true;

    public MenuItem(String text, int imgUrl, int type) {
        this.text = text;
        this.imgUrl = imgUrl;
        this.type = type;
    }

    public MenuItem(String text, int imgUrl, int type, boolean purview) {
        this.text = text;
        this.imgUrl = imgUrl;
        this.type = type;
        this.purview = purview;
    }

    public MenuItem(String text, int imgUrl, int type, int messageCount) {
        this.text = text;
        this.imgUrl = imgUrl;
        this.type = type;
        this.messageCount = messageCount;
    }

    public static List<MenuItem> getAllMenu() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("一户一档", R.drawable.main_menu_report, 1));
        list.add(new MenuItem("站点检查", R.drawable.main_menu_instruction, 2));
        list.add(new MenuItem("器具检查", R.drawable.main_menu_maintenance, 3));
        list.add(new MenuItem("联络册", R.drawable.main_menu_assess, 4));
      /*  list.add(new MenuItem("轨迹查询", R.drawable.main_menu_track, 5));
        list.add(new MenuItem("路龄管理", R.drawable.main_menu_road, 6));
        list.add(new MenuItem("物料管理", R.drawable.main_menu_consume, 7));
        list.add(new MenuItem("质量评估", R.drawable.main_menu_check, 8));*/
        list.add(new MenuItem("稽查执法", R.drawable.main_menu_select, 5));
        list.add(new MenuItem("事故现场", R.drawable.main_menu_statistics, 6));
        list.add(new MenuItem("通知通告", R.drawable.main_menu_message, 7));
        list.add(new MenuItem("设置中心", R.drawable.main_menu_more, 8));
        return list;
    }

   /**
    * @params null
    * @return List<MenuItem>
    * @describe  市级账号
    * @author Deng
    * @time 2020/6/4 9:00
    */
    public static List<MenuItem> getMunicipalMenu() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("一户一档", R.drawable.main_menu_report, 1));
//        list.add(new MenuItem("事件处置", R.drawable.main_menu_check, 7,false));
        list.add(new MenuItem("站点检查", R.drawable.main_menu_instruction, 2));
        list.add(new MenuItem("器具检查", R.drawable.main_menu_maintenance, 3));
        list.add(new MenuItem("联络册", R.drawable.main_menu_assess, 4));
        list.add(new MenuItem("稽查执法", R.drawable.main_menu_check, 5));
        list.add(new MenuItem("事故现场", R.drawable.main_menu_statistics, 6));
//        list.add(new MenuItem("管线查看", R.drawable.main_menu_allocation, 8));
        list.add(new MenuItem("企业查询", R.drawable.main_menu_selfcheck, 9));
        list.add(new MenuItem("气瓶溯源", R.drawable.main_menu_select, 11));
        list.add(new MenuItem("设置中心", R.drawable.main_menu_more, 10));
        return list;
    }
  /**
   * @params null
   * @return List
   * @describe 区级账号
   * @author Deng
   * @time 2020/6/4 9:01
   */
    public static List<MenuItem> getDistrictMenu() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("一户一档", R.drawable.main_menu_report, 1));
        list.add(new MenuItem("事件处置", R.drawable.main_menu_check, 7,false));
        list.add(new MenuItem("站点检查", R.drawable.main_menu_instruction, 2,false));
        list.add(new MenuItem("器具检查", R.drawable.main_menu_maintenance, 3,false));
        list.add(new MenuItem("联络册", R.drawable.main_menu_assess, 4,false));
        list.add(new MenuItem("稽查执法", R.drawable.main_menu_check, 5,false));
        list.add(new MenuItem("事故现场", R.drawable.main_menu_statistics, 6,false));
        list.add(new MenuItem("气瓶溯源", R.drawable.main_menu_select, 11));
        list.add(new MenuItem("设置中心", R.drawable.main_menu_more, 10));
        return list;
    }/**
   * @params null
   * @return List
   * @describe 街镇账号
   * @author Deng
   * @time 2020/6/4 9:01
   */
    public static List<MenuItem> getStreetMenu() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("一户一档", R.drawable.main_menu_report, 1));
        list.add(new MenuItem("事件处置", R.drawable.main_menu_check, 7,true));
        list.add(new MenuItem("站点检查", R.drawable.main_menu_instruction, 2,false));
        list.add(new MenuItem("器具检查", R.drawable.main_menu_maintenance, 3,false));
        list.add(new MenuItem("联络册", R.drawable.main_menu_assess, 4,false));
        list.add(new MenuItem("稽查执法", R.drawable.main_menu_check, 5,false));
        list.add(new MenuItem("事故现场", R.drawable.main_menu_statistics, 6,false));
        list.add(new MenuItem("气瓶溯源", R.drawable.main_menu_select, 11));
        list.add(new MenuItem("设置中心", R.drawable.main_menu_more, 10));
        return list;
    }
}
