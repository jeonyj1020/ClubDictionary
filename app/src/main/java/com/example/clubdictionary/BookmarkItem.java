package com.example.clubdictionary;

import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookmarkItem {

    private CircleImageView clubProfile;
    private ImageView bookMark;
    private TextView clubName, clubHashtag;

    public CircleImageView getClubProfile() {
        return clubProfile;
    }

    public void setClubProfile(CircleImageView clubProfile) {
        this.clubProfile = clubProfile;
    }

    public ImageView getBookMark() {
        return bookMark;
    }

    public void setBookMark(ImageView bookMark) {
        this.bookMark = bookMark;
    }

    public TextView getClubName() {
        return clubName;
    }

    public void setClubName(TextView clubName) {
        this.clubName = clubName;
    }

    public TextView getClubHashtag() {
        return clubHashtag;
    }

    public void setClubHashtag(TextView clubHashtag) {
        this.clubHashtag = clubHashtag;
    }
}
