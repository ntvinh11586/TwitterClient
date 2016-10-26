package com.codepath.apps.twitterclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.codepath.apps.twitterclient.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Vinh on 10/25/2016.
 */
@Table(name = "Users")
public class User extends Model implements Serializable {

    @Column(name = "userid", unique = true)
    private long id;
    @Column(name = "uid")
    private long uid;
    @Column(name = "name")
    private String name;
    @Column(name = "screenName")
    private String screenName;
    @Column(name = "profileImageUrl")
    private String profileImageUrl;
    @Column(name = "tagline")
    private String tagline;
    @Column(name = "followersCount")
    private int followersCount;
    @Column(name = "followingsCount")
    private int followingsCount;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public User() {
        super();
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

    public static User fromJSON(JSONObject json) {
        User u = new User();
        try {
            u.id = ++Constants.userId;
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.tagline = json.getString("description");
            u.followersCount = json.getInt("followers_count");
            u.followingsCount = json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        u.save();

        return u;
    }
}


