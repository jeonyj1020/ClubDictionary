package com.example.clubdictionary.ClubPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

                Log.e("기존 질문 있는지 체크", updateQuestion.getText().toString());
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
                                    toast("질문이 성공적으로 수정되었습니다. 동아리 관리자에게 알림을 보냈습니다.");
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
                                    toast("질문이 수정되지 못했습니다. 다시시도해 주세요.");
                                }
                            });
                }
                else{
                    toast("내용을 입력해 주세요.");
                }
            }
        });
    }

    private void toast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void sendSMS(){

        String SMS = "신비한 동아리 사전\n질문글이 수정되었습니다.\n수정된 질문 내용: \n"+updateQuestion.getText().toString().trim();
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo,null, SMS,null,null);
            toast("동아리 관리자에게 알림을 보냈습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            toast("알림을 보내지 못했습니다.");
        }
    }
}