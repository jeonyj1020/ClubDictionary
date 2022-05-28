package com.example.clubdictionary.BookMark;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookmarkItem {

    private String clubUid, clubName, major, minor;
    //private CircleImageView icon;
    //private ImageView bookMark;

    public BookmarkItem(){}

    public BookmarkItem(String clubUid, String clubName, String major, String minor){
        this.clubUid = clubUid;
        this.clubName = clubName;
        this.major = major;
        this.minor = minor;
    }

    public String getClubUid() {
        return clubUid;
    }
    public void setClubUid(String clubUid) {
        this.clubUid = clubUid;
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

    /*public CircleImageView getIcon() {
        return icon;
    }

    public void setIcon(CircleImageView icon) {
        this.icon = icon;
    }*/
}
