package com.example.clubdictionary.MyPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.clubdictionary.ClubPage.ClubPageActivity;
import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowScrapPost extends AppCompatActivity {
    PostInfo scrapPost;
    CircleImageView icon;
    TextView clubName, upTime, minor, hashTag, contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scrap_post);
        scrapPost = (PostInfo) getIntent().getSerializableExtra("scrapPost");

        icon = findViewById(R.id.icon);
        Glide.with(this).load(scrapPost.getIconUrl()).into(icon);
        icon.setOnClickListener(onClickListener);

        clubName.findViewById(R.id.clubName);
        clubName.setText(scrapPost.getName());
        clubName.setOnClickListener(onClickListener);

        upTime.setText(String.valueOf(scrapPost.getUpTime()));
        minor.setText(scrapPost.getMinor());
        hashTag.setText(scrapPost.getHashTag());
        contents.setText(scrapPost.getContents());
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.icon :
                case R.id.clubName:
                    Intent intent = new Intent(ShowScrapPost.this, ClubPageActivity.class);
                    intent.putExtra("name", clubName.getText().toString());
                    startActivity(intent);
                    break;
            }
        }
    };
}