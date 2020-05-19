package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_users_tab, container, false);

        listView  = view.findViewById(R.id.listview);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);

        listView.setOnItemClickListener(UsersTab.this);
        listView.setOnItemLongClickListener(UsersTab.this);

        final TextView txtloadingusers = view.findViewById(R.id.txtloadingusers);


        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null){

                    if(objects.size()>0){

                        for(ParseUser user : objects){

                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        txtloadingusers.animate().alpha(0f).setDuration(2000);
                        listView.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        return view;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getContext(), Usersposts.class);
        intent.putExtra("username", arrayList.get(position));
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", arrayList.get(position));
        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if(user!=null && e == null){

                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());
                    prettyDialog.setTitle(user.getUsername() + "'s info")
                            .setMessage("Bio: "+user.get("ProfileBio")+"\n"+"Profession: "
                            + user.get("ProfileProfession")+"\n"+"Hobbies: "
                            + user.get("profileHobbies")+"\n"+"Favourite Movie: "
                            + user.get("ProfileFavouriteMovie"))
                            .setIcon(R.drawable.person)
                            .addButton(
                            "OK",     // button text
                            R.color.pdlg_color_white,  // button text color
                            R.color.pdlg_color_green,  // button background color
                            new PrettyDialogCallback() {  // button OnClick listener
                                @Override
                                public void onClick() {
                                    // Do what you gotta do
                                    prettyDialog.dismiss();
                                }
                            }
                    ).show();

                }

            }
        });

        return true;
    }
}
