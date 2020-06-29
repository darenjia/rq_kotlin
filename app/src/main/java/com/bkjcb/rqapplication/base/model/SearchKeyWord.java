package com.bkjcb.rqapplication.base.model;

import com.bkjcb.rqapplication.base.datebase.ObjectBox;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by DengShuai on 2020/3/31.
 * Description :
 */
@Entity
public class SearchKeyWord {
    @Id(assignable = true)
    long id;
    String key;
    int count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private static Box<SearchKeyWord> getBox() {
        return ObjectBox.INSTANCE.getBoxStore().boxFor(SearchKeyWord.class);
    }

    public static void save(String key) {
        Box<SearchKeyWord> box = getBox();
        SearchKeyWord keyWord = box.query().equal(SearchKeyWord_.key, key).build().findFirst();
        if (keyWord != null) {
            keyWord.count = keyWord.count + 1;
        } else {
            keyWord = new SearchKeyWord();
            keyWord.key = key;
            keyWord.count = 1;
        }
        box.put(keyWord);
    }

    public static List<SearchKeyWord> getAll() {
        return getBox().query().order(SearchKeyWord_.count).build().find(0, 20);
    }
    public static void removeAll(){
        getBox().removeAll();
    }

}
