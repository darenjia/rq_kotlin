package com.bkjcb.rqapplication.datebase;

import android.content.Context;

import com.bkjcb.rqapplication.model.ContactModel;
import com.bkjcb.rqapplication.model.Emergency;
import com.bkjcb.rqapplication.model.Level;
import com.bkjcb.rqapplication.model.Level_;
import com.bkjcb.rqapplication.model.Unit_;
import com.bkjcb.rqapplication.model.User;
import com.bkjcb.rqapplication.model.User_;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.query.QueryBuilder;

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
public class ContactDataUtil {

    public static void init(Context context) throws IOException {
        if (ObjectBox.getUserBox().isEmpty()) {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(context.getAssets().open("database.json"));
            ContactModel contactModel = gson.fromJson(reader, ContactModel.class);
            if (contactModel != null) {
                ObjectBox.getEmergencyBox().put(contactModel.getEmergency());
                ObjectBox.getLevelBox().put(contactModel.getLevel());
                ObjectBox.getUserBox().put(contactModel.getUser());
                ObjectBox.getUnitBox().put(contactModel.getUnit());
            }
        }

    }

    public static List<User> getAllUsers() {
        return ObjectBox.getUserBox().getAll();
    }

    public static List<Emergency> getAllEmergency() {
        return ObjectBox.getEmergencyBox().getAll();
    }

    public static ArrayList<Level> getAllDistractName() {
        ArrayList<Level> strings = new ArrayList<>(16);
        strings.add(new Level("1", "宝山区", 1, "宝山区"));
        strings.add(new Level("2", "崇明区", 1, "崇明区"));
        strings.add(new Level("3", "奉贤区", 1, "奉贤区"));
        strings.add(new Level("4", "虹口区", 1, "虹口区"));
        strings.add(new Level("5", "黄浦区", 1, "黄浦区"));
        strings.add(new Level("6", "嘉定区", 1, "嘉定区"));
        strings.add(new Level("7", "金山区", 1, "金山区"));
        strings.add(new Level("8", "静安区", 1, "静安区"));
        strings.add(new Level("9", "闵行区", 1, "闵行区"));
        strings.add(new Level("10", "浦东新区", 1, "浦东新区"));
        strings.add(new Level("11", "普陀区", 1, "普陀区"));
        strings.add(new Level("12", "青浦区", 1, "青浦区"));
        strings.add(new Level("13", "松江区", 1, "松江区"));
        strings.add(new Level("14", "徐汇区", 1, "徐汇区"));
        strings.add(new Level("15", "杨浦区", 1, "杨浦区"));
        strings.add(new Level("16", "长宁区", 1, "长宁区"));
        return strings;
    }

    public static List<Level> queryLevel(int level, int kind1, int kind2, int kind3, String quxian) {
        QueryBuilder<Level> builder = ObjectBox.getLevelBox().query();
        builder.equal(Level_.level, level);
        if (level == 1 && !("").equals(quxian)) {
            builder.equal(Level_.quxian, quxian);
        }
        if (kind1 == -1) {
            builder.notEqual(Level_.kind1, 0).equal(Level_.kind2, 0).equal(Level_.kind3, 0);
        } else if (kind2 != -1 && kind3 == -1) {
            //sql.append("and Kind1 = ? and Kind2 = ? AND Kind3 != 0");
            builder.equal(Level_.kind1, kind1).equal(Level_.kind2, kind2).notEqual(Level_.kind3, 0);
        } else if (kind3 != -1) {
            //sql.append("and Kind1 = ? and Kind2 = ? AND Kind3 = ?");
            builder.equal(Level_.kind1, kind1).equal(Level_.kind2, kind2).equal(Level_.kind3, kind3);
        } else {
            //sql.append("and Kind1 = ? AND Kind2 != 0 AND Kind3 = 0");
            builder.equal(Level_.kind1, kind1).and().notEqual(Level_.kind2, 0).and().equal(Level_.kind3, 0);
        }
        return builder.build().find();

    }

    public static List<User> queryUser(Level level) {
        if (level == null || level.getLevel() == -1) {
            return new ArrayList<>();
        }
        int[] levelIds = ObjectBox.getLevelBox().query().equal(Level_.uid, level.getUid()).build().property(Level_.uid).distinct().findInts();
        int[] unitIds = ObjectBox.getUnitBox().query().in(Unit_.levelid, levelIds).build().property(Unit_.uid).distinct().findInts();
        return ObjectBox.getUserBox().query().in(User_.unitid, unitIds).build().find();
    }
}
