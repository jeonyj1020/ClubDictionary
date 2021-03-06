package com.example.clubdictionary.ClubPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.clubdictionary.Home.ImageViewPagerAdapter;
import com.example.clubdictionary.MyPage.ShowScrapPost;
import com.example.clubdictionary.R;
import com.example.clubdictionary.UserManagement.LoginActivity;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;

public class ShowPostActivity extends AppCompatActivity {
    TextView clubName, upTime, minor, hashTag, contents;
    ViewPager2 viewPager2;
    CircleIndicator3 mIndicator;
    ImageButton backButton, scrap;
    boolean isScrap;
    DocumentReference userRef;
    ImageView menu;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    boolean isManager;
    PostInfo post;
    ArrayList<String> scrapList;
    String upTimeString;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
        mAuth = FirebaseAuth.getInstance();

        isManager = getIntent().getBooleanExtra("isManager", false);
        post = (PostInfo) getIntent().getSerializableExtra("post");

        scrap = findViewById(R.id.scrap);
        scrap.setOnClickListener(onClickListener);
        upTimeString = String.valueOf(post.getUpTime());

        db.collection("users").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (!documentSnapshot.exists()) {
                                db.collection("clubs").document(user.getUid()).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                userRef = documentSnapshot.getReference();
                                                scrapList = (ArrayList<String>) documentSnapshot.get("scrap");
                                                if(scrapList.contains(upTimeString)){
                                                    isScrap = true;
                                                    scrap.setImageResource(R.drawable.icon_scrap_selected);
                                                }
                                                else{
                                                    isScrap = false;
                                                    scrap.setImageResource(R.drawable.icon_scrap);
                                                }
                                            }
                                        });
                            } else {
                                userRef = documentSnapshot.getReference();
                                scrapList = (ArrayList<String>) documentSnapshot.get("scrap");
                                if(scrapList.contains(upTimeString)){
                                    isScrap = true;
                                    scrap.setImageResource(R.drawable.icon_scrap_selected);
                                }
                                else{
                                    isScrap = false;
                                    scrap.setImageResource(R.drawable.icon_scrap);
                                }
                            }
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    }
                });

        CircleImageView icon;
        icon = findViewById(R.id.icon);
        Glide.with(this).load(post.getIconUrl()).into(icon);
        icon.setOnClickListener(onClickListener);

        clubName = findViewById(R.id.clubName);
        clubName.setText(post.getName());
        clubName.setOnClickListener(onClickListener);

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

        menu = findViewById(R.id.item_dropdown_menu);
        menu.setOnClickListener(onClickListener);
        //if (!isManager) menu.setVisibility(View.INVISIBLE);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.icon:
                case R.id.clubName:
                    Intent intent = new Intent(ShowPostActivity.this, ClubPageActivity.class);
                    intent.putExtra("name", clubName.getText().toString());
                    startActivity(intent);
                    break;

                case R.id.back_btn:
                    ShowPostActivity.super.onBackPressed();
                    break;

                case R.id.scrap:
                    if (isScrap) {
                        isScrap = false;
                        scrap.setImageResource(R.drawable.icon_scrap);
                        userRef.update("scrap", FieldValue.arrayRemove(upTimeString));
                    } else {
                        isScrap = true;
                        scrap.setImageResource(R.drawable.icon_scrap_selected);
                        userRef.update("scrap", FieldValue.arrayUnion(upTimeString));
                    }
                    break;

                case R.id.item_dropdown_menu:
                    PopupMenu popup = new PopupMenu(ShowPostActivity.this, menu);
                    MenuInflater menuInflater = popup.getMenuInflater();
                    menuInflater.inflate(R.menu.clubpage_post_menu_forclub, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.fixIt:
                                    Intent intent = new Intent(ShowPostActivity.this, EditPostActivity.class);
                                    intent.putExtra("post", post);
                                    intent.putExtra("isScrap", isScrap);
                                    startActivity(intent);
                                    return true;

                                case R.id.deleteIt:
                                    new AlertDialog.Builder(ShowPostActivity.this)
                                            .setTitle("?????? ??????")
                                            .setMessage("?????????????????????????")
                                            .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            })
                                            .setNegativeButton("???", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    userRef.collection("posts").document(upTimeString)
                                                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(ShowPostActivity.this,
                                                                            "???????????? ??????????????????.",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                    finish();
                                                }
                                            })
                                            .show();
                                    return true;

                                default:
                                    return false;
                            }
                        }
                    });
                    break;
            }
        }
    };
}