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
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.R;

import java.util.ArrayList;

public class QuestionFragment extends Fragment {

    Context mContext;
    RecyclerView recyclerView;
    String name;
    Button writeQuestion;
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
        ArrayList<sampleText> st = new ArrayList<>();

        st.add(new sampleText("마지막 질문", "답변은 얼마나 길어져도 괜찮을까 여기서 일정 길이 이상 들어가면 더보기가 나와야되는데 제대로 들어갈까답변은 얼마나 길어져도 괜찮을까 여기서 일정 길이 이상 들어가면 더보기가 나와야되는데 제대로 들어갈까?"));

        for(int i = 0; i < 10; i++){
            st.add(new sampleText(i + " 번째 질문", i + " 번째 답변"));
        }
        QuestionRecyclerViewAdapter adapter = new QuestionRecyclerViewAdapter(mContext, st);
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
