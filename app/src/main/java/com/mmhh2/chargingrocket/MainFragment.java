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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


/**
 * Created by AbuAli on 8/4/2016.
 */

public class MainFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    protected EditText EdTNumCard, EdTNumID;
    protected RadioButton RadBuSTC, RadBuMobily, RadBuZain;
    protected Button BuRecharge;
    private String numCard, numID, type, number;
    private static int typeNumberCharge;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE_Charge = 0;


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
        final Context context = getActivity();

        BuRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE_Charge);
                        }

                        return;
                    }
                }
                number = setVariables(context);
                if (checkCharge(context)) {
                    startActivity(call(number));
                }
            }


        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }
    public void onPause() {
        super.onPause();
        saveData();
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

    protected void saveData() {
        numCard = EdTNumCard.getText().toString();
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

    protected Intent call(String number) {
        Intent i = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", number, null));
        return i;
    }

    public boolean checkCharge(Context context) {
        try {


            if (numID.equals(null)) {
                Toast.makeText(context, getResources().getString(R.string.noneNetwork), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (numID.length() == 10) {
                    if (!type.equals("none")) {
                        if (!numCard.equals(null)) {
                            if (numCard.matches("[0-9]+")) {
                                if (numCard.length() >= 14) {
                                    return true;

                                } else {
                                    Toast.makeText(context,getResources().getString(R.string.CardNumShort), Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            } else {
                                Toast.makeText(context, getResources().getString(R.string.noneCardNum), Toast.LENGTH_SHORT).show();
                                return false;
                            }

                        } else {

                            Toast.makeText(context, getResources().getString(R.string.noneCardNum), Toast.LENGTH_SHORT).show();
                            return false;
                        }

                    }

                } else {
                    Toast.makeText(context, getResources().getString(R.string.IDNumIncorrect), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return false;
        } catch (Exception ex) {
            Toast.makeText(context, getResources().getString(R.string.noneNetwork), Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public String setVariables(Context context) {


        try {

            if (type.equals("stc")) {
                typeNumberCharge = 155;
                return "*" + typeNumberCharge + "*" + numCard + "*" + numID + "#";
            } else if (type.equals("mobily")) {
                typeNumberCharge = 1400;
                return "*" + typeNumberCharge + "*" + numCard + "*" + numID + "#";
            } else if (type.equals("zain")) {
                typeNumberCharge = 141;
                return "*" + typeNumberCharge + "*" + numCard + "*" + numID + "#";
            } else {
                return "none";
            }

        } catch (Exception ex) {
            Toast.makeText(context, getResources().getString(R.string.noneNetwork), Toast.LENGTH_SHORT).show();

        }
        return "none";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Context context = getActivity();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE_Charge: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    number = setVariables(context);
                    if (checkCharge(context)) {
                        startActivity(call(number));
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }


            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

