package com.codepath.apps.twitterclient.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinh on 10/27/2016.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvBody)
    TextView tvBody;
    @BindView(R.id.tvTimestamp)
    TextView tvTimestamp;

    public TweetViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}