package com.example.clubdictionary.ClubPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clubdictionary.ClubInfo;
import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuestionUpdateActivity extends AppCompatActivity {
    EditText updateQuestion;
    Button updateButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String phoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_update);

        Toolbar toolbar = findViewById(R.id.qu_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        updateQuestion = findViewById(R.id.updateQuestion);
        updateButton = findViewById(R.id.updateButton);
        Intent intent = getIntent();
        String questionId = intent.getStringExtra("questionId");
        String answerId = intent.getStringExtra("answerId");
        String itemId = intent.getStringExtra("itemId");
        db.collection("clubs").document(answerId).collection("QnA").document(itemId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                QuestionItem questionItem = task.getResult().toObject(QuestionItem.class);
                updateQuestion.setText(questionItem.getQuestion());

                Log.e("?????? ?????? ????????? ??????", updateQuestion.getText().toString());
            }
        });
        db.collection("clubs").document(answerId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ClubInfo clubInfo = task.getResult().toObject(ClubInfo.class);
                phoneNo = clubInfo.getPhoneNum();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateQuestion.getText().length()>0){
                    db.collection("clubs").document(answerId).collection("QnA").document(itemId).update("question",updateQuestion.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    toast("????????? ??????????????? ?????????????????????. ????????? ??????????????? ????????? ???????????????.");
                                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                                        if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                                            sendSMS();
                                        }
                                        else{
                                            requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                                        }
                                    }
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    toast("????????? ???????????? ???????????????. ??????????????? ?????????.");
                                }
                            });
                }
                else{
                    toast("????????? ????????? ?????????.");
                }
            }
        });
    }

    private void toast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void sendSMS(){

        String SMS = "????????? ????????? ??????\n???????????? ?????????????????????.\n????????? ?????? ??????: \n"+updateQuestion.getText().toString().trim();
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo,null, SMS,null,null);
            toast("????????? ??????????????? ????????? ???????????????.");
        } catch (Exception e) {
            e.printStackTrace();
            toast("????????? ????????? ???????????????.");
        }
    }
}