package com.walkernation.db.provider;

import java.io.IOException;

import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;

public class SampleSyncAdapter extends AbstractThreadedSyncAdapter {

	public final static String LOG_TAG = SampleSyncAdapter.class.getCanonicalName();

	private AccountManager mAccountManager;
	private ContentResolver mContentResolver;

	public SampleSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);

		mAccountManager = AccountManager.get(context);
		mContentResolver = context.getContentResolver();
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {

		String authtoken = null;
		try {
			authtoken = mAccountManager.blockingGetAuthToken(account,
					LoginActivity.PARAM_AUTHTOKEN_TYPE, true);

			// Dummy sample. Do whatever you want in this method.
			// List data = fetchData(authtoken);
			//
			// syncRemoteDeleted(data);
			// syncFromServerToLocalStorage(data);
			// syncDirtyToServer(authtoken, getDirtyList(mContentResolver));
		} catch (Exception e) {
			handleException(authtoken, e, syncResult);
		}
	}

	private void handleException(String authtoken, Exception e,
			SyncResult syncResult) {
		if (e instanceof AuthenticatorException) {
			syncResult.stats.numParseExceptions++;
			Log.e(LOG_TAG, "AuthenticatorException", e);
		} else if (e instanceof OperationCanceledException) {
			Log.e(LOG_TAG, "OperationCanceledExcepion", e);
		} else if (e instanceof IOException) {
			Log.e(LOG_TAG, "IOException", e);
			syncResult.stats.numIoExceptions++;
		} else if (e instanceof AuthenticationException) {
			mAccountManager.invalidateAuthToken(
					LoginActivity.PARAM_ACCOUNT_TYPE, authtoken);
			// The numAuthExceptions require user intervention and are
			// considered hard errors.
			// We automatically get a new hash, so let's make SyncManager retry
			// automatically.
			syncResult.stats.numIoExceptions++;
			Log.e(LOG_TAG, "AuthenticationException", e);
		} else if (e instanceof ParseException) {
			syncResult.stats.numParseExceptions++;
			Log.e(LOG_TAG, "ParseException", e);
		} else if (e instanceof JSONException) {
			syncResult.stats.numParseExceptions++;
			Log.e(LOG_TAG, "JSONException", e);
		}
	}

	// ...
}