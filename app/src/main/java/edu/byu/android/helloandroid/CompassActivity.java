package edu.byu.android.helloandroid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by Liddle on 1/14/16.
 */
public class CompassActivity extends AppCompatActivity implements  SensorEventListener {

    String MY_TAG = "HW.Activity.Compass";
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    GeomagneticField geoField;
    SensorManager mSensorManager;
    float currentDegree = 0f;


    TextView latitudeLabel;
    TextView longitudeLabel;
    TextView altitudeLabel;
    TextView accuracyLabel;
    TextView speedLabel;
    TextView headingLabel;
    TextView providerLabel;
    ImageView compassFace;


    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(MY_TAG, "onCreate");
        // for the system's orientation sensor registered listeners



        setContentView(R.layout.compass_fragment);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBarHelper.addAppLogo(this, ActionBarHelper.UP_ENABLED);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

        initLocationListener();
        updateLastLocation();

    }


    protected void onStart() {
        super.onStart();
        Log.d(MY_TAG, "OnStart");
    }

    protected void onStop() {
        super.onStop();
        Log.d(MY_TAG, "OnStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(MY_TAG, "OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(MY_TAG, "OnPause");

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    private void updateLastLocation() {
        Log.d(MY_TAG, "calling update last location");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(MY_TAG, "Permisons not allowing for GPS");
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (locationGPS == null){
            Log.d(MY_TAG, "Location gps is null");
            return;
        }

        if (locationNet == null){
            Log.d(MY_TAG, "Location net is null");
            return;
        }

        long GPSLocationTime = 0;
        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            mLastLocation = locationGPS;
            Log.d(MY_TAG, "Update with gps, LAT: " + mLastLocation.getLatitude() + " LONG: " + mLastLocation.getLongitude());
        }
        else {
            mLastLocation = locationNet;
            Log.d(MY_TAG, "Update with net, LAT: " + mLastLocation.getLatitude() + " LONG: " + mLastLocation.getLongitude());
        }
        updateView();
    }

    private void updateView() {
        Log.d(MY_TAG, "updateView");
        latitudeLabel = (TextView) findViewById(R.id.latitudeLabel);
        longitudeLabel = (TextView) findViewById(R.id.longitudeLabel);
        altitudeLabel = (TextView) findViewById(R.id.altitudeLabel);
        accuracyLabel = (TextView) findViewById(R.id.errorLabel);
        speedLabel = (TextView) findViewById(R.id.speedLabel);
        headingLabel = (TextView) findViewById(R.id.headingLabel);
        providerLabel = (TextView) findViewById(R.id.providerLabel);
        compassFace = (ImageView) findViewById(R.id.compassImage);

        latitudeLabel.setText("LAT: " + String.valueOf(Math.round(mLastLocation.getLatitude() * 100.0)/100.0));
        longitudeLabel.setText("LONG: " + String.valueOf(Math.round(mLastLocation.getLongitude() * 100.0)/100.0));
        altitudeLabel.setText("ALT: " + String.valueOf(mLastLocation.getAltitude()));
        accuracyLabel.setText(getString(R.string.accuracyString, "42"));
        speedLabel.setText(getString(R.string.speedString, "1.4"));
//            headingLabel.setText("HEADING: " + geoField.getDeclination());
        providerLabel.setText(getString(R.string.providerString, "gps"));
//            compassFace.setRotation(-42.0f);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(MY_TAG, "onSensorChanged" + " Event " + event);
        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);
//        headingLabel.setText("Heading: " + Float.toString(degree) + " degrees");
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
        Log.d(MY_TAG, "here 2" + event);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void initLocationListener() {
        Log.d(MY_TAG, "initLocationListener");
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
    }
}
