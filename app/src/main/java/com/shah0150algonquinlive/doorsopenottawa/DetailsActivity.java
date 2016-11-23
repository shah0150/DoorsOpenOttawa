package com.shah0150algonquinlive.doorsopenottawa;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shah0150algonquinlive.doorsopenottawa.BuildingAdapter;
import com.shah0150algonquinlive.doorsopenottawa.parsers.BuildingJSONParser;
public class DetailsActivity extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private Geocoder mGeocoder;
    private TextView mTitle,mDescription,mDate,mAddress;
    private ImageView mImage;
    private Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        mGeocoder = new Geocoder( this );
        b=getIntent().getExtras();
        mTitle=(TextView)findViewById(R.id.buildingName);
        mDescription=(TextView)findViewById(R.id.buildingDes);
        mDate=(TextView)findViewById(R.id.buildingDate);
        mAddress=(TextView)findViewById(R.id.buildingAddress);
        mImage=(ImageView)findViewById(R.id.image);
        displayData();
    }

    private void displayData() {
        mTitle.setText(b.getString("title"));
        mAddress.setText(b.getString("address"));
        mDescription.setText(b.getString("description"));
        

    }


    private void pin( String locationName ) {
        try {
            Address address = mGeocoder.getFromLocationName(locationName, 1).get(0);
            LatLng ll = new LatLng( address.getLatitude(), address.getLongitude() );
            mMap.addMarker( new MarkerOptions().position(ll).title(locationName) );
            mMap.moveCamera( CameraUpdateFactory.newLatLng(ll) );
            Toast.makeText(this, "Pinned: " + locationName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Not found: " + locationName, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
