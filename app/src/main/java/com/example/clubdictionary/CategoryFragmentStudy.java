package com.example.clubdictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CategoryFragmentStudy extends Fragment {

    public CategoryFragmentStudy() {
    }

    public static CategoryFragmentStudy newInstance(){
        return new CategoryFragmentStudy();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_study, container, false);
        return view;
    }
}

