package com.example.clubdictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clubdictionary.Home.HomeFragment;
import com.example.clubdictionary.Home.HomeRecyclerViewAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> filtering;

    List<CheckBox> societyList = new ArrayList<>();
    List<CheckBox> studyList = new ArrayList<>();
    List<CheckBox> artsList = new ArrayList<>();
    List<CheckBox> sportsList = new ArrayList<>();
    List<CheckBox> religionList = new ArrayList<>();

    GridLayout society, study, arts, sports, religion;
    CheckBox societyChecked, studyChecked, artsChecked, sportsChecked, religionChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        findViewById(R.id.apply).setOnClickListener(onClickListener);
        findViewById(R.id.allUnCheck).setOnClickListener(onClickListener);
        societyChecked = (CheckBox) findViewById(R.id.societyChecked);
        studyChecked = (CheckBox) findViewById(R.id.studyChecked);
        artsChecked = (CheckBox) findViewById(R.id.artsChecked);
        sportsChecked = (CheckBox) findViewById(R.id.sportsChecked);
        religionChecked = (CheckBox) findViewById(R.id.religionChecked);

        societyChecked.setOnClickListener(onClickListener);
        society = findViewById(R.id.society);
        for(int i = 0; i < society.getChildCount(); i++){
            societyList.add((CheckBox) society.getChildAt(i));
        }

        studyChecked.setOnClickListener(onClickListener);
        study = findViewById(R.id.study);
        for(int i = 0; i < study.getChildCount(); i++){
            studyList.add((CheckBox) study.getChildAt(i));
        }

        artsChecked.setOnClickListener(onClickListener);
        arts = findViewById(R.id.arts);
        for(int i = 0; i < arts.getChildCount(); i++){
            artsList.add((CheckBox) arts.getChildAt(i));
        }

        sportsChecked.setOnClickListener(onClickListener);
        sports = findViewById(R.id.sports);
        for(int i = 0; i < sports.getChildCount(); i++){
            sportsList.add((CheckBox) sports.getChildAt(i));
        }

        religionChecked.setOnClickListener(onClickListener);
        religion = findViewById(R.id.religion);
        for(int i = 0; i < religion.getChildCount(); i++){
            religionList.add((CheckBox) religion.getChildAt(i));
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean allCheck;
            switch(view.getId()){

                case R.id.societyChecked:
                    allCheck = ((CheckBox)view).isChecked();
                    if(allCheck) {
                        for (CheckBox now : societyList){
                            now.setChecked(true);
                        }
                    }
                    else{
                        for (CheckBox now : societyList){
                            now.setChecked(false);
                        }
                    }
                    break;

                case R.id.studyChecked :
                    allCheck = ((CheckBox)view).isChecked();
                    if(allCheck) {
                        for (CheckBox now : studyList){
                            now.setChecked(true);
                        }
                    }
                    else{
                        for (CheckBox now : studyList){
                            now.setChecked(false);
                        }
                    }
                    break;

                case R.id.artsChecked :
                    allCheck = ((CheckBox)view).isChecked();
                    if(allCheck) {
                        for (CheckBox now : artsList){
                            now.setChecked(true);
                        }
                    }
                    else{
                        for (CheckBox now : artsList){
                            now.setChecked(false);
                        }
                    }
                    break;

                case R.id.sportsChecked :
                    allCheck = ((CheckBox)view).isChecked();
                    if(allCheck) {
                        for (CheckBox now : sportsList){
                            now.setChecked(true);
                        }
                    }
                    else{
                        for (CheckBox now : sportsList){
                            now.setChecked(false);
                        }
                    }
                    break;

                case R.id.religionChecked :
                    allCheck = ((CheckBox)view).isChecked();
                    if(allCheck) {
                        for (CheckBox now : religionList){
                            now.setChecked(true);
                        }
                    }
                    else{
                        for (CheckBox now : religionList){
                            now.setChecked(false);
                        }
                    }
                    break;

                case R.id.allUnCheck:
                    societyChecked.setChecked(false);
                    studyChecked.setChecked(false);
                    artsChecked.setChecked(false);
                    sportsChecked.setChecked(false);
                    religionChecked.setChecked(false);

                    for(CheckBox now : societyList){
                        now.setChecked(false);
                    }
                    for(CheckBox now : studyList){
                        now.setChecked(false);
                    }
                    for(CheckBox now : artsList){
                        now.setChecked(false);
                    }
                    for(CheckBox now : sportsList){
                        now.setChecked(false);
                    }
                    for(CheckBox now : religionList){
                        now.setChecked(false);
                    }
                    break;

                case R.id.apply :
                    ArrayList<String> checked = new ArrayList<>();
                    for(CheckBox now : societyList){
                        if(now.isChecked()) checked.add(now.getText().toString());
                    }
                    for(CheckBox now : studyList){
                        if(now.isChecked()) checked.add(now.getText().toString());
                    }
                    for(CheckBox now : artsList){
                        if(now.isChecked()) checked.add(now.getText().toString());
                    }
                    for(CheckBox now : sportsList){
                        if(now.isChecked()) checked.add(now.getText().toString());
                    }
                    for(CheckBox now : religionList) {
                        if (now.isChecked()) checked.add(now.getText().toString());
                    }

                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("filtering", checked);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    };
}
