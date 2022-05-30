package com.example.clubdictionary.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.BookMark.BookmarkRecyclerViewAdapter;
import com.example.clubdictionary.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ListItem> data = new ArrayList<>();
    Context mContext;

    public ListRecyclerViewAdapter(ArrayList<ListItem> data, Context mContext) {
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

        ListItem item = data.get(position);
        holder.clubName.setText(item.clubName);
        //clubIcon도 해야됨

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView clubName;
        CircleImageView clubIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clubName = itemView.findViewById(R.id.clubName);
            clubIcon = itemView.findViewById(R.id.clubIcon);
        }
    }

    public class ListItem {

        public String iconUrl, clubName;

    }
}
