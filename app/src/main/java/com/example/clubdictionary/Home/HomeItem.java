package com.example.clubdictionary.Home;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeItem {

    private String clubName, minor, iconUrl, hashTag, contents;
    private long upTime;
    private boolean recruit;
    ArrayList<String> imageUrlList = new ArrayList<>(); // -> url로 가져옴

    public HomeItem() {}

    public HomeItem(String clubName, String minor, long upTime, String iconUrl, String hashTag,
                    String contents, ArrayList<String> imageUrlList, boolean recruit) {
        this.clubName = clubName;
        this.minor = minor;
        this.upTime = upTime;
        this.iconUrl = iconUrl;
        this.hashTag = hashTag;
        this.contents = contents;
        this.imageUrlList = imageUrlList;
        this.recruit = recruit;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public long getUpTime() {
        return upTime;
    }

    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String icon) {
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

    public void setImageUrlList(ArrayList<String> imagesUrl) {
        this.imageUrlList = imageUrlList;
    }

    public boolean isRecruit() {
        return recruit;
    }

    public void setRecruit(boolean recruit) {
        this.recruit = recruit;
    }
}
