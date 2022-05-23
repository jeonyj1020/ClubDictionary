package com.example.clubdictionary.Category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.clubdictionary.R;

public class SocietyFragment extends Fragment {

    public SocietyFragment() {
    }

    TextView volunteer;
    public static SocietyFragment newInstance(){
        return new SocietyFragment();
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_society, container, false);
        volunteer = view.findViewById(R.id.volunteer);
        volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Bundle bundle = new Bundle();
                bundle.putString("minor", "volunteer");
                ClubListFragment clubListFragment = new ClubListFragment();
                clubListFragment.setArguments(bundle);
                ((MainActivity)getActivity()).replaceFragment(ClubListFragment.newInstance());*/
                Bundle result = new Bundle();
                result.putString("minor", "volunteer");
                ClubListFragment clubListFragment = new ClubListFragment();
                clubListFragment.setArguments(result);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, clubListFragment).addToBackStack(null).commit();
                //((MainActivity)getActivity()).replaceFragment(ClubListFragment.newInstance());
            }
        });
        return view;
    }
}