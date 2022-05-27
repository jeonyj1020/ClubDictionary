package com.example.clubdictionary.BookMark;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class BookmarkFragment extends Fragment {

    RecyclerView recyclerView;
    BookmarkRecyclerViewAdapter bookmarkRecyclerViewAdapter;
    private FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, List<String>> bookMark = new HashMap<>();
    DocumentSnapshot userDoc;
    ArrayList<BookmarkItem> bookmarkItemList = new ArrayList<>();

    public BookmarkFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users").document(user.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    userDoc = task.getResult();
                    if(!userDoc.exists()){
                        db.collection("clubs").document(user.getUid()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    Log.d("user", "동아리입니다");
                                    userDoc = task.getResult();
                                    getBookMarkClubList();
                                }
                            }
                        });
                    }
                    else {
                        Log.d("user", "유저입니다");
                        getBookMarkClubList();
                    }
                }
                else Log.d("task", "실패");
            }
        });

        recyclerView = view.findViewById(R.id.fragment_bookmark_recyclerview);
        bookmarkRecyclerViewAdapter = new BookmarkRecyclerViewAdapter(bookmarkItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(bookmarkRecyclerViewAdapter);

        return view;
    }

    private void getBookMarkClubList(){
        bookMark = (Map<String, List<String>>) userDoc.get("bookMark");
        for(String minor : bookMark.keySet()){
            List<String> clubs = bookMark.get(minor);
            db.collection("clubs").whereIn("name", clubs).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                     BookmarkItem bookmarkItem = new BookmarkItem(
                                             (String)document.get("icon"), (String)document.get("name"),
                                             (String) document.get("major"), minor);
                                     bookmarkItemList.add(bookmarkItem);
                                }
                            }
                        }
                    });

           /* String clubList = "";
            for(String club : clubs){
                clubList += club + " ";
            }
            Log.d(minor, clubList);*/
        }
    }
}
