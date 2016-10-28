package com.codepath.apps.twitterclient.unities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.activeandroid.util.SQLiteUtils;

/**
 * Created by Vinh on 10/27/2016.
 */

public class PersistingDataHelper {
    public static String HOME_TAG = "home";
    public static String MENTION_TAG = "mention";
    public static String NONE_TAG = "none";

    public static void createPersistingData(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(Constants.EXIST_DATA, false);
        edit.apply();
    }

    public static void deletePersistingData(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if (pref.getBoolean("username", true)) {
            SQLiteUtils.execSql("DELETE FROM Tweets");
            SQLiteUtils.execSql("DELETE FROM Users");
        }
    }

    public static void enablePersistingData(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(Constants.EXIST_DATA, true);
        edit.apply();
    }
}
