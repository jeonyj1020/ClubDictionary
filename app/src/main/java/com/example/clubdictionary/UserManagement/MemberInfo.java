package com.example.clubdictionary.UserManagement;

import java.util.ArrayList;
import java.util.Arrays;

public class MemberInfo {
    private String Uid;
    private String name;
    private String schoolNum;
    private String department;
    private String phoneNum;
    private ArrayList<String> bookMark;
    private String filtering;
    private ArrayList<Boolean> checked;
    private ArrayList<String> scrap;
    private ArrayList<String> questionList;
    private ArrayList<String> alarmList;

    public MemberInfo(String uid, String name, String schoolNum, String department, String phoneNum, ArrayList<String> bookMark, String filtering, ArrayList<Boolean> checked, ArrayList<String> scrap, ArrayList<String> questionList, ArrayList<String> alarmList) {
        this.Uid = uid;
        this.name = name;
        this.schoolNum = schoolNum;
        this.department = department;
        this.phoneNum = phoneNum;
        this.bookMark = bookMark;
        this.filtering = filtering;
        this.checked = checked;
        this.scrap = scrap;
        this.questionList = questionList;
        this.alarmList = alarmList;
    }


    public MemberInfo(String uid, String name, String schoolNum, String department, String phoneNum) {
        this.Uid = uid;
        this.name = name;
        this.schoolNum = schoolNum;
        this.department = department;
        this.phoneNum = phoneNum;

        this.bookMark = null;
        this.filtering = null;
        this.checked = new ArrayList<Boolean>(Arrays.asList(false, false));
        this.scrap = null;
        this.questionList = null;
        this.alarmList = null;
    }

    public MemberInfo(){}

    public MemberInfo(String name, String schoolNum, String department, String phoneNum, ArrayList<String> bookMark
            , String filtering, ArrayList<Boolean> checked) {
       this.Uid = "";
        this.name = name;
        this.schoolNum = schoolNum;
        this.department = department;
        this.phoneNum = phoneNum;
        this.bookMark = bookMark;
        this.filtering = filtering;
        this.checked = checked;
        this.scrap = null;
        this.questionList = null;
        this.alarmList = null;
    }

    public MemberInfo(String name, String schoolNum, String department, String phoneNum) {
        this.Uid = "";
        this.name = name;
        this.schoolNum = schoolNum;
        this.department = department;
        this.phoneNum = phoneNum;
        this.bookMark = null;
        this.filtering = null;
        this.checked = new ArrayList<Boolean>(Arrays.asList(false, false));
        this.scrap = null;
        this.questionList = null;
        this.alarmList = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolNum() {
        return schoolNum;
    }

    public void setSchoolNum(String schoolNum) {
        this.schoolNum = schoolNum;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public ArrayList<String> getBookMark() {return bookMark;}
    public void setBookMark(ArrayList<String> bookMark) {
        this.bookMark = bookMark;
    }

    public String getFiltering() {
        return filtering;
    }
    public void setFiltering(String filtering) { this.filtering = filtering;}

    public ArrayList<Boolean> getChecked(){return checked;}
    public void setChecked(ArrayList<Boolean> checked) {this.checked = checked;}

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
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

    public void setQuestionList(ArrayList<String> questionList) {
        this.questionList = questionList;
    }

    public ArrayList<String> getAlarmList() {
        return alarmList;
    }

    public void setAlarmList(ArrayList<String> alarmList) {
        this.alarmList = alarmList;
    }
}
