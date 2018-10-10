package com.obydul.fbaccountkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;

import static com.facebook.accountkit.ui.SkinManager.Skin.CONTEMPORARY;

public class MainActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE = 99;
    private Button mBtnEmailLogin;
    private Button mBtnPhoneLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getObjects();
        setProperties();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String responseMessage;
            if (loginResult.getError() != null) {
                responseMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                responseMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    responseMessage = "UserProfile: " + loginResult.getAccessToken().getAccountId();
                } else {
                    responseMessage = String.format(
                            "UserProfile:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // UserProfile! Start your next activity...
                goToUserProfileActivity();
            }
        }
    }

    private void getObjects() {
        mBtnEmailLogin = findViewById(R.id.btnEmailLogin);
        mBtnPhoneLogin = findViewById(R.id.btnPhoneLogin);
    }

    private void setProperties() {
        AccountKit.initialize(getApplicationContext(), new AccountKit.InitializeCallback() {
            @Override
            public void onInitialized() {
            }
        });

        mBtnEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin(false);
            }
        });

        mBtnPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin(true);
            }
        });

        if (AccountKit.getCurrentAccessToken() != null) {
            goToUserProfileActivity();
        }
    }

    public void goToLogin(boolean isSMSLogin) {
        LoginType loginType = isSMSLogin ? LoginType.PHONE : LoginType.EMAIL;

        Intent intent = new Intent(getApplicationContext(), AccountKitActivity.class);

        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );

        configurationBuilder.setUIManager(new SkinManager(
                        CONTEMPORARY,
                        getResources().getColor(R.color.red_500),
                        R.drawable.background,
                        SkinManager.Tint.BLACK,
                        0.10
                )
        );

        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());

        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    private void goToUserProfileActivity() {
        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
        startActivity(intent);
        finish();
    }

}