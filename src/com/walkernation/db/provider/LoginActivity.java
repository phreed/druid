package com.walkernation.db.provider;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.walkernation.db.R;

public class LoginActivity extends AccountAuthenticatorActivity {
	EditText mUsername;
	EditText mPassword;
	Button mLoginButton;
	public final static String PARAM_AUTHTOKEN_TYPE = "";
	public final static String PARAM_ACCOUNT_TYPE = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authenticator_layout);

		mUsername = (EditText) findViewById(R.id.username);
		mPassword = (EditText) findViewById(R.id.password);

		mLoginButton = (Button) findViewById(R.id.login);
		mLoginButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String user = mUsername.getText().toString().trim()
						.toLowerCase();
				String password = mPassword.getText().toString().trim()
						.toLowerCase();

				if (user.length() > 0 && password.length() > 0) {
					LoginTask t = new LoginTask(LoginActivity.this);
					t.execute(user, password);
				}
			}

		});
	}

	private class LoginTask extends AsyncTask<String, Void, Boolean> {
		Context mContext;
		ProgressDialog mDialog;

		LoginTask(Context c) {
			mContext = c;
			mLoginButton.setEnabled(false);

			mDialog = ProgressDialog.show(c, "",
					getString(R.string.authenticating), true, false);
			mDialog.setCancelable(true);
		}

		@Override
		public Boolean doInBackground(String... params) {
			String user = params[0];
			String pass = params[1];

			// Do something internetty
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Bundle result = null;
			Account account = new Account(user,
					mContext.getString(R.string.ACCOUNT_TYPE));
			AccountManager am = AccountManager.get(mContext);
			if (am.addAccountExplicitly(account, pass, null)) {
				result = new Bundle();
				result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
				result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
				setAccountAuthenticatorResult(result);
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void onPostExecute(Boolean result) {
			mLoginButton.setEnabled(true);
			mDialog.dismiss();
			if (result)
				finish();
		}
	}
}