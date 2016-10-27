package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.ProfileActivity;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.codepath.apps.twitterclient.unities.NetworkHelper;
import com.codepath.apps.twitterclient.unities.TwitterClientHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Vinh on 10/25/2016.
 */

public class TweetArrayAdapter extends RecyclerView.Adapter<TweetArrayAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimestamp;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.image_profile);
            tvUsername = (TextView) itemView.findViewById(R.id.text_username);
            tvBody = (TextView) itemView.findViewById(R.id.text_body);
            tvTimestamp = (TextView) itemView.findViewById(R.id.text_timestamp);
        }
    }

    private List<Tweet> mTweets;

    private Context mContext;

    public TweetArrayAdapter(Context context, List<Tweet> tweets) {
        mTweets = tweets;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public TweetArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetArrayAdapter.ViewHolder viewHolder, int position) {
        final Tweet tweet = mTweets.get(position);

        ImageView ivProfileImage = viewHolder.ivProfileImage;
        TextView tvUsername = viewHolder.tvUsername;
        TextView tvBody = viewHolder.tvBody;
        TextView tvTimestamp = viewHolder.tvTimestamp;

        tvUsername.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvTimestamp.setText(TwitterClientHelper.getRelativeTimeAgo(tweet.getTimestamp()));

        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);


        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetworkHelper.isOnline()) {
                    User user = tweet.getUser();
                    Intent i = new Intent(getContext(), ProfileActivity.class);
                    i.putExtra("user", user);
                    getContext().startActivity(i);
                } else {
                    Toast.makeText(getContext(), "Offline mode", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
}
