package com.example.clubdictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookmarkFragment extends Fragment {

    RecyclerView recyclerView;
    BookmarkRecyclerViewAdapter bookmarkRecyclerViewAdapter;

    public BookmarkFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        ArrayList<String> testDataSet = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            testDataSet.add("TEST DATA" + i);
        }

        recyclerView = view.findViewById(R.id.fragment_bookmark_recyclerview);
        bookmarkRecyclerViewAdapter = new BookmarkRecyclerViewAdapter(testDataSet);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(bookmarkRecyclerViewAdapter);

        return view;
    }
}
