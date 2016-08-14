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
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * Created by AbuAli on 8/4/2016.
 */

public class LayoutConverSTCAndMobily extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    protected EditText EdtNumPhon,EdTBalance;
    protected Button BuConver;
    protected Spinner SPBalance;
    private String numID, type, number,num_amount;
    private String numPhone;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE_Conver = 2;


    public LayoutConverSTCAndMobily() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LayoutConverSTCAndMobily newInstance() {
        LayoutConverSTCAndMobily fragment = new LayoutConverSTCAndMobily();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_conver_stc_and_mobily, container, false);
        EdtNumPhon = (EditText)rootView.findViewById(R.id.EdtNumPhon);
        SPBalance = (Spinner)rootView.findViewById(R.id.SPBalance);
        BuConver = (Button)rootView.findViewById(R.id.BuConver);
        final Context context = getActivity();
        BuConver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numPhone = EdtNumPhon.getText().toString();
                loadData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE_Conver);
                        }

                        return;
                    }
                }

                number = setVariablesConver(context);
                if (checkConver(context)) {
                    startActivity(call(number));
                }


            }
        });
        SPBalance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0 :
                        num_amount = null;
                        break;
                    case 1 :
                        num_amount = "5";
                        break;
                    case 2 :
                        num_amount = "10";
                        break;
                    case 3 :
                        num_amount = "15";
                        break;
                    case 4 :
                        num_amount = "20";
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                SPBalance.setSelection(0);

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
    protected void send(String number) {
        SmsManager smsManager = SmsManager.getDefault();
       // smsManager.sendTextMessage(numPhone,null,number,null,null);
    }

    public String setVariablesConver(Context context) {
        int typeNumberConver;

        try {

            if (type.equals("stc")) {
                typeNumberConver = 133;
                return "*" + typeNumberConver + "*" + numPhone + "*" + num_amount + "#";
            } else if (type.equals("mobily")) {
                typeNumberConver = 123;
                return "*" + typeNumberConver + "*" + numPhone + "*" + num_amount + "#";
            } else if (type.equals("zain")) {
                return "bt " + numPhone + " " + num_amount ;
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
            case MY_PERMISSIONS_REQUEST_CALL_PHONE_Conver: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    numPhone = EdtNumPhon.getText().toString();
                    loadData();
                    number = setVariablesConver(context);
                    if (checkConver(context)) {
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

    public boolean checkConver(Context context) {
        try {


            if (!type.equals("none")) {
                if (!numPhone.isEmpty()){
                    if (numPhone.length() == 10 ){
                        if (!(num_amount == null)){
                            return true;
                        } else {
                            Toast.makeText(context, "تأكد من اختيار المبلغ", Toast.LENGTH_SHORT).show();
                        }

                    }  else {
                        Toast.makeText(context, "رقم الجوال أقل من 10 ارقام", Toast.LENGTH_SHORT).show();
                    }
                }  else {
                    Toast.makeText(context, "تأكد من كتابة رقم الجوال", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, getResources().getString(R.string.noneNetwork), Toast.LENGTH_SHORT).show();
            }


        return false;} catch (Exception ex){
            Toast.makeText(context, getResources().getString(R.string.noneNetwork), Toast.LENGTH_SHORT).show();
            return false;
        }


    }





}

