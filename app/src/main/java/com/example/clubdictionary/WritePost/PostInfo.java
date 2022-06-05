package com.example.clubdictionary.WritePost;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class PostInfo implements Comparable<PostInfo>, Serializable {
    private String name;
    private String iconUrl;
    private String major;
    private String minor;
    private long upTime;
    private String hashTag;
    private String contents;
    private boolean recruit;
    private ArrayList<String> imageUrlList;
    private String clubUid;

    public PostInfo(){}

    public PostInfo(String name, String iconUrl, String major, String minor, long upTime,
                    String hashTag, String contents, ArrayList<String> imageUrlList, boolean recruit,
                    String clubUid){
        this.name = name;
        this.iconUrl = iconUrl;
        this.major = major;
        this.minor = minor;
        this.upTime = upTime;
        this.hashTag = hashTag;
        this.contents = contents;
        this.imageUrlList = imageUrlList;
        this.recruit = recruit;
        this.clubUid = clubUid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUpTime() {
        return upTime;
    }

    public void setUpTime(long upTime) {
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

    public boolean isRecruit() {
        return recruit;
    }

    public void setRecruit(boolean recruit) {
        this.recruit = recruit;
    }

    public String getClubUid() {
        return clubUid;
    }

    public void setClubUid(String clubUid) {
        this.clubUid = clubUid;
    }

    @Override
    public int compareTo(PostInfo postInfo) {
        if(this.upTime < postInfo.upTime){
            return 1;
        }
        else if(this.upTime == postInfo.upTime){
            return 0;
        }
        else{
            return -1;
        }
    }


}
