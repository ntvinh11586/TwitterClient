package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetArrayAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetsListFragment extends Fragment {

    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter aTweets;
    protected RecyclerView rvTweets;

    private SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        rvTweets = (RecyclerView) v.findViewById(R.id.lvTweets);
        rvTweets.setAdapter(aTweets);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(linearLayoutManager);


        return v;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<>();
        aTweets = new TweetArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> t) {
        tweets.addAll(t);
        aTweets.notifyDataSetChanged();
    }

    public void refreshAll(List<Tweet> t) {
        Toast.makeText(getActivity(), "normal", Toast.LENGTH_SHORT).show();
        tweets.clear();
        tweets.addAll(t);
        aTweets.notifyDataSetChanged();
    }

//    swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
//
//    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//        @Override
//        public void onRefresh() {
//            fetchTimelineAsync(0);
//        }
//    });
//
//    swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//    android.R.color.holo_green_light,
//    android.R.color.holo_orange_light,
//    android.R.color.holo_red_light);

}
