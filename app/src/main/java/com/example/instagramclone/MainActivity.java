package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtemail,edtusername,edtpassword;
    private Button btnlogin,btnsignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sign Up");
        edtemail = findViewById(R.id.edtemailsignup);
        edtusername = findViewById(R.id.edtusernamesignup);
        edtpassword = findViewById(R.id.edtpasswordsignup);

        edtpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){

                    onClick(btnsignup);
                }
                return false;
            }
        });

        btnlogin = findViewById(R.id.btnloginsignup);
        btnsignup = findViewById(R.id.btnsignupsignup);
        btnlogin.setOnClickListener(this);
        btnsignup.setOnClickListener(this);
        if(ParseUser.getCurrentUser()!=null) {
            //ParseUser.getCurrentUser().logOut();
            transitiontosocialmediaactivity();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnsignupsignup:

                if(edtemail.getText().toString().equals("")||edtusername.getText().toString().equals("")
                        ||edtpassword.getText().toString().equals("")){
                    FancyToast.makeText(MainActivity.this, "All the fields need to be filled",
                            Toast.LENGTH_LONG, FancyToast.INFO, true).show();

                }
                else {
                    final ParseUser appuser = new ParseUser();
                    appuser.setEmail(edtemail.getText().toString());
                    appuser.setUsername(edtusername.getText().toString());
                    appuser.setPassword(edtpassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing up " + edtusername.getText().toString());
                    progressDialog.show();
                    appuser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(MainActivity.this, appuser.getUsername() + " is signed up!",
                                        Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                transitiontosocialmediaactivity();
                            } else {
                                FancyToast.makeText(MainActivity.this, "There was an error" + e.getMessage(),
                                        Toast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btnloginsignup:

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                break;
        }
    }
    public void rootlayouttapped(View view){
        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch(Exception e){

            e.printStackTrace();

        }
    }
    private void transitiontosocialmediaactivity(){

        Intent intent = new Intent(MainActivity.this, SocialMedia.class);
        startActivity(intent);
    }
}

