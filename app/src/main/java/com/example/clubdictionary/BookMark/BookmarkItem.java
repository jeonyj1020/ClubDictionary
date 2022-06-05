package com.example.clubdictionary.BookMark;

import com.google.firebase.firestore.DocumentReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookmarkItem {

    private String iconUrl, clubName, major, minor;
    private boolean alarmChecked;
    private DocumentReference clubRef;

    public BookmarkItem(){}

    public BookmarkItem(String iconUrl, String clubName, String major, String minor, boolean alarmChecked,
                        DocumentReference clubRef){
        this.iconUrl = iconUrl;
        this.clubName = clubName;
        this.major = major;
        this.minor = minor;
        this.alarmChecked = alarmChecked;
        this.clubRef = clubRef;
    }

    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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

    public boolean isAlarmChecked() {
        return alarmChecked;
    }

    public void setAlarmChecked(boolean alarmChecked) {
        this.alarmChecked = alarmChecked;
    }

    public DocumentReference getClubRef() {
        return clubRef;
    }

    public void setClubRef(DocumentReference clubRef) {
        this.clubRef = clubRef;
    }
}
