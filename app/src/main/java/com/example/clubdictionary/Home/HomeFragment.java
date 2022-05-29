package com.example.clubdictionary.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.FilterActivity;
import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    HomeRecyclerViewAdapter homeRecyclerViewAdapter;
    Context mContext;

    // 필터링 관련 변수들
    DocumentReference docRef = null;
    ArrayList<String> filtering = new ArrayList<>();
    String filteringBinary = null;
    ArrayList<Boolean> checked = new ArrayList<>();
    CheckBox onlyFavorite, onlyRecruit;
    Map<String, List<String>> bookMark = null;

    private FirebaseUser user;

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

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        docRef = db.collection("clubs").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(!documentSnapshot.exists()){
                        docRef = db.collection("users").document(user.getUid());
                        getFiltering();
                    }
                    else getFiltering();

                }
            }
        });

        ArrayList<HomeItem> testDataSet = new ArrayList<>();

        recyclerView = view.findViewById(R.id.fragmrnt_home_recyclerview);
        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(testDataSet, mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(homeRecyclerViewAdapter);

        return view;
    }

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
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.filter:
                    Intent intent = new Intent(getActivity(), FilterActivity.class);
                    intent.putExtra("filteringBinary", filteringBinary);
                    startActivityForResult(intent, 1);
                    break;
                case R.id.onlyFavorite:
                case R.id.onlyRecruit:
                    checked.set(0, onlyRecruit.isChecked());
                    checked.set(1, onlyFavorite.isChecked());

                    if(onlyFavorite.isChecked())

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
            }
        }
    };

    private void getFiltering(){
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
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
        Log.d("filter", " \n" + filtering + "\n" + filteringBinary + "\n" + onlyRecruitChecked +
                ", " + onlyFavoriteChecked + "\n" + bookMark);

        ArrayList<String> intersection = null;
        /*if(onlyFavoriteChecked){
            for(){

            }
        }
        else{
            intersection = filtering;
        }*/
        /*ArrayList<Task> tasks = null;
        for(int i = 0; i <= filtering.size()/10; i++) {
            List<String> now = null;
            if(filtering.size() >= 10 * i) {
                now = filtering.subList(10 * i, 10 * i + 9);
            }
            else{
                now = filtering.subList(10 * i, filtering.size() - 1);
            }
            Query query = clubsRef.whereIn("minor", now);
            tasks.set(i, query.get());
        }*/
    }
}
