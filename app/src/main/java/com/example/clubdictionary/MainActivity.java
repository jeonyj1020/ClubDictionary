package com.example.clubdictionary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.clubdictionary.BookMark.BookmarkFragment;
import com.example.clubdictionary.Category.CategoryFragment;
import com.example.clubdictionary.Home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    BookmarkFragment bookmarkFragment = new BookmarkFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    MyPageFragment mypageFragment = new MyPageFragment();

    ArrayList<String> filtering = new ArrayList<>();
    CheckBox onlyFavorite, onlyRecruit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();

        bottomNavigationView = findViewById(R.id.activity_main_navigation_bar);

        findViewById(R.id.filter).setOnClickListener(onClickListener);
        onlyFavorite = findViewById(R.id.onlyFavorite);
        onlyFavorite.setOnClickListener(onClickListener);
        onlyRecruit = findViewById(R.id.onlyRecruit);
        onlyRecruit.setOnClickListener(onClickListener);

/*
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            //매인피드시작
        }
*/

        //이부분이 메인 피드 시작 부분
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainframe, homeFragment);
        transaction.commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragmentManager fm = getSupportFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                switch(item.getItemId()){
                    case R.id.home:

                        transaction.replace(R.id.mainframe, homeFragment);
                        transaction.commit();
                        return true;
                    case R.id.bookmark:
                        transaction.replace(R.id.mainframe, bookmarkFragment);
                        transaction.commit();
                        return true;
                    case R.id.category:
                        transaction.replace(R.id.mainframe, categoryFragment);
                        transaction.commit();
                        return true;
                    case R.id.mypage:
                        transaction.replace(R.id.mainframe, mypageFragment);
                        transaction.commit();
                        return true;
                }
                return false;
            }

        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 필터 버튼, 찜한 동아리, 모집공고 3개 중 하나라도 눌리면 바로 쿼리 함수 변경해서 쿼리해야함
            // 필터 버튼의 선택된 소분류들을 메인에서 받은 다음에 쿼리 - onActivityResult 에서 쿼리처리
            // 체크박스는 둘 중 하나라도 상태 변경되면 바로 쿼리 - switch 문에서 바로 쿼리
            boolean onlyFavoriteChecked, onlyRecruitChecked;

            switch (v.getId()){
                case R.id.filter:
                    Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                    startActivityForResult(intent, 1);
                    break;

                case R.id.onlyFavorite:
                case R.id.onlyRecruit:
                    onlyFavoriteChecked = onlyFavorite.isChecked();
                    onlyRecruitChecked = onlyRecruit.isChecked();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==  RESULT_OK){
            switch(requestCode){
                case 1 :
                    filtering = data.getStringArrayListExtra("filtering");
                    String show = "";
                    for(String now : filtering){
                        show += now + " ";
                    }

                    Toast.makeText(this, "" + show, Toast.LENGTH_LONG).show();
                    // 이 밑에서 쿼리
                    break;
            }
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, fragment).addToBackStack(null).commit();
    }

    /*public void toast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }*/
}