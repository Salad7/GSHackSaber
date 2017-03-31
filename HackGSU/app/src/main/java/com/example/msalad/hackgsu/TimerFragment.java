package com.example.msalad.hackgsu;


import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bcgdv.asia.lib.ticktock.TickTockView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.DEVICE_POLICY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import android.app.admin.DeviceAdminReceiver;
/**
 * Created by msalad on 3/30/2017.
 */

public class TimerFragment extends Fragment {
    TextView pointsTxt;
    TickTockView mCountUp;
    public TextView thanks;
    Chronometer chronometer;
    Button begin;
    LinearLayout redeemScreen;
    Button redeem;
    int points;
    Context ctx;
    SharedPreferences.Editor s;
    SharedPreferences sp;
    private DevicePolicyManager mgr=null;
    private ComponentName cn=null;
    Button lock;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timer_fragment,container,false);
    mCountUp = (TickTockView) v.findViewById(R.id.view_ticktock_countup);
    chronometer = (Chronometer) v.findViewById(R.id.chronometer2);
        begin = (Button) v.findViewById(R.id.begin);
        redeemScreen = (LinearLayout) v.findViewById(R.id.redeemScreent);
       s = ((MainActivity) ctx).getPreferences(MODE_PRIVATE).edit();
        sp = ((MainActivity) ctx).getPreferences(MODE_PRIVATE);
        redeem = (Button) v.findViewById(R.id.redeemBtn);
        thanks = (TextView) v.findViewById(R.id.thanks);
        pointsTxt = (TextView) v.findViewById(R.id.redeemTxt);
        cn=new ComponentName(getActivity(), MyAdminReceiver.class);
        mgr=(DevicePolicyManager)getActivity().getSystemService(DEVICE_POLICY_SERVICE);
        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Switch to next tab
                ((MainActivity)ctx).viewPager.setCurrentItem(2);

            }
        });

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chronometer.start();
                Toast.makeText(getActivity().getApplicationContext(),"Turn off your phone in (5s) and drive safe!",Toast.LENGTH_LONG).show();
                lockMeNow(new View(getContext()));
           }
        });
        redeemScreen.setVisibility(View.GONE);
        return v;
}

    @Override
    public void onStart() {
        super.onStart();
       // mCountUp.start();
        redeemScreen.setVisibility(View.GONE);


    }

    public void lockMeNow(View v) {
        if (mgr.isAdminActive(cn)) {
            mgr.lockNow();
            Log.d("d","Admin is active");
        }
        else {
            Intent intent=
                    new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    getString(R.string.locked));
            startActivity(intent);
            Log.d("younis","younis");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        chronometer.start();

    }

    @Override
    public void onResume() {
        super.onResume();
        redeemScreen.setVisibility(View.VISIBLE);
        chronometer.stop();
        String time = chronometer.getText().toString();
        if(time.equals("00:00")){
            thanks.setText( "Ready to drive safely!? WOOO");
        } else{
            points+= (10*(chronometer.getText().charAt(3)+chronometer.getText().charAt(3)));
            Log.d("points",points+"");
            s.putInt("points",points).commit();
            thanks.setText("Thank's For Driving Safetly for "+time+" Seconds");
        }
        pointsTxt.setText("You've redeemed "+sp.getInt("points",1)+" Points!");


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }
}
