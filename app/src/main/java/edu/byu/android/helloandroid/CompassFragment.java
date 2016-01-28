package edu.byu.android.helloandroid;

import android.app.Activity;
import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Liddle on 1/13/16.
 */
public class CompassFragment extends Fragment {
    Location location;
    String MY_TAG = "HW.Fragment.Compass";

    public interface CompassUpdateListener {
        void updateLocation();
    }

    CompassUpdateListener  mCompassUpdateListener;

    public CompassFragment() {
        // Default constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCompassUpdateListener = (CompassUpdateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compass_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();



        mCompassUpdateListener.updateLocation();
        GeomagneticField geoField = ((CompassActivity) this.getActivity()).geoField;

        TextView latitudeLabel = (TextView) getView().findViewById(R.id.latitudeLabel);
        TextView longitudeLabel = (TextView) getView().findViewById(R.id.longitudeLabel);
        TextView altitudeLabel = (TextView) getView().findViewById(R.id.altitudeLabel);
        TextView accuracyLabel = (TextView) getView().findViewById(R.id.errorLabel);
        TextView speedLabel = (TextView) getView().findViewById(R.id.speedLabel);
        TextView headingLabel = (TextView) getView().findViewById(R.id.headingLabel);
        TextView providerLabel = (TextView) getView().findViewById(R.id.providerLabel);
        ImageView compassFace = (ImageView) getView().findViewById(R.id.compassImage);

        latitudeLabel.setText("LAT: " + String.valueOf(((CompassActivity)this.getActivity()).geoField));
        longitudeLabel.setText("LONG: " + String.valueOf(((CompassActivity) this.getActivity()).mLongitude));
        altitudeLabel.setText("ALT: " + String.valueOf(((CompassActivity) this.getActivity()).mAltitude));
        accuracyLabel.setText(getString(R.string.accuracyString, "42"));
        speedLabel.setText(getString(R.string.speedString, "1.4"));
        headingLabel.setText("HEADING: " + geoField.getDeclination());
        providerLabel.setText(getString(R.string.providerString, "gps"));
        compassFace.setRotation(-42.0f);
    }
}
