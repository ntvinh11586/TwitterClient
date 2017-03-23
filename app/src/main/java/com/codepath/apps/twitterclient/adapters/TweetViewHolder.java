package com.codepath.apps.twitterclient.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.unities.LinkifiedTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TweetViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvBody)
    LinkifiedTextView tvBody;
    @BindView(R.id.tvTimestamp)
    TextView tvTimestamp;
    @BindView(R.id.ivTimeline)
    ImageView ivTimeline;

    public TweetViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}