package com.example.clubdictionary;

import android.content.Intent;
import android.database.Observable;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String filteringBinary;
    String newFilteringBinary = "";

    List<CheckBox> societyList = new ArrayList<>();
    List<CheckBox> studyList = new ArrayList<>();
    List<CheckBox> artsList = new ArrayList<>();
    List<CheckBox> sportsList = new ArrayList<>();
    List<CheckBox> religionList = new ArrayList<>();
    List<CheckBox> allList = new ArrayList<>();
    List<CheckBox> allChecked = new ArrayList<>();
    ArrayList<Integer> childCnt = new ArrayList<>();
    Toolbar toolbar;
    ArrayList<String> allMinor = new ArrayList<String>(Arrays.asList(
            "idea",
            "it", "liberalArts", "nature", "idea",
            "band", "instrument", "song", "play", "art", "cook", "dance", "picture", "handicraft",
            "ball", "racket", "martialArts", "extreme", "archery", "game",
            "christian", "buddhism", "catholic", "transpiration"
    ));

    GridLayout society, study, arts, sports, religion;
    CheckBox societyChecked, studyChecked, artsChecked, sportsChecked, religionChecked;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        filteringBinary = getIntent().getStringExtra("filteringBinary");

        toolbar = findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.apply).setOnClickListener(onClickListener);
        findViewById(R.id.allUnCheck).setOnClickListener(onClickListener);

        societyChecked = (CheckBox) findViewById(R.id.societyChecked);
        allChecked.add((CheckBox) societyChecked);
        studyChecked = (CheckBox) findViewById(R.id.studyChecked);
        allChecked.add((CheckBox) studyChecked);
        artsChecked = (CheckBox) findViewById(R.id.artsChecked);
        allChecked.add((CheckBox) artsChecked);
        sportsChecked = (CheckBox) findViewById(R.id.sportsChecked);
        allChecked.add((CheckBox) sportsChecked);
        religionChecked = (CheckBox) findViewById(R.id.religionChecked);
        allChecked.add((CheckBox) religionChecked);

        societyChecked.setOnClickListener(onClickListener);
        society = findViewById(R.id.society);
        for (int i = 0; i < society.getChildCount(); i++) {
            societyList.add((CheckBox) society.getChildAt(i));
            allList.add((CheckBox) society.getChildAt(i));
        }
        childCnt.add(societyList.size());

        studyChecked.setOnClickListener(onClickListener);
        study = findViewById(R.id.study);
        for (int i = 0; i < study.getChildCount(); i++) {
            studyList.add((CheckBox) study.getChildAt(i));
            allList.add((CheckBox) study.getChildAt(i));
        }
        childCnt.add(studyList.size());

        artsChecked.setOnClickListener(onClickListener);
        arts = findViewById(R.id.arts);
        for (int i = 0; i < arts.getChildCount(); i++) {
            artsList.add((CheckBox) arts.getChildAt(i));
            allList.add((CheckBox) arts.getChildAt(i));
        }
        childCnt.add(artsList.size());

        sportsChecked.setOnClickListener(onClickListener);
        sports = findViewById(R.id.sports);
        for (int i = 0; i < sports.getChildCount(); i++) {
            sportsList.add((CheckBox) sports.getChildAt(i));
            allList.add((CheckBox) sports.getChildAt(i));
        }
        childCnt.add(sportsList.size());

        religionChecked.setOnClickListener(onClickListener);
        religion = findViewById(R.id.religion);
        for (int i = 0; i < religion.getChildCount(); i++) {
            religionList.add((CheckBox) religion.getChildAt(i));
            allList.add((CheckBox) religion.getChildAt(i));
        }
        childCnt.add(religionList.size());

        Toast.makeText(this, ""+filteringBinary, Toast.LENGTH_LONG).show();
        if (filteringBinary == null) {
            for (CheckBox now : allList) {
                now.setChecked(true);
            }
            for (CheckBox now : allChecked) {
                now.setChecked(true);
            }
        }
        else {
            if (!filteringBinary.isEmpty()) {
                int index = -1;
                for(int i = 0; i<5; i++){
                    int n = childCnt.get(i);
                    int checkedCnt = 0;
                    for(int j = 1; j<=n; j++){
                        index++;
                        if(filteringBinary.charAt(index) == '1') {
                            allList.get(index).setChecked(true);
                            checkedCnt++;
                        }
                    }
                    if(checkedCnt == n) allChecked.get(i).setChecked(true);
                }
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean allCheck;
            switch (view.getId()) {
                case R.id.societyChecked:
                    allCheck = ((CheckBox) view).isChecked();
                    if (allCheck) {
                        for (CheckBox now : societyList) {
                            now.setChecked(true);
                        }
                    } else {
                        for (CheckBox now : societyList) {
                            now.setChecked(false);
                        }
                    }
                    break;

                case R.id.studyChecked:
                    allCheck = ((CheckBox) view).isChecked();
                    if (allCheck) {
                        for (CheckBox now : studyList) {
                            now.setChecked(true);
                            now.setTextColor(Color.RED);
                        }
                    } else {
                        for (CheckBox now : studyList) {
                            now.setChecked(false);
                        }
                    }

                    break;

                case R.id.artsChecked:
                    allCheck = ((CheckBox) view).isChecked();
                    if (allCheck) {
                        for (CheckBox now : artsList) {
                            now.setChecked(true);
                        }
                    } else {
                        for (CheckBox now : artsList) {
                            now.setChecked(false);
                        }
                    }
                    break;

                case R.id.sportsChecked:
                    allCheck = ((CheckBox) view).isChecked();
                    if (allCheck) {
                        for (CheckBox now : sportsList) {
                            now.setChecked(true);
                        }
                    } else {
                        for (CheckBox now : sportsList) {
                            now.setChecked(false);
                        }
                    }
                    break;

                case R.id.religionChecked:
                    allCheck = ((CheckBox) view).isChecked();
                    if (allCheck) {
                        for (CheckBox now : religionList) {
                            now.setChecked(true);
                        }
                    } else {
                        for (CheckBox now : religionList) {
                            now.setChecked(false);
                        }
                    }
                    break;

                case R.id.allUnCheck:
                    for (CheckBox now : allChecked) {
                        now.setChecked(false);
                    }
                    for (CheckBox now : allList) {
                        now.setChecked(false);
                    }
                    break;

                case R.id.apply:

                    ArrayList<String> newFiltering = new ArrayList<>();

                    for(int idx = 0; idx < allList.size(); idx++){
                        CheckBox now = allList.get(idx);
                        if(now.isChecked()) {
                            newFilteringBinary += "1";
                            newFiltering.add(allMinor.get(idx));
                        }
                        else newFilteringBinary += "0";
                    }

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DocumentReference docRef = db.collection("users").document(user.getUid());
                    docRef.update("filtering", newFiltering, "filteringBinary", newFilteringBinary)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(FilterActivity.this, "펄터링을 수정했습니다",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putStringArrayListExtra("newFiltering", newFiltering);
                                    intent.putExtra("newFilteringBinary", newFilteringBinary);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(FilterActivity.this, "펄터링 수정에 실패했습니다!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
            }
        }
    };
}
