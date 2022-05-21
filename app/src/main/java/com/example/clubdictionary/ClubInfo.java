package com.example.clubdictionary;

import com.google.common.collect.ArrayTable;


import java.util.ArrayList;

public class ClubInfo {

    private String icon;
    private String name;
    private String day;
    private String money;
    private String activityTime;
    private String registerUrl;
    private String introduce;
    private ArrayList<String> introducePicture;
    private ArrayList<ArrayList<String>> QnA;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public ArrayList<String> getIntroducePicture() {
        return introducePicture;
    }

    public void setIntroducePicture(ArrayList<String> introducePicture) {
        this.introducePicture = introducePicture;
    }

    public ArrayList<ArrayList<String>> getQnA() {
        return QnA;
    }

    public void setQnA(ArrayList<ArrayList<String>> qnA) {
        QnA = qnA;
    }
}
