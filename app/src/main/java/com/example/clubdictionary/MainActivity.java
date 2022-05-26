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
import com.example.clubdictionary.UserManagement.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    BookmarkFragment bookmarkFragment = new BookmarkFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    MyPageFragment mypageFragment = new MyPageFragment();

    ArrayList<String> filtering = new ArrayList<>();
    String filteringBinary = null;
    ArrayList<Boolean> checked = new ArrayList<>();
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

        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            //매인피드시작
            getFiltering();
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
                    if (filteringBinary == null || filteringBinary.isEmpty())
                        query(true, onlyFavorite.isChecked(), onlyRecruit.isChecked());
                    else
                        query(false, onlyFavorite.isChecked(), onlyRecruit.isChecked());
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
                    if (filtering == null || filtering.isEmpty())
                        query(true, onlyFavorite.isChecked(), onlyRecruit.isChecked());
                    else
                        query(false, onlyFavorite.isChecked(), onlyRecruit.isChecked());
                    break;
            }
        }
    }

    private void query(boolean isFilteringEmpty, boolean onlyFavoriteChecked, boolean onlyRecruitChecked) {
        Toast.makeText(this, ""+filtering, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "" + onlyFavoriteChecked + onlyRecruitChecked, Toast.LENGTH_SHORT).show();
    }

    private void getFiltering(){
        db.collection("users").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            filteringBinary = (String) document.get("filtering");
                            checked = (ArrayList<Boolean>) document.get("checked");

                            if (checked.get(0)) onlyRecruit.setChecked(true);
                            else onlyRecruit.setChecked(false);
                            if (checked.get(1)) onlyFavorite.setChecked(true);
                            else onlyFavorite.setChecked(false);
                            Toast.makeText(MainActivity.this, "" + filteringBinary, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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