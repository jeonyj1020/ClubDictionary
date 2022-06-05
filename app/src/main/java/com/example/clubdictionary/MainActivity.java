package com.example.clubdictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.clubdictionary.BookMark.BookmarkFragment;
import com.example.clubdictionary.Category.Category2Fragment;
import com.example.clubdictionary.Group.GroupFragment;
import com.example.clubdictionary.Home.HomeFragment;
import com.example.clubdictionary.MyPage.MyPageFragment;
import com.example.clubdictionary.UserManagement.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    BookmarkFragment bookmarkFragment = new BookmarkFragment();
    Category2Fragment categoryFragment = new Category2Fragment();
    MyPageFragment mypageFragment = new MyPageFragment();
    GroupFragment groupFragment = new GroupFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();

        bottomNavigationView = findViewById(R.id.activity_main_navigation_bar);

        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            //매인피드시작

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainframe, homeFragment);
            transaction.commit();

            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    FragmentManager fm = getSupportFragmentManager();
                    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                    switch (item.getItemId()) {
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
                        case R.id.group:
                            transaction.replace(R.id.mainframe, groupFragment);
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