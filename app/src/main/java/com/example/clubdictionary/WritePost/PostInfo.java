package com.example.clubdictionary.WritePost;

import java.util.ArrayList;

public class PostInfo {
    private String name;
    private String iconUrl;
    private String major;
    private String minor;
    private String upTime;
    private String hashTag;
    private String contents;
    private ArrayList<String> imageUrlList;

    public PostInfo(){}

    public PostInfo(String name, String iconUrl, String major, String minor, String upTime,
                    String hashTag, String contents, ArrayList<String> imageUrlList){
        this.name = name;
        this.iconUrl = iconUrl;
        this.major = major;
        this.minor = minor;
        this.upTime = upTime;
        this.hashTag = hashTag;
        this.contents = contents;
        this.imageUrlList = imageUrlList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public ArrayList<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(ArrayList<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
}
