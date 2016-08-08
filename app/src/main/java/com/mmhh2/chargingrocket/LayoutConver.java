package com.mmhh2.chargingrocket;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * Created by AbuAli on 8/4/2016.
 */

public class LayoutConver extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private String  numID, type;

    public LayoutConver() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LayoutConver newInstance() {
        LayoutConver fragment = new LayoutConver();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_conver, container, false);


        return rootView;
    }
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {
                loadData();
            }

        }
    }
    protected void loadData() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        type = sharedPref.getString("type", null);
        numID = sharedPref.getString("numID", null);


    }

}

