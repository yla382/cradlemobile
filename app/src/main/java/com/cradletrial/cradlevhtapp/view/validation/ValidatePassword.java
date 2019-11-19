package com.cradletrial.cradlevhtapp.view.validation;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cradletrial.cradlevhtapp.R;
import com.cradletrial.cradlevhtapp.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatePassword {

    private Activity activity;


    public ValidatePassword(Activity activity) {
        this.activity = activity;
    }

    public void addPasswordValidation(){

        EditText passwordEditText = activity.findViewById(R.id.password);

        passwordEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }

            @Override
            public void afterTextChanged(Editable editable) {

                User.getCurrentUser().setPassword(editable.toString());

                ImageView passwordErrorImageView = activity.findViewById(R.id.password_check);

                TextView inputCheckText = activity.findViewById(R.id.input_check_text);

                inputCheckText.setText(R.string.password_error_text);
                inputCheckText.setVisibility(View.VISIBLE);


                ValidateUserInputs.checkLength(User.getCurrentUser().getPassword(), passwordErrorImageView);

                // Regular expression source: https://gist.github.com/ravibharathii/3975295

                Pattern p = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
                Matcher m = p.matcher(User.getCurrentUser().getPassword());

                if (m.find()) {
                    passwordErrorImageView.setImageResource(R.drawable.correct_icon);
                    inputCheckText.setVisibility(View.INVISIBLE);

                } else {
                    passwordErrorImageView.setImageResource(R.drawable.error_icon);
                }

                ImageView confirmedPasswordErrorImageView = activity.findViewById(R.id.confirm_password_check);
                confirmedPasswordErrorImageView.setVisibility(View.INVISIBLE);

                EditText confirmPasswordEditText = activity.findViewById(R.id.confirm_password);
                String confirmPassword = confirmPasswordEditText.getText().toString();

                ValidateUserInputs.checkPasswordValidity(m, activity, confirmPassword);
            }
        });
    }

    public void addPasswordConfirmationValidation(){

        EditText confirmPasswordEditText = activity.findViewById(R.id.confirm_password);

        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {

                ImageView confirmedPasswordErrorImageView = activity.findViewById(R.id.confirm_password_check);
                confirmedPasswordErrorImageView.setVisibility(View.VISIBLE);

                ImageView passwordErrorImageView = activity.findViewById(R.id.password_check);
                ValidateUserInputs.checkLength(User.getCurrentUser().getPassword(), passwordErrorImageView);

                Pattern p = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
                Matcher m = p.matcher(User.getCurrentUser().getPassword());

                String confirmPassword = editable.toString();
                ValidateUserInputs.checkPasswordValidity(m, activity, confirmPassword);
            }
        });
    }

}
