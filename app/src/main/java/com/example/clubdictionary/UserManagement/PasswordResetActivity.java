package com.example.clubdictionary.UserManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        findViewById(R.id.sendButton).setOnClickListener(onClickListener);

        mAuth = FirebaseAuth.getInstance();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sendButton:
                    send();
                    break;
            }
        }
    };

    private void send(){

        String email = ((EditText) findViewById(R.id.emailEditText_password_reset)).getText().toString();

        if (email.length() > 0) {

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(PasswordResetActivity.this,"이메일을 보냈습니다.이메일 속 링크를 클릭해주세요.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PasswordResetActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        } else {
            Toast.makeText(PasswordResetActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

}