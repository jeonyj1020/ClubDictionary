package com.example.clubdictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookmarkRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> ClubNameSet;

    public BookmarkRecyclerViewAdapter(ArrayList<String> clubNameSet) {
        ClubNameSet = clubNameSet;
    }

    @NonNull
    @Override
    public BookmarkRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);
        BookmarkRecyclerViewAdapter.ViewHolder viewHolder = new BookmarkRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkRecyclerViewAdapter.ViewHolder holder, int position) {
        String text = ClubNameSet.get(position);
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return ClubNameSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.bookmark_item_profile_name);

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
        public TextView getTextView(){
            return textView;
        }
    }
}

