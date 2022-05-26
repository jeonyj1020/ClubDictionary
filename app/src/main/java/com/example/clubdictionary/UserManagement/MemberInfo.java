package com.example.clubdictionary.UserManagement;

import java.util.ArrayList;
import java.util.Arrays;

public class MemberInfo {
    private String name;
    private String schoolNum;
    private String department;
    private String phoneNum;
    private ArrayList<String> bookMark;
    private String filtering;
    private ArrayList<Boolean> checked;

    public MemberInfo(){}

    public MemberInfo(String name, String schoolNum, String department, String phoneNum, ArrayList<String> bookMark
            , String filtering, ArrayList<Boolean> checked) {
        this.name = name;
        this.schoolNum = schoolNum;
        this.department = department;
        this.phoneNum = phoneNum;
        this.bookMark = bookMark;
        this.filtering = filtering;
        this.checked = checked;
    }

    public MemberInfo(String name, String schoolNum, String department, String phoneNum) {
        this.name = name;
        this.schoolNum = schoolNum;
        this.department = department;
        this.phoneNum = phoneNum;
        this.bookMark = null;
        this.filtering = null;
        this.checked = new ArrayList<Boolean>(Arrays.asList(false, false));
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
}
