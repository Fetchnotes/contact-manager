package com.example.android.contactmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public abstract class SingleFragmentActivity extends FragmentActivity{

	public static final String TAG = "SingleFragmentActivity";

	/**
     * Called to determine which fragment to inflate.
     */
    protected abstract Fragment createFragment();

    /**
     * Called to determine which layout holds the container.
     */
    protected int getLayourResId() {
        return R.layout.activity_fragment;
    }

    /**
     * Inflates the specified fragment from createFragment into 
     * the specified fragment container.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "ActivityState: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(getLayourResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

}

