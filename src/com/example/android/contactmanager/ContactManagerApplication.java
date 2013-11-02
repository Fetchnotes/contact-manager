package com.example.android.contactmanager;

import com.kinvey.android.Client;
import android.app.Application;

public class ContactManagerApplication extends Application {

	private Client mKinveyClient;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		/**
	     * Creates kinvey client for communication with backend.
	     */
		mKinveyClient = new Client.Builder(this.getApplicationContext()).build();
	}
	
	public void setsClient(Client myClient) {
        this.mKinveyClient = myClient;
    }

    public Client getClient(){
        return this.mKinveyClient;
    }

}
