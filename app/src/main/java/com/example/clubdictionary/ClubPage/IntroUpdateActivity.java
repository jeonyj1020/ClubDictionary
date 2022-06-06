package com.example.clubdictionary.ClubPage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.clubdictionary.R;

public class IntroUpdateActivity extends AppCompatActivity {

    EditText day, money, time, introduce, applyLink;
    Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_update);

        Toolbar toolbar = findViewById(R.id.aiu_tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        day = findViewById(R.id.day);
        money = findViewById(R.id.money);
        time = findViewById(R.id.time);
        introduce = findViewById(R.id.introduce);
        applyLink = findViewById(R.id.applyLink);
        applyButton = findViewById(R.id.applyButton);

        Intent intent = getIntent();


    }
}