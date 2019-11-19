package com.cradletrial.cradlevhtapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cradletrial.cradlevhtapp.R;
import com.cradletrial.cradlevhtapp.model.User;
import com.cradletrial.cradlevhtapp.model.UserInfoStorage;
import com.cradletrial.cradlevhtapp.proxy.ProxyBuilder;
import com.cradletrial.cradlevhtapp.proxy.ProxyManager;

import retrofit2.Call;


public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity";
    private final static int CREATE_USER = 0;
    private final static int REMOVE_USER_INFO_ON_LOG_OUT = 1;
    private final static int DISABLE_TIME_IN_MS = 3000;


    public static final String savedInfo = "SaveInfo";

    private UserInfoStorage userInfoStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfoStorage = new UserInfoStorage(this);
        userInfoStorage.getSavedInfo();

        if(wasUserLoggedIn()){
            goToReadingList();
        }

        setupLoginButton();
        createNewUser();
    }

    private boolean wasUserLoggedIn() {
        return ProxyManager.getToken() != null;
    }

    private void goToReadingList(){

        userInfoStorage.saveUserInfo();
        startActivityForResult(ReadingsListActivity.makeIntent(LoginActivity.this), REMOVE_USER_INFO_ON_LOG_OUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){

            case CREATE_USER:

                if(resultCode == Activity.RESULT_OK){
                    updateInputs(getString(R.string.result_ok), User.getCurrentUser().getEmail(), false);
                } else {
                    updateInputs(getString(R.string.cancelled), getString(R.string.empty), true);
                }

            case REMOVE_USER_INFO_ON_LOG_OUT:

                if(resultCode == Activity.RESULT_OK){

                    ProxyManager.setToken(null);
                    userInfoStorage.removeUserInfo();

                    updateInputs(getString(R.string.result_ok), getString(R.string.empty), true);
                }
        }
    }

    private void updateInputs(String message, String email, boolean isPasswordEmpty) {

        Log.i(TAG, message);
        EditText emailEditText = findViewById(R.id.email);
        emailEditText.setText(email);

        if(isPasswordEmpty){
            EditText passwordEditText = findViewById(R.id.password);
            passwordEditText.setText(getString(R.string.empty));
        }
    }

    private void setupLoginButton(){

        Button loginBtn = findViewById(R.id.login_button);

        loginBtn.setOnClickListener((View view) -> {

            Log.i(TAG, getString(R.string.login_btn_clicked));

            EditText emailEditText = findViewById(R.id.email);
            String email = emailEditText.getText().toString();
            User.getCurrentUser().setEmail(email);

            EditText passwordEditText = findViewById(R.id.password);
            String password = passwordEditText.getText().toString();
            User.getCurrentUser().setPassword(password);

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.empty_error_toast, Toast.LENGTH_SHORT).show();
            } else {
                disableButton();
                checkLogin();
            }
        });
    }

    private void disableButton(){

        Button loginBtn = findViewById(R.id.login_button);

        loginBtn.setAlpha(.5f);
        loginBtn.setEnabled(false);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {

            loginBtn.setAlpha(1f);
            loginBtn.setEnabled(true);

        }, DISABLE_TIME_IN_MS);
    }

    private void checkLogin(){

        ProxyBuilder.setOnTokenReceiveCallback(this::onReceiveToken);
        Call<Void> caller = ProxyManager.getProxy(this).login(User.getCurrentUser());
        ProxyBuilder.callProxy(this, caller, this::onUserLoggedIn);
    }

    private void onReceiveToken(String token){

        Log.i(TAG, getString(R.string.now_have_token) + token);
        ProxyManager.setToken(token);
    }

    private void onUserLoggedIn(Void returnedNothing) {

        Call<User> caller = ProxyManager.getProxy(this).getUserByEmail(User.getCurrentUser().getEmail());
        ProxyBuilder.callProxy(this, caller, this::onCurrentUserReceived);
    }

    private void onCurrentUserReceived(User currentUser) {

        User.getCurrentUser().setUserId(currentUser.getUserId());
        User.getCurrentUser().setFirstName(currentUser.getFirstName());
        User.getCurrentUser().setLastName(currentUser.getLastName());
        User.getCurrentUser().setEmail(currentUser.getEmail());
        User.getCurrentUser().setPassword(currentUser.getPassword());
        User.getCurrentUser().setPhoneNumber(currentUser.getPhoneNumber());
        User.getCurrentUser().setRole(currentUser.getRolesAsString());

        goToReadingList();
    }

    private void createNewUser() {

        TextView createUserTextView = findViewById(R.id.create_user_text);
        createUserTextView.setOnClickListener((View view) -> {

            Intent intent = NewUserActivity.makeIntent(LoginActivity.this);
            startActivityForResult(intent, CREATE_USER);
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

}

