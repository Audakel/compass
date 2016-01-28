package edu.byu.android.helloandroid.utility;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import edu.byu.android.helloandroid.R;

/**
 * Created by Liddle on 1/14/16.
 */
public class ActionBarHelper {
    /**
     * Show an up navigation button in our action bar.
     */
    public static final boolean UP_ENABLED = true;

    /**
     * Do not show an up navigation button in the action bar.
     */
    public static final boolean UP_DISABLED = false;

    public static void addAppLogo(AppCompatActivity activity, boolean upEnabled) {
        configureActionBar(activity.getSupportActionBar(), upEnabled);
    }

    public static void configureActionBar(ActionBar actionBar, boolean upEnabled) {
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_helloandroid);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(upEnabled);
    }
}
