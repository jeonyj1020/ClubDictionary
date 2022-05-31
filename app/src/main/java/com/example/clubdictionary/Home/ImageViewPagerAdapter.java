package com.example.clubdictionary.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clubdictionary.R;

import java.util.ArrayList;

public class ImageViewPagerAdapter extends RecyclerView.Adapter<ImageViewPagerAdapter.ViewHolder> {

    ArrayList<String> imageUrlList = new ArrayList<>();
    Context context;

    public ImageViewPagerAdapter(ArrayList<String> imageUrlList, Context context) {
        this.imageUrlList = imageUrlList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewPagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.square_image, parent, false);
        ImageViewPagerAdapter.ViewHolder viewHolder = new ImageViewPagerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewPagerAdapter.ViewHolder holder, int position) {
        holder.bindSliderImage(imageUrlList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.square_image_icon);
        }

        public void bindSliderImage(String imageURL) {
            // 여기서 Url 통해서 가져온 아이템 imageView 에 load 하면 됨
            Glide.with(context).load(imageURL).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
