package edu.neu.a7_babycareteam.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private long msgId;
    public int stickerID;
    public String stickerName;
    public String senderName;
    public String sendTime;

    public Message() {

    }

    public Message(int stickerID, String stickerName, String senderName) {
        this.stickerID = stickerID;
        this.stickerName = stickerName;
        this.senderName = senderName;
        this.sendTime = getCurrentTime();
        this.msgId = System.currentTimeMillis();
    }

    public long getMsgId() {
        return msgId;
    }

    public static String getCurrentTime() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
        return ft.format(dNow);
    }

    public int getStickerID() {
        return stickerID;
    }

    public void setStickerID(int stickerID) {
        this.stickerID = stickerID;
    }

    public String getStickerName() {
        return stickerName;
    }

    public void setStickerName(String stickerName) {
        this.stickerName = stickerName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSendTime() {
        return sendTime;
    }
}
