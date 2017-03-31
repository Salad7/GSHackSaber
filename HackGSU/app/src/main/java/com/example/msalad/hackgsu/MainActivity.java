package com.example.msalad.hackgsu;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    ViewPager viewPager;
    ActionBar actionBar ;
    ActionBar.TabListener tabListener;
    TextView tabTitle;
    Button leftNav;
    Button rightNav;
    public int tabNum = 0;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    Button sendBtn;
    String phoneNo;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomViewPager customViewPager = new CustomViewPager(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(customViewPager);
        tabTitle = (TextView) findViewById(R.id.currentTab);
        leftNav = (Button) findViewById(R.id.navLeft);
        rightNav = (Button) findViewById(R.id.navRight);

        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tabNum > 0) {
                    viewPager.setCurrentItem(tabNum-1);
                    tabNum-=1;
                    updateTitle(false);
                    leftNav.setVisibility(View.VISIBLE);
                    rightNav.setVisibility(View.VISIBLE);
                }
                else{
                    leftNav.setVisibility(View.GONE);
                   // viewPager.setCurrentItem(tabNum-1);
                   // tabNum-=1;
                    updateTitle(false);
                }
            }
        });

        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tabNum < 3) {
                    viewPager.setCurrentItem(tabNum+1);
                    tabNum+=1;
                    updateTitle(false);
                    rightNav.setVisibility(View.VISIBLE);
                    leftNav.setVisibility(View.VISIBLE);

                }
                else
                {
                    rightNav.setVisibility(View.GONE);
                   // viewPager.setCurrentItem(tabNum+1);
                    //tabNum+=1;
                    updateTitle(false);
                }
            }
        });
//        actionBar = getSupportActionBar();
//        // Specify that tabs should be displayed in the action bar.
//        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//        // Create a tab listener that is called when the user changes tabs.
//        tabListener = new ActionBar.TabListener() {
//            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // show the given tabActionBar.TabListener tabListener = new ActionBar.TabListener() {
//                // When the tab is selected, switch to the
//                // corresponding page in the ViewPager.
//                viewPager.setCurrentItem(tab.getPosition());
//
//            }
//
//            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // hide the given tab
//            }
//
//            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//                // probably ignore this event
//            }
//        };
//
//        // Add 3 tabs, specifying the tab's text and TabListener
//        for (int i = 0; i < 3; i++) {
//            if(i == 0){
//                actionBar.addTab(
//                        actionBar.newTab()
//                                .setIcon(R.drawable.cake)
//                                .setTabListener(tabListener));
//            }
//            else if (i==1){
//                actionBar.addTab(
//                        actionBar.newTab()
//                                .setIcon(R.drawable.coffee)
//                                .setTabListener(tabListener));
//            }
//            else if (i==2){
//                actionBar.addTab(
//                        actionBar.newTab()
//                                .setIcon(R.drawable.coffeecup)
//                                .setTabListener(tabListener));
//            }
//            else if (i==3){
//                actionBar.addTab(
//                        actionBar.newTab()
//                                .setIcon(R.drawable.df)
//                                .setTabListener(tabListener));
//            }
    }
        public void updateTitle(boolean notArrowClick){

            if(notArrowClick){

            }
        if(tabNum == 0){
            //Change title to quick park
            tabTitle.setText("Quick Park");
        }
            else if(tabNum == 1){
            tabTitle.setText("Ventureprise");
        }   else if(tabNum == 2){
            tabTitle.setText("Window Shopping");
        }
            else if(tabNum == 3){
            tabTitle.setText("Redeem!!");
        }




        }


    protected void sendSMSMessage() {
      //  phoneNo = //txtphoneNo.getText().toString();
        message = "Hi Dude";

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("+7045799523", null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faield, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }



    public class CustomViewPager extends FragmentStatePagerAdapter{

        public CustomViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            if (position == 0){
                f = new MapFragment();

            }
            else if(position == 1){
                f = new TimerFragment();
            }
           else if(position == 2) {
                 f = new ListOfRedeemableItemsFragment();
            }
            else if(position == 3){
                 f = new SingleItemFragment();
            }

            return f;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
