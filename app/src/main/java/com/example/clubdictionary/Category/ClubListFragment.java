package com.example.clubdictionary.Category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clubdictionary.ClubPageActivity;
import com.example.clubdictionary.MainActivity;
import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;


public class ClubListFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ClubListFragment() {
        // Required empty public constructor
    }


    private Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We set the listener on the child fragmentManager
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_club_list, container, false);
        TextView testTextView = view.findViewById(R.id.testTextView);
        Bundle bundle = this.getArguments();
        String result = bundle.getString("minor");
        db.collection("clubs").whereEqualTo("minor", result).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()) {
                               //조건에 맞는 동아리들을 불러왔고, foreach문으로 각각의 동아리의 이름을
                                //String name = document.getId();
                               // ClubInfo club = document.toObject(ClubInfo.class);
                                //String name = club.getName();
                                String name = document.get("name").toString();
                                testTextView.setText(name);
                                testTextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), ClubPageActivity.class);
                                        intent.putExtra("name", name);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                        else{
                            Toast.makeText(context, "정보를 불러오는데 실패하였습니다.",Toast.LENGTH_LONG).show();
                        }
                    }
                });

        return view;
    }
}