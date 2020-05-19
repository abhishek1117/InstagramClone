package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class Usersposts extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersposts);

        linearLayout = findViewById(R.id.linearlayout);

        Intent receiveIntentObject = getIntent();
        final String receivedUsername = receiveIntentObject.getStringExtra("username");
        FancyToast.makeText(this, receivedUsername, Toast.LENGTH_SHORT, FancyToast.INFO, true).show();

        setTitle(receivedUsername + "'s posts");

        final ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username", receivedUsername);
        parseQuery.orderByDescending("createdAt");

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0 && e == null){

                    for(ParseObject post : objects){

                        final TextView postdescription = new TextView(Usersposts.this);
                        postdescription.setText(post.get("image_des") + "");
                        ParseFile postpicture = (ParseFile)post.get("picture");
                        postpicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(data != null && e == null){

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView postimageview = new ImageView(Usersposts.this);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(5,5,5,5);
                                    postimageview.setLayoutParams(params);
                                    postimageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postimageview.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams des_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    des_params.setMargins(5,5,5,5);
                                    postdescription.setLayoutParams(des_params);
                                    postdescription.setGravity(Gravity.CENTER);
                                    postdescription.setBackgroundColor(Color.BLUE);
                                    postdescription.setTextColor(Color.WHITE);
                                    postdescription.setTextSize(25f);

                                    linearLayout.addView(postimageview);
                                    linearLayout.addView(postdescription);

                                }
                            }
                        });
                    }
                }else{
                    FancyToast.makeText(Usersposts.this, receivedUsername + " hasn't posted anything yet.", Toast.LENGTH_LONG,
                    FancyToast.INFO, true).show();
                    finish();
                }

                dialog.dismiss();
            }
        });

    }
}
