package com.star.mockwalker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import io.reactivex.disposables.CompositeDisposable;

public class MockWalkerFragment extends Fragment {
    private CompoundButton mStartButton;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Intent mServiceIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mockwalker, container, false);

        mStartButton = v.findViewById(R.id.start_button);
        mStartButton.setOnClickListener(v1 -> {
            MockWalker mockWalker = MockWalker.get(getActivity());

            if (mockWalker.isStarted()) {
                getActivity().stopService(mServiceIntent);
            } else {
                getActivity().startService(mServiceIntent);
            }
        });

        mServiceIntent = new Intent(getActivity(), MockWalkerService.class);

        mCompositeDisposable.add(MockWalker.get(getActivity()).getChanges()
                .subscribe(mockWalker -> updateUI()));

        updateUI();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.dispose();
    }

    private void updateUI() {
        MockWalker mockWalker = MockWalker.get(getActivity());
        if (mockWalker.isStarted()) {
            mStartButton.setChecked(true);
        } else {
            mStartButton.setChecked(false);
        }
    }
}
