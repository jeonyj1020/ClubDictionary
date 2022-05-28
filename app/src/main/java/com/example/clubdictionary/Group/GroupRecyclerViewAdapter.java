package com.example.clubdictionary.Group;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.GroupPostInfo;
import com.example.clubdictionary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder>{

    private ArrayList<GroupPostInfo> arrayList;

    public GroupRecyclerViewAdapter() {
    }

    public GroupRecyclerViewAdapter(ArrayList<GroupPostInfo> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_post_item, parent, false);
        GroupRecyclerViewAdapter.ViewHolder viewHolder = new GroupRecyclerViewAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupPostInfo groupPostInfo = arrayList.get(position);
        holder.noName.setText("익명");
        holder.title.setText(groupPostInfo.getTitle());
        holder.content.setText(groupPostInfo.getContent());
        holder.date.setText(groupPostInfo.getDate());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size(): 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noName, title, content, date;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            noName = itemView.findViewById(R.id.noName);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            //클릭했을때 기능같은거 여기에 넣기
        }

    }

}
