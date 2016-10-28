package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
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

public class MentionsTimelineFragment extends TweetsListFragment implements CreateTweetDialogFragment.CreateNewTweetListener {

    TwitterClient client;

    public static MentionsTimelineFragment newInstance() {
        return new MentionsTimelineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        if (NetworkHelper.isOnline()) {
            populateTimeLine();
            PersistingDataHelper.enablePersistingData(getContext());
        } else {
            NetworkHelper.showOfflineNetwork(getContext());
            addAll(Tweet.getAll(PersistingDataHelper.MENTION_TAG));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddTweet);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateTweetDialogFragment editNameDialogFragment = CreateTweetDialogFragment.newInstance();
        editNameDialogFragment.setTargetFragment(this, 2);
        editNameDialogFragment.show(fm, "fragment_add_tweet");
    }

    @Override
    public void onFinishCreateNewTweet(String inputText) {
        TwitterClient client;
        client = TwitterApplication.getRestClient();
        client.setNewTweet(inputText, new JsonHttpResponseHandler());
    }

    @Override
    void populateTimeLine() {
        loadDataFromApi(1);
    }

    @Override
    public void fetchTimelineAsync(int page) {
        client.getMentionTimeline(1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                refreshAll(Tweet.fromJSONArray(json, PersistingDataHelper.MENTION_TAG));
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
        client.getMentionTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                addAll(Tweet.fromJSONArray(json, PersistingDataHelper.MENTION_TAG));
                pbLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                NetworkHelper.showFailureMessage(getActivity(), errorResponse);
                pbLoading.setVisibility(View.GONE);
            }
        });
    }
}
