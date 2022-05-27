package com.example.clubdictionary.Group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class GroupWriteActivity extends AppCompatActivity {
    private static final String TAG = "GroupWriteActivity";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_write);
        EditText title = findViewById(R.id.title);
        EditText content = findViewById(R.id.content);
        Button applyButton= findViewById(R.id.applyButton);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileUpdate();
                finish();
            }
        });
    }

    private void profileUpdate(){
        EditText title = findViewById(R.id.title);
        EditText content = findViewById(R.id.content);
        EditText kakaoLink = findViewById(R.id.kakaoLink);
        String titleStr = title.getText().toString();
        String contentStr = content.getText().toString();
        String kakaoLinkStr =kakaoLink.getText().toString();
        if(title.length()>0 && content.length()>0 && kakaoLinkStr.length() > 0){
            GroupPostInfo groupPostInfo = new GroupPostInfo(titleStr, contentStr, kakaoLinkStr, user.getUid());
            uploader(groupPostInfo);
        }
        else{
            toast("제목과 내용을 모두 입력해주세요.\n카카오톡링크는 필수입니다.");
        }
    }

    private void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void uploader(GroupPostInfo groupPostInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("group").add(groupPostInfo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
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