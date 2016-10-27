package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.twitterclient.unities.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterclient.unities.NetworkHelper;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.TweetArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public abstract class TweetsListFragment extends Fragment {

    protected ArrayList<Tweet> tweets;
    protected TweetArrayAdapter aTweets;
    protected RecyclerView rvTweets;
    protected SwipeRefreshLayout swipeContainer;
    protected ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //// TODO: 10/27/2016 refactory
        tweets = new ArrayList<>();
        aTweets = new TweetArrayAdapter(getActivity(), tweets);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        progressBar = (ProgressBar) view.findViewById(R.id.pbLoading);

        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);
        rvTweets.setAdapter(aTweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (NetworkHelper.isOnline()) {
                    progressBar.setVisibility(View.VISIBLE);
                    customLoadMoreDataFromApi(page);
                } else {
                    Toast.makeText(getActivity(), "Offline", Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkHelper.isOnline()) {
                    fetchTimelineAsync(0);
                } else {
                    Toast.makeText(getActivity(), "Offline", Toast.LENGTH_SHORT).show();
                    swipeContainer.setRefreshing(false);
                }
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);

        return view;
    }

    abstract void fetchTimelineAsync(int page);

    abstract void customLoadMoreDataFromApi(int page);

    public void addAll(List<Tweet> t) {
        tweets.addAll(t);
        aTweets.notifyDataSetChanged();
    }

    public void refreshAll(List<Tweet> t) {
        tweets.clear();
        tweets.addAll(t);
        aTweets.notifyDataSetChanged();
    }

}
