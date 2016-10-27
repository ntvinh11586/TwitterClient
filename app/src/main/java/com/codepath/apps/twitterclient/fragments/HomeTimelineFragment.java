package com.codepath.apps.twitterclient.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.util.SQLiteUtils;
import com.codepath.apps.twitterclient.unities.NetworkHelper;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.networks.TwitterApplication;
import com.codepath.apps.twitterclient.networks.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeTimelineFragment extends TweetsListFragment implements CreateTweetDialogFragment.EditNameDialogListener {

    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        client = TwitterApplication.getRestClient();

        if (NetworkHelper.isOnline()) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if (pref.getBoolean("username", true)) {
                SQLiteUtils.execSql("DELETE FROM Tweets");
                SQLiteUtils.execSql("DELETE FROM Users");
            }
            populateTimeLine();
        } else {
            Toast.makeText(getContext(), "Offline mode", Toast.LENGTH_SHORT).show();
            tweets.addAll(Tweet.getAll("home"));
            aTweets.notifyDataSetChanged();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floating);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                refreshAll(Tweet.fromJSONArray(json, "home"));
                SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainer);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                NetworkHelper.showFailureMessage(getActivity(), errorResponse);
                swipeContainer.setRefreshing(false);
            }
        }, 1);
    }

    void customLoadMoreDataFromApi(int page) {

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                addAll(Tweet.fromJSONArray(json, "home"));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                NetworkHelper.showFailureMessage(getActivity(), errorResponse);
                progressBar.setVisibility(View.GONE);
            }
        }, page + 1);

    }

    private void showEditDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateTweetDialogFragment editNameDialogFragment = CreateTweetDialogFragment.newInstance("Some Title");
        editNameDialogFragment.setTargetFragment(this, 1);
        editNameDialogFragment.show(fm, "fragment_add_tweet");
    }

    private void populateTimeLine() {
        if (NetworkHelper.isOnline()) {
            client.getHomeTimeline(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    addAll(Tweet.fromJSONArray(json, "home"));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    NetworkHelper.showFailureMessage(getActivity(), errorResponse);
                }
            }, 1);
        } else {
            Toast.makeText(getActivity(), "Offline", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onFinishEditDialog(String inputText) {

        TwitterClient client;
        client = TwitterApplication.getRestClient();
        client.setNewTweet(new JsonHttpResponseHandler(), inputText);

        for (int i = 0; i < 200000000; i++) {
        } // loop for true results

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                refreshAll(Tweet.fromJSONArray(json, "home"));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                NetworkHelper.showFailureMessage(getActivity(), errorResponse);
            }
        }, 1);
    }

}



