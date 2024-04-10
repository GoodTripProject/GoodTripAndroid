package ru.hse.goodtrip.room;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * AppPreferences that provides access to SharedPreferences.
 */
public class AppPreferences {

  private static final String IS_USER_LOGGED_IN = "isUserLoggedIn";

  private static final String SHARED_PREF = "GtSharedPref";

  public static boolean isUserLoggedIn(Context ctx) {
    SharedPreferences prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
    return prefs.getBoolean(IS_USER_LOGGED_IN, false);
  }

  public static void setUserLoggedIn(Context ctx, Boolean value) {
    SharedPreferences prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
    SharedPreferences.Editor prefsEditor = prefs.edit();
    prefsEditor.putBoolean(IS_USER_LOGGED_IN, value);
    prefsEditor.apply();
  }

}
