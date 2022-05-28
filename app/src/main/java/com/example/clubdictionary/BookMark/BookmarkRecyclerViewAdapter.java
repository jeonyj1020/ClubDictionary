package com.example.clubdictionary.BookMark;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clubdictionary.MainActivity;
import com.example.clubdictionary.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookmarkRecyclerViewAdapter.ViewHolder> {
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private ArrayList<BookmarkItem> bookMarkItemList;

    public BookmarkRecyclerViewAdapter(ArrayList<BookmarkItem> bookMarkItemList) {
        this.bookMarkItemList = bookMarkItemList;
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
        holder.name.setText(bookmarkItem.getClubName());
        holder.major.setText("#"+bookmarkItem.getMajor()+" ");
        holder.minor.setText("#"+bookmarkItem.getMinor()+" ");

        //String text = bookmarkItemList.get(position);
        //holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return bookMarkItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView icon;
        private TextView name;
        private TextView major;
        private TextView minor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.bookmark_item_icon);
            Glide.with(itemView).
                    load("gs://clubdictionary.appspot.com/clubs/5cLadUS3cng18VygrQzHd8Qrorg1/24283C3858F778CA2E.jpg")
                    .into(icon);
            name = itemView.findViewById(R.id.bookmark_item_name);
            major = itemView.findViewById(R.id.bookmark_item_major);
            minor = itemView.findViewById(R.id.bookmark_item_minor);

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

    }
}

