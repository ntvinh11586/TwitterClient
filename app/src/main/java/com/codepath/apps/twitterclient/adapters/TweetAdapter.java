package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.unities.DateTimeHelper;
import com.codepath.apps.twitterclient.unities.NetworkHelper;

import org.parceler.Parcels;
import java.util.List;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import static android.view.View.GONE;

public class TweetAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    private List<Tweet> mTweets;
    private Context mContext;

    public TweetAdapter(Context context, List<Tweet> tweets) {
        mTweets = tweets;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        return new TweetViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder viewHolder, int position) {
        final Tweet tweet = mTweets.get(position);

        viewHolder.tvName.setText(tweet.getUser().getName());
        viewHolder.tvUsername.setText("@" + tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody().trim());
        viewHolder.tvTimestamp.setText(DateTimeHelper.getRelativeTimeAgo(tweet.getTimestamp()));
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(getContext(), 5, 0))
                .into(viewHolder.ivProfileImage);

        if (tweet.getMediaUrl() != null) {
            Glide.with(getContext())
                    .load(tweet.getMediaUrl())
                    .bitmapTransform(new RoundedCornersTransformation(getContext(), 10, 0))
                    .into(viewHolder.ivTimeline);
        } else {
            viewHolder.ivTimeline.setVisibility(GONE);
        }


        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkHelper.isOnline()) {
                    Intent intent = new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("user", Parcels.wrap(tweet.getUser()));
                    getContext().startActivity(intent);
                } else {
                    NetworkHelper.showOfflineNetwork(getContext());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}
