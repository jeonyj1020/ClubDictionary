package com.example.clubdictionary.BookMark;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clubdictionary.ClubPage.ClubPageActivity;
import com.example.clubdictionary.MainActivity;
import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookmarkRecyclerViewAdapter.ViewHolder> {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference userRef;
    String userUid = user.getUid();

    private ArrayList<BookmarkItem> bookMarkItemList;
    Context mContext;

    public BookmarkRecyclerViewAdapter(ArrayList<BookmarkItem> bookMarkItemList, DocumentReference userRef, Context mContext) {
        this.bookMarkItemList = bookMarkItemList;
        this.userRef = userRef;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BookmarkRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_bookmark_item, parent, false);
        BookmarkRecyclerViewAdapter.ViewHolder viewHolder = new BookmarkRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkRecyclerViewAdapter.ViewHolder holder, int position) {

        BookmarkItem bookmarkItem = bookMarkItemList.get(position);

        //holder.icon.se
        DocumentReference clubRef = bookmarkItem.getClubRef();
        holder.name.setText(bookmarkItem.getClubName());
        holder.major.setText("#"+bookmarkItem.getMajor()+" ");
        holder.minor.setText("#"+bookmarkItem.getMinor()+" ");
        holder.bindProfileImage(bookmarkItem.getIconUrl());
        boolean alarmChecked = bookmarkItem.isAlarmChecked();
        Log.e("알람", ""+alarmChecked);
        if(alarmChecked){
            holder.alarm.setImageResource(R.drawable.icon_alarmon);
        }
        else{
            holder.alarm.setImageResource(R.drawable.icon_alarmoff);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ClubPageActivity.class);
                intent.putExtra("name", holder.name.getText().toString());
                mContext.startActivity(intent);
            }
        });
        holder.alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.isChecked) {
                    holder.alarm.setImageResource(R.drawable.icon_alarmoff);
                    holder.isChecked = false;
                    userRef.update("alarmList", FieldValue.arrayRemove(userUid));
                    clubRef.update("subscribers." + userUid, false);
                }
                else {
                    holder.alarm.setImageResource(R.drawable.icon_alarmon);
                    holder.isChecked = true;
                    userRef.update("alarmList", FieldValue.arrayUnion(userUid));
                    clubRef.update("subscribers." + userUid, true);
                }
            }
        });
        //String text = bookmarkItemList.get(position);
        //holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return bookMarkItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout linearLayout;
        private CircleImageView icon;
        private TextView name;
        private TextView major;
        private TextView minor;
        private ImageView alarm;
        private String iconUrl;
        private boolean isChecked = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.bookmark_item_icon);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            name = itemView.findViewById(R.id.bookmark_item_name);
            major = itemView.findViewById(R.id.bookmark_item_major);
            minor = itemView.findViewById(R.id.bookmark_item_minor);
            alarm = itemView.findViewById(R.id.alarm);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Bo item = ClubNameSet
                    }
                }
            });*/
        }

        public void bindProfileImage(String iconUrl){
            Glide.with(mContext).load(iconUrl).into(icon);
            icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

    }
}

