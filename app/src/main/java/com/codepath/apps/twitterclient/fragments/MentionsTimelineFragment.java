package com.codepath.apps.twitterclient.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.twitterclient.EditNameDialogFragment;
import com.codepath.apps.twitterclient.NetworkHelper;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApplication;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MentionsTimelineFragment extends TweetsListFragment implements EditNameDialogFragment.EditNameDialogListener {

    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();

        if (NetworkHelper.isOnline()) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            if (pref.getBoolean("username", true)) {
//                SQLiteUtils.execSql("DROP TABLE IF EXISTS Tweets");
//                SQLiteUtils.execSql("DROP TABLE IF EXISTS Users");
//            }
            populateTimeLine();
            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean("existData", true);
            edit.commit();
        } else {
            tweets.addAll(Tweet.getAll("mention"));
            aTweets.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.floating);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Some Title");
        editNameDialogFragment.setTargetFragment(this, 2);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        TwitterClient client;
        client = TwitterApplication.getRestClient();
        client.setNewTweet(new JsonHttpResponseHandler(), inputText);
    }

    private void populateTimeLine() {
        client.getMentionTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                addAll(Tweet.fromJSONArray(json, "mention"));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                NetworkHelper.showFailureMessage(getActivity(), errorResponse);
            }
        }, 1);
    }

    @Override
    public void fetchTimelineAsync(int page) {
        client.getMentionTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                refreshAll(Tweet.fromJSONArray(json, "mention"));
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
        client.getMentionTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                addAll(Tweet.fromJSONArray(json, "mention"));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                NetworkHelper.showFailureMessage(getActivity(), errorResponse);
                progressBar.setVisibility(View.GONE);
            }
        }, page + 1);
    }
}
