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

public class LayoutTalkMe extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    protected EditText EdTNumPhone;
    protected Button BuTalkMe;
    private String  numID, type,number;
    private String numPhone;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE_TalkMe = 1;


    public LayoutTalkMe() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LayoutTalkMe newInstance() {
        LayoutTalkMe fragment = new LayoutTalkMe();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_talk_me, container, false);
        EdTNumPhone = (EditText) rootView.findViewById(R.id.EdTNumPhone);
        BuTalkMe = (Button) rootView.findViewById(R.id.BuTalkMe);
        final Context context = getActivity();

        BuTalkMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
                numPhone = EdTNumPhone.getText().toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE_TalkMe);
                        }

                        return;
                    }
                }
                number = setVariablesTalkMe(context);
                if (checkTalkMe(context)) {
                    startActivity(call(number));
                }
            }


        });
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
    protected Intent call(String number) {
        Intent i = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", number, null));
        return i;
    }
    public String setVariablesTalkMe(Context context) {
        int typeNumberTalkMe;

        try {

            if (type.equals("stc")) {
                typeNumberTalkMe = 177;
                return "*" + typeNumberTalkMe + "*" + numPhone + "#";
            } else if (type.equals("mobily")) {
                typeNumberTalkMe = 199;
                return "*" + typeNumberTalkMe + "*" + numPhone  + "#";
            } else if (type.equals("zain")) {
                typeNumberTalkMe = 123;
                return "*" + typeNumberTalkMe + "*" + numPhone  + "#";
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
            case MY_PERMISSIONS_REQUEST_CALL_PHONE_TalkMe: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadData();
                    numPhone = EdTNumPhone.getText().toString();
                    number = setVariablesTalkMe(context);
                    if (checkTalkMe(context)) {
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

    public boolean checkTalkMe(Context context) {
        try {


            if (!type.equals("none")) {
                return true;
            }
            else {
                Toast.makeText(context, getResources().getString(R.string.noneNetwork), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(context, getResources().getString(R.string.noneNetwork), Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }




}

