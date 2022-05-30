package com.example.clubdictionary.ClubPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clubdictionary.R;
import com.example.clubdictionary.view.ExpandableHeightGridView;

import java.util.ArrayList;

public class PostFragment extends Fragment {

    private ExpandableHeightGridView gridview = null;
    private GridViewAdapter adapter = null;

    public static PostFragment newInstance(int number) {
        PostFragment postFragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);

        postFragment.setArguments(bundle);
        return postFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int num = getArguments().getInt("number");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment_clubpage_post, container, false);

        gridview = (ExpandableHeightGridView) view.findViewById(R.id.post_gridview);
        gridview.setExpanded(true);
        adapter = new GridViewAdapter();

        adapter.addItem(new PostItem(R.drawable.cat01));
        adapter.addItem(new PostItem(R.drawable.cat02));
        adapter.addItem(new PostItem(R.drawable.cat03));
        adapter.addItem(new PostItem(R.drawable.cat04));
        adapter.addItem(new PostItem(R.drawable.cat05));
        adapter.addItem(new PostItem(R.drawable.cat06));
        adapter.addItem(new PostItem(R.drawable.cat07));
        adapter.addItem(new PostItem(R.drawable.cat08));
        adapter.addItem(new PostItem(R.drawable.cat04));
        adapter.addItem(new PostItem(R.drawable.cat05));
        adapter.addItem(new PostItem(R.drawable.cat06));
        adapter.addItem(new PostItem(R.drawable.cat07));
        adapter.addItem(new PostItem(R.drawable.cat08));
        adapter.addItem(new PostItem(R.drawable.cat04));
        adapter.addItem(new PostItem(R.drawable.cat05));
        adapter.addItem(new PostItem(R.drawable.cat06));
        adapter.addItem(new PostItem(R.drawable.cat07));
        adapter.addItem(new PostItem(R.drawable.cat08));
        adapter.addItem(new PostItem(R.drawable.cat04));
        adapter.addItem(new PostItem(R.drawable.cat05));
        adapter.addItem(new PostItem(R.drawable.cat06));
        adapter.addItem(new PostItem(R.drawable.cat07));
        adapter.addItem(new PostItem(R.drawable.cat08));

        gridview.setAdapter(adapter);

        return view;
    }
}

