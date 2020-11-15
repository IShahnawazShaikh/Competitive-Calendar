package com.shahnawazshaikh.competitive.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.shahnawazshaikh.competitive.activity.Login;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSession {

    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared preferences mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREFER_NAME = "login_preference";

    // All Shared Preferences Keys
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_INSTITUTE = "institute_name";
    public static final String KEY_PASSWORD = "password";


//    public static final String KEY_STATUS="StatusCheck";
     public static final String KEY_LOGIN = "LoginType";

    // Constructor
    public UserSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String email, String name, String institute,String b,String password) {
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing name in preferences
        editor.putString(KEY_NAME, name);

        // Storing email in preferences
        editor.putString(KEY_EMAIL, email);

        editor.putString(KEY_INSTITUTE, institute);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_LOGIN, b);


       // editor.putString(KEY_STATUS,status);
        // commit changes
        editor.apply();
    }


    public void setName(String name) {
        editor.putString(KEY_NAME, name);
        editor.apply();
    }
//    public void setStatus(String status) {
//        editor.putString(KEY_STATUS, status);
//        editor.apply();
//    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

            return true;
        }
        return false;
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

       // Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_INSTITUTE, pref.getString(KEY_INSTITUTE, null));
        user.put(KEY_LOGIN, pref.getString(KEY_LOGIN, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.apply();

        // After logout redirect user to LoginActivity

    }


    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
