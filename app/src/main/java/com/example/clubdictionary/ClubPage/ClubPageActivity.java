package com.example.clubdictionary.ClubPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.clubdictionary.ClubInfo;
import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClubPageActivity extends AppCompatActivity {
    private String TAG = "ClubPageActivity";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference userDocRef = null;            // 지금 동아리 페이지 보고 있는 유저의 문서 주소 - bookMark 등 업데이트 용
    DocumentReference clubDocRef = null;            // 해당 동아리 문서의 주소 - 이걸로 동아리 정보 수정하면 될 듯
    DocumentSnapshot userDoc = null;                //
    String minor = null;
    String clubName = null;
    Map<String, List<String>> bookMark = new HashMap<>();
    Map<String, Boolean> subscribers = new HashMap<>();
    boolean bookMarked;
    CircleImageView icon;
    ImageButton apply_btn;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_page);

        Toolbar toolbar = findViewById(R.id.club_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.club_activity_collapsebar);
        collapsingToolbarLayout.setTitle("동아리 페이지");

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager2);



        apply_btn = findViewById(R.id.bookMark);
        TextView nameTextView, day, activityTime, money, registerUrl;
        ImageButton back_btn = findViewById(R.id.back_btn);

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookMarked) {
                    bookMarked = false;
                    deleteBookMark();
                    apply_btn.setImageResource(R.drawable.png_bookmark);

                    //이미지설정?
                } else {
                    bookMarked = true;
                    addBookMark();
                    apply_btn.setImageResource(R.drawable.png_bookmark_selected);
                    // 이미지 설정?
                }
                Toast.makeText(ClubPageActivity.this, "" + bookMarked, Toast.LENGTH_SHORT).show();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClubPageActivity.super.onBackPressed();
            }
        });

        nameTextView = findViewById(R.id.nameTextView);
        day = findViewById(R.id.day);
        activityTime = findViewById(R.id.activityTime);
        money = findViewById(R.id.money);
        registerUrl = findViewById(R.id.registerUrl);
        icon = findViewById(R.id.icon);
        // FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        db.collection("clubs").whereEqualTo("name", name).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                clubDocRef = document.getReference();
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                ClubInfo clubInfo = document.toObject(ClubInfo.class);
                                nameTextView.setText(clubInfo.getName());
                                day.setText(clubInfo.getDay());
                                activityTime.setText(clubInfo.getActivityTime());
                                money.setText(clubInfo.getMoney());
                                clubName = clubInfo.getName();
                                minor = clubInfo.getMinor();
                                subscribers = clubInfo.getSubscribers();
                                String iconUrl = clubInfo.getIconUrl();
                                Glide.with(ClubPageActivity.this).load(iconUrl).into(icon);
                                icon.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                registerUrl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(clubInfo.getRegisterUrl()));
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        userDocRef = db.collection("users").document(user.getUid());
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    userDoc = task.getResult();
                    if (!userDoc.exists()) {
                        userDocRef = db.collection("clubs").document(user.getUid());
                        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.d("user", "동아리입니다");
                                    userDoc = task.getResult();
                                    getBookMark();
                                }
                            }
                        });
                    } else {
                        Log.d("user", "유저입니다");
                        getBookMark();
                    }
                } else Log.d("task", "실패");
            }
        });

        ClubPageViewPagerAdapter clubPageViewPagerAdapter = new ClubPageViewPagerAdapter(this, name);
        viewPager2.setAdapter(clubPageViewPagerAdapter);
        
        ArrayList<String> tabElement = new ArrayList<>();
        tabElement.add("게시물");
        tabElement.add("소개글");
        tabElement.add("Q&A");

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(ClubPageActivity.this);
                textView.setText(tabElement.get(position));
                textView.setGravity(Gravity.CENTER);
                tab.setCustomView(textView);
            }
        }).attach();

        tabLayout.setTabTextColors(R.color.gray_dbdbdb, R.color.maintheme);


    }

    private void getBookMark() {
        bookMark = (Map<String, List<String>>) userDoc.get("bookMark");
        for(String minor : bookMark.keySet()){
            ArrayList<String> minorList = new ArrayList<>();
            minorList = (ArrayList<String>) bookMark.get(minor);
            for(String club : minorList){
                Log.e(minor, club);
            }
        }
        for(String userUid : subscribers.keySet()){
            Log.e(userUid,  ""+subscribers.get(userUid));
        }
        if (subscribers.keySet().contains(user.getUid())) {
            bookMarked = true;
            apply_btn.setImageResource(R.drawable.png_bookmark_selected);
        } else {
            bookMarked = false;
            apply_btn.setImageResource(R.drawable.png_bookmark);
        }
        //Log.d("bookMark", bookMark.get("ball").get(0) + ", " + bookMark.get("ball").get(1));
        //Toast.makeText(this, ""+bookMarked, Toast.LENGTH_SHORT).show();
    }

    private void addBookMark() {
        if( bookMark.containsKey("minor")){
            bookMark.get("minor").add(clubName);
        }
        else {
            List<String> newList = new ArrayList<>();
            newList.add(clubName);
            bookMark.put(minor, newList);
        }
        userDocRef.update("bookMark."+minor, FieldValue.arrayUnion(clubName));

        // 동아리의 subscribers에 userUid add
        clubDocRef.update("subscribers." + user.getUid(), true);
    }

    private void deleteBookMark() {
        List<String> list = new ArrayList<>();
        list = bookMark.get(minor);
        list.remove(clubName);
        if(list.isEmpty()){
            userDocRef.update("bookMark."+minor, FieldValue.delete());
            bookMark.remove(minor);
        }
        else{
            userDocRef.update("bookMark."+minor, FieldValue.arrayRemove(clubName));
            bookMark.put(minor, list);
        }

        // 동아리의 subscribers에서 userUid 없애기
        clubDocRef.update("subscribers." + user.getUid(), FieldValue.delete());
    }
    public String getName(){
        return name;
    }
}