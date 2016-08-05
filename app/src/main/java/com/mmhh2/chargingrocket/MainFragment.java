package com.mmhh2.chargingrocket;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;


/**
 * Created by AbuAli on 8/4/2016.
 */

    public  class MainFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        protected EditText EdTNumCard,EdTNumID;
        protected RadioButton RadBuSTC,RadBuMobily,RadBuZain;
        protected Button BuRecharge;
        private String numCard,numID,type;
        private static final String ARG_SECTION_NUMBER = "section_number";



        public MainFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MainFragment newInstance(int sectionNumber) {
            MainFragment fragment = new MainFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            EdTNumCard = (EditText) rootView.findViewById(R.id.EdTNumCard);
            EdTNumID = (EditText) rootView.findViewById(R.id.EdTNumID);
            RadBuSTC = (RadioButton) rootView.findViewById(R.id.RadBuSTC);
            RadBuMobily = (RadioButton) rootView.findViewById(R.id.RadBuMobily);
            RadBuZain = (RadioButton) rootView.findViewById(R.id.RadBuZain);
            BuRecharge = (Button) rootView.findViewById(R.id.BuRecharge);



            BuRecharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numCard = EdTNumCard.getText().toString();
                    numID = EdTNumID.getText().toString();
                    type = setType();
                    saveData();


                }
            });
            return rootView;
        }

    @Override
    public void onStart(){
        super.onStart();
        loadData();
    }

    protected void loadData(){
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        type = sharedPref.getString("type",null);
        numID = sharedPref.getString("numID",null);
        EdTNumID.setText(numID);
        if (type == "stc"){
            RadBuSTC.setChecked(true);
        }
        else if (type == "mobliy"){
            RadBuMobily.setChecked(true);
        }
        else if (type == "zain"){
            RadBuMobily.setChecked(true);
        }

    }
    protected void saveData(){
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("type",type);
        editor.putString("numID",numID);
        editor.commit();
    }
    protected String setType(){
        if (RadBuSTC.isChecked())
        {
            return "stc";
        }
        else if (RadBuMobily.isChecked())
        {
            return "mobily";
        }
        else if (RadBuZain.isChecked())
        {
            return "zain";
        }
        else {
            return "none";
        }
    }
    }

