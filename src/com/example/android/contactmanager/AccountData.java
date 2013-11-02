package com.example.android.contactmanager;

import com.google.api.client.util.Key;

import android.accounts.AuthenticatorDescription;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * A container class used to repreresent all known information about an account.
 */
public class AccountData extends com.google.api.client.json.GenericJson {
    
	@Key("_id")
    private String id; 
	@Key
	private String mName;
	@Key
    private String mType;
    private CharSequence mTypeLabel;
    private Drawable mIcon;

    /**
     * @param name The name of the account. This is usually the user's email address or
     *        username.
     * @param description The description for this account. This will be dictated by the
     *        type of account returned, and can be obtained from the system AccountManager.
     */
    public AccountData(Context context, String name, AuthenticatorDescription description) {
        mName = name;
        if (description != null) {
            mType = description.type;

            // The type string is stored in a resource, so we need to convert it into something
            // human readable.
            String packageName = description.packageName;
            PackageManager pm = context.getPackageManager();

            if (description.labelId != 0) {
                mTypeLabel = pm.getText(packageName, description.labelId, null);
                if (mTypeLabel == null) {
                    throw new IllegalArgumentException("LabelID provided, but label not found");
                }
            } else {
                mTypeLabel = "";
            }

            if (description.iconId != 0) {
                mIcon = pm.getDrawable(packageName, description.iconId, null);
                if (mIcon == null) {
                    throw new IllegalArgumentException("IconID provided, but drawable not " +
                            "found");
                }
            } else {
                mIcon = context.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
            }
        }
    }

    public String getName() {
        return mName;
    }

    public String getType() {
        return mType;
    }

    public CharSequence getTypeLabel() {
        return mTypeLabel;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public String toString() {
        return mName;
    }
}