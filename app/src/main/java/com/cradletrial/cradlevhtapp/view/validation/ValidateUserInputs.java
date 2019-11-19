package com.cradletrial.cradlevhtapp.view.validation;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.cradletrial.cradlevhtapp.R;
import com.cradletrial.cradlevhtapp.model.User;

import java.util.regex.Matcher;

public class ValidateUserInputs {

    private Activity activity;
    private static boolean correctPassword;


    public ValidateUserInputs(Activity activity) {
        this.activity = activity;
    }

    public static boolean isPasswordCorrect(){
        return correctPassword;
    }

    public void addInputValidation(){

        ValidateEmail emailValidator = new ValidateEmail(activity);
        emailValidator.addEmailValidation();

        ValidatePassword passwordValidator =  new ValidatePassword(activity);
        passwordValidator.addPasswordValidation();
        passwordValidator.addPasswordConfirmationValidation();
    }

    public static void checkLength(String text, ImageView imageView){

        if (text != null && text.length() == 0) {
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public static void checkPasswordValidity(Matcher m, Activity activity, String confirmPassword){

        ImageView confirmedPasswordErrorImageView = activity.findViewById(R.id.confirm_password_check);

        if (confirmPassword.equals(User.getCurrentUser().getPassword()) && m.find()) {

            confirmedPasswordErrorImageView.setImageResource(R.drawable.correct_icon);
            correctPassword = true;

        } else {

            confirmedPasswordErrorImageView.setImageResource(R.drawable.error_icon);
            correctPassword = false;
        }

    }

}
