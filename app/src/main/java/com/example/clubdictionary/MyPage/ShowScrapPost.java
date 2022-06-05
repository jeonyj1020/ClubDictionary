package com.example.clubdictionary.MyPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

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
    ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scrap_post);
        PostInfo scrapPost;
        CircleImageView icon;

        scrapPost = (PostInfo) getIntent().getSerializableExtra("scrapPost");

        icon = findViewById(R.id.icon);
        Glide.with(this).load(scrapPost.getIconUrl()).into(icon);
        icon.setOnClickListener(onClickListener);

        clubName = findViewById(R.id.clubName);
        clubName.setText(scrapPost.getName());
        clubName.setOnClickListener(onClickListener);

        upTime = findViewById(R.id.upTime);
        upTime.setText(String.valueOf(scrapPost.getUpTime()));

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

        menu = findViewById(R.id.item_dropdown_menu);
        menu.setOnClickListener(onClickListener);
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
                    ShowScrapPost.super.onBackPressed();
                    break;

                case R.id.scrap :
                    if(isScrap){
                        isScrap = false;
                        scrap.setImageResource(R.drawable.icon_scrap);
                        userRef.update("scrap", FieldValue.arrayRemove(upTime));
                    }
                    else{
                        isScrap = true;
                        scrap.setImageResource(R.drawable.icon_scrap_selected);
                        userRef.update("scrap", FieldValue.arrayUnion(upTime));
                    }

                case R.id.item_dropdown_menu:
                    PopupMenu popup = new PopupMenu(ShowScrapPost.this, menu);
                    MenuInflater menuInflater = popup.getMenuInflater();
                    menuInflater.inflate(R.menu.clubpage_post_menu_forclub, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch (menuItem.getItemId()) {
                                case R.id.fixIt:
                                    Toast.makeText(ShowScrapPost.this, "수정하기 클릭", Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.deleteIt:
                                    Toast.makeText(ShowScrapPost.this, "삭제하기 클릭", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
            }
        }
    };
}