package com.example.clubdictionary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.clubdictionary.BookMark.BookmarkFragment;
import com.example.clubdictionary.Category.CategoryFragment;
import com.example.clubdictionary.Group.GroupFragment;
import com.example.clubdictionary.Home.HomeFragment;
import com.example.clubdictionary.UserManagement.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    BookmarkFragment bookmarkFragment = new BookmarkFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    MyPageFragment mypageFragment = new MyPageFragment();
    GroupFragment groupFragment = new GroupFragment();
    // 필터링 관련 변수들
    DocumentReference docRef = null;
    ArrayList<String> filtering = new ArrayList<>();
    String filteringBinary = null;
    ArrayList<Boolean> checked = new ArrayList<>();
    CheckBox onlyFavorite, onlyRecruit;
    Map<String, List<String>> bookMark = null;

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

        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            //매인피드시작
            docRef = db.collection("clubs").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(!document.exists()){
                            docRef = db.collection("users").document(user.getUid());
                            Log.d("type", "유저입니다");
                            getFiltering();
                        }
                        else getFiltering();
                    }
                }
            });

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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.filter:
                    Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                    intent.putExtra("filteringBinary", filteringBinary);
                    startActivityForResult(intent, 1);
                    break;

                case R.id.onlyFavorite:
                case R.id.onlyRecruit:
                    checked.set(0, onlyRecruit.isChecked());
                    checked.set(1, onlyFavorite.isChecked());

                    docRef.update("checked", checked)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                    query();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    filtering = data.getStringArrayListExtra("newFiltering");
                    filteringBinary = data.getStringExtra("newFilteringBinary");
                    query();
                    break;
            }
        }
    }

    private void getFiltering(){
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            filtering = (ArrayList<String>) document.get("filtering");
                            filteringBinary = (String) document.get("filteringBinary");
                            checked = (ArrayList<Boolean>) document.get("checked");
                            bookMark = (Map<String, List<String>>) document.get("bookMark");
                            /*Map<String, List<String>> bookMark = new HashMap<>();
                            bookMark = (Map<String, List<String>>) document.get("bookMark");
                            List<String> newBookMark = new ArrayList<>();

                            if(bookMark.containsKey("ball")){
                                newBookMark = bookMark.get("ball");
                            }
                            newBookMark.add("스트라이크");
                            bookMark.put("ball", newBookMark);

                            docRef.update("bookMark", bookMark);*/
                            if (checked.get(0)) onlyRecruit.setChecked(true);
                            else onlyRecruit.setChecked(false);
                            if (checked.get(1)) onlyFavorite.setChecked(true);
                            else onlyFavorite.setChecked(false);

                            query();
                        }
                    }
                });
    }

    private void query() {
        boolean onlyRecruitChecked = onlyRecruit.isChecked();
        boolean onlyFavoriteChecked = onlyFavorite.isChecked();
        Log.d("filter", " \n" + filtering + "\n" + filteringBinary + "\n" + onlyRecruitChecked +
                ", " + onlyFavoriteChecked + "\n" + bookMark);

        ArrayList<String> intersection = null;
        /*if(onlyFavoriteChecked){
            for(){

            }
        }
        else{
            intersection = filtering;
        }*/
        /*ArrayList<Task> tasks = null;
        for(int i = 0; i <= filtering.size()/10; i++) {
            List<String> now = null;
            if(filtering.size() >= 10 * i) {
                now = filtering.subList(10 * i, 10 * i + 9);
            }
            else{
                now = filtering.subList(10 * i, filtering.size() - 1);
            }
            Query query = clubsRef.whereIn("minor", now);
            tasks.set(i, query.get());
        }*/
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