package com.cradletrial.cradlevhtapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cradletrial.cradlevhtapp.R;
import com.cradletrial.cradlevhtapp.model.User;
import com.cradletrial.cradlevhtapp.proxy.ProxyBuilder;
import com.cradletrial.cradlevhtapp.proxy.ProxyManager;
import com.cradletrial.cradlevhtapp.view.validation.ValidateEmail;
import com.cradletrial.cradlevhtapp.view.validation.ValidateUserInputs;

import retrofit2.Call;

public class NewUserActivity extends AppCompatActivity {

    private final static String TAG = "NewUserActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);


        ValidateUserInputs inputValidator = new ValidateUserInputs(this);
        inputValidator.addInputValidation();

        setCreateUserBtn();


    }

    private void setCreateUserBtn(){

        Button createUserBtn = findViewById(R.id.create_user_button);

        createUserBtn.setOnClickListener((View view) -> {

            EditText firstNameEditText = findViewById(R.id.first_name);
            String firstName = firstNameEditText.getText().toString();
            User.getCurrentUser().setFirstName(firstName);

            EditText lastNameEditText = findViewById(R.id.last_name);
            String lastName = lastNameEditText.getText().toString();
            User.getCurrentUser().setLastName(lastName);


            EditText emailEditText = findViewById(R.id.email);
            String email = emailEditText.getText().toString();
            User.getCurrentUser().setEmail(email);

            EditText passwordEditText = findViewById(R.id.password);
            String password = passwordEditText.getText().toString();
            User.getCurrentUser().setPassword(password);

            if(ValidateEmail.isEmailCorrect() && ValidateUserInputs.isPasswordCorrect()){
                executeUserCreate();
            } else {
                Toast.makeText(this, R.string.check_inputs, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void executeUserCreate() {

        Call<User> caller = ProxyManager.getProxy(NewUserActivity.this).createUser(User.getCurrentUser());
        ProxyBuilder.callProxy(NewUserActivity.this, caller, this::onUserCreated);
    }

    private void onUserCreated(User user) {

        setResult(Activity.RESULT_OK);
        finish();
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, NewUserActivity.class);
    }

}
