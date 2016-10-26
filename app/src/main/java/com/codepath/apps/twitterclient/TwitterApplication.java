package com.codepath.apps.twitterclient;

import android.app.Application;
import android.content.Context;

public class TwitterApplication extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();

		TwitterApplication.context = this;
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApplication.context);
	}
}