package com.example.clubdictionary.MyPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;
import com.google.firebase.firestore.DocumentSnapshot;

public class ScrapItem {
    PostInfo scrapPost;

    public ScrapItem(PostInfo scrapPost) {
        this.scrapPost = scrapPost;
    }
}