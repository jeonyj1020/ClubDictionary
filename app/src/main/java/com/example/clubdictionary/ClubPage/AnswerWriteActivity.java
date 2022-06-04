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
import com.example.clubdictionary.UserManagement.MemberInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AnswerWriteActivity extends AppCompatActivity {
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    EditText answerStr;
    Button answerButton;

    String phoneNo;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_write);

        Intent intent = getIntent();
        String questionId = intent.getStringExtra("questionId");
        String answerId = intent.getStringExtra("answerId");
        String itemId = intent.getStringExtra("itemId");

        answerStr = findViewById(R.id.answerStr);
        answerButton = findViewById(R.id.answerButton);
        //질문자 번호 구하기
        db.collection("users").document(questionId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    MemberInfo memberInfo = task.getResult().toObject(MemberInfo.class);
                    phoneNo = memberInfo.getPhoneNum();
                }
                else{
                    db.collection("clubs").document(questionId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            ClubInfo clubInfo = task.getResult().toObject(ClubInfo.class);
                            phoneNo = clubInfo.getPhoneNum();
                        }
                    });
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            //답변 수정 문자보내기
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                db.collection("clubs").document(answerId).collection("QnA").document(itemId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        QuestionItem questionItem = task.getResult().toObject(QuestionItem.class);
                        answerStr.setText(questionItem.getAnswer());
                        Log.e("기존 답변 있는지 체크", answerStr.getText().toString());
                    }
                });
                answerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(answerStr.getText().length()>0){
                            db.collection("clubs").document(answerId).collection("QnA").document(itemId).update("answer",answerStr.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                                                if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                                                    sendSMS();
                                                    toast("답변이 성공적으로 달렸습니다.질문한 사람에게 알림을 보냈습니다.");
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
                                            toast("답변이 달리지 못했습니다. 다시시도해 주세요.");
                                        }
                                    });
                        }
                        else{
                            toast("내용을 입력해 주세요.");
                        }
                    }
                });
            }
        });




    }

    private void toast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void sendSMS(){
        String SMS = "신비한 동아리 사전\n답변이 달렸습니다.\n답변 내용: \n"+answerStr.getText().toString().trim();
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo,null, SMS,null,null);
            toast("질문자에게 알림을 보냈습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            toast("알림을 보내지 못했습니다.");
        }
    }
}