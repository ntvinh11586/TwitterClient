package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.UserPagerAdapter;
import com.codepath.apps.twitterclient.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.tvFullname)
    TextView tvFullname;
    @BindView(R.id.tvTagline)
    TextView tvTagline;
    @BindView(R.id.tvFollower)
    TextView tvFollowers;
    @BindView(R.id.tvFollowing)
    TextView tvFollowing;
    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.viewPager)
    ViewPager vpPager;
    @BindView(R.id.tabStrip)
    PagerSlidingTabStrip tabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        getSupportActionBar().setTitle(user.getScreenName());
        populateProfileHeader(user);

        vpPager = (ViewPager) findViewById(R.id.viewPager);
        vpPager.setAdapter(new UserPagerAdapter(getSupportFragmentManager(), user));
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        tabStrip.setViewPager(vpPager);
    }


    private void populateProfileHeader(User user) {
        tvFullname.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingsCount() + " Following");
        Glide.with(this)
                .load(user.getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(this, 5, 0))
                .into(ivProfileImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, TimelineActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

