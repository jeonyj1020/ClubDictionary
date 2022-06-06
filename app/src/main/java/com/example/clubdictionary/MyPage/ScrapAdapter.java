package com.example.clubdictionary.MyPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;

import java.util.ArrayList;

public class ScrapAdapter extends BaseAdapter {
    Context context;
    ArrayList<PostInfo> scrapPostList;
    ArrayList<String> scrapList;
    String type;

    public ScrapAdapter(ArrayList<PostInfo> scrapPostList, ArrayList<String> scrapList, Context context,
                        String type){
        this.scrapPostList = scrapPostList;
        this.context = context;
        this.scrapList = scrapList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return scrapPostList.size();
    }

    @Override
    public Object getItem(int position) {
        return scrapPostList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final PostInfo scrapPost = scrapPostList.get(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_scrap_item, viewGroup, false);

            ImageView img = view.findViewById(R.id.scrapItem);
            String firstImageUrl = scrapPost.getImageUrlList().get(0);
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
                Intent intent = new Intent(context, ShowScrapPost.class);
                intent.putExtra("scrapPost", scrapPost);
                intent.putExtra("scrapList", scrapList);
                intent.putExtra("type", type);
                ((Activity) context).startActivityForResult(intent, 1);
            }
        });

        return view;
    }
}