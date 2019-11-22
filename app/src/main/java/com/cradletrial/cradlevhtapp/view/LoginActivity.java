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
import com.cradletrial.cradlevhtapp.model.UserRetrofitApi;
import com.cradletrial.cradlevhtapp.model.User;
import com.cradletrial.cradlevhtapp.model.UserInfoStorage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


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

        if(userInfoStorage.isLoggedIn()){
            goToReadingList();
        }

        setupLoginButton();
        createNewUser();

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
                    Toast.makeText(LoginActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                    updateInputs();
                    goToReadingList();
                } else {
                    updateInputs();
                }

            case REMOVE_USER_INFO_ON_LOG_OUT:

                if(resultCode == Activity.RESULT_OK){
                    userInfoStorage.removeUserInfo();
//                    updateInputs(getString(R.string.result_ok));
                }
        }
    }

    private void updateInputs() {

        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);

        emailEditText.setText("");
        emailEditText.findFocus();
        passwordEditText.setText("");

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
                checkLogin(email, password);
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

    private void checkLogin(String email, String password){

//        ProxyBuilder.setOnTokenReceiveCallback(this::onReceiveToken);
//        Call<Void> caller = ProxyManager.getProxy(this).login(User.getCurrentUser());
//        ProxyBuilder.callProxy(this, caller, this::onUserLoggedIn);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        UserRetrofitApi userRetrofitApi = retrofit.create(UserRetrofitApi.class);
        Call<User> call = userRetrofitApi.login(email, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Response: " + response.code() + "\nIncorrect email or password", Toast.LENGTH_SHORT).show();
                    return;
                }

                onCurrentUserReceived(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Error: "  + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateInputs();
    }
}

