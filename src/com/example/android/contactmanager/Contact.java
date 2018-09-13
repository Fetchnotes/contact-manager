package com.example.android.contactmanager;

import com.google.api.client.util.Key;

/**
 * A container class used to repreresent all known information about an account.
 */
public class Contact extends com.google.api.client.json.GenericJson {
    
	@Key("_id")
    private String id; 
	@Key
	private String mName;
	@Key
    private String mPhone;
	@Key
    private String mEmail;
	@Key
    private int mPhoneType;
	@Key
    private int mEmailType;

    public Contact() {}

    public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public int getPhoneType() {
		return mPhoneType;
	}

	public void setPhoneType(int mPhoneType) {
		this.mPhoneType = mPhoneType;
	}

	public int getEmailType() {
		return mEmailType;
	}

	public void setEmailType(int mEmailType) {
		this.mEmailType = mEmailType;
	}

	public String toString() {
        return mName;
    }
}