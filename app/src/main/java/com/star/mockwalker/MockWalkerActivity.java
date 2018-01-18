package com.star.mockwalker;

import android.app.Dialog;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MockWalkerActivity extends SingleFragmentActivity {

    private static final int REQUEST_ERROR = 0;

    @Override
    protected Fragment createFragment() {
        return new MockWalkerFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int errorCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = googleApiAvailability
                    .getErrorDialog(this, errorCode, REQUEST_ERROR,
                            dialog -> {
                                // Leave if services are unavailable.
                                finish();
                            });

            errorDialog.show();
        }
    }
}
