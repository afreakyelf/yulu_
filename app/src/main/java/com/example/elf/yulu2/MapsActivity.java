package com.example.elf.yulu2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.elf.yulu2.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,MyListener {

    MyListener ml;
    Toolbar toolbar;
    static public GoogleMap mMap;
    ArrayList<HashMap<String, Object>> al_details;
    String data;
    Adapterforlocations adapter;
    MapsActivity maps;
    LatLng latLng;



    Marker m1 = null, m2 = null;
    Polyline p1 = null;

    GoogleApiClient client;
    RecyclerView recyclerView;
    LocationRequest locationRequest = null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        mapFragment.getMapAsync(this);
        loadJSON();
        listener();
    }

    public void myLocation(View v) {
        {
            client = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            client.connect();
        }
    }

/*
    public void mapLoad(final View view) {
        //***************************** Setting the marker on the basis of edit text

        EditText et = (EditText) findViewById(R.id.et);
        data = et.getText().toString();

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> list = geocoder.getFromLocationName(data, 1);
            final Address address = list.get(0);
            double lat = address.getLatitude();
            double lng = address.getLongitude();
            latLng = new LatLng(lat, lng);

            CameraUpdate cf = CameraUpdateFactory.newLatLngZoom(latLng, 12);
            MarkerOptions markerOptions = new MarkerOptions().title("Marker in Sydney")
                    .position(latLng)
                    .title(data)
                    .snippet(address.getAddressLine(0) + " " + address.getAddressLine(1) + " " + address.getAddressLine(2) + " " + address.getAddressLine(3) + " " + address.getAddressLine(4))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .draggable(true)
                    .flat(true)
                    .alpha(0.5f);


            //***************************** Banner for full information
  */
/*           mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {

                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    LatLng l = marker.getPosition();
                    double lat = l.latitude;
                    double lng = l.longitude;

                    txt3.setText("Lat = " + lat);
                    txt4.setText("Lng = " + lng);
                    return v;
                }
            });*//*


            //  mMap.addCircle(c);
            mMap.moveCamera(cf);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
*/


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng l = new LatLng(12.929327618, 77.684214897);
        CameraUpdate cf = CameraUpdateFactory.newLatLngZoom(l, 10);
        mMap.moveCamera(cf);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                LatLng mylatlng = new LatLng(12.929327618,77.684214897);
                /* zoom level 1 = subcontinent,  5 = country, 10 = cities, 15= Locality/town, 20= street*/
                CameraUpdate c = CameraUpdateFactory.newLatLngZoom(mylatlng,17);
                mMap.addMarker(new MarkerOptions().title("You are here").position(mylatlng).title("bro").icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder))
                        .draggable(true)
                        .flat(true)
                        .alpha(0.5f));
                mMap.addCircle(new CircleOptions().radius(100).center(mylatlng).fillColor(Color.BLUE).visible(true));
                mMap.animateCamera(c);



            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void listener() {
        setOnEventListener(this);
    }

    public void setOnEventListener(MyListener ml) {
        this.ml = ml;
    }

    public void loadJSON(){

        String json_string = null;

        try {
            InputStream inputStream = getAssets().open("andorid_task_json.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            ArrayList<HashMap<String,String>> data = new ArrayList<>();
            HashMap<String, Object> dataMap = null;


            json_string = new String(buffer,"UTF-8");

            JSONObject jsonObject = new JSONObject(json_string);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            dataMap = new HashMap<String, Object>();

            for(int i =0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                HashMap<String,String> samachar = new HashMap<>();

                String title = jsonObject1.getString("title");
                String image = jsonObject1.getString("image");
                Double lat = jsonObject1.getDouble("latitude");
                Double longt = jsonObject1.getDouble("longitude");

                samachar.put("title",title);
                samachar.put("image",image);
                samachar.put("latitude", String.valueOf(lat));
                samachar.put("longitude", String.valueOf(longt));

                data.add(samachar);

            }

            dataMap.put("result",data);

            al_details = (ArrayList<HashMap<String, Object>>) dataMap.get("result");
            if (al_details.size()!=0) {
                adapter = new Adapterforlocations(al_details, maps, getApplicationContext());
                recyclerView.setAdapter(adapter);

            }




        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void callback(HashMap<String, Object> tmpMap, String dataType, int mode) {
        if(mode == 0){
            al_details = (ArrayList<HashMap<String, Object>>) tmpMap.get("result");
            if (al_details.size()!=0){
                adapter = new Adapterforlocations(al_details,maps,getApplicationContext());
                recyclerView.setAdapter(adapter);

            }
        }
    }
}