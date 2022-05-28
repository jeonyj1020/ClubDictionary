package com.example.clubdictionary.Group;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.clubdictionary.GroupPostInfo;
import com.example.clubdictionary.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class GroupBoardActivity extends AppCompatActivity {

   private String TAG = "GroupBoardActivity";
   private  RecyclerView recyclerView;
   private GroupRecyclerViewAdapter adapter;
   private ArrayList<GroupPostInfo> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_board);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(GroupBoardActivity.this));

        FirebaseFirestore db= FirebaseFirestore.getInstance();

        db.collection("group").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupBoardActivity.this, GroupWriteActivity.class);
                startActivity(intent);
            }
        });
    }

}