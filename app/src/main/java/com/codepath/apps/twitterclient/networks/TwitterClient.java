package com.codepath.apps.twitterclient.networks;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "h2u1BtRI6PI5vlrE5qkBRzrQF";       // Change this
	public static final String REST_CONSUMER_SECRET = "WNZBO7iCtFnYKCA7GVzo2sItQNo966IKCFiv9UCst5vPTXFVlu"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	private static final String STATUSES_HOME_TIMELINE = "statuses/home_timeline.json";
	private static final String STATUSES_UPDATE = "statuses/update.json";
	private static final String STATUSES_MENTIONS_TIMELINE = "statuses/mentions_timeline.json";
	private static final String STATUSES_USER_TIMELINE = "statuses/user_timeline.json";
	private static final String FAVORITES_LIST = "favorites/list.json";
	private static final String ACCOUNT_VERIFY_CREDENTIALS = "account/verify_credentials.json";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler, int page) {
		String apiUrl = getApiUrl(STATUSES_HOME_TIMELINE);

		RequestParams params = new RequestParams();
		params.put("count", 10);
		params.put("since_id", 1);
		params.put("page", page);

		getClient().get(apiUrl, params, handler);
	}


	public void setNewTweet(AsyncHttpResponseHandler handler, String tweet) {
		String apiUrl = getApiUrl(STATUSES_UPDATE);

		RequestParams params = new RequestParams();
		params.put("status", tweet);

		getClient().post(apiUrl, params, handler);
	}


	public void getMentionTimeline(JsonHttpResponseHandler handler, int page) {
		String apiUrl = getApiUrl(STATUSES_MENTIONS_TIMELINE);

		RequestParams params = new RequestParams();
		params.put("count", 7);
		params.put("page", page);

		getClient().get(apiUrl, params, handler);
	}

	public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler, int page) {
		String apiUrl = getApiUrl(STATUSES_USER_TIMELINE);
		RequestParams params = new RequestParams();
		params.put("count", 7);
		params.put("screen_name", screenName);
		params.put("page", page);
		getClient().get(apiUrl, params, handler);
	}

	public void getUserFavorite(String screenName, AsyncHttpResponseHandler handler, int page) {
		String apiUrl = getApiUrl(FAVORITES_LIST);
		RequestParams params = new RequestParams();
		params.put("count", 7);
		params.put("screen_name", screenName);
		params.put("page", page);
		getClient().get(apiUrl, params, handler);
	}

	public void getUserInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl(ACCOUNT_VERIFY_CREDENTIALS);
		getClient().get(apiUrl, null, handler);
	}
}