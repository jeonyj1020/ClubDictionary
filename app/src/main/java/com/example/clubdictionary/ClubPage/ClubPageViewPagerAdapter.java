package com.example.clubdictionary.ClubPage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ClubPageViewPagerAdapter extends FragmentStateAdapter {

    String clubUid;

    public ClubPageViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ClubPageViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String Uid) {
        super(fragmentActivity);
        this.clubUid = Uid;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return PostFragment.newInstance(position);
            case 1:
                return IntroFragment.newInstance(position);
            case 2:
                return QuestionFragment.newInstance(position, clubUid);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
