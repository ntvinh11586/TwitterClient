package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.twitterclient.unities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinh on 10/25/2016.
 */
@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {

    @Column(name = "tweetid", unique = true)
    private long id;
    @Column(name = "uid")
    private long uid;
    @Column(name = "body")
    private String body;
    @Column(name = "user",
            onUpdate = Column.ForeignKeyAction.CASCADE,
            onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;
    @Column(name = "createdAt")
    private String createdAt;
    @Column(name = "timestamp")
    private String timestamp;
    @Column(name = "tagName")
    private String tagName;

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Tweet() {
        super();
    }

    public static Tweet fromJSON(JSONObject jsonObject, String tagName) {
        Tweet tweet = new Tweet();
        try {
            tweet.id = ++Constants.tweetId;
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.timestamp = jsonObject.getString("created_at");
            tweet.tagName = tagName;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray, String tagName) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson, tagName);
                if (tweet != null) {
                    tweet.save();
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }

    public static List<Tweet> getAll(String tagName) {
        return new Select()
                .from(Tweet.class)
                .where("tagName = ?", tagName)
                .execute();
    }


}
