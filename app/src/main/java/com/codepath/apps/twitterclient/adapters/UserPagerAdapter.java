package com.codepath.apps.twitterclient.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.twitterclient.fragments.UserFavoriteFragment;
import com.codepath.apps.twitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclient.models.User;

public class UserPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = {"Post", "Favorites"};

    private User user;

    public UserPagerAdapter(FragmentManager fm, User user) {
        super(fm);
        this.user = user;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return UserTimelineFragment.newInstance(user.getScreenName());
        } else {
            return UserFavoriteFragment.newInstance(user.getScreenName());
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
