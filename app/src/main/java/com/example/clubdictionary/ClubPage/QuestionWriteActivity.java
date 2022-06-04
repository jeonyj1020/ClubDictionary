package com.example.clubdictionary.ClubPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import com.bumptech.glide.Glide;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class QuestionWriteActivity extends AppCompatActivity {


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String name;
    String questionStr, answerStr, answerId, questionId, itemId, itemTime;
    String clubUid;



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
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        //답변자 전화번호 저장하기
        db.collection("clubs").whereEqualTo("name", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ClubInfo clubInfo = document.toObject(ClubInfo.class);
                        answerId = clubInfo.getPhoneNum();
                        clubUid = clubInfo.getUid();
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //질문자 전화번호 저장하기
                db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            MemberInfo memberInfo = document.toObject(MemberInfo.class);
                            if (memberInfo != null) {
                                questionId = memberInfo.getPhoneNum();
                            }
                            else{
                                db.collection("clubs").document(clubUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            ClubInfo clubInfo = task.getResult().toObject(ClubInfo.class);
                                            if (memberInfo != null) {
                                                questionId = clubInfo.getPhoneNum();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });

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
                    questionStr = question.getText().toString();
                    //자기가 질문한 글 거기에 추가가 되어야하고 db에도 질문글 단위로 추가가 되어야하고 알람은 동아리에게 가야한다.
                    //db 등록
                    itemTime = String.valueOf(System.currentTimeMillis());
                    QuestionItem questionItem = new QuestionItem(questionStr, "", user.getUid(), clubUid,"",itemTime);
                    Log.e("0602", " "+questionStr + " "+questionId + " "+answerId +" "+ clubUid);
                    db.collection("clubs").document(clubUid).collection("QnA")
                            .add(questionItem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    toast("질문이 등록되었습니다.");
                                    documentReference.update("itemId", documentReference.getId());
                                    //문자 보내기
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

                            });
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

    private void sendSMS(){
        String phoneNo = answerId.trim();
        String SMS = "신비한 동아리 사전\n질문글이 달렸습니다.\n질문 내용: \n"+questionStr.trim();
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