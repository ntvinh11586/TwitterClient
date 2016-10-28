package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.networks.TwitterApplication;
import com.codepath.apps.twitterclient.networks.TwitterClient;
import com.codepath.apps.twitterclient.unities.NetworkHelper;
import com.codepath.apps.twitterclient.unities.PersistingDataHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeTimelineFragment extends TweetsListFragment
        implements CreateTweetDialogFragment.CreateNewTweetListener {

    TwitterClient client;
    FloatingActionButton floatingActionButton;

    public static HomeTimelineFragment newInstance() {
        return new HomeTimelineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        client = TwitterApplication.getRestClient();

        if (NetworkHelper.isOnline()) {
            PersistingDataHelper.deletePersistingData(getContext());
            populateTimeLine();
        } else {
            NetworkHelper.showOfflineNetwork(getContext());
            addAll(Tweet.getAll(PersistingDataHelper.HOME_TAG));
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabAddTweet);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateTweetDialog();
            }
        });
    }

    @Override
    void fetchTimelineAsync(int page) {
        client.getHomeTimeline(1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                refreshAll(Tweet.fromJSONArray(json, PersistingDataHelper.HOME_TAG));
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                NetworkHelper.showFailureMessage(getActivity(), errorResponse);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    void loadDataFromApi(int page) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                addAll(Tweet.fromJSONArray(json, PersistingDataHelper.HOME_TAG));
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                NetworkHelper.showFailureMessage(getActivity(), errorResponse);
                pbLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    void populateTimeLine() {
        loadDataFromApi(1);
    }

    @Override
    public void onFinishCreateNewTweet(String inputText) {

        TwitterClient client;
        client = TwitterApplication.getRestClient();
        client.setNewTweet(inputText, new JsonHttpResponseHandler());

        for (int i = 0; i < 200000000; i++) {
        } // loop for true results

        client.getHomeTimeline(1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                refreshAll(Tweet.fromJSONArray(json, PersistingDataHelper.HOME_TAG));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                NetworkHelper.showFailureMessage(getActivity(), errorResponse);
            }
        });
    }

    private void showCreateTweetDialog() {
        CreateTweetDialogFragment createTweetDialogFragment = CreateTweetDialogFragment.newInstance();
        createTweetDialogFragment.setTargetFragment(this, 1);
        createTweetDialogFragment.show(
                getActivity().getSupportFragmentManager(),
                "fragment_add_tweet");
    }
}



