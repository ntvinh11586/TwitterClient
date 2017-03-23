package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.view.View;

import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.networks.TwitterApplication;
import com.codepath.apps.twitterclient.networks.TwitterClient;
import com.codepath.apps.twitterclient.unities.NetworkHelper;
import com.codepath.apps.twitterclient.unities.PersistingDataHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserFavoriteFragment extends TweetsListFragment {
    TwitterClient client;
    String screenName;

    public static UserFavoriteFragment newInstance(String screen_name) {
        UserFavoriteFragment userFragment = new UserFavoriteFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        screenName = getArguments().getString("screen_name");
        populateTimeLine();
    }

    @Override
    void fetchTimelineAsync(int page) {
        client.getUserFavorite(screenName, 1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                refreshAll(Tweet.fromJSONArray(json, PersistingDataHelper.NONE_TAG));
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
        client.getUserFavorite(screenName, page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                addAll(Tweet.fromJSONArray(json, PersistingDataHelper.NONE_TAG));
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                NetworkHelper.showFailureMessage(getActivity(), errorResponse);
                pbLoading.setVisibility(View.GONE);
            }
        });
    }

    void populateTimeLine() {
        loadDataFromApi(1);
    }
}
