package com.star.mockwalker;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MockWalker implements GoogleApiClient.ConnectionCallbacks {

    private static MockWalker sMockWalker;

    public static synchronized MockWalker get(Context context) {
        if (sMockWalker == null) {
            sMockWalker = new MockWalker(context);
        }

        return sMockWalker;
    }

    private PublishSubject<MockWalker> mChanges;
    private Context mContext;
    private final GoogleApiClient mGoogleApiClient;
    private boolean mStarted;
    private MockWalk mMockWalk;

    private MockWalker(Context context) {
        mContext = context;
        mChanges = PublishSubject.create();
        mGoogleApiClient = new GoogleApiClient.Builder(mContext.getApplicationContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();
    }

    public Observable<MockWalker> getChanges() {
        return mChanges;
    }

    @Override
    public void onConnected(Bundle bundle) {
        syncWalkState();
    }

    public void setStarted(boolean started) {
        mStarted = started;
        syncWalkState();
        mChanges.onNext(this);
    }

    public boolean isStarted() {
        return mStarted;
    }

    private void syncWalkState() {
        if (!mStarted && mMockWalk != null) {
            mMockWalk.quit();
            mMockWalk = null;
        } else if (!mStarted && mMockWalk == null) {
            // all good
        } else if (mStarted && mMockWalk == null && !mGoogleApiClient.isConnected()) {
            // all good
        } else if (mStarted && mMockWalk == null && mGoogleApiClient.isConnected()) {
            mMockWalk = new MockWalk(mGoogleApiClient);
            mMockWalk.start();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
