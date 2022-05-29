package com.example.clubdictionary;


import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClubInfo {
    private String Uid;
    private String iconUrl;
    private String name;
    private String day;
    private String activityTime;
    private String major;
    private String minor;
    private String money;
    private String registerUrl;
    private String filteringBinary;
    private String introduce;
    private ArrayList<String> filtering;
    private ArrayList<String> introducePicture;
    private ArrayList<Boolean> checked;
    private ArrayList<String> scrap;
    private ArrayList<String> questionList;
    private ArrayList<String> alarmList;
    private ArrayList<String> subscribers;
    private CollectionReference QnA;
    private CollectionReference posts;
    private Map<String, List<String>> bookMark;

    public  ClubInfo(){}

    public ClubInfo(String uid, String iconUrl, String name, String day, String activityTime, String major, String minor,
                    String money, String registerUrl, String filteringBinary, String introduce, ArrayList<String> introducePicture,
                    ArrayList<Boolean> checked, ArrayList<String> scrap, ArrayList<String> questionList, ArrayList<String> alarmList,
                    ArrayList<String> subscribers, ArrayList<String> filtering, Map<String, List<String>> bookMark) {
        Uid = uid;
        this.iconUrl = iconUrl;
        this.name = name;
        this.day = day;
        this.activityTime = activityTime;
        this.major = major;
        this.minor = minor;
        this.money = money;
        this.registerUrl = registerUrl;
        this.filteringBinary = filteringBinary;
        this.introduce = introduce;
        this.introducePicture = introducePicture;
        this.checked = checked;
        this.scrap = scrap;
        this.questionList = questionList;
        this.alarmList = alarmList;
        this.subscribers = subscribers;
        this.filtering = filtering;
        this.bookMark = bookMark;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public String getFilteringBinary() {
        return filteringBinary;
    }
    public void setFilteringBinary(String filteringBinary) {
        this.filteringBinary = filteringBinary;
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

    public ArrayList<Boolean> getChecked() {
        return checked;
    }
    public void setChecked(ArrayList<Boolean> checked) {
        this.checked = checked;
    }

    public ArrayList<String> getFiltering() {
        return filtering;
    }
    public void setCFiltering(ArrayList<String> filtering) {
        this.filtering = filtering;
    }

    public ArrayList<String> getScrap() {
        return scrap;
    }
    public void setScrap(ArrayList<String> scrap) {
        this.scrap = scrap;
    }

    public ArrayList<String> getQuestionList() {
        return questionList;
    }
    public void setQuestionList(ArrayList<String> questionList) {this.questionList = questionList;}

    public ArrayList<String> getAlarmList() {
        return alarmList;
    }
    public void setAlarmList(ArrayList<String> alarmList) {
        this.alarmList = alarmList;
    }

    public ArrayList<String> getSubscribers() {
        return subscribers;
    }
    public void setSubscribers(ArrayList<String> subscribers) {
        this.subscribers = subscribers;
    }
    public CollectionReference getQnA() {
        return QnA;
    }

    public void setQnA(CollectionReference qnA) {
        QnA = qnA;
    }

    public CollectionReference getPosts() {
        return posts;
    }

    public void setPosts(CollectionReference posts) {
        this.posts = posts;
    }

    public Map<String, List<String>> getBookMark(){return bookMark;}
    public void setBookMark(Map<String, List<String>> bookMark) {
        this.bookMark = bookMark;
    }
}
