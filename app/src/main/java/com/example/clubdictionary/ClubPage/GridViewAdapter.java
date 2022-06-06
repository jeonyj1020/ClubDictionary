package com.example.clubdictionary.ClubPage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.clubdictionary.MyPage.ShowScrapPost;
import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<PostInfo> postList = new ArrayList<PostInfo>();
    boolean isManager;

    public GridViewAdapter(ArrayList<PostInfo> postList, boolean isManager, Context context){
        this.postList = postList;
        this.isManager = isManager;
        this.context = context;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    public void addItem(PostInfo post){
        postList.add(post);
    }

    @Override
    public Object getItem(int i) {
        return postList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final PostInfo post = postList.get(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.square_image, viewGroup, false);

            ImageView img = view.findViewById(R.id.square_image_icon);
            String firstImageUrl = post.getImageUrlList().get(0);
            Glide.with(context).load(firstImageUrl).into(img);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else{
            View change_view = new View(context);
            change_view = (View) view;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                Intent intent = new Intent(context, ShowPostActivity.class);
                intent.putExtra("post", post);
                intent.putExtra("isManager", isManager);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
