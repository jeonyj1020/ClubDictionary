package com.example.clubdictionary.Category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clubdictionary.MainActivity;
import com.example.clubdictionary.R;

public class CategoryFragment extends Fragment {

    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(){
        return new CategoryFragment();
    }

    TextView society, study, sports, arts, religion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        society = view.findViewById(R.id.society);
        study = view.findViewById(R.id.study);
        sports = view.findViewById(R.id.sports);
        arts = view.findViewById(R.id.arts);
        religion = view.findViewById(R.id.religion);


        society.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(SocietyFragment.newInstance());
            }
        });
        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(StudyFragment.newInstance());
            }
        });
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(SportsFragment.newInstance());
            }
        });
        arts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(ArtsFragment.newInstance());
            }
        });
        religion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(ReligionFragment.newInstance());
            }
        });
        return view;
    }



}