package com.example.kkwbustracking;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.Address;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.security.Permission;
import java.util.List;
import java.util.Locale;

//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener, com.google.android.gms.location.LocationListener
/*public class MapsActivity extends FragmentActivity
{
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private  Location mLocation;
    double lat, lng;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    // Better to use GoogleApiClient to show device location. I am using this way in my aap.

        // private AddressResultReceiver mResultReceiver;
        // removed here because cause wrong code when implemented and
        // its not necessary like the author says

        //Define fields for Google API Client
        private Location lastLocation;
        private LocationRequest locationRequest;


        private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 14;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


    // mResultReceiver = new AddressResultReceiver(null);
            // cemented as above explained
            try {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                               // if (location != null) {
                                    // Logic to handle location object
                                   // txtLatitude.setText(String.valueOf(location.getLatitude()));
                                    //txtLongitude.setText(String.valueOf(location.getLongitude()));
                                //    if (mResultReceiver != null)
                                  //      txtAddress.setText(mResultReceiver.getAddress());
                               // }
                            }
                        });
                locationRequest = LocationRequest.create();
                locationRequest.setInterval(5000);
                locationRequest.setFastestInterval(1000);
                //if (txtAddress.getText().toString().equals(""))
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
               // else
                //    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        for (Location location : locationResult.getLocations()) {
                            // Update UI with location data
                           // txtLatitude.setText(String.valueOf(location.getLatitude()));
                           // txtLongitude.setText(String.valueOf(location.getLongitude()));
                        }
                    }

                    ;
                };
            } catch (SecurityException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStart() {
            super.onStart();

            if (!checkPermissions()) {
                startLocationUpdates();
                requestPermissions();
            } else {
                getLastLocation();
                startLocationUpdates();
            }
        }

        @Override
        public void onPause() {
            stopLocationUpdates();
            super.onPause();
        }

        /**
         * Return the current state of the permissions needed.
         */
      /*  private boolean checkPermissions() {
            int permissionState = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            return permissionState == PackageManager.PERMISSION_GRANTED;
        }

        private void startLocationPermissionRequest() {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }


        private void requestPermissions() {
            boolean shouldProvideRationale =
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION);

            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            if (shouldProvideRationale) {
             //   Log.i(TAG, "Displaying permission rationale to provide additional context.");

               /* showSnackbar(R.string.permission_rationale, android.R.string.ok,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Request permission
                                startLocationPermissionRequest();
                            }
                        });*/

      /*      } else {
             //   Log.i(TAG, "Requesting permission");
                // Request permission. It's possible this can be auto answered if device policy
                // sets the permission in a given state or the user denied the permission
                // previously and checked "Never ask again".
                startLocationPermissionRequest();
            }
        }

        /**
         * Callback received when a permissions request has been completed.
         */
    /*    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {
          //  Log.i(TAG, "onRequestPermissionResult");
            if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
                if (grantResults.length <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
               //     Log.i(TAG, "User interaction was cancelled.");
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted.
                    getLastLocation();
                } else {
                    // Permission denied.

                    // Notify the user via a SnackBar that they have rejected a core permission for the
                    // app, which makes the Activity useless. In a real app, core permissions would
                    // typically be best requested during a welcome-screen flow.

                    // Additionally, it is important to remember that a permission might have been
                    // rejected without asking the user for permission (device policy or "Never ask
                    // again" prompts). Therefore, a user interface affordance is typically implemented
                    // when permissions are denied. Otherwise, your app could appear unresponsive to
                    // touches or interactions which have required permissions.
                  /*  showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Build intent that displays the App settings screen.
                                    Intent intent = new Intent();
                                    intent.setAction(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package",
                                            BuildConfig.APPLICATION_ID, null);
                                    intent.setData(uri);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });*/
      /*          }
            }
        }


        /**
         * Provides a simple way of getting a device's location and is well suited for
         * applications that do not require a fine-grained location and that do not need location
         * updates. Gets the best and most recent location currently available, which may be null
         * in rare cases when a location is not available.
         * <p>
         * Note: this method should be called after location permission has been granted.
         */
    /*    @SuppressWarnings("MissingPermission")
        private void getLastLocation() {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                lastLocation = task.getResult();

                              //  txtLatitude.setText(String.valueOf(lastLocation.getLatitude()));
                              //  txtLongitude.setText(String.valueOf(lastLocation.getLongitude()));

                            } else {
                              //  Log.w(TAG, "getLastLocation:exception", task.getException());
                             //   showSnackbar(getString(R.string.no_location_detected));
                            }
                        }
                    });
        }

        private void stopLocationUpdates() {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }

        private void startLocationUpdates() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
        }

        // private void showSnackbar(final String text) {
        //    if (canvasLayout != null) {
        //        Snackbar.make(canvasLayout, text, Snackbar.LENGTH_LONG).show();
        //    }
        //}
        // this also cause wrong code and as I see it dont is necessary
        // because the same method which is really used


        private void showSnackbar(final int mainTextStringId, final int actionStringId,
                                  View.OnClickListener listener)
        {
            Snackbar.make(this.findViewById(android.R.id.content),
                    getString(mainTextStringId),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(actionStringId), listener).show();
        }
    }



   /* @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Check runtime Permission //
        int Permission_All = 1;
        String[] Permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
        if(!hasPermissions(this, Permissions))
        {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

        createLocationRequest();
        // make a buidler for GoogleApiClient //
        if(mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    protected  void createLocationRequest()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED )
        {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(20000); // 20 seconds
            mLocationRequest.setFastestInterval(10000); //10 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lng);
        String address = getAddress(this,lat,lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title(address));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    // Get Address from latitude and longitude //
    public String getAddress(Context ctx, double lat,double lng)
    {
        String fullAdd=null;
        try
        {
            Geocoder geocoder= new Geocoder(ctx, Locale.getDefault());
            List<android.location.Address> addresses = geocoder.getFromLocation(lat,lng,1);
            if(addresses.size()>0)
            {
                Address address = addresses.get(0);
                fullAdd = address.getAddressLine(0);

                // if you want only city or pin code use following code //
                   /* String Location = address.getLocality();
                    String zip = address.getPostalCode();
                    String Country = address.getCountryName();
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        return fullAdd;
    }


    // must declare methods //

    public void onStart()
    {
        mGoogleApiClient.connect();
        super.onStart();
        if(mGoogleApiClient.isConnected())
        {
            startLocationUpdates();
        }
    }
    public void onStop()
    {
        mGoogleApiClient.disconnect();
        stopLocationUpdate();
        super.onStop();
    }
    public void onPause()
    {
        mGoogleApiClient.connect();
        stopLocationUpdate();
        super.onPause();
    }
    public void onResume()
    {
        mGoogleApiClient.connect();
        super.onResume();
        if(mGoogleApiClient.isConnected())
        {
            startLocationUpdates();
        }
    }

    // create method for location update //
    protected void startLocationUpdates()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED )
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected  void stopLocationUpdate()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);



        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    // Must Declare LocatonListener Methods //
    public void onLocationChanged(Location location)
    {
        if(location!=null)
        {
            lat = location.getLatitude();
            lng = location.getLongitude();
            mapFragment.getMapAsync(this);
        }
    }

    public void onConnectionSuspended(int arg0)
    {

    }
    public void onStatusChange(String provider, int status, Bundle extras)
    {

    }

    // Must Declare Callback Methods //
    public void onConnected(Bundle args0)
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED )
        {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLocation!=null)
            {
                lat = mLocation.getLatitude();
                lng = mLocation.getLatitude();
                mapFragment.getMapAsync(this);
            }
            if(mGoogleApiClient.isConnected())
            {
                startLocationUpdates();
            }
        }
    }

    public static boolean hasPermissions(Context context, String... permissions)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null)
        {
            for(String permission: permissions)
            {
                if(ActivityCompat.checkSelfPermission(context, permission)!=PackageManager.PERMISSION_GRANTED)
                {
                    return  false;
                }
            }
        }
        return true;
    }

       /* // Request code to use when launching the resolution activity
        private static final int REQUEST_RESOLVE_ERROR = 1001;
        // Unique tag for the error dialog fragment
        private static final String DIALOG_ERROR = "dialog_error";
        // Bool to track whether the app is already resolving an error
        private boolean mResolvingError = false;  */

        // ...
/*
        @Override
        public void onConnectionFailed(ConnectionResult result)
        {
        /*if (mResolvingError)
        {
            // Already attempting to resolve an error.
            return;
        }
        else if (result.hasResolution())
        {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }*/


        // The rest of this code is all about building the error dialog

        /* Creates a dialog for an error message */
      /*  private void showErrorDialog(int errorCode)
        {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

        /* Called from ErrorDialogFragment when the dialog is dismissed. */
    /*    public void onDialogDismissed() {
        mResolvingError = false;
    }

        /* A fragment to display an error dialog */
      /*  public static class ErrorDialogFragment extends DialogFragment {
            public ErrorDialogFragment() { }

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Get the error code and retrieve the appropriate dialog
                int errorCode = this.getArguments().getInt(DIALOG_ERROR);
                return GoogleApiAvailability.getInstance().getErrorDialog(
                        this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
            }

           /* @Override
            public void onDismiss(DialogInterface dialog)
            {
                ((MyActivity) getActivity()).onDialogDismissed();
            }*/



public class MapsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



    }
}