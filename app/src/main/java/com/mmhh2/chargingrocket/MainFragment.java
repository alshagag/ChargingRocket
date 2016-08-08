package com.mmhh2.chargingrocket;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


/**
 * Created by AbuAli on 8/4/2016.
 */

public class MainFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private String  numID, type;


    public MainFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     * @param
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        RadioGroup RadGroServices = (RadioGroup)rootView.findViewById(R.id.RadGroServices);
        final RadioButton RadBuConver = (RadioButton)rootView.findViewById(R.id.RadBuConver);
        final RadioButton RadBuTalkMe = (RadioButton)rootView.findViewById(R.id.RadBuTalkme);
        final RadioButton RadBuCharge = (RadioButton)rootView.findViewById(R.id.RadBuCharge);
        final LinearLayout LiL = (LinearLayout)rootView.findViewById(R.id.LiL);
        if(!RadGroServices.isClickable()){
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.LiL,new LayoutCharge(),"Charge");
            transaction.commit();
        }
        RadGroServices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == RadBuConver.getId()){

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(LiL.getId(),new LayoutConver(),"Conver");
                    transaction.commit();
                }
                else if (i == RadBuTalkMe.getId()){

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.LiL,new LayoutTalkMe(),"TalkMe");
                    transaction.commit();
                }
                else if (i == RadBuCharge.getId()){
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.LiL,new LayoutCharge(),"Charge");
                    transaction.commit();
                }


            }});

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

