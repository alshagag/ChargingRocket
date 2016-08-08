package com.mmhh2.chargingrocket;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by AbuAli on 8/5/2016.
 */
public class SettingsFragment extends android.support.v4.app.Fragment {
    protected RadioButton RadBuSTC, RadBuMobily, RadBuZain;
    protected EditText EdTNumID;
    private String  numID, type;
    public SettingsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        EdTNumID = (EditText) rootView.findViewById(R.id.EdTNumID);
        RadBuSTC = (RadioButton) rootView.findViewById(R.id.RadBuSTC);
        RadBuMobily = (RadioButton) rootView.findViewById(R.id.RadBuMobily);
        RadBuZain = (RadioButton) rootView.findViewById(R.id.RadBuZain);

        return rootView;
    }

    protected void saveData() {

        numID = EdTNumID.getText().toString();
        type = setType();
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("type", type);
        editor.putString("numID", numID);
        editor.commit();
    }

    protected String setType() {
        if (RadBuSTC.isChecked()) {
            return "stc";
        } else if (RadBuMobily.isChecked()) {
            return "mobily";
        } else if (RadBuZain.isChecked()) {
            return "zain";
        } else {
            return "none";
        }
    }
    protected void loadData() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        type = sharedPref.getString("type", null);
        numID = sharedPref.getString("numID", null);
        EdTNumID.setText(numID);
        try {
            if (type.equals("stc")) {
                RadBuSTC.setChecked(true);
            } else if (type.equals("mobily")) {
                RadBuMobily.setChecked(true);
            } else if (type.equals("zain")) {
                RadBuZain.setChecked(true);
            }


        } catch (Exception ex){
            Toast.makeText(context, getResources().getString(R.string.noneNetwork), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
                saveData();
            }
            else {
                loadData();
            }
        }
    }


}
