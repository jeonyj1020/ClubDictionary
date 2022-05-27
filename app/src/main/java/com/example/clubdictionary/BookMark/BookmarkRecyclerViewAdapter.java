package com.example.clubdictionary.BookMark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.R;

import java.util.ArrayList;

public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookmarkRecyclerViewAdapter.ViewHolder> {

    private ArrayList<BookmarkItem> bookmarkItemList;

    public BookmarkRecyclerViewAdapter(ArrayList<BookmarkItem> bookmarkItemList) {
        bookmarkItemList = bookmarkItemList;
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
        //String text = bookmarkItemList.get(position);
        //holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return bookmarkItemList.size();
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

