package com.example.clubdictionary.MyPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

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
import com.example.clubdictionary.ClubPage.ClubPageActivity;
import com.example.clubdictionary.Home.ImageViewPagerAdapter;
import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;

public class ShowScrapPost extends AppCompatActivity {
    TextView clubName, upTime, minor, hashTag, contents;
    ViewPager2 viewPager2;
    CircleIndicator3 mIndicator;
    ImageButton backButton, scrap;
    boolean isScrap;
    DocumentReference userRef;
    String type;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String upTimeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scrap_post);
        PostInfo scrapPost;
        CircleImageView icon;
        ArrayList<String> scrapList;

        scrapPost = (PostInfo) getIntent().getSerializableExtra("scrapPost");
        scrapList = getIntent().getStringArrayListExtra("scrapList");
        type = getIntent().getStringExtra("type");

        db.collection(type).document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        userRef = task.getResult().getReference();
                    }
                });

        icon = findViewById(R.id.icon);
        Glide.with(this).load(scrapPost.getIconUrl()).into(icon);
        icon.setOnClickListener(onClickListener);

        clubName = findViewById(R.id.clubName);
        clubName.setText(scrapPost.getName());
        clubName.setOnClickListener(onClickListener);

        upTimeString = String.valueOf(scrapPost.getUpTime());
        upTime = findViewById(R.id.upTime);
        upTime.setText(upTimeString);

        minor = findViewById(R.id.minor);
        minor.setText(scrapPost.getMinor());

        hashTag = findViewById(R.id.hashTag);
        hashTag.setText(scrapPost.getHashTag());

        contents = findViewById(R.id.contents);
        contents.setText(scrapPost.getContents());

        viewPager2 = findViewById(R.id.home_viewpager2);
        mIndicator = findViewById(R.id.indicator);

        ArrayList<String> imageUrlList = scrapPost.getImageUrlList();
        viewPager2.setAdapter(new ImageViewPagerAdapter(imageUrlList, this));
        viewPager2.setOffscreenPageLimit(1);

        mIndicator.setViewPager(viewPager2);
        mIndicator.createIndicators(imageUrlList.size(), 0);

        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(onClickListener);

        scrap = findViewById(R.id.scrap);
        scrap.setOnClickListener(onClickListener);

        String upTime = String.valueOf(scrapPost.getUpTime());
        if(scrapList.contains(upTime)){
            isScrap = true;
            scrap.setImageResource(R.drawable.icon_scrap_selected);
        }
        else{
            isScrap = false;
            scrap.setImageResource(R.drawable.icon_scrap);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.icon :
                case R.id.clubName:
                    Intent intent = new Intent(ShowScrapPost.this, ClubPageActivity.class);
                    intent.putExtra("name", clubName.getText().toString());
                    startActivity(intent);
                    break;

                case R.id.back_btn:
                    onBackPressed();
                    break;

                case R.id.scrap :
                    if(isScrap){
                        isScrap = false;
                        scrap.setImageResource(R.drawable.icon_scrap);
                        userRef.update("scrap", FieldValue.arrayRemove(upTimeString));
                    }
                    else{
                        isScrap = true;
                        scrap.setImageResource(R.drawable.icon_scrap_selected);
                        userRef.update("scrap", FieldValue.arrayUnion(upTimeString));
                    }
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("return", "ok");
        setResult(1);
        finish();
    }
}