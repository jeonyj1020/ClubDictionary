package com.example.clubdictionary.Group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.clubdictionary.R;

public class GroupPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_post);
        TextView title, content, date, kakaoLink;
        title= findViewById(R.id.title);
        content = findViewById(R.id.content);
        date  = findViewById(R.id.date);
        kakaoLink = findViewById(R.id.kakaoLink);
        Intent intent = getIntent();

        title.setText(intent.getStringExtra("title"));
        content.setText(intent.getStringExtra("content"));
        date.setText(intent.getStringExtra("date"));
        kakaoLink.setText(intent.getStringExtra("kakaoLink"));
        kakaoLink.setPaintFlags(kakaoLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        kakaoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(kakaoLink.getText().toString()));
                startActivity(intent);
            }
        });
    }
}