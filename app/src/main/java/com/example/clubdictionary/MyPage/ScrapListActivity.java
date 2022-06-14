package com.example.clubdictionary.MyPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ScrapListActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<String> scrapList = new ArrayList<>();
    ArrayList<Long> scrapListUpTime = new ArrayList<>();
    ArrayList<PostInfo> scrapPostList = new ArrayList<>();
    String type;
    int scrapCnt, cmp;
    ScrapAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_list);

        type = getIntent().getStringExtra("type");

        db.collection(type).document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        scrapList = (ArrayList<String>) document.get("scrap");
                        ArrayList<Long> scrapListUpTime = new ArrayList<>();
                        if(scrapList.size() == 0){
                            Toast.makeText(ScrapListActivity.this, "좋아요한 게시글이 없습니다!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            for (String upTime : scrapList) {
                                scrapListUpTime.add(Long.parseLong(upTime));
                            }

                            int ten = scrapList.size() / 10;
                            if (scrapList.size() % 10 == 0) ten--;
                            scrapPostList.clear();
                            for (int i = 0; i <= ten; i++) {
                                if (i != ten) {
                                    List<Long> scrap = scrapListUpTime.subList(i * 10, (i + 1) * 10);
                                    queryByUpTime(scrap, false);
                                } else {
                                    List<Long> scrap = scrapListUpTime.subList(i * 10, scrapListUpTime.size());
                                    queryByUpTime(scrap, true);
                                }
                            }
                        }
                    }
                });
    }

    private void queryByUpTime(List<Long> scrap, boolean last) {
        db.collectionGroup("posts").whereIn("upTime", scrap).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        scrapCnt = queryDocumentSnapshots.size();
                        cmp = 0;

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            scrapPostList.add(documentSnapshot.toObject(PostInfo.class));
                            cmp++;
                            if (last && cmp == scrapCnt) {
                                Log.e("size", "" + scrapPostList.size());
                                GridView gridView = findViewById(R.id.scrapListGridView);
                                adapter = new ScrapAdapter(scrapPostList, scrapList,
                                        ScrapListActivity.this, type);
                                gridView.setAdapter(adapter);
                            }
                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            adapter.notifyDataSetChanged();
            scrapPostList.clear();
            type = getIntent().getStringExtra("type");
            db.collection(type).document(user.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            scrapList = (ArrayList<String>) document.get("scrap");
                            ArrayList<Long> scrapListUpTime = new ArrayList<>();
                            for (String upTime : scrapList) {
                                scrapListUpTime.add(Long.parseLong(upTime));
                            }

                            int ten = scrapList.size() / 10;
                            if (scrapList.size() % 10 == 0) ten--;

                            for (int i = 0; i <= ten; i++) {
                                if (i != ten) {
                                    List<Long> scrap = scrapListUpTime.subList(i * 10, (i + 1) * 10);
                                    queryByUpTime(scrap, false);
                                } else {
                                    List<Long> scrap = scrapListUpTime.subList(i * 10, scrapListUpTime.size());
                                    queryByUpTime(scrap, true);
                                }
                            }
                        }
                    });
        }
    }
}