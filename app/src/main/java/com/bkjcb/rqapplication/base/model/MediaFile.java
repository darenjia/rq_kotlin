package com.bkjcb.rqapplication.base.model;

import com.bkjcb.rqapplication.base.datebase.ObjectBox;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by DengShuai on 2020/3/9.
 * Description :
 */
@Entity
public class MediaFile {
    @Id(assignable = true)
    public long id;
    private int type;//0图片 1视频 2音频 3文件
    private String itemId;
    private String path;
    private String fileName;
    private boolean isLocal = true;

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static Box<MediaFile> getBox() {
        return ObjectBox.INSTANCE.getBoxStore().boxFor(MediaFile.class);
    }

    public static void save(List<MediaFile> list) {
        getBox().put(list);
    }

    public static void save(MediaFile list) {
        getBox().put(list);
    }

    public static List<MediaFile> getAll(String uuid) {
        return getBox().query().equal(MediaFile_.itemId, uuid).build().find();
    }
}
