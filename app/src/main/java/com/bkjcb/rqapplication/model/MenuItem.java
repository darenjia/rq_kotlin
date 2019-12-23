package com.bkjcb.rqapplication.model;


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

    public MenuItem(String text, int imgUrl, int type) {
        this.text = text;
        this.imgUrl = imgUrl;
        this.type = type;
    }

    public MenuItem(String text, int imgUrl, int type, int messageCount) {
        this.text = text;
        this.imgUrl = imgUrl;
        this.type = type;
        this.messageCount = messageCount;
    }

    public static List<MenuItem> getAllMenu() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("问题上报", R.drawable.main_menu_report, 1));
        list.add(new MenuItem("指令工作", R.drawable.main_menu_instruction, 2));
        list.add(new MenuItem("协助维修", R.drawable.main_menu_maintenance, 3));
        list.add(new MenuItem("立项管理", R.drawable.main_menu_assess, 4));
        list.add(new MenuItem("轨迹查询", R.drawable.main_menu_track, 5));
        list.add(new MenuItem("路龄管理", R.drawable.main_menu_road, 6));
        list.add(new MenuItem("物料管理", R.drawable.main_menu_consume, 7));
        list.add(new MenuItem("质量评估", R.drawable.main_menu_check, 8));
        list.add(new MenuItem("综合查询", R.drawable.main_menu_select, 9));
        list.add(new MenuItem("统计分析", R.drawable.main_menu_statistics, 10));
        list.add(new MenuItem("通知公告", R.drawable.main_menu_message, 11));
        list.add(new MenuItem("查看更多", R.drawable.main_menu_more, 12));
        return list;
    }
    public static List<MenuItem> getManagerMenu() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("立项管理", R.drawable.main_menu_assess, 1));
        list.add(new MenuItem("质量考核", R.drawable.main_menu_check, 2));
        list.add(new MenuItem("综合查询", R.drawable.main_menu_select, 3));
        list.add(new MenuItem("统计分析", R.drawable.main_menu_statistics, 4));
        list.add(new MenuItem("路龄管理", R.drawable.main_menu_road, 5));
        list.add(new MenuItem("物料管理", R.drawable.main_menu_consume, 6));
        list.add(new MenuItem("通知公告", R.drawable.main_menu_message, 7));
        return list;
    }
    public static List<MenuItem> getLeaderMenu() {
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("立项管理", R.drawable.main_menu_assess, 1));
        list.add(new MenuItem("暂缓任务", R.drawable.main_menu_allocation, 8));
        list.add(new MenuItem("抽检管理", R.drawable.main_menu_selfcheck, 9));
        list.add(new MenuItem("综合查询", R.drawable.main_menu_select, 3));
        list.add(new MenuItem("统计分析", R.drawable.main_menu_statistics, 4));
        list.add(new MenuItem("路龄管理", R.drawable.main_menu_road, 5));
        list.add(new MenuItem("物料管理", R.drawable.main_menu_consume, 6));
        list.add(new MenuItem("通知公告", R.drawable.main_menu_message, 7));
        return list;
    }
}
