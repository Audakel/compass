package edu.byu.android.helloandroid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.byu.android.helloandroid.utility.ActionBarHelper;

/**
 * Created by Liddle on 1/14/16.
 */
public class CompassActivity extends AppCompatActivity implements CompassFragment.CompassUpdateListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        SensorEventListener {

    String MY_TAG = "HW.Activity.Compass";
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    double mLatitude;
    double mLongitude;
    double mAltitude;
    GeomagneticField geoField;
    SensorManager mSensorManager;
    float currentDegree = 0f;


    TextView latitudeLabel ;
    TextView longitudeLabel;
    TextView altitudeLabel ;
    TextView accuracyLabel ;
    TextView speedLabel ;
    TextView headingLabel;
    TextView providerLabel;
    ImageView compassFace;


    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass_fragment);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

//        ActionBarHelper.addAppLogo(this, ActionBarHelper.UP_ENABLED);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                geoField = new GeomagneticField(
                        Double.valueOf(location.getLatitude()).floatValue(),
                        Double.valueOf(location.getLongitude()).floatValue(),
                        Double.valueOf(location.getAltitude()).floatValue(),
                        System.currentTimeMillis()
                );
                Log.d(MY_TAG, "updating geoloationa");


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
            Log.d(MY_TAG, "Built google api");
        }

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        latitudeLabel = (TextView) findViewById(R.id.latitudeLabel);
        longitudeLabel = (TextView) findViewById(R.id.longitudeLabel);
        altitudeLabel = (TextView) findViewById(R.id.altitudeLabel);
        accuracyLabel = (TextView) findViewById(R.id.errorLabel);
        speedLabel = (TextView) findViewById(R.id.speedLabel);
        headingLabel = (TextView) findViewById(R.id.headingLabel);
        providerLabel = (TextView) findViewById(R.id.providerLabel);
        compassFace = (ImageView) findViewById(R.id.compassImage);

        getLastLocation();

    }

    @Override
    public void updateLocation() {
        getLastLocation();
    }


    protected void onStart() {
        super.onStart();
        Log.d(MY_TAG, "Tryign to connect");
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Compass Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.byu.android.helloandroid/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Compass Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://edu.byu.android.helloandroid/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(MY_TAG, "in side onConnected");
        mLastLocation = getLastLocation();
        Log.d(MY_TAG, "got location: " + mLastLocation);


    }

    private Location getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(MY_TAG, "Error on permisions with getLastLocation");
            return null;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            mLatitude=(mLastLocation.getLatitude());
            mLongitude=(mLastLocation.getLongitude());

            Log.d(MY_TAG, "about to get lat and long");
            Log.d(MY_TAG, "lat: " + mLatitude + " long: " + mLongitude);
        }
        else {
            Log.d(MY_TAG, "fused location not workign on emulator");

            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            long GPSLocationTime = 0;
            if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

            long NetLocationTime = 0;

            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }

            if ( 0 < GPSLocationTime - NetLocationTime ) {
                location = locationGPS;
            }
            else {
                location = locationNet;
            }
            Log.d(MY_TAG, "b4");

//            Log.d(MY_TAG,"LAT: " + (String.valueOf(location.getLatitude())));
//            Log.d(MY_TAG,"LONG: " + (String.valueOf(location.getLongitude())));
            Log.d(MY_TAG, "after");

            mLatitude=(location.getLatitude());
            mLongitude=(location.getLongitude());
            mAltitude=(location.getAltitude());

            latitudeLabel.setText("LAT: " + String.valueOf(Math.round(mLatitude * 100.0)/100.0));
            longitudeLabel.setText("LONG: " + String.valueOf(Math.round(mLongitude * 100.0)/100.0));
            altitudeLabel.setText("ALT: " + String.valueOf(mAltitude));
            accuracyLabel.setText(getString(R.string.accuracyString, "42"));
            speedLabel.setText(getString(R.string.speedString, "1.4"));
//            headingLabel.setText("HEADING: " + geoField.getDeclination());
            providerLabel.setText(getString(R.string.providerString, "gps"));
//            compassFace.setRotation(-42.0f);


        }
        return location;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(MY_TAG, "error: " + String.valueOf(mGoogleApiClient.isConnected()));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        headingLabel.setText("Heading: " + Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        compassFace.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }
}
