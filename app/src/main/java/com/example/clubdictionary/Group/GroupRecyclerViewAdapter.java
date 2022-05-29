package com.example.clubdictionary.Group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.GroupPostInfo;
import com.example.clubdictionary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder>{

    private ArrayList<GroupPostInfo> arrayList;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db= FirebaseFirestore.getInstance();

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
        holder.publisher = groupPostInfo.getPublisher();
        holder.postUid = groupPostInfo.getPostUid();
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(holder.publisher.equals(user.getUid())){
                    Log.e("@#@#", "니가 썼다.");
                    new AlertDialog.Builder(holder.cardView.getContext())
                            .setTitle("알람 팝업")
                            .setMessage("삭제하시겠습니까?")
                            .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    db.collection("group").document(holder.postUid).delete();
                                }
                            })
                            .show();
                }
                else{
                    Log.e("@#@#", "니가 안 썼다.");

                }
                return true;
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(view.getContext(), GroupPostActivity.class);
                intent.putExtra("title", groupPostInfo.getTitle());
                intent.putExtra("date", groupPostInfo.getDate());
                intent.putExtra("content", groupPostInfo.getContent());
                intent.putExtra("kakaoLink", groupPostInfo.getKakaoLink());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size(): 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noName, title, content, date;
        CardView cardView;
        String postUid;
        String publisher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            noName = itemView.findViewById(R.id.noName);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);

        }

    }

}
