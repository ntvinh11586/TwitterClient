package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.networks.TwitterApplication;
import com.codepath.apps.twitterclient.networks.TwitterClient;
import com.codepath.apps.twitterclient.unities.DateTimeHelper;
import com.codepath.apps.twitterclient.unities.NetworkHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.view.View.GONE;
import static com.activeandroid.Cache.getContext;

public class TweetActivity extends AppCompatActivity {
    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvBody)
    TextView tvBody;
    @BindView(R.id.tvTimestamp)
    TextView tvTimestamp;
    @BindView(R.id.etTweet)
    EditText etTweet;
    @BindView(R.id.tvAvailableCharacters)
    TextView tvAvailableCharacters;
    @BindView(R.id.btnTweet)
    Button btnTweet;
    @BindView(R.id.ivTimeline)
    ImageView ivTimeline;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        ButterKnife.bind(this);

        final Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        tvName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvTimestamp.setText(DateTimeHelper.getRelativeTimeAgo(tweet.getTimestamp()));
        ivProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .into(ivProfileImage);
        if (tweet.getMediaUrl() != null) {
            Glide.with(getContext())
                    .load(tweet.getMediaUrl())
                    .bitmapTransform(new RoundedCornersTransformation(getContext(), 10, 0))
                    .into(ivTimeline);
        } else {
            ivTimeline.setVisibility(GONE);
        }


        ivProfileImage.setTag(tweet);
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkHelper.isOnline()) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("user", Parcels.wrap(((Tweet) view.getTag()).getUser()));
                    startActivity(intent);
                } else {
                    NetworkHelper.showOfflineNetwork(getContext());
                }
            }
        });


        tvAvailableCharacters.setTextColor(ContextCompat.getColor(
                getContext(), android.R.color.holo_green_light));
        etTweet.requestFocus();
        etTweet.setText("@" + tweet.getUser().getScreenName() + " ");
        etTweet.setSelection(etTweet.getText().length());
        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTextAvailableCharacter(etTweet.getText().toString().length());
            }

            private void setTextAvailableCharacter(int size) {
                if (140 - size >= 0) {
                    tvAvailableCharacters.setTextColor(ContextCompat.getColor(getContext(),
                            android.R.color.holo_green_light));
                    btnTweet.setEnabled(true);
                } else {
                    tvAvailableCharacters.setTextColor(ContextCompat.getColor(getContext(),
                            android.R.color.holo_red_light));
                    btnTweet.setEnabled(false);
                }
                tvAvailableCharacters.setText(String.valueOf(140 - size));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client = TwitterApplication.getRestClient();
                client.setNewTweetReply(etTweet.getText().toString(), tweet.getUid(),
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                Toast.makeText(TweetActivity.this, "Reply Successfully!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                Toast.makeText(TweetActivity.this, responseString, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
