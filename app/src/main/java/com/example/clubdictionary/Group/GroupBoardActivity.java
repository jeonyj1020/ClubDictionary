package com.example.clubdictionary.Group;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.clubdictionary.GroupPostInfo;
import com.example.clubdictionary.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class GroupBoardActivity extends AppCompatActivity {

   private String TAG = "GroupBoardActivity";
   private  RecyclerView recyclerView;
   private GroupRecyclerViewAdapter adapter;
   private ArrayList<GroupPostInfo> arrayList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_board);
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(GroupBoardActivity.this));

        getGroupPostList();


        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupBoardActivity.this, GroupWriteActivity.class);
                startActivity(intent);
            }
        });

        refreshingTask();
    }

    private void refreshingTask() {
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGroupPostList();
            }
        });
    }

    private void getGroupPostList(){

        if(swipeRefreshLayout.isRefreshing())swipeRefreshLayout.setRefreshing(false);

        FirebaseFirestore db= FirebaseFirestore.getInstance();

        db.collection("group").orderBy("postUid", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                arrayList.clear();
                for(QueryDocumentSnapshot document : value){
                    GroupPostInfo groupPostInfo = document.toObject(GroupPostInfo.class);
                    Log.e("###", groupPostInfo.getTitle());
                    arrayList.add(groupPostInfo);

                }
                adapter.notifyDataSetChanged();
            }
        });

        Log.e("size", ""+arrayList.size());
        adapter = new GroupRecyclerViewAdapter(arrayList);
        recyclerView.setAdapter(adapter);

    }
}