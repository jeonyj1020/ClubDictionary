package com.example.clubdictionary.ClubPage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clubdictionary.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuestionWriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_write);
        FirebaseFirestore db= FirebaseFirestore.getInstance();

        TextView title;
        Button applyButton;
        EditText question;
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