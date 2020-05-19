package com.example.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener{

    private ImageView imgshare;
    private EditText edtdescription;
    private Button btnshare;
    Bitmap receivedimagebitmap;

    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        imgshare = view.findViewById(R.id.imgshare);
        edtdescription = view.findViewById(R.id.edtdescription);
        btnshare = view.findViewById(R.id.btnshare);

        imgshare.setOnClickListener(SharePictureTab.this);
        btnshare.setOnClickListener(SharePictureTab.this);

        return view;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.imgshare:

                if(Build.VERSION.SDK_INT>=23 && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                    requestPermissions(new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},1000);

                }else{
                    getchosenimage();
                }

                break;
            case R.id.btnshare:

                if(receivedimagebitmap!=null){

                    if(edtdescription.getText().toString().equals("")){
                        FancyToast.makeText(getContext(), "Please specify a description.",
                                Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }else{

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedimagebitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("pic.png", bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_des", edtdescription.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Please wait...");
                        dialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    FancyToast.makeText(getContext(), "DONE!",
                                            Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                }else{
                                    FancyToast.makeText(getContext(), "Unknown Error!",
                                            Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                }
                                dialog.dismiss();
                            }
                        });

                    }

                }else{
                    FancyToast.makeText(getContext(), "No image selected!",
                            Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                }

                break;

        }

    }

    private void getchosenimage() {
        FancyToast.makeText(getContext(), "Permission Granted",
                Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1000){

            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getchosenimage();
               }
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            //Do something with the selected image.
            try{

                Uri selectedimage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedimage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnindex = cursor.getColumnIndex(filePathColumn[0]);
                String picturepath = cursor.getString(columnindex);
                cursor.close();
                receivedimagebitmap = BitmapFactory.decodeFile(picturepath);
                imgshare.setImageBitmap(receivedimagebitmap);
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }
}

