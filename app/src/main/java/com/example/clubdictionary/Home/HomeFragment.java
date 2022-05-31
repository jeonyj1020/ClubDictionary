package com.example.clubdictionary.Home;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.FilterActivity;
import com.example.clubdictionary.MainActivity;
import com.example.clubdictionary.R;
import com.example.clubdictionary.WritePost.PostInfo;
import com.example.clubdictionary.WritePost.WriteContentsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.apphosting.datastore.testing.DatastoreTestTrace;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    HomeRecyclerViewAdapter homeRecyclerViewAdapter;
    Context mContext;

    // 필터링 관련 변수들
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference docRef = null;
    ArrayList<String> filtering = new ArrayList<>();
    String filteringBinary = null;
    ArrayList<Boolean> checked = new ArrayList<>();
    CheckBox onlyFavorite, onlyRecruit;
    Map<String, List<String>> bookMark = null;
    FloatingActionButton writePostButton;
    ArrayList<String> allMinor = new ArrayList<String>(Arrays.asList(
            "idea",
            "it", "liberalArts", "nature", "idea",
            "band", "instrument", "song", "play", "art", "cook", "dance", "picture", "handicraft",
            "ball", "racket", "martialArts", "extreme", "archery", "game",
            "christian", "buddhism", "catholic", "transpiration"
    ));
    ArrayList<PostInfo> postList = new ArrayList<>();
    ArrayList<String> intersectionClubs = new ArrayList<>();
    ArrayList<String> exceptFiltering = new ArrayList<>();

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = container.getContext();

        view.findViewById(R.id.filter).setOnClickListener(onClickListener);

        onlyFavorite = view.findViewById(R.id.onlyFavorite);
        onlyFavorite.setOnClickListener(onClickListener);

        onlyRecruit = view.findViewById(R.id.onlyRecruit);
        onlyRecruit.setOnClickListener(onClickListener);

        writePostButton = view.findViewById(R.id.writePostButton);
        writePostButton.setOnClickListener(onClickListener);

        docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (!documentSnapshot.exists()) {
                        Log.e("###", "클럽입니다");
                        docRef = db.collection("clubs").document(user.getUid());
                        writePostButton.setVisibility(View.VISIBLE);
                        getFiltering();
                    } else {
                        Log.e("###", "유저입니다");
                        //writePostButton.setVisibility(View.VISIBLE);
                        getFiltering();
                    }
                }
            }
        });

        //ArrayList<HomeItem> testDataSet = new ArrayList<>();

        //테스트 용
        ArrayList<String> testDataSet = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            testDataSet.add(i + "번째 아이템");
        }

        recyclerView = view.findViewById(R.id.fragmrnt_home_recyclerview);
        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(testDataSet, mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(homeRecyclerViewAdapter);

        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.filter:
                    Intent intent = new Intent(getActivity(), FilterActivity.class);
                    intent.putExtra("filteringBinary", filteringBinary);
                    startActivityForResult(intent, 1);
                    break;

                case R.id.onlyFavorite:
                case R.id.onlyRecruit:
                    checked.set(0, onlyRecruit.isChecked());
                    checked.set(1, onlyFavorite.isChecked());

                    if (onlyFavorite.isChecked())

                        docRef.update("checked", checked)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                    query();
                    break;

                case R.id.writePostButton:
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, 2);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case 1:
                    filtering = data.getStringArrayListExtra("newFiltering");
                    filteringBinary = data.getStringExtra("newFilteringBinary");
                    query();
                    break;

                case 2:
                    if (resultCode == Activity.RESULT_OK) {
                        ArrayList<Uri> imageList = new ArrayList<>();

                        if (data.getClipData() == null) {
                            imageList.add(Uri.parse(data.getData().getPath()));
                        } else {
                            ClipData clipData = data.getClipData();
                            if (clipData.getItemCount() > 10) {
                                Toast.makeText(getActivity(), "사진은 10개까지 선택가능 합니다.", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (clipData.getItemCount() == 1) {
                                imageList.add(clipData.getItemAt(0).getUri());
                            } else if (clipData.getItemCount() > 1 && clipData.getItemCount() < 10) {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    imageList.add((clipData.getItemAt(i).getUri()));
                                }
                            }
                        }

                        Intent intent = new Intent(getActivity(), WriteContentsActivity.class);
                        intent.putExtra("imageList", imageList);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "사진 선택을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void getFiltering() {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    filtering = (ArrayList<String>) documentSnapshot.get("filtering");
                    filteringBinary = (String) documentSnapshot.get("filteringBinary");
                    checked = (ArrayList<Boolean>) documentSnapshot.get("checked");
                    bookMark = (Map<String, List<String>>) documentSnapshot.get("bookMark");
                            /*Map<String, List<String>> bookMark = new HashMap<>();
                            bookMark = (Map<String, List<String>>) documentSnapshot.get("bookMark");
                            List<String> newBookMark = new ArrayList<>();

                            if(bookMark.containsKey("ball")){
                                newBookMark = bookMark.get("ball");
                            }
                            newBookMark.add("스트라이크");
                            bookMark.put("ball", newBookMark);

                            docRef.update("bookMark", bookMark);*/
                    if (checked.get(0)) onlyRecruit.setChecked(true);
                    else onlyRecruit.setChecked(false);
                    if (checked.get(1)) onlyFavorite.setChecked(true);
                    else onlyFavorite.setChecked(false);

                    query();
                }
            }
        });
    }

    private void query() {
        boolean onlyRecruitChecked = onlyRecruit.isChecked();
        boolean onlyFavoriteChecked = onlyFavorite.isChecked();
        Log.e("filter", " \n" + filtering + "\n" + filteringBinary + "\n" + onlyRecruitChecked +
                ", " + onlyFavoriteChecked + "\n" + bookMark);

        postList.clear();
        String is = "";
        if (onlyFavoriteChecked) {
            intersectionClubs.clear();
            for (String minor : bookMark.keySet()) {
                if (filtering.contains(minor)) {
                    List<String> clubs = bookMark.get(minor);
                    for (int i = 0; i < clubs.size(); i++) {
                        intersectionClubs.add(clubs.get(i));
                        is += clubs.get(i);
                    }
                }
            }
        }

        exceptFiltering.clear();
        for (String minor : allMinor) {
            if (!filtering.contains(minor)) {
                exceptFiltering.add(minor);
            }
        }

        Log.e("intersection", is);

        // 찜한 동아리만 보기가 선택되면 intersectionClubs의 동아리 이름들로 쿼리
        if (onlyFavoriteChecked) {
            if (intersectionClubs.size() == 0) {
                Toast.makeText(getActivity(), "필터링과 찜한 동아리 돌 다에 해당하는 동아리가 한 개도 없습니다!", Toast.LENGTH_SHORT).show();
            } else {
                int ten = intersectionClubs.size() / 10;
                Log.e("size", "" + intersectionClubs.size());
                if (onlyRecruitChecked) {
                    for (int i = 0; i <= ten; i++) {
                        if (i != ten) {
                            List<String> clubNames = intersectionClubs.subList(i * 10, (i + 1) * 10 - 1);
                            queryByClubNameAndOnlyRecruit(clubNames, 10);
                        } else {
                            List<String> clubNames = intersectionClubs.subList(i * 10, intersectionClubs.size());
                            Log.e("size", "" + clubNames.get(0));
                            queryByClubNameAndOnlyRecruit(clubNames, clubNames.size());
                        }
                    }
                } else {
                    for (int i = 0; i <= ten; i++) {
                        if (i != ten) {
                            List<String> clubNames = intersectionClubs.subList(i * 10, (i + 1) * 10 - 1);
                            queryByClubName(clubNames, 10);
                        } else {
                            List<String> clubNames = intersectionClubs.subList(i * 10, intersectionClubs.size());
                            Log.e("size", "" + clubNames.get(0));
                            queryByClubName(clubNames, clubNames.size());
                        }
                    }
                }
            }
        }
        // 찜한 동아리만 보기가 선택되지 않았으면, filtering의 소분류들로만 쿼리
        else {
            if (filtering == null || filtering.size() == allMinor.size()) {
                if (onlyRecruitChecked) {
                    db.collectionGroup("posts")
                            .whereEqualTo("recruit", true).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        PostInfo postInfo = documentSnapshot.toObject(PostInfo.class);
                                        postList.add(postInfo);
                                    }
                                }
                            });
                } else {
                    db.collectionGroup("posts").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        PostInfo postInfo = documentSnapshot.toObject(PostInfo.class);
                                        postList.add(postInfo);
                                    }
                                }
                            });
                }
            } else if (filtering.size() == 0) {
                Toast.makeText(getContext(), "소분류를 한 개도 선택하지 않아 게시물을 불러오지 않습니다!", Toast.LENGTH_SHORT).show();
            } else if (filtering.size() <= allMinor.size() / 2) {
                int ten = filtering.size() / 10;
                if (onlyRecruitChecked) {
                    for (int i = 0; i <= ten; i++) {
                        if (i != ten) {
                            List<String> minors = filtering.subList(i * 10, (i + 1) * 10 - 1);
                            queryByMinorAndOnlyRecruit(minors, 10);
                        } else {
                            List<String> minors = filtering.subList(i * 10, filtering.size());
                            queryByMinorAndOnlyRecruit(minors, minors.size());
                        }
                    }
                } else {
                    for (int i = 0; i <= ten; i++) {
                        if (i != ten) {
                            List<String> minors = filtering.subList(i * 10, (i + 1) * 10 - 1);
                            queryByMinor(minors, 10);
                        } else {
                            List<String> minors = filtering.subList(i * 10, filtering.size());
                            queryByMinor(minors, minors.size());
                        }
                    }
                }
            } else if (filtering.size() < allMinor.size()) {
                int ten = exceptFiltering.size() / 10;
                if(onlyRecruitChecked){
                    for (int i = 0; i <= ten; i++) {
                        if (i != ten) {
                            List<String> minors = exceptFiltering.subList(i * 10, (i + 1) * 10 - 1);
                            queryByMinorAndOnlyRecruit(minors, 10);
                        } else {
                            List<String> minors = exceptFiltering.subList(i * 10, exceptFiltering.size());
                            queryByMinorAndOnlyRecruit(minors, minors.size());
                        }
                    }
                }
                else{
                    for (int i = 0; i <= ten; i++) {
                        if (i != ten) {
                            List<String> minors = exceptFiltering.subList(i * 10, (i + 1) * 10 - 1);
                            queryByMinor(minors, 10);
                        } else {
                            List<String> minors = exceptFiltering.subList(i * 10, exceptFiltering.size());
                            queryByMinor(minors, minors.size());
                        }
                    }
                }

            }
        }
    }

    public void queryByClubName(List<String> clubNames, int max) {
        db.collection("clubs").whereIn("name", clubNames).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            documentSnapshot.getReference().collection("posts").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                postList.add(document.toObject(PostInfo.class));
                                            }
                                            if (max != 10) {
                                                Log.e("size", "" + postList.size());
                                                for (PostInfo postInfo : postList) {
                                                    Log.e("post", "" + postInfo.getName() + ", " + postInfo.getIconUrl()
                                                            + ", " + postInfo.getMajor() + ", " + postInfo.getMinor()
                                                            + ", " + postInfo.getUpTime());
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void queryByClubNameAndOnlyRecruit(List<String> clubNames, int max) {
        db.collection("clubs").whereIn("name", clubNames).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            documentSnapshot.getReference().collection("posts")
                                    .whereEqualTo("recruit", true).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                postList.add(document.toObject(PostInfo.class));
                                            }
                                            if (max != 10) {
                                                Log.e("size", "" + postList.size());
                                                for (PostInfo postInfo : postList) {
                                                    Log.e("post", "" + postInfo.getName() + ", " + postInfo.getIconUrl()
                                                            + ", " + postInfo.getMajor() + ", " + postInfo.getMinor()
                                                            + ", " + postInfo.getUpTime());
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void queryByMinor(List<String> Minors, int max) {
        db.collection("clubs").whereIn("minor", Minors).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            documentSnapshot.getReference().collection("posts").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                PostInfo postInfo = document.toObject(PostInfo.class);
                                                postList.add(postInfo);
                                            }
                                            if (max != 10) {
                                                Log.e("size", "" + postList.size());
                                                for (PostInfo postInfo : postList) {
                                                    Log.e("post", "" + postInfo.getName() + ", " + postInfo.getIconUrl()
                                                            + ", " + postInfo.getMajor() + ", " + postInfo.getMinor()
                                                            + ", " + postInfo.getUpTime());
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void queryByMinorAndOnlyRecruit(List<String> Minors, int max) {
        db.collection("clubs").whereIn("minor", Minors).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            documentSnapshot.getReference().collection("posts")
                                    .whereEqualTo("recruit", true).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                PostInfo postInfo = document.toObject(PostInfo.class);
                                                postList.add(postInfo);
                                            }
                                            if (max != 10) {
                                                Log.e("size", "" + postList.size());
                                                for (PostInfo postInfo : postList) {
                                                    Log.e("post", "" + postInfo.getName() + ", " + postInfo.getIconUrl()
                                                            + ", " + postInfo.getMajor() + ", " + postInfo.getMinor()
                                                            + ", " + postInfo.getUpTime());
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
