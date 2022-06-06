package com.example.clubdictionary.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.clubdictionary.ClubPage.ClubPageActivity;
import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{

    private ArrayList<PostInfo> postList;
    Context context;
    DocumentReference userRef;
    ArrayList<String> scrapList;

    public HomeRecyclerViewAdapter(ArrayList<PostInfo> postList, Context context, DocumentReference userRef,
                                   ArrayList<String> scrapList) {
        this.postList = postList;
        this.context = context;
        this.userRef = userRef;
        this.scrapList = scrapList;
    }

    @NonNull
    @Override
    public HomeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_item, parent, false);
        HomeRecyclerViewAdapter.ViewHolder viewHolder = new HomeRecyclerViewAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewAdapter.ViewHolder holder, int position) {
        PostInfo postInfo = postList.get(position);

        String upTime = String.valueOf(postInfo.getUpTime());
        if(scrapList.contains(upTime)){
            holder.isScrap = true;
            holder.scrap.setImageResource(R.drawable.icon_scrap_selected);
        }
        else{
            holder.isScrap = false;
            holder.scrap.setImageResource(R.drawable.icon_scrap);
        }

        holder.clubName.setText(postInfo.getName());
        holder.minor.setText("#"+postInfo.getMinor()+" ");
        holder.upTime.setText(String.valueOf(postInfo.getUpTime()));
        holder.imageUrlList = postInfo.getImageUrlList();
        holder.contents.setText(postInfo.getContents());
        holder.hashTag.setText(postInfo.getHashTag());
        holder.bindProfileImage(postInfo.getIconUrl());
        holder.clubUid = postInfo.getClubUid();

        holder.viewPager2.setAdapter(new ImageViewPagerAdapter(holder.imageUrlList, context));
        holder.viewPager2.setOffscreenPageLimit(1);

        holder.mIndicator.setViewPager(holder.viewPager2);
        holder.mIndicator.createIndicators(holder.imageUrlList.size(), 0);

        holder.clubName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClubPageActivity.class);
                intent.putExtra("name", holder.clubName.getText().toString());
                context.startActivity(intent);
            }
        });

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClubPageActivity.class);
                intent.putExtra("name", holder.clubName.getText().toString());
                context.startActivity(intent);
            }
        });

        holder.scrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.isScrap){
                    holder.isScrap = false;
                    holder.scrap.setImageResource(R.drawable.icon_scrap);
                    userRef.update("scrap", FieldValue.arrayRemove(upTime));
                }
                else{
                    holder.isScrap = true;
                    holder.scrap.setImageResource(R.drawable.icon_scrap_selected);
                    userRef.update("scrap", FieldValue.arrayUnion(upTime));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView clubName, minor, hashTag, contents, upTime;
        private ArrayList<String> imageUrlList = new ArrayList<>();
        private ViewPager2 viewPager2;
        private CircleImageView icon;
        private ImageView scrap;
        private boolean isScrap;
        private CircleIndicator3 mIndicator;
        private String clubUid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clubName = itemView.findViewById(R.id.clubName);
            //major = itemView.findViewById(R.id.major);
            minor = itemView.findViewById(R.id.minor);
            icon = itemView.findViewById(R.id.icon);
            scrap = itemView.findViewById(R.id.scrap);
            upTime = itemView.findViewById(R.id.upTime);
            hashTag = itemView.findViewById(R.id.hashTag);
            contents = itemView.findViewById(R.id.contents);
            viewPager2 = itemView.findViewById(R.id.home_viewpager2);
            mIndicator = itemView.findViewById(R.id.indicator);

        }

        /*private void setupIndicators(int count) {
            ImageView[] indicators = new ImageView[count];
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(16, 8, 16, 8);

            for (int i = 0; i < indicators.length; i++) {
                indicators[i] = new ImageView(context);
                indicators[i].setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.icon_dot));
                indicators[i].setLayoutParams(params);
                layoutIndicator.addView(indicators[i]);
            }
            setCurrentIndicator(0);
        }

        private void setCurrentIndicator(int position) {
            int childCount = layoutIndicator.getChildCount();
            for (int i = 0; i < childCount; i++) {
                ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
                if (i == position) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(
                            context,
                            R.drawable.icon_dot_selected
                    ));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(
                            context,
                            R.drawable.icon_dot
                    ));
                }
            }
        }*/

        public void bindProfileImage(String iconUrl){
            Glide.with(context).load(iconUrl).into(icon);
            icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}