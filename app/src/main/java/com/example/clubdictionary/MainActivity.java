package com.example.clubdictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;

    ImageButton movehome, movebookmark, movecategory, movemypage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();

        movehome = findViewById(R.id.movehome);
        movebookmark = findViewById(R.id.movebookmark);
        movecategory = findViewById(R.id.movecategory);
        movemypage = findViewById(R.id.movemypage);

/*
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            //매인피드시작
        }
*/

        movehome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.mainframe, homeFragment);
                transaction.commit();
            }
        });
        movebookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                BookmarkFragment bookmarkFragment = new BookmarkFragment();
                transaction.replace(R.id.mainframe, bookmarkFragment);
                transaction.commit();
            }
        });
        movecategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                CategoryFragment categoryFragment = new CategoryFragment();
                transaction.replace(R.id.mainframe, categoryFragment);
                transaction.commit();
            }
        });
        movemypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                MyPageFragment movefragment = new MyPageFragment();
                transaction.replace(R.id.mainframe, movefragment);
                transaction.commit();
            }
        });


    }

}