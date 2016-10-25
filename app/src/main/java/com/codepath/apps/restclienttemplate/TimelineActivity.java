package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Vinh on 10/25/2016.
 */

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter aTweets;
    private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (ListView) findViewById(R.id.lvTweets);

        tweets = new ArrayList<>();
        aTweets = new TweetArrayAdapter(this, tweets);

        lvTweets.setAdapter(aTweets);


        client = TwitterApplication.getRestClient();

        populateTimeLine();
    }

    private void populateTimeLine() {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                Log.d("debug", json.toString());
//                Toast.makeText(TimelineActivity.this, "lol", Toast.LENGTH_SHORT).show();
//                ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);


                aTweets.addAll(Tweet.fromJSONArray(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("debug", errorResponse.toString());
                Toast.makeText(TimelineActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}