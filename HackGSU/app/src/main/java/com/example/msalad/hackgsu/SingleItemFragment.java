package com.example.msalad.hackgsu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by msalad on 3/30/2017.
 */

public class SingleItemFragment extends Fragment {
    public TextView vendorTV;
    public TextView pointsTxtSingle;
    public Button illbeThereBtn;
    public ImageView pizza;
    public ImageView taco;
    public TextView whatsNew;
    public View v;
    public MainActivity activity;
    public ImageView confetti;
    public TextView scoreTV;
    public ListView items;
    public List<String> test;
    SharedPreferences sp;
    CustomArrayAdapter customArrayAdapter;
    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.single_item_fragment, container, false);
        vendorTV = (TextView) v.findViewById(R.id.vendor_click);
        pointsTxtSingle = (TextView) v.findViewById(R.id.pointsTxtSingle);
        illbeThereBtn = (Button) v.findViewById(R.id.illBeThereBtn);
        whatsNew = (TextView) v.findViewById(R.id.whatsitabout);
        items = (ListView) v.findViewById(R.id.storeItemsList);
        test = new ArrayList();
        test.add("30% Macchiato!");
        test.add("5$  Starbucks Giftcard");
        test.add("30% Caramel Latte");
        test.add("30% Peach Float");
        test.add("Free Lemon cupcake");
        test.add("Free Coffee");
        new runASync().execute();

//        CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(getActivity().getApplicationContext(), R.layout.custom_store_redeemable_item,test);
//        customArrayAdapter.setScoreTxr(pointsTxtSingle);
//        items.setAdapter(customArrayAdapter);


        illbeThereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Thanks for showing support!",Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }

   public void onReturnFromAsync(){
       pointsTxtSingle.setText(sp.getInt("points",0)+" Points");
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }



    public class CustomArrayAdapter extends ArrayAdapter<String>{
        Context context;
        ImageView imageLogo;
        ImageView imagePoints;
        Button details;
        List<String> obj;
        TextView scoreTxr;

        public CustomArrayAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.context = context;
            obj = objects;
        }
        public void setScoreTxr(TextView t){
            scoreTxr = t;
        }
        public void updateScoreTxt(){
            pointsTxtSingle.setText(sp.getInt("points",0)+" Points");
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_store_redeemable_item,parent,false);
            details = (Button) convertView.findViewById(R.id.details);
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor s = getActivity().getPreferences(MODE_PRIVATE).edit();
                int p = sp.getInt("points",0);
                    Log.d("ppoints",p+"");
                    s.putInt("points",p-100).commit();
                    updateScoreTxt();
                   activity.sendSMSMessage();
                }
            });

             if(position == 1){
                details.setCompoundDrawablesWithIntrinsicBounds( R.drawable.gift, 0, R.drawable.s2, 0);

            }
            else if(position == 4){
                //cake.
                details.setCompoundDrawablesWithIntrinsicBounds( R.drawable.cake, 0, R.drawable.s2, 0);

            }
            else if (position == 5){
                //coffee
                 details.setCompoundDrawablesWithIntrinsicBounds( R.drawable.coffee, 0, R.drawable.s2, 0);

            }
            else {
                    details.setCompoundDrawablesWithIntrinsicBounds( R.drawable.coffeecup, 0, R.drawable.s2, 0);

            }
            details.setText(obj.get(position));
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }

    public class runASync extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            sp = ((MainActivity) activity).getPreferences(MODE_PRIVATE);
            customArrayAdapter = new CustomArrayAdapter(getActivity().getApplicationContext(), R.layout.custom_store_redeemable_item,test);
            customArrayAdapter.setScoreTxr(pointsTxtSingle);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            items.setAdapter(customArrayAdapter);
            onReturnFromAsync();
        }
    }
}
