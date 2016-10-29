package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.TweetsPagerAdapter;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.networks.TwitterApplication;
import com.codepath.apps.twitterclient.networks.TwitterClient;
import com.codepath.apps.twitterclient.unities.NetworkHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Vinh on 10/25/2016.
 */
public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    @BindView(R.id.viewPager)
    ViewPager vpPager;
    @BindView(R.id.tabStrip)
    PagerSlidingTabStrip tabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ActiveAndroid.initialize(this);
        ButterKnife.bind(this);

        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(vpPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miCompose:
                client = TwitterApplication.getRestClient();
                client.clearAccessToken();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onProfileView(MenuItem item) {
        client = TwitterApplication.getRestClient();
        if (NetworkHelper.isOnline()) {
            client.getUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // TODO: 10/27/2016 parcelable
                    Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
                    intent.putExtra("user", Parcels.wrap(User.fromJSON(response)));
                    startActivity(intent);
                }
            });
        } else {
            NetworkHelper.showOfflineNetwork(this);
        }
    }
}