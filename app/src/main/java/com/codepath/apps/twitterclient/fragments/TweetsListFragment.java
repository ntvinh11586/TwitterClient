package com.codepath.apps.twitterclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.TweetActivity;
import com.codepath.apps.twitterclient.adapters.TweetAdapter;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.unities.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterclient.unities.ItemClickSupport;
import com.codepath.apps.twitterclient.unities.NetworkHelper;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class TweetsListFragment extends Fragment {
    protected ArrayList<Tweet> mTweets;
    protected TweetAdapter tweetAdapter;

    @BindView(R.id.rvTweets)
    protected RecyclerView rvTweets;
    @BindView(R.id.swipeContainer)
    protected SwipeRefreshLayout swipeContainer;
    @BindView(R.id.pbLoading)
    protected ProgressBar pbLoading;
    protected Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDataHolding();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        unbinder = ButterKnife.bind(this, view);

        rvTweets.setAdapter(tweetAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (NetworkHelper.isOnline()) {
                    pbLoading.setVisibility(View.VISIBLE);
                    loadDataFromApi(page + 1);
                } else {
                    NetworkHelper.showOfflineNetwork(getContext());
                }
            }
        });
        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                  User user = mTweets.get(position).getUser();
                        Intent intent = new Intent(getActivity(), TweetActivity.class);
                        intent.putExtra("tweet", Parcels.wrap(mTweets.get(position)));
                        startActivity(intent);
                    }
                }
        );


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkHelper.isOnline()) {
                    fetchTimelineAsync(0);
                } else {
                    NetworkHelper.showOfflineNetwork(getContext());
                    swipeContainer.setRefreshing(false);
                }
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    abstract void fetchTimelineAsync(int page);

    abstract void loadDataFromApi(int page);

    abstract void populateTimeLine();

    private void setupDataHolding() {
        mTweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(getActivity(), mTweets);
    }

    public void addAll(List<Tweet> tweets) {
        mTweets.addAll(tweets);
        tweetAdapter.notifyDataSetChanged();
    }

    public void refreshAll(List<Tweet> tweets) {
        mTweets.clear();
        mTweets.addAll(tweets);
        tweetAdapter.notifyDataSetChanged();
    }
}
