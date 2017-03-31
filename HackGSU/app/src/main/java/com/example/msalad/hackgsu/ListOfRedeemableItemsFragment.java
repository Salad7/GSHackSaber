package com.example.msalad.hackgsu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by msalad on 3/30/2017.
 */

public class ListOfRedeemableItemsFragment extends Fragment {

    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.redeemable_items_fragment,container,false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        ArrayList<String> f = new ArrayList<String>();
        f.add("Starbucks");
        f.add("Subways");
        f.add("Chick Fil A");
        f.add("McDonalds");
        f.add("Insomina Cookies");
        f.add("Papa Johns");
        MyAdapterGrid mAdapter = new MyAdapterGrid(f,getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 2));

        return v;
    }


}
