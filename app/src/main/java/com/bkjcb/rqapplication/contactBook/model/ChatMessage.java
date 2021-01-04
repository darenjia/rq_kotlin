package com.bkjcb.rqapplication.contactBook.model;

import com.bkjcb.rqapplication.base.datebase.ObjectBox;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class ChatMessage {
    @Id(assignable = true)
    public long id;
    private int receiver;
    private String receiverName;
    private String content;
    private int messageType;
    private String contentType;
    private long timestamp;
    private String filePath;
    private String mimeType;
    private String fileType;
    private long voiceTime;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(long voiceTime) {
        this.voiceTime = voiceTime;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public static Box<ChatMessage> getBox() {
        return ObjectBox.get().boxFor(ChatMessage.class);
    }

    public static void insert(ChatMessage message) {
        getBox().put(message);
    }

    public static List<ChatMessage> queryMessage(int receiver) {
        return getBox().query().equal(ChatMessage_.receiver, receiver).order(ChatMessage_.timestamp).build().find();
    }

    public static List<ChatMessage> queryMessage() {
        List<ChatMessage> list = new ArrayList<>();
        int[] persons = getBox().query().orderDesc(ChatMessage_.timestamp).build().property(ChatMessage_.receiver).distinct().findInts();
        for (int i : persons) {
            ChatMessage message = getBox().query().equal(ChatMessage_.receiver, i).orderDesc(ChatMessage_.timestamp).build().findFirst();
            list.add(message);
        }
        return list;
    }
}
