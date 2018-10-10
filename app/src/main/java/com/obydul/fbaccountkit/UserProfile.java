package com.obydul.fbaccountkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;

public class UserProfile extends AppCompatActivity {

    private TextView mTxtAccountKitID;
    private TextView mTxtUserLoginData;
    private TextView mTxtUserLoginMode;
    private Button mBtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getObjects();
        setProperties();
    }

    private void getObjects() {
        mTxtAccountKitID = findViewById(R.id.txtAccountKitID);
        mTxtUserLoginMode = findViewById(R.id.txtUserLoginMode);
        mTxtUserLoginData = findViewById(R.id.txtUserLoginData);
        mBtnLogout = findViewById(R.id.btnLogout);
    }

    private void setProperties() {

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        setUserInformation();
    }

    public void setUserInformation() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                boolean SMSLoginMode = false;

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                String phoneNumberString = "";
                if (phoneNumber != null) {
                    phoneNumberString = phoneNumber.toString();
                    SMSLoginMode = true;
                }

                // Get email
                String email = account.getEmail();

                mTxtAccountKitID.setText(accountKitId);
                mTxtUserLoginMode.setText(SMSLoginMode ? "Phone:" : "Email:");
                if (SMSLoginMode) {
                    mTxtUserLoginData.setText(phoneNumberString);
                } else {
                    mTxtUserLoginData.setText(email);
                }

            }

            @Override
            public void onError(final AccountKitError error) {
            }
        });
    }

    public void logout() {
        AccountKit.logOut();
        Intent initialActivity = new Intent(this, MainActivity.class);
        startActivity(initialActivity);
        finish();
    }

}
