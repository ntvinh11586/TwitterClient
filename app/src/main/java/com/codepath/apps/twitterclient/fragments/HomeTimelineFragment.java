package com.codepath.apps.twitterclient.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.util.SQLiteUtils;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.networks.TwitterApplication;
import com.codepath.apps.twitterclient.networks.TwitterClient;
import com.codepath.apps.twitterclient.unities.NetworkHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class HomeTimelineFragment extends TweetsListFragment
        implements CreateTweetDialogFragment.CreateNewTweetListener {

    private TwitterClient client;
    private FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        client = TwitterApplication.getRestClient();

        // // TODO: 10/27/2016 tach ham ra
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

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabAddTweet);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateTweetDialog();
            }
        });
    }

    void fetchTimelineAsync(int page) {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                refreshAll(Tweet.fromJSONArray(json, "home"));
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

    @Override
    public void onFinishCreateNewTweet(String inputText) {

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

    private void showCreateTweetDialog() {
        CreateTweetDialogFragment createTweetDialogFragment = CreateTweetDialogFragment.newInstance();
        createTweetDialogFragment.setTargetFragment(this, 1);
        createTweetDialogFragment.show(
                getActivity().getSupportFragmentManager(),
                "fragment_add_tweet");
    }
}



