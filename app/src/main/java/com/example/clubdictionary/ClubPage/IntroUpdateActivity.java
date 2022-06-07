package com.example.clubdictionary.ClubPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class IntroUpdateActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText day, money, activityTime, introduce, applyLink;
    Button applyButton;
    String clubUid, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_update);


        Toolbar toolbar = findViewById(R.id.aiu_tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        day = findViewById(R.id.day);
        money = findViewById(R.id.money);
        activityTime = findViewById(R.id.time);
        introduce = findViewById(R.id.introduce);
        applyLink = findViewById(R.id.applyLink);
        applyButton = findViewById(R.id.applyButton);

        Intent intent = getIntent();
        clubUid = intent.getStringExtra("clubUid");
        name = intent.getStringExtra("name");
        day.setText(intent.getStringExtra("day"));
        money.setText(intent.getStringExtra("money"));
        //activityTime 이 실제 db 필드값
        activityTime.setText(intent.getStringExtra("activityTime"));
        introduce.setText(intent.getStringExtra("introduce"));
        applyLink.setText(intent.getStringExtra("registerUrl"));

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("update된 day  그리고 클럽 uid ", day.getText().toString() + clubUid);
                DocumentReference docRef = db.collection("clubs").document(clubUid.trim());
                docRef.update("day", day.getText().toString());
                docRef.update("introduce", introduce.getText().toString());
                docRef.update("activityTime", activityTime.getText().toString());
                docRef.update("registerUrl", applyLink.getText().toString());
                toast("동아리 정보가 수정되었습니다.");
                Intent intent1 = new Intent(IntroUpdateActivity.this, ClubPageActivity.class);
                intent1.putExtra("name", name);
                finish();
                startActivity(intent1);

            }
        });
    }

    private void toast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}