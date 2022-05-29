package com.example.clubdictionary.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.clubdictionary.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{

    //private ArrayList<HomeItem> ClubNameSet;
    private ArrayList<String> ClubNameSet;
    Context context;

    public HomeRecyclerViewAdapter(ArrayList<String> clubNameSet, Context context) {
        ClubNameSet = clubNameSet;
        this.context = context;
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

        //HomeItem homeItem = ClubNameSet.get(position);
        //holder.clubName.setText(homeItem.getClubName());

        //태스트용
        holder.clubName.setText(ClubNameSet.get(position));

    }

    @Override
    public int getItemCount() {
        return ClubNameSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView clubName, major, minor, upTime, hashTag, cotents;
        private ArrayList<String> images = new ArrayList<>();
        private ViewPager2 viewPager2;
        private CircleImageView icon;
        private ImageView scrap;
        private boolean isScrap;
        private LinearLayout layoutIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clubName = itemView.findViewById(R.id.clubName);
            major = itemView.findViewById(R.id.major);
            //minor = itemView.findViewById(R.id.minor);
            icon = itemView.findViewById(R.id.icon);
            scrap = itemView.findViewById(R.id.scrap);

            viewPager2 = itemView.findViewById(R.id.viewpager2);
            layoutIndicator = itemView.findViewById(R.id.layout_indicator);

            /*viewPager2.setOffscreenPageLimit(1);
            viewPager2.setAdapter(new ImageViewPagerAdapter(images, context));

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    setCurrentIndicator(position);
                }
            });

            setupIndicators(images.size());*/
        }

        private void setupIndicators(int count) {
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
        }
    }
}
