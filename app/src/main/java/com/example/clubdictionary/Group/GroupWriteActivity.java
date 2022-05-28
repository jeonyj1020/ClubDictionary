package com.example.clubdictionary.Group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clubdictionary.GroupPostInfo;
import com.example.clubdictionary.R;
import com.example.clubdictionary.UserManagement.MemberInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupWriteActivity extends AppCompatActivity {
    private static final String TAG = "GroupWriteActivity";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    long lnow = System.currentTimeMillis();
    String now = String.valueOf(lnow);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_write);
        EditText title = findViewById(R.id.title);
        EditText content = findViewById(R.id.content);
        Button applyButton = findViewById(R.id.applyButton);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileUpdate();

            }
        });
    }

    private void profileUpdate() {
        EditText title = findViewById(R.id.title);
        EditText content = findViewById(R.id.content);
        EditText kakaoLink = findViewById(R.id.kakaoLink);
        String titleStr = title.getText().toString();
        String contentStr = content.getText().toString();
        String kakaoLinkStr = kakaoLink.getText().toString();

        Date date = new Date(lnow);
        SimpleDateFormat sdf = new SimpleDateFormat("MM / dd HH : mm");
        String getTime = sdf.format(date);
        if (title.length() > 0 && content.length() > 0 && kakaoLinkStr.length() > 0) {
            GroupPostInfo groupPostInfo = new GroupPostInfo(titleStr, contentStr, kakaoLinkStr, user.getUid(), getTime, now);
            uploader(groupPostInfo);
            Log.e("!!!", "작성됨");
            finish();
        } else {
            toast("제목과 내용을 모두 입력해주세요.\n카카오톡링크는 필수입니다.");
        }
    }

    private void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void uploader(GroupPostInfo groupPostInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("group").document(now).set(groupPostInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                toast("게시글을 등록하였습니다.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                toast("게시글을 등록하지 못했습니다.");
            }
        });
    }

}