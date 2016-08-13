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
import android.support.annotation.Nullable;
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
import android.widget.RelativeLayout;
import android.widget.Toast;


/**
 * Created by AbuAli on 8/4/2016.
 */

public class MainFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private String  numID, type ,number;
    private int  typeNumberInquire;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE_Balance = 1;
    protected  RadioGroup RadGroServices;
    protected RadioButton RadBuCharge,RadBuConver,RadBuTalkMe;
    protected LinearLayout LiL;

    protected Button BuBalance;



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

        BuBalance = (Button)rootView.findViewById(R.id.BuBalance);
        RadGroServices = (RadioGroup)rootView.findViewById(R.id.RadGroServices);
        RadBuConver = (RadioButton)rootView.findViewById(R.id.RadBuConver);
        RadBuTalkMe = (RadioButton)rootView.findViewById(R.id.RadBuTalkme);
        RadBuCharge = (RadioButton)rootView.findViewById(R.id.RadBuCharge);
        LiL = (LinearLayout)rootView.findViewById(R.id.LiL);
        final Context context = getActivity();

        RadGroServices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                loadData();
                if (i == RadBuConver.getId()){
                    if (type.equals("stc") || type.equals("mobily") || type.equals("none") || (type == null)  ) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(LiL.getId(), new LayoutConverSTCAndMobily(), "Conver");
                        transaction.commit();
                    } else {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(LiL.getId(), new LayoutConverZain(), "Conver");
                        transaction.commit();
                    }
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
        BuBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE_Balance);
                        }

                        return;
                    }
                }
                number = setVariablesBalance(context);
                if (checkBalance(context)) {
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
            RadBuCharge.setChecked(true);

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
    public boolean checkBalance(Context context) {
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
    public String setVariablesBalance( Context context) {
        try {

            switch (type) {
                case "stc":
                    typeNumberInquire = 166;
                    return "*" + typeNumberInquire + "#";


                case "mobily":
                    typeNumberInquire = 1411;
                    return "*" + typeNumberInquire + "#";

                case "zain":
                    typeNumberInquire = 142;
                    return  "*" + typeNumberInquire + "#";

                default:
                    break;
            }
        } catch (Exception ex) {
            Toast.makeText(context, getResources().getString(R.string.noneNetwork), Toast.LENGTH_SHORT).show();

        }
        return "none";
    }
    protected Intent call(String number) {
        Intent i = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", number, null));
        return i;
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Context context = getActivity();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE_Balance: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadData();
                    number = setVariablesBalance(context);
                    if (checkBalance(context)) {
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.LiL,new LayoutCharge(),"Charge");
        transaction.commit();
    }

}

