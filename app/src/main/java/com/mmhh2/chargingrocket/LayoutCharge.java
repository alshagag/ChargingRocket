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

public class LayoutCharge extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    protected EditText EdTNumCard;
    protected Button BuRecharge;
    private String numCard, numID, type, number;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE_Charge = 0;

    public LayoutCharge() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LayoutCharge newInstance() {
        LayoutCharge fragment = new LayoutCharge();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_charge, container, false);
        EdTNumCard = (EditText) rootView.findViewById(R.id.EdTNumCard);
        BuRecharge = (Button) rootView.findViewById(R.id.BuRecharge);
        final Context context = getActivity();

        BuRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
                numCard = EdTNumCard.getText().toString();
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


    protected void loadData() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        type = sharedPref.getString("type", null);
        numID = sharedPref.getString("numID", null);


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
        int typeNumberCharge;

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
                    loadData();
                    numCard = EdTNumCard.getText().toString();
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
}




