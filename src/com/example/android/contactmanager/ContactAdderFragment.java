package com.example.android.contactmanager;

import java.util.ArrayList;
import java.util.Iterator;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ContactAdderFragment extends Fragment {
	
	public static final String TAG = "ContactsAdderFragment";
    public static final String ACCOUNT_NAME =
            "com.example.android.contactmanager.ContactsAdder.ACCOUNT_NAME";
    public static final String ACCOUNT_TYPE =
            "com.example.android.contactmanager.ContactsAdder.ACCOUNT_TYPE";

    private ArrayList<User> mAccounts;
    private AccountAdapter mAccountAdapter;
    private Spinner mAccountSpinner;
    private EditText mContactEmailEditText;
    private ArrayList<Integer> mContactEmailTypes;
    private Spinner mContactEmailTypeSpinner;
    private EditText mContactNameEditText;
    private EditText mContactPhoneEditText;
    private ArrayList<Integer> mContactPhoneTypes;
    private Spinner mContactPhoneTypeSpinner;
    private Button mContactSaveButton;
    
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
		View v = inflater.inflate(R.layout.contact_adder, container, false);
		
		// Obtain handles to UI objects
        mAccountSpinner = (Spinner) v.findViewById(R.id.accountSpinner);
        mContactNameEditText = (EditText) v.findViewById(R.id.contactNameEditText);
        mContactPhoneEditText = (EditText) v.findViewById(R.id.contactPhoneEditText);
        mContactEmailEditText = (EditText) v.findViewById(R.id.contactEmailEditText);
        mContactPhoneTypeSpinner = (Spinner) v.findViewById(R.id.contactPhoneTypeSpinner);
        mContactEmailTypeSpinner = (Spinner) v.findViewById(R.id.contactEmailTypeSpinner);
        mContactSaveButton = (Button) v.findViewById(R.id.contactSaveButton);

        // Prepare list of supported account types
        // Note: Other types are available in ContactsContract.CommonDataKinds
        //       Also, be aware that type IDs differ between Phone and Email, and MUST be computed
        //       separately.
        mContactPhoneTypes = new ArrayList<Integer>();
        mContactPhoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        mContactPhoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        mContactPhoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        mContactPhoneTypes.add(ContactsContract.CommonDataKinds.Phone.TYPE_OTHER);
        mContactEmailTypes = new ArrayList<Integer>();
        mContactEmailTypes.add(ContactsContract.CommonDataKinds.Email.TYPE_HOME);
        mContactEmailTypes.add(ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        mContactEmailTypes.add(ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
        mContactEmailTypes.add(ContactsContract.CommonDataKinds.Email.TYPE_OTHER);

        // Prepare model for account spinner
        mAccounts = new ArrayList<User>();
        mAccounts.add(mKinveyClient.user());
        mAccountAdapter = new AccountAdapter(getActivity(), mAccounts);
        mAccountSpinner.setAdapter(mAccountAdapter);

        // Populate list of account types for phone
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Iterator<Integer> iter;
        iter = mContactPhoneTypes.iterator();
        while (iter.hasNext()) {
            adapter.add(ContactsContract.CommonDataKinds.Phone.getTypeLabel(
                    this.getResources(),
                    iter.next(),
                    getString(R.string.undefinedTypeLabel)).toString());
        }
        mContactPhoneTypeSpinner.setAdapter(adapter);
        mContactPhoneTypeSpinner.setPrompt(getString(R.string.selectLabel));

        // Populate list of account types for email
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iter = mContactEmailTypes.iterator();
        while (iter.hasNext()) {
            adapter.add(ContactsContract.CommonDataKinds.Email.getTypeLabel(
                    this.getResources(),
                    iter.next(),
                    getString(R.string.undefinedTypeLabel)).toString());
        }
        mContactEmailTypeSpinner.setAdapter(adapter);
        mContactEmailTypeSpinner.setPrompt(getString(R.string.selectLabel));

        // Register handlers for UI elements
        mAccountSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long i) {
                // No longer need to worry about this either, because we'll only have one account
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // We don't need to worry about nothing being selected, since Spinners don't allow
                // this.
            }
        });
        mContactSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
		
		return v;
	}

	/**
     * Actions for when the Save button is clicked. Creates a contact entry and terminates the
     * activity.
     */
    private void onSaveButtonClicked() {
        Log.v(TAG, "Save button clicked");
        createContactEntry();
        getActivity().finish();
    }

    /**
     * Creates a contact entry from the current UI values in the account named by mSelectedAccount.
     */
    protected void createContactEntry() {
        // Get values from UI
        String name = mContactNameEditText.getText().toString();
        String phone = mContactPhoneEditText.getText().toString();
        String email = mContactEmailEditText.getText().toString();
        int phoneType = mContactPhoneTypes.get(
                mContactPhoneTypeSpinner.getSelectedItemPosition());
        int emailType = mContactEmailTypes.get(
                mContactEmailTypeSpinner.getSelectedItemPosition());;

        // Create new contact and set fields
        Contact newContact = new Contact();
        
        newContact.setName(name);
        newContact.setPhone(phone);
        newContact.setEmail(email);
        newContact.setPhoneType(phoneType);
        newContact.setEmailType(emailType);
        
        // Save to backend
        AsyncAppData<Contact> myevents = mKinveyClient.appData("contact", Contact.class);
        myevents.save(newContact, new KinveyClientCallback<Contact>() {
          @Override
          public void onFailure(Throwable e) {
              Log.e(TAG, "failed to save event data", e); 
          }
          @Override
          public void onSuccess(Contact r) {
              Log.d(TAG, "saved data for entity "+ r.getName()); 
          }
        });
    }

    /**
     * Custom adapter used to display account icons and descriptions in the account spinner.
     */
    private class AccountAdapter extends ArrayAdapter<User> {
        public AccountAdapter(Context context, ArrayList<User> accountData) {
            super(context, android.R.layout.simple_spinner_item, accountData);
            setDropDownViewResource(R.layout.account_entry);
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            // Inflate a view template
            if (convertView == null) {
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.account_entry, parent, false);
            }
            TextView firstAccountLine = (TextView) convertView.findViewById(R.id.firstAccountLine);
            TextView secondAccountLine = (TextView) convertView.findViewById(R.id.secondAccountLine);
            ImageView accountIcon = (ImageView) convertView.findViewById(R.id.accountIcon);
            
            // Populate template
            User user = getItem(position);
            firstAccountLine.setText(user.getId());
            secondAccountLine.setText(R.string.kinveyUser);
            Drawable icon = getResources().getDrawable(R.drawable.kinvey);
            accountIcon.setImageDrawable(icon);
            return convertView;
        }
    }

}
