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

public class LayoutConverZain extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    protected EditText EdtNumPhon,EdTBalance;
    protected Button BuConver;
    private String numID, type, number,num_amount;
    private String numPhone;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE_Conver = 3;


    public LayoutConverZain() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LayoutConverZain newInstance() {
        LayoutConverZain fragment = new LayoutConverZain();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_conver_zain, container, false);
        EdtNumPhon = (EditText)rootView.findViewById(R.id.EdtNumPhon);
        EdTBalance = (EditText)rootView.findViewById(R.id.EdTBalance);
        BuConver = (Button)rootView.findViewById(R.id.BuConver);
        final Context context = getActivity();
        BuConver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numPhone = EdtNumPhon.getText().toString();
                num_amount = EdTBalance.getText().toString();
                loadData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_CALL_PHONE_Conver);
                        }

                        return;
                    }
                }

                number = setVariablesConver(context);
                if (checkConver(context)) {
                    send(number);
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
        smsManager.sendTextMessage(String.valueOf(702702),null,number,null,null);
    }

    public String setVariablesConver(Context context) {

        try {

             if (type.equals("zain")) {
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
                    num_amount = EdTBalance.getText().toString();
                    loadData();
                    number = setVariablesConver(context);
                    if (checkConver(context)) {
                        send(number);
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
        if (!type.equals("none")) {
            if (!numPhone.isEmpty()){
                if (numPhone.length() == 10 ){
                    if (!(num_amount == null)){
                        if (num_amount.matches("[0-9]+")) {
                            if (Integer.parseInt(num_amount) >= 10 && Integer.parseInt(num_amount) <= 50 ){
                                return true;
                            }else {
                                Toast.makeText(context, "أفل مبلغ للتحويل 10 واعلى مبلغ 50", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(context, "تأكد من كتابة ارقام وليس احرف", Toast.LENGTH_SHORT).show();
                        }

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


        return false;


    }





}

