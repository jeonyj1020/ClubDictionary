package com.example.clubdictionary.ClubPage;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.clubdictionary.ClubInfo;
import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClubPageActivity extends AppCompatActivity {
    private String TAG = "ClubPageActivity";

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

        ClubPageViewPagerAdapter clubPageViewPagerAdapter = new ClubPageViewPagerAdapter(this);
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
                tab.setCustomView(textView);
            }
        }).attach();



        TextView Uid, icon, nameTextView, day, activityTime, money, registerUrl;
/*        Uid = findViewById(R.id.Uid);
        icon = findViewById(R.id.icon);
        nameTextView = findViewById(R.id.nameTextView);
        day = findViewById(R.id.day);
        activityTime = findViewById(R.id.activityTime);
        money = findViewById(R.id.money);
        registerUrl = findViewById(R.id.registerUrl);*/

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        db.collection("clubs").whereEqualTo("name", name).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                /*Uid.setText(document.get("Uid").toString());
                                icon.setText(document.get("icon").toString());
                                nameTextView.setText(document.get("name").toString());
                                day.setText(document.get("day").toString());
                                hour.setText(document.get("hour").toString());
                                money.setText(document.get("money").toString());
                                registerUrl.setText(document.get("registerUrl").toString());*/
                                ClubInfo clubInfo = document.toObject(ClubInfo.class);
/*                                icon.setText(clubInfo.getIcon());
                                nameTextView.setText(clubInfo.getName());
                                day.setText(clubInfo.getDay());
                                activityTime.setText(clubInfo.getActivityTime());
                                money.setText(clubInfo.getMoney());
                                registerUrl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(clubInfo.getRegisterUrl()));
                                        startActivity(intent);
                                    }
                                });*/
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}