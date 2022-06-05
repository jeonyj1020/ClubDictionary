package com.example.clubdictionary.MyPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;

import java.util.ArrayList;

public class ScrapListActivity extends AppCompatActivity {
    ArrayList<PostInfo> scrapPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_list);

        scrapPostList = (ArrayList<PostInfo>) getIntent().getSerializableExtra("scrapPostList");
        GridView gridView = findViewById(R.id.scrapListGridView);
        ScrapAdapter adapter = new ScrapAdapter(scrapPostList, ScrapListActivity.this);
        gridView.setAdapter(adapter);
    }
}