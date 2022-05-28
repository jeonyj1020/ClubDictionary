package com.example.clubdictionary.Home;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeItem {

    private String clubName, major, minor, upTime, icon, hashTag, contents;
    ArrayList<String> images = new ArrayList<>(); // -> url로 가져옴

    public HomeItem() {
    }

    public HomeItem(String clubName, String major, String minor, String upTime, String icon, String hashTag, String contents, ArrayList<String> images) {
        this.clubName = clubName;
        this.major = major;
        this.minor = minor;
        this.upTime = upTime;
        this.icon = icon;
        this.hashTag = hashTag;
        this.contents = contents;
        this.images = images;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
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

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
