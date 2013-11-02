/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.contactmanager;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public final class ContactManager extends SingleFragmentActivity
{
	
	public static final String TAG = "ContactManager";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
	     * Creates kinvey client for communication with backend.
	     */
		final Client mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
		
		mKinveyClient.ping(new KinveyPingCallback() {
		    public void onFailure(Throwable t) {
		        Log.e(TAG, "Kinvey Ping Failed", t);
		    }
		    public void onSuccess(Boolean b) {
		        Log.d(TAG, "Kinvey Ping Success");
		    }
		});
	}

	/**
     * Called to determine which fragment to inflate.
     */
	@Override
	protected Fragment createFragment() {
		return new ContactManagerFragment();
	}
	
}
