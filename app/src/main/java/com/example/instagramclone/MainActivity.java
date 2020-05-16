package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnsave;
    private EditText edtname,edtps,edtpp,edtkp,edtks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnsave = findViewById(R.id.btnsave);
        btnsave.setOnClickListener(MainActivity.this);
        edtname = findViewById(R.id.edtname);
        edtps = findViewById(R.id.edtps);
        edtpp = findViewById(R.id.edtpp);
        edtks = findViewById(R.id.edtks);
        edtkp = findViewById(R.id.edtkp);

    }

    @Override
    public void onClick(View v) {
        final ParseObject kickboxer = new ParseObject("Kickboxer");
        kickboxer.put("Name", edtname.getText().toString());
        kickboxer.put("Punchspeed", edtps.getText().toString());
        kickboxer.put("Punchpower", edtpp.getText().toString());
        kickboxer.put("Kickspeed", edtks.getText().toString());
        kickboxer.put("Kickpower", edtkp.getText().toString());
        kickboxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    //Toast.makeText(MainActivity.this, kickboxer.get("Name")+" is saved to the Parse server.", Toast.LENGTH_SHORT).show();
                    FancyToast.makeText(MainActivity.this,kickboxer.get("Name")+" is saved to the server.",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                }else {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

