package edu.neu.a7_babycareteam.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    public String userName;
    public String latestLoginTime;

    public User() {
    }

    public User(String username) {
        this.userName = username;
        this.latestLoginTime = getCurrentTime();
    }

    public String getUserName() {
        return userName;
    }

    public static String getCurrentTime() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
        return ft.format(dNow);
    }
}
