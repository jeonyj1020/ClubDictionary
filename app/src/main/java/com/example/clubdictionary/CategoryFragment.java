package com.example.clubdictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CategoryFragment extends Fragment {

    public CategoryFragment() {
    }
//
TextView society, study, sports, culture, religion;;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        society = view.findViewById(R.id.society);
        study = view.findViewById(R.id.study);
        sports = view.findViewById(R.id.sports);
        culture = view.findViewById(R.id.culture);
        religion = view.findViewById(R.id.religion);
        society.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SocietyFragment.class);
                startActivity(intent);
            }
        });
        return view;
    }
}