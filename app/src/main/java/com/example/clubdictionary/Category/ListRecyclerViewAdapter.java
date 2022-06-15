package com.example.clubdictionary.Category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clubdictionary.BookMark.BookmarkRecyclerViewAdapter;
import com.example.clubdictionary.ClubInfo;
import com.example.clubdictionary.ClubPage.ClubPageActivity;
import com.example.clubdictionary.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ClubInfo> data = new ArrayList<>();
    Context mContext;

    public ListRecyclerViewAdapter(ArrayList<ClubInfo> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ListRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_list_item, parent, false);
        ListRecyclerViewAdapter.ViewHolder viewHolder = new ListRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecyclerViewAdapter.ViewHolder holder, int position) {

        ClubInfo item = data.get(position);
        holder.clubName.setText(item.getName());
        holder.bindProfileImage(item.getIconUrl());
        //clubIcon도 해야됨
        //눌렀을때 클럽페이지로 이동하기
        holder.clubListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.clubListLayout.getContext(), ClubPageActivity.class);
                intent.putExtra("name", holder.clubName.getText().toString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView clubName;
        CircleImageView clubIcon;
        LinearLayout clubListLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clubListLayout = itemView.findViewById(R.id.clubListLayout);
            clubName = itemView.findViewById(R.id.clubName);
            clubIcon = itemView.findViewById(R.id.clubIcon);
        }

        public void bindProfileImage(String iconUrl){
            Glide.with(mContext).load(iconUrl).into(clubIcon);
            clubIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
