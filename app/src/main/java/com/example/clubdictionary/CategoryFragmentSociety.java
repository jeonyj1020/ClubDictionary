package com.example.clubdictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CategoryFragmentSociety extends Fragment {

    public CategoryFragmentSociety() {
    }

    public static CategoryFragmentSociety newInstance(){
        return new CategoryFragmentSociety();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_society, container, false);
        return view;
    }
}