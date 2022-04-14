package edu.neu.a7_babycareteam.model;

public class Sticker {
    private int stickerId;
    private String stickerName;
    private int sentTimes;

    public Sticker() {
    }

    public Sticker(int iId, String iName) {
        this.stickerId = iId;
        this.stickerName = iName;
        sentTimes = 0;
    }

    public int getStickerId() {
        return stickerId;
    }

    public void setStickerId(int stickerId) {
        this.stickerId = stickerId;
    }

    public String getStickerName() {
        return stickerName;
    }

    public void setStickerName(String stickerName) {
        this.stickerName = stickerName;
    }

    public void setSentTimes(int sentTimes) {
        this.sentTimes = sentTimes;
    }

    public int getSentTimes() {
        return sentTimes;
    }
}
