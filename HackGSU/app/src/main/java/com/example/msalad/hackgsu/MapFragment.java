package com.example.msalad.hackgsu;

/**
 * Created by msalad on 3/31/2017.
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //private String[] data = {"Woodward"};
    private ArrayList<Deck> decks = new ArrayList<>();
    //private lecho.lib.hellocharts.view.LineChartView test;
    private DatabaseReference mDatabase;
    private DatabaseReference mConditionRef;
    private ArrayList<Deck> decks2 = new ArrayList<>();
    private ArrayList<String> addressOfDecks;
    private ArrayList<Marker> myMarker;
    private int count = 1;
    Button road;

    private GoogleMap mMap;
    MainActivity ctx;
    Context thiscontext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment,container,false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myMarker = new ArrayList<>();
        populateMinFive();
        mDatabase = FirebaseDatabase.getInstance().getReference();
thiscontext = container.getContext();
        populateFB();
road = (Button) v.findViewById(R.id.jack);

        //RelativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        //ViewGroup.LayoutParams layoutParams = RelativeLayout.getLayoutParams();
        // layoutParams.height = 2500;
        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send to activity to send to tab 1;
                ctx.viewPager.setCurrentItem(1);
            }
        });



        // use a Relative layout manager
        mLayoutManager = new LinearLayoutManager(ctx.getApplicationContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(decks,ctx.getApplicationContext(), mDatabase);
        mAdapter.setMapF(this);
        mRecyclerView.setAdapter(mAdapter);

        SpacesItemDecoration dividerItemDecoration = new SpacesItemDecoration(30);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
////

        mConditionRef = mDatabase.child("users");
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int position = 0;//Integer.parseInt(dataSnapshot.getKey());

                for (int i = 0; i < decks.size(); i++) {
                    mAdapter.updateList(i, dataSnapshot.child(i + "").child("availability").getValue(Integer.class));

                    //Modify the decks arraylist to match the database


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for(int i = 0; i < decks.size(); i++) {
                    if (marker.getTitle().equals(decks.get(i).getName())) {
                        mLayoutManager.smoothScrollToPosition(mRecyclerView, new RecyclerView.State(), i);
                    }
                }
                return false;
            }
        });
        mMap = googleMap;


        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        /**
         * Code for starting android at my location
         */
        // Add a marker in Sydney and move the camera
        LatLng home = new LatLng(35.305373, -80.730964);
        // Getting LocationManager object from System Service LOCATION_SERVICE
        //LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        //// Getting the name of the best provider
        //String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
      //  Location location = locationManager.getLastKnownLocation(provider);

       // if (location != null) {
            // Getting latitude of the current location
            double latitude = 33.752019f;//location.getLatitude();

            // Getting longitude of the current location
            double longitude = -84.387551f;//location.getLongitude();

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            //home = new LatLng(decks.get(6).getLat(),decks.get(6).getLon());//new LatLng(latitude, longitude);

            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Me"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.setMinZoomPreference(16.0f);
            mMap.setMaxZoomPreference(20.0f);

            addMarkers(googleMap);
       // }



    }
    public void addMarkers(GoogleMap googleMap){


        ArrayList<LatLng> latLngList = new ArrayList<>();
        for(int i = 0; i < decks.size(); i++){
            latLngList.add(new LatLng(decks.get(i).getLat(),decks.get(i).getLon()));
            myMarker.add(googleMap.addMarker(new MarkerOptions().position(latLngList.get(i)).title(decks.get(i).getName())));
        };

    }
    public void moveCameraToPos(LatLng latLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public void populateMinFive(){
        //for(int i = 0; i < 22; i++) {
        decks.add(new Deck(0, "G", 577, "Communter & F/S", 33.752019f, -84.387551f)); //
        decks.add(new Deck(1, "K", 559, "Resident, Communter & F/S", 33.751135f, -84.384109f));//
        decks.add(new Deck(2, "M", 246, "Communter & F/S", 33.753231f, -84.384057f)); //
        decks.add(new Deck(3, "N", 32, ".50 per 15 minutes", 33.751272f, -84.384513f));//
        //Missing LOt 4A 35.306879f,-80.725144f
        decks.add(new Deck(10, "Piedmont North", 123, "Commuter & F/S", 33.759082f,-84.381569f));//
        decks.add(new Deck(4, "S Deck", 534, "Communter & F/S", 33.751549f, -84.383270f)); //
        decks.add(new Deck(5, "T Deck", 577, "1$ per half hour", 33.755049f, -84.386732f)); //
        //Missing East 3 35.305978f, -80.726063f
        //decks.add(new Deck(11, "Georgia State Stadium", 559, "Resident, Communter & F/S", 35.305978f, -80.726063f));//
        //Missing LOt 6A 35.308697f, -80.724458f
        decks.add(new Deck(12, "University Commons", 798, "Commuter & F/S", 33.757418f, -84.382012f));//
        //Lot 26 35.313470f,-80.731967f
        decks.add(new Deck(13, "University Loft", 559, "Resident, Communter & F/S", 35.313470f,-80.731967f));//
        decks.add(new Deck(6, "U Lot", 1166, "Resident, Communter & F/S", 35.313469f, -80.731494f)); //
        decks.add(new Deck(7, "Lot 19", 256, "Communter & F/S", 35.308374f, -80.735112f)); //
        decks.add(new Deck(8, "West Deck", 762, "Communter & F/S", 35.305470f, -80.736605f)); //
        decks.add(new Deck(9, "Lot 27", 128, "Resident, Commuter & F/S", 35.301130f, -80.735521f));//
        //Missing lot 7 35.304601f, -80.735890f
        decks.add(new Deck(14, "Lot 7", 125, "Commuter & F/S", 35.304601f, -80.735890f));////
        //Missing COne deck 1  35.304472f, -80.734519f
        decks.add(new Deck(15, "Cone Deck 1", 557, "F/S", 35.304472f, -80.734519f));//
        //Missing union deck 35.309167f, -80.735192f
        decks.add(new Deck(16, "Union Deck", 1012, "Resident, Communter & F/S", 35.309167f, -80.735192f));//
        //MIssing LOt 18 35.309229f, -80.736509f //
        decks.add(new Deck(17, "Lot 18", 94, "Commuter & F/S", 35.309229f, -80.736509f ));//
        //Missing Lot 7A 35.304270f, -80.735843f
        decks.add(new Deck(18, "Lot 7A", 42, "Commuter & F/S", 35.304270f, -80.735843f));//
        //Missing LOt 14 35.306787f ,-80.737921f
        decks.add(new Deck(19, "Lot 14", 34, "Commuter & F/S", 35.306787f ,-80.737921f));//
        //Missing Lot 8 35.301130f, -80.735521f
        decks.add(new Deck(20, "Lot 8", 247, "Resident & F/S", 35.301130f, -80.735521f));//
        //Missing Lot 27 35.301130f, -80.735521
        decks.add(new Deck(21, "Lot 27", 185, "Resident, Communter & F/S",35.301130f, -80.735521f));//

        decks.add(new Deck(22, "Cone Deck 2", 254, "F/S", 35.304892f, -80.734387f));//
        decks.add(new Deck(23, "Lot 8A", 61, "Resident & F/S", 35.301128f, -80.734472f));////
        //}

    }

    //Already populated it once no need to do it again
    public void populateFB(){
        for(int i = 0; i < decks.size(); i++) {
            //Send to firebase to update data
            mDatabase.child("users").child(Integer.toString(decks.get(i).getId())).setValue(decks.get(i));
            //decks.get(i) = mDatabase.child("users").child(Integer.toString(decks.get(i).getId()));

        }

    }

//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        //Log.d("TAG","OnMarkerCLick works");
//        //
//        //for (int i = 0; i < decks.size(); i++) {
//        //Log.d("TAG",marker.getTitle());
//        //Log.d("TAG",decks.get(15).getName());
//        /**
//         if (marker.getTitle().equals(myMarker.get(i).getTitle())) {
//         //handle click here
//         Log.d("TAG","OnMarkerCLick works"); //works
//         //Log.d("TAG",marker.getTitle());
//         for (int x = 0; x < decks.size(); x++) {
//         if (marker.getTitle().equals(decks.get(x).getName())) {
//         //
//         mLayoutManager.smoothScrollToPosition(mRecyclerView, new RecyclerView.State(), x);
//         }
//         }
//         return false;
//         }
//         return false;
//         }
//         **/
//        for(int i = 0; i < decks.size(); i++) {
//            if (marker.getTitle().equals(decks.get(i).getName())) {
//                mLayoutManager.smoothScrollToPosition(mRecyclerView, new RecyclerView.State(), i);
//            }
//        }
//        return false;
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = (MainActivity) context;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissions.length == 1 &&
                permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ctx.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
        } else {
            // Permission was denied. Display an error message.
        }
    }



    /**
     * Created by mohamed on 12/20/16.
     */

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<Deck> mDataset;
        private Context ctx;
        private DatabaseReference mDatabase;
        private MapFragment mapF;

        public ArrayList<Deck> getDataset() {
            return mDataset;
        }

        private DatabaseReference mConditionRef;
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            // each data item is just a string in this case
            public TextView mTextView;
            public TextView mDeckName;
            public TextView mDeckIden;
            public TextView mDeckAvail;
            public TextView mDeckAvailIden;
            public TextView mDeckType;
            public TextView mDeckTypeIden;
            public RatingBar mRating;



            @Override
            public void onClick(View view) {

            }

            public ViewHolder(View v) {
                super(v);
                mDeckName = (TextView) v.findViewById(R.id.textDeckName);
                mDeckIden = (TextView) v.findViewById(R.id.textDeckIdentifier);

                mDeckType = (TextView) v.findViewById(R.id.textType);
                mDeckTypeIden = (TextView) v.findViewById(R.id.textTypeIden);

                mDeckAvail = (TextView) v.findViewById(R.id.textDeckNum);
                mDeckAvailIden = (TextView) v.findViewById(R.id.availIden);
                //mRating = (RatingBar) v.findViewById(R.id.ratingBar);
            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(final ArrayList<Deck> myDataset, Context ctx, DatabaseReference mDatabase){
            this.ctx = ctx;
            this.mDatabase = mDatabase;
            this.mDataset = myDataset;


        }

        public void setMapF( MapFragment m){
            mapF = m;
        }


        public void updateList(int index, int val){
            //mDataset.clear();

            mDataset.get(index).setAvailability(val);
            this.notifyDataSetChanged();
        }


        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_deck, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }



        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mDeckName.setText("Lot Name : "+mDataset.get(position).getName());
            holder.mDeckType.setText("Availability : "+mDataset.get(position).getLotType());
            holder.mDeckAvail.setText("Usage : "+mDataset.get(position).getAvailability()+"");
            holder.mDeckName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MapFragment) mapF).moveCameraToPos(mDataset.get(position).getLatLng());
                }
            });
            // holder.mRating.setNumStars(1);
            holder.mDeckAvailIden.setText("Spots Left :");
            holder.mDeckIden.setText("Parking Deck :");
            holder.mDeckTypeIden.setText("Deck Type :");

            //Gets key value
            //mDataset.get(position).setAvailablity(Integer.parseInt(mDatabase.child("users").child(Integer.toString(position)).getKey()));

            //mDataset.get(position).setAvailablity(Integer.parseInt(mDatabase.child("users").child(Integer.toString(position)).getKey()));
            //holder.mDeckAvail.setText(mDataset.get(position).getAvailablity()+"");
            //holder.mRating.setNumStars(mDataset.get(position).getName());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }


    }



    /**
     * Created by mohamed on 12/21/16.
     */

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if(parent.getChildAdapterPosition(view) == 0)
                outRect.top = space;
        }
    }


}
