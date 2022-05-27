package com.example.clubdictionary.BookMark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarkFragment extends Fragment {

    RecyclerView recyclerView;
    BookmarkRecyclerViewAdapter bookmarkRecyclerViewAdapter;
    private FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, List<String>> bookMark = new HashMap<>();

    public BookmarkFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        //db.collection(get)
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
