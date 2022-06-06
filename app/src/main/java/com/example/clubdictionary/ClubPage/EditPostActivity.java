package com.example.clubdictionary.ClubPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.clubdictionary.Home.ImageViewPagerAdapter;
import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;

public class EditPostActivity extends AppCompatActivity {

    TextView clubName, upTime, minor;
    EditText hashTag, contents;
    ViewPager2 viewPager2;
    CircleIndicator3 mIndicator;
    ImageButton backButton, scrap;
    Button upload;
    boolean isScrap;
    DocumentReference userRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    PostInfo post;
    String upTimeString;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        mAuth = FirebaseAuth.getInstance();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        post = (PostInfo) getIntent().getSerializableExtra("post");
        isScrap = getIntent().getBooleanExtra("isScrap", false);

        scrap = findViewById(R.id.scrap);
        if(isScrap){
            scrap.setImageResource(R.drawable.icon_scrap_selected);
        }
        else{
            scrap.setImageResource(R.drawable.icon_scrap);
        }

        upTimeString = String.valueOf(post.getUpTime());

        db.collection("clubs").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            userRef = task.getResult().getReference();
                        }
                    }
                });

        CircleImageView icon;
        icon = findViewById(R.id.icon);
        Glide.with(this).load(post.getIconUrl()).into(icon);
        icon.setOnClickListener(onClickListener);

        clubName = findViewById(R.id.clubName);
        clubName.setText(post.getName());

        upTime = findViewById(R.id.upTime);
        upTime.setText(String.valueOf(post.getUpTime()));

        minor = findViewById(R.id.minor);
        minor.setText(post.getMinor());

        hashTag = findViewById(R.id.hashTag);
        hashTag.setText(post.getHashTag());

        contents = findViewById(R.id.contents);
        contents.setText(post.getContents());

        viewPager2 = findViewById(R.id.home_viewpager2);
        mIndicator = findViewById(R.id.indicator);

        ArrayList<String> imageUrlList = post.getImageUrlList();
        viewPager2.setAdapter(new ImageViewPagerAdapter(imageUrlList, this));
        viewPager2.setOffscreenPageLimit(1);

        mIndicator.setViewPager(viewPager2);
        mIndicator.createIndicators(imageUrlList.size(), 0);

        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(onClickListener);

        upload = findViewById(R.id.upload);
        upload.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back_btn:
                    EditPostActivity.super.onBackPressed();
                    break;

                case R.id.upload:
                    new AlertDialog.Builder(EditPostActivity.this)
                            .setTitle("알람 팝업")
                            .setMessage("수정하시겠습니까?")
                            .setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .setNegativeButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    userRef.collection("posts").document(upTimeString)
                                            .update("hashTag", hashTag.getText().toString(),
                                                    "contents", contents.getText().toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(EditPostActivity.this, "수정했습니다.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                    finish();
                                }
                            })
                            .show();
            }
        }
    };
}