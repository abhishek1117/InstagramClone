package com.example.instagramclone;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    private EditText edtprofilename, edtprofilebio, edtprofileprofession, edtprofilehobbies, edtprofilemovie;
    private Button btnupdateinfo;


    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_tab, container, false);

        edtprofilename = view.findViewById(R.id.edtprofilename);
        edtprofilebio = view.findViewById(R.id.edtprofilebio);
        edtprofileprofession = view.findViewById(R.id.edtprofileprofession);
        edtprofilehobbies = view.findViewById(R.id.edtprofilehobbies);
        edtprofilemovie = view.findViewById(R.id.edtprofilemovie);
        btnupdateinfo = view.findViewById(R.id.btnupdateinfo);

        final ParseUser parseUser = ParseUser.getCurrentUser();

        edtprofilename.setText(parseUser.get("ProfileName")+"");
        edtprofilebio.setText(parseUser.get("ProfileBio")+"");
        edtprofileprofession.setText(parseUser.get("ProfileProfession")+"");
        edtprofilehobbies.setText(parseUser.get("profileHobbies")+"");
        edtprofilemovie.setText(parseUser.get("ProfileFavouriteMovie")+"");

        btnupdateinfo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                parseUser.put("ProfileName", edtprofilename.getText().toString());
                parseUser.put("ProfileBio", edtprofilebio.getText().toString());
                parseUser.put("ProfileProfession", edtprofileprofession.getText().toString());
                parseUser.put("profileHobbies", edtprofilehobbies.getText().toString());
                parseUser.put("ProfileFavouriteMovie", edtprofilemovie.getText().toString());

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            FancyToast.makeText(getContext(), "Info Updated",
                                    Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
                        }else{
                            FancyToast.makeText(getContext(), e.getMessage(),
                                    Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                        }
                    }
                });
            }
        });

        return view;

    }
}
