package com.example.clubdictionary.BookMark;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.Group.GroupBoardActivity;
import com.example.clubdictionary.GroupPostInfo;
import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    Context mContext;
    BookmarkRecyclerViewAdapter bookmarkRecyclerViewAdapter;
    private FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentSnapshot userDoc;
    DocumentReference userRef;
    Map<String, List<String>> bookMark = new HashMap<>();

    ArrayList<BookmarkItem> bookmarkItemList = new ArrayList<>();
    int tenCnt = 0;

    public BookmarkFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        mContext = container.getContext();

        bookMark.clear();
        //bookmarkItemList.clear();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users").document(user.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            userDoc = task.getResult();
                            userRef = userDoc.getReference();
                            if(!userDoc.exists()){
                                db.collection("clubs").document(user.getUid()).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    Log.d("user", "동아리입니다");
                                                    userDoc = task.getResult();
                                                    userRef = userDoc.getReference();
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

        return view;
    }

    private void getBookMarkClubList() {

        bookMark = (Map<String, List<String>>) userDoc.get("bookMark");
        bookmarkItemList.clear();

        ArrayList<String> clubList = new ArrayList<>();
        for(String minor : bookMark.keySet()){
            List<String> minorList = bookMark.get(minor);
            for(String club : minorList){
                clubList.add(club);
            }
        }
        //Log.e("###", ""+clubList.size());

        tenCnt = clubList.size() / 10;
        if(clubList.size() % 10 == 0) tenCnt--;

        if(tenCnt > 0) {
            for (int i = 1; i <= tenCnt; i++) {
                List<String> now = clubList.subList(10 * (i - 1), 10 * i - 1);
                db.collection("clubs").whereIn("name", now).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        boolean alarmChecked = ((Map<String, Boolean>) document.get("subscribers"))
                                                .containsKey(user.getUid());
                                        bookmarkItemList.add(new BookmarkItem(
                                                (String) document.get("icon"), (String) document.get("name"),
                                                (String) document.get("major"), (String) document.get("minor"),
                                                alarmChecked, document.getReference()));
                                    }
                                }
                            }
                        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (bookmarkItemList.size() == tenCnt * 10) {
                                    List<String> now = clubList.subList(10 * tenCnt, clubList.size());
                                    db.collection("clubs").whereIn("name", now).get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            boolean alarmChecked = ((Map<String, Boolean>) document.get("subscribers"))
                                                                    .containsKey(user.getUid());
                                                            bookmarkItemList.add(new BookmarkItem(
                                                                    (String) document.get("icon"), (String) document.get("name"),
                                                                    (String) document.get("major"), (String) document.get("minor"),
                                                                    alarmChecked, document.getReference()));

                                                            if(bookmarkItemList.size() == clubList.size()){
                                                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                                bookmarkRecyclerViewAdapter = new BookmarkRecyclerViewAdapter
                                                                        (bookmarkItemList, userRef, mContext);
                                                                recyclerView.setAdapter(bookmarkRecyclerViewAdapter);
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        }
        else if(clubList.size() > 0){
            List<String> now = clubList.subList(0, clubList.size());
            db.collection("clubs").whereIn("name", now).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    boolean alarmChecked = ((Map<String, Boolean>) document.get("subscribers"))
                                            .containsKey(user.getUid());
                                    bookmarkItemList.add(new BookmarkItem(
                                            (String) document.get("iconUrl"), (String) document.get("name"),
                                            (String) document.get("major"), (String) document.get("minor"),
                                            alarmChecked, document.getReference()));
                                    Log.e("alarmChecked", ""+alarmChecked);

                                    if(bookmarkItemList.size() == clubList.size()){
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        bookmarkRecyclerViewAdapter = new BookmarkRecyclerViewAdapter(bookmarkItemList, userRef, mContext);
                                        recyclerView.setAdapter(bookmarkRecyclerViewAdapter);
                                    }
                                }
                            }
                        }
                    });
        }
        else{

        }
    }
}
