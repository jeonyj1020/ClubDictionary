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

    ImageButton moveHome, moveBookmark, moveCategory, moveMypage;

    HomeFragment homeFragment = new HomeFragment();
    BookmarkFragment bookmarkFragment = new BookmarkFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    MyPageFragment movefragment = new MyPageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();

        moveHome = findViewById(R.id.movehome);
        moveBookmark = findViewById(R.id.movebookmark);
        moveCategory = findViewById(R.id.movecategory);
        moveMypage = findViewById(R.id.movemypage);

/*
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            //매인피드시작
        }
*/
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainframe, homeFragment);
        transaction.commit();

        moveHome.setOnClickListener(onClickListener);
        moveBookmark.setOnClickListener(onClickListener);
        moveCategory.setOnClickListener(onClickListener);
        moveMypage.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch (view.getId()){
                case R.id.movehome:
                    transaction.replace(R.id.mainframe, homeFragment);
                    transaction.commit();
                    return;
                case R.id.movebookmark:
                    transaction.replace(R.id.mainframe, bookmarkFragment);
                    transaction.commit();
                    return;
                case R.id.movecategory:
                    transaction.replace(R.id.mainframe, categoryFragment);
                    transaction.commit();
                    return;
                case R.id.movemypage:
                    transaction.replace(R.id.mainframe, movefragment);
                    transaction.commit();
                    return;
            }
        }
    };
}