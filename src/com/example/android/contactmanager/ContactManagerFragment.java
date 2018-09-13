package com.example.android.contactmanager;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ContactManagerFragment extends Fragment {
	
	public static final String TAG = "ContactManagerFragment";

    private Button mAddAccountButton;
    private ListView mContactList;
    private LinearLayout mProgress;
    
    private Client mKinveyClient;

    /**
     * Called when the fragment is first created. Responsible for initializing the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Fragment State: onCreate()");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        
        mKinveyClient = ((ContactManagerApplication)getActivity().getApplication()).getClient();
    }
    
    /**
     * Called when the fragment is first created. Responsible for initializing the UI.
     */
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.contact_manager, container, false);
		
		// Obtain handles to UI objects
        mAddAccountButton = (Button) v.findViewById(R.id.addContactButton);
        mContactList = (ListView) v.findViewById(R.id.contactList);
        mProgress = (LinearLayout) v.findViewById(R.id.manageContactProgress);
        
        // Register handler for UI elements
        mContactList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
				contactClicked((Contact)adapter.getItemAtPosition(position));
			}
		});
        mAddAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "mAddAccountButton clicked");
                launchContactAdder();
            }
        });
		
		return v;
	}

    /**
     * Re-populates the contact list when the fragment resumes.
     */
	@Override
	public void onResume() {
		super.onResume();
		
		// Populate the contact list
        populateContactList();
	}

	/**
     * Populate the contact list based on account currently selected in the account spinner.
     */
    private void populateContactList() {
    	// Reset adapter
    	mContactList.setAdapter(null);
    	
    	// Show progress bar
    	mContactList.setVisibility(View.INVISIBLE);
    	mProgress.setVisibility(View.VISIBLE);
    	
        // Build adapter with contact entries from kinvey search
    	AsyncAppData<Contact> myevents = mKinveyClient.appData("contact", Contact.class);
    	myevents.get(new KinveyListCallback<Contact>()     {
    	  @Override
    	  public void onSuccess(Contact[] results) { 
    	    Log.v(TAG, "received "+ results.length + " events");
    	    
    	    // Create array adapter with results and set list adapter
    	    ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(getActivity(), 
    	    		android.R.layout.simple_list_item_1, results);
            mContactList.setAdapter(adapter);
            
            // Hide progress bar
        	mContactList.setVisibility(View.VISIBLE);
        	mProgress.setVisibility(View.INVISIBLE);
    	  }
    	  @Override
    	  public void onFailure(Throwable error)  { 
    	    Log.e(TAG, "failed to fetch all", error);
    	  }
    	});
    }

    /**
     * Launches the ContactAdder activity to add a new contact to the selected account.
     */
    protected void launchContactAdder() {
        Intent i = new Intent(getActivity(), ContactAdder.class);
        startActivity(i);
    }
    
    /**
     * Creates a toast showing information about the contact that was clicked.
     */
    private void contactClicked(Contact contactClicked) {
    	String toastString = getResources().getString(R.string.contactClicked, 
				contactClicked.getName(), 
				contactClicked.getPhone(), 
				contactClicked.getEmail());
		
		Toast.makeText(getActivity(), toastString, Toast.LENGTH_LONG).show();	
    }

}
