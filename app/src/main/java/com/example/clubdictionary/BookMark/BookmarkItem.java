package com.example.clubdictionary.BookMark;

import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookmarkItem {

    private String clubProfile, clubName, major, minor;
    //private ImageView bookMark;

    public BookmarkItem(){}

    public BookmarkItem(String clubProfile, String clubName, String major, String minor){
        this.clubProfile = clubProfile;
        this.clubName = clubName;
        this.major = major;
        this.minor = minor;
    }

    public String getClubProfile() {
        return clubProfile;
    }
    public void setClubProfile(String clubProfile) {
        this.clubProfile = clubProfile;
    }

    //public ImageView getBookMark() {return bookMark;}
    //public void setBookMark(ImageView bookMark) {this.bookMark = bookMark;}

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
}
