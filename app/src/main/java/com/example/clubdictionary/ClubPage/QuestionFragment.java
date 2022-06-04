package com.example.clubdictionary.ClubPage;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.ClubInfo;
import com.example.clubdictionary.GroupPostInfo;
import com.example.clubdictionary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class QuestionFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context mContext;
    RecyclerView recyclerView;
    String name;
    Button writeQuestion;
    String clubUid;
    private ArrayList<QuestionItem> arrayList = new ArrayList<>();
    public QuestionFragment(){}
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static QuestionFragment newInstance(int number, String name) {
        QuestionFragment questionFragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        bundle.putString("name", name);
        questionFragment.setArguments(bundle);
        return questionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int num =getArguments().getInt("number");
            name = getArguments().getString("name");
            Log.e("0530", name);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(inflater.getContext()).inflate(R.layout.fragment_clubpage_question, container, false);

        recyclerView = view.findViewById(R.id.quetion_list);
        writeQuestion  = view.findViewById(R.id.writeQuestion);
        /*ArrayList<QuestionItem> st = new ArrayList<>();*/
        QuestionRecyclerViewAdapter adapter = new QuestionRecyclerViewAdapter(mContext, arrayList);
        //db 연동해서 붙이기

        db.collection("clubs").whereEqualTo("name", name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        ClubInfo clubInfo = document.toObject(ClubInfo.class);
                        clubUid = clubInfo.getUid();
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               db.collection("clubs").document(clubUid).collection("QnA").orderBy("itemTime", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                   @Override
                   public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                       arrayList.clear();
                       for(QueryDocumentSnapshot document : value){
                           QuestionItem questionItem = document.toObject(QuestionItem.class);
                           arrayList.add(questionItem);
                       }
                       adapter.notifyDataSetChanged();
                   }
               });
            }
        });



        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);

        writeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), QuestionWriteActivity.class);
                intent.putExtra("name", name);
                mContext.startActivity(intent);
            }
        });
        return view;
    }
}
