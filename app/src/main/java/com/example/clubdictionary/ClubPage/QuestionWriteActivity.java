package com.example.clubdictionary.ClubPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clubdictionary.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuestionWriteActivity extends AppCompatActivity {


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_write);
        FirebaseFirestore db= FirebaseFirestore.getInstance();

        TextView title;
        Button applyButton;
        EditText question;

        Toolbar toolbar = findViewById(R.id.qw_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.title);
        question = findViewById(R.id.question);
        applyButton = findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(question.getText().length()>0){

                }
                else{
                    toast("질문을 입력해주세요.");
                }
            }
        });
    }

    private void toast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}