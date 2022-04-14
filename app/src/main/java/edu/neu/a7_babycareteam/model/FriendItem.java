package edu.neu.a7_babycareteam.model;

public class FriendItem {

    private String friendName;
    private boolean isSelected;


    public FriendItem(String urlName) {
        this.friendName = urlName;
    }

    public String getFriendName() {
        return friendName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}