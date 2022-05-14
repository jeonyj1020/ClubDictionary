package com.example.clubdictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MyPageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        TextView passwordResetTextView, applyTextView,logoutTextView,deleteTextView;

        applyTextView = findViewById(R.id.applyTextView);
        logoutTextView = findViewById(R.id.logoutTextView);
        deleteTextView = findViewById(R.id.deleteTextView);
        passwordResetTextView = findViewById(R.id.passwordResetTextView);
        mAuth = FirebaseAuth.getInstance();
        passwordResetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, PasswordResetActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        applyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"clubdictionary@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT, "신비한 동아리 사전 동아리/소모임 신청 메일입니다.");
                email.putExtra(Intent.EXTRA_TEXT, "동아리 이름:\n\n동아리 활동 요약(날짜, 회비, 활동 장소):\n\n중앙 동아리 여부:\n\n기타 문의 사항:\n\n");
                startActivity(email);
            }
        });
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(mAuth.getCurrentUser()).delete();
                Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}