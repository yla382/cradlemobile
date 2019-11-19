package com.cradletrial.cradlevhtapp.model;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.cradletrial.cradlevhtapp.R;
import com.cradletrial.cradlevhtapp.proxy.ProxyManager;


/**
 * The UserInfoStorage class handles storing and retrieving user info.
 */

public class UserInfoStorage {

    private final static String savedInfo = "SaveInfo";
    private final static String TAG = "LoginActivity";

    private Activity activity;


    public UserInfoStorage(Activity activity) {
        this.activity = activity;
    }

    public void getSavedInfo(){

        SharedPreferences preferences = activity.getSharedPreferences(savedInfo, Context.MODE_PRIVATE);

        Integer id = preferences.getInt(activity.getString(R.string.save_id), -1);
        String firstName = preferences.getString(activity.getString(R.string.save_first_name), null);
        String lastName = preferences.getString(activity.getString(R.string.save_last_name), null);
        String email = preferences.getString(activity.getString(R.string.save_user_email), null);
        String phoneNumber = preferences.getString(activity.getString(R.string.save_phone_number), null);
        String roles = preferences.getString(activity.getString(R.string.save_roles), null);
        String password = preferences.getString(activity.getString(R.string.save_user_password), null);

        User.getCurrentUser().setUserId(id);
        User.getCurrentUser().setFirstName(firstName);
        User.getCurrentUser().setLastName(lastName);
        User.getCurrentUser().setEmail(email);
        User.getCurrentUser().setPassword(password);
        User.getCurrentUser().setPhoneNumber(phoneNumber);
        User.getCurrentUser().setRole(roles);

        ProxyManager.setToken(preferences.getString(activity.getString(R.string.save_token), null));
    }

    public void saveUserInfo(){

        SharedPreferences sharedPreferences = activity.getSharedPreferences(savedInfo, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(activity.getString(R.string.save_id), User.getCurrentUser().getUserId());
        editor.putString(activity.getString(R.string.save_first_name), User.getCurrentUser().getFirstName());
        editor.putString(activity.getString(R.string.save_last_name), User.getCurrentUser().getLastName());
        editor.putString(activity.getString(R.string.save_user_email), User.getCurrentUser().getEmail());
        editor.putString(activity.getString(R.string.save_user_password), User.getCurrentUser().getPassword());
        editor.putString(activity.getString(R.string.save_phone_number), User.getCurrentUser().getPhoneNumber());
        editor.putString(activity.getString(R.string.save_roles), User.getCurrentUser().getRolesAsString());

        editor.putString(activity.getString(R.string.save_token), ProxyManager.getToken());

        Log.i(TAG, "user info saved");
        editor.apply();
    }

    public void removeUserInfo(){

        SharedPreferences sharedPreferences = activity.getSharedPreferences(savedInfo, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        Log.i(TAG, "user info removed");
    }

}


