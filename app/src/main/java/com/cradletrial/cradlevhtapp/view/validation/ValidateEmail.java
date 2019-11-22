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

public class ValidateEmail {

    private Activity activity;
    private static boolean correctEmail;


    public ValidateEmail(Activity activity) {
        this.activity = activity;
    }

    public static boolean isEmailCorrect(){
        return correctEmail;
    }

    public void addEmailValidation(){

        EditText emailEditText = activity.findViewById(R.id.email);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {


                User.getCurrentUser().setEmail(editable.toString());

                ImageView emailErrorImageView = activity.findViewById(R.id.email_check);
                TextView inputCheckText = activity.findViewById(R.id.input_check_text);

                inputCheckText.setText(R.string.email_error_text);
                inputCheckText.setVisibility(View.VISIBLE);

                ValidateUserInputs.checkLength(User.getCurrentUser().getEmail(), emailErrorImageView);


                Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
                Matcher m = p.matcher(User.getCurrentUser().getEmail());

                if (m.find()) {

                    inputCheckText.setVisibility(View.INVISIBLE);
                    emailErrorImageView.setImageResource(R.drawable.correct_icon);
                    correctEmail = true;

                } else {

                    emailErrorImageView.setImageResource(R.drawable.error_icon);
                    correctEmail = false;
                }
            }
        });


    }

}
