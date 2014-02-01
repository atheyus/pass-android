package com.atheyus.pass;

import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.EditText;

public class Prefs extends PreferenceActivity {
	private Preference mDialogoNormal;
	private String PassHa = "";

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.prefs);
		mDialogoNormal = findPreference("keyforPass");

		SqlAd fdb = new SqlAd(this, "DB", null, 1);

		SQLiteDatabase db = fdb.getWritableDatabase();

		if (db != null) {
			Cursor c = db.rawQuery("SELECT * FROM MasterPass;", null);
			c.moveToFirst();
			if (c.getCount() > 0)
				PassHa = c.getString(1);
			else
				PassHa = "";
		}

		db.close();
		fdb.close();

		mDialogoNormal
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						if (PassHa.equals("")) {
							startActivity(new Intent(Prefs.this,
									CheckPass.class));
						} else {

							final EditText input = new EditText(Prefs.this);

							new AlertDialog.Builder(Prefs.this)
									.setTitle("Password")
									.setMessage("Password para cambiarlo")
									.setView(input)
									.setPositiveButton(
											"Aceptar",
											new DialogInterface.OnClickListener() {

												public void onClick(
														DialogInterface dialog,
														int whichButton) {
													String sname = input
															.getText()
															.toString();
													try {
														if (CheckPass.cifrar(
																sname).equals(
																PassHa)) {
															startActivity(new Intent(
																	Prefs.this,
																	CheckPass.class));
														}
													} catch (NoSuchAlgorithmException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}

												}
											})
									.setNegativeButton(
											"Cancelar",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int whichButton) {
												}
											}).show();

						}
						return false;
					}
				});
	}
}