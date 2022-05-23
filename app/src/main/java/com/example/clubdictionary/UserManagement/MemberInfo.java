package com.example.clubdictionary.UserManagement;

import java.util.ArrayList;

public class MemberInfo {
    private String name;
    private String schoolNum;
    private String department;
    private String phoneNum;
    private ArrayList<String>BookMark;

    public ArrayList<String> getBookMark() {
        return BookMark;
    }

    public void setBookMark(ArrayList<String> bookMark) {
        BookMark = bookMark;
    }

    public MemberInfo(){}

    public MemberInfo(String name, String schoolNum, String department, String phoneNum, ArrayList<String> bookMark) {
        this.name = name;
        this.schoolNum = schoolNum;
        this.department = department;
        this.phoneNum = phoneNum;
        this.BookMark = bookMark;
    }

    public MemberInfo(String name, String schoolNum, String department, String phoneNum) {
        this.name = name;
        this.schoolNum = schoolNum;
        this.department = department;
        this.phoneNum = phoneNum;
        this.BookMark = null;

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
}
