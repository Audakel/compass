package edu.byu.android.helloandroid;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public interface MainCommandListener {
        public void onShowCalculatorSelected();
        public void onShowCompassSelected();
        public void onShowGPSStatusSelected();
        public void onShowMapSelected();
        public void onShowWookieSelected();
    }

    private MainCommandListener mMainCommandListener;

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mMainCommandListener = (MainCommandListener) context;
        } catch (ClassCastException e) {
            // Ignore
            mMainCommandListener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        view.findViewById(R.id.copyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView label = (TextView) view.findViewById(R.id.topTextLabel);
                EditText textField = (EditText) view.findViewById(R.id.mainEditText);

                label.setText(textField.getText());
            }
        });

        view.findViewById(R.id.showCalculatorButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainCommandListener != null) {
                    mMainCommandListener.onShowCalculatorSelected();
                }
            }
        });

        view.findViewById(R.id.showCompassButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainCommandListener != null) {
                    mMainCommandListener.onShowCompassSelected();
                }
            }
        });

        view.findViewById(R.id.showGPSStatusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainCommandListener != null) {
                    mMainCommandListener.onShowGPSStatusSelected();
                }
            }
        });

        view.findViewById(R.id.showMapButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainCommandListener != null) {
                    mMainCommandListener.onShowMapSelected();
                }
            }
        });

        view.findViewById(R.id.showWookieButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainCommandListener != null) {
                    mMainCommandListener.onShowWookieSelected();
                }
            }
        });

        return view;
    }
}
