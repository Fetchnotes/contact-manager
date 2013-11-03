package com.example.android.contactmanager;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

import android.app.Application;
import android.util.Log;

public class ContactManagerApplication extends Application {
	
	public static final String TAG = "ContactManagerApplication";

	private Client mKinveyClient;
	
	@Override
	public void onCreate() {
		super.onCreate();		
		
	    // Creates kinvey client for communication with backend.
		mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
		
		// Logs in if user is not already logged in.
		if (!mKinveyClient.user().isUserLoggedIn()) {
			mKinveyClient.user().login(new KinveyUserCallback() {
			    @Override
			    public void onFailure(Throwable error) {
			        Log.e(TAG, "Login Failure", error);
			    }
			    @Override
			    public void onSuccess(User result) {
			        Log.i(TAG,"Logged in a new implicit user with id: " + result.getId());
			    }
			});
		}
	}
	
	public void setsClient(Client myClient) {
        this.mKinveyClient = myClient;
    }

    public Client getClient(){
        return this.mKinveyClient;
    }

}
