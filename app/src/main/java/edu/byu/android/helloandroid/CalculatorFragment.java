package edu.byu.android.helloandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Liddle on 1/13/16.
 */
public class CalculatorFragment extends Fragment {
    public CalculatorFragment() {
        // Default constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calculator_fragment, container, false);
    }
}
