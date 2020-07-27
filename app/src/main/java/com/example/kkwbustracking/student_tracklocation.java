package com.example.kkwbustracking;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class student_tracklocation extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    FirebaseDatabase database;      // used for store URLs of uploaded files
    String licence_no = "Atharva2204";
    int route = 0;
    double latitude, longitude;
    FloatingActionButton floatingActionButton;
    TextView name, phone, email, licence;
    String name1, phone1, email1, licence1;
    Button call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_tracklocation);

        database = FirebaseDatabase.getInstance();  // returns an object of firebase database

        //licence_no = getIntent().getStringExtra("licence_no");

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        final AlertDialog alertDialog = builder.create();

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.driver_details_dialog, null);

        alertDialog.setView(dialoglayout);

        // Button no = dialoglayout.findViewById(R.id.close);

        floatingActionButton = findViewById(R.id.driver_details);
        name = dialoglayout.findViewById(R.id.name);
        phone = dialoglayout.findViewById(R.id.phone_no);
        email = dialoglayout.findViewById(R.id.email);
        licence = dialoglayout.findViewById(R.id.licence_no);
        call = dialoglayout.findViewById(R.id.call);

        call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String phone = phone1.substring(phone1.indexOf(':')+1);

               // Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));

                //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));

               // Intent intent = new Intent(Intent.ACTION_CALL);
              //  intent.setData(Uri.parse("tel:"+phone.trim()));

                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+Uri.encode(phone.trim())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try
                {
                    startActivity(callIntent);
                }
                catch(android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(student_tracklocation.this, "Could not find an activity to place the call.", Toast.LENGTH_SHORT).show();
                }


               /* if (ActivityCompat.checkSelfPermission(student_tracklocation.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }*/
               // startActivity(intent);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Tracking Bus Location");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white,getTheme()));
        setSupportActionBar(toolbar);

        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue2, this.getTheme()));
       // getWindow().setNavigationBarColor(getResources().getColor(R.color.yellow,this.getTheme()));

        /*no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });*/

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(route + "");//reference inside Uploads

                databaseReference.addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        String latlong = dataSnapshot.getKey();

                        if (latlong.contentEquals("Name"))
                            name1 = dataSnapshot.getValue(String.class);
                        if (latlong.contentEquals("Phone"))
                            phone1 = dataSnapshot.getValue(String.class);
                        if (latlong.contentEquals("Email"))
                            email1 = dataSnapshot.getValue(String.class);
                        if (latlong.contentEquals("Licence_no"))
                            licence1 = dataSnapshot.getValue(String.class);

                        name.setText("Name :  "+name1);
                        phone.setText("Phone No.  : "+phone1);
                        email.setText("Email  : "+email1);
                        licence.setText("Licence No. :  "+licence1);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
                    {
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                alertDialog.show();
            }
        });

        route_dialog();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFrag != null;
        mapFrag.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.route)
            route_dialog();
        else if(id == R.id.share)
        {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        else
        {
            Intent intent = new Intent(student_tracklocation.this,about.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void route_dialog()
    {
        // setup the alert builder
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Choose a Route");

        builder1.setCancelable(false);

        // add a radio button list
        String[] routes = {"Jail Road (1)", "Pune Road (2)", "Cidco (3)","College Road (4)","Gangapur Road (5)"};

        int checkedItem = route-1;

        builder1.setSingleChoiceItems(routes, checkedItem, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // user checked an item
                switch(which)
                {
                    case 0: route = 1;  break;
                    case 1: route = 2;  break;
                    case 2: route = 3;  break;
                    case 3: route = 4;  break;
                    case 4: route = 5;  break;
                }
            }
        });
        // add OK and Cancel buttons
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // user clicked OK

                // year_textInputEditText.setText(year);

                onMapReady(mGoogleMap);
            }
        });
        builder1.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        final AlertDialog dialog1 = builder1.create();
        dialog1.show();
    }

    @Override
    public void onPause()
    {
        super.onPause();

       // stop location updates when Activity is no longer active
       if (mFusedLocationClient != null)
       {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
       }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //Location Permission already granted
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
        else
        {
            //Request Location Permission
            checkLocationPermission();
        }
    }

    LocationCallback mLocationCallback = new LocationCallback()
    {
        @Override
        public void onLocationResult(LocationResult locationResult)
        {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0)
            {
                //The last location in the list is the newest
                final Location location = locationList.get(locationList.size() - 1);
                Log.i("student_tracklocation", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null)
                {
                    mCurrLocationMarker.remove();
                }

               /* Polyline line1 = mGoogleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(19.9958193, 73.7518029), new LatLng(20.7, 74.7518029))
                        .width(5)
                        .color(0xFFFF0000)); //non transparent red*/



                //Place current location marker

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(route + "");//reference inside Uploads

                databaseReference.addChildEventListener(new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                    {
                        String latlong = dataSnapshot.getKey();

                        if (latlong.contentEquals("Latitude"))
                            latitude = dataSnapshot.getValue(Double.class);
                        if (latlong.contentEquals("Longitude"))
                            longitude = dataSnapshot.getValue(Double.class);

                        LatLng latLng = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Current Position");
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                        // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_marker));

                        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                        //move map camera
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
                    {
                        String latlong = snapshot.getKey();

                        if (latlong.contentEquals("Latitude"))
                            latitude = snapshot.getValue(Double.class);
                        if (latlong.contentEquals("Longitude"))
                            longitude = snapshot.getValue(Double.class);


                        Polyline line2 = mGoogleMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(20.013173, 73.820034), new LatLng(latitude, longitude))
                                .width(10)
                                .color(0x7F0000FF)); //semi-transparent blue


                        LatLng latLng = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Current Position");
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                        // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.))

                        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                        //move map camera
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // Should we show an explanation?
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(student_tracklocation.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            }
            else
            {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION)
        {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // permission was granted, yay! Do the location-related task you need to do.
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                 //   mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mGoogleMap.setMyLocationEnabled(true);
                }

            }
            else
             {
                // permission denied, boo! Disable the functionality that depends on this permission.
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            }

            // other 'case' lines to check for other permissions this app might request
        }
    }

   /* @Override
    public boolean onMarkerClick(Marker marker)
    {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(student_tracklocation.this)
                .waypoints(new LatLng(latitude, longitude), marker.getPosition())
                .key("AIzaSyDGvkmJ6osdJz7v8OSg7U")
                .build();
        routing.execute();
        return false;
    }*/

    @Override
    public void onRoutingFailure(RouteException e) {
        Log.e("check", e.getMessage());
    }

    @Override
    public void onRoutingStart() {
        Log.e("check", "onRoutingStart");
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex)
    {

        Log.e("check", "onRoutingSuccess");
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        List<Polyline> polylines = new ArrayList<>();

        mGoogleMap.moveCamera(center);

        if (polylines.size() > 0)
        {
            for (Polyline poly : polylines)
            {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++)
        {
            //In case of more than 5 alternative routes

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.blue,getTheme()));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mGoogleMap.addPolyline(polyOptions);
            polylines.add(polyline);
        }
    }

    @Override
    public void onRoutingCancelled() {
        Log.e("check", "onRoutingCancelled");
    }
}