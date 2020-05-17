package com.example.instagramclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpLoginActivity extends AppCompatActivity {
    private EditText edtusernamesignup,edtpasswordsignup,edtusernamelogin,edtpasswordlogin;
    private Button btnsignup,btnlogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_login_activity);

        edtusernamesignup = findViewById(R.id.edtusernamesignup);
        edtpasswordsignup = findViewById(R.id.edtpasswordsignup);
        edtusernamelogin = findViewById(R.id.edtusernamelogin);
        edtpasswordlogin = findViewById(R.id.edtpasswordlogin);
        btnsignup = findViewById(R.id.btnsignup);
        btnlogin = findViewById(R.id.btnlogin);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ParseUser appuser = new ParseUser();
                appuser.setUsername(edtusernamesignup.getText().toString());
                appuser.setPassword(edtpasswordsignup.getText().toString());

                appuser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            FancyToast.makeText(SignUpLoginActivity.this, appuser.getUsername() + " is successfully signed up",
                                    FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                        }else{
                            FancyToast.makeText(SignUpLoginActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }
                    }
                });

            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logInInBackground(edtusernamelogin.getText().toString(), edtpasswordlogin.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null && e == null){
                            FancyToast.makeText(SignUpLoginActivity.this, user.getUsername() + " is successfully logged in",
                                    FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                        }else{
                            FancyToast.makeText(SignUpLoginActivity.this, e.getMessage(),
                                    FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                        }
                    }
                });

            }
        });

    }
}
