package com.atheyus.pass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CheckPass extends Activity {
	private EditText EBox1;
	private EditText EBox2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setpass);
		EBox1 = (EditText) findViewById(R.id.editText1);
		EBox2 = (EditText) findViewById(R.id.editText2);
	}

	public void checkPass(View v) {
		String Ebox1 = EBox1.getText().toString();
		String Ebox2 = EBox2.getText().toString();
		int pass = 1;
		if (Ebox1.equals("")) {
			EBox1.setHint("Favor de escribir");
			pass++;
		}
		if (Ebox2.equals("")) {
			EBox2.setHint("Favor de escribir");
			pass++;
		}
		if (Ebox1.equals(Ebox2) && pass == 1) {
			try {
				String sha = cifrar(Ebox1);

				SqlAd fdb = new SqlAd(this, "DB", null, 1);

				SQLiteDatabase db = fdb.getWritableDatabase();

				if (db != null) {
					Cursor c = db.rawQuery("SELECT * FROM MasterPass;", null);
					c.moveToFirst();
					if (c.getCount() > 0) {
						db.execSQL("DROP TABLE IF EXISTS MasterPass");
						db.execSQL("CREATE TABLE MasterPass (id INTEGER,key_pass TEXT);");
						db.execSQL("INSERT INTO MasterPass (id, key_pass) "
								+ "VALUES (" + 1 + ", '" + sha + "')");
					} else {
						db.execSQL("INSERT INTO MasterPass (id, key_pass) "
								+ "VALUES (" + 1 + ", '" + sha + "')");
					}
				}
				db.close();
				fdb.close();
				finish();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Checar contraseñas,no coinciden", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	static String cifrar(String p) throws NoSuchAlgorithmException {
		byte[] passB = p.getBytes();

		MessageDigest messageD = MessageDigest.getInstance("SHA-256");

		messageD.update(passB);

		byte[] bytes = messageD.digest();

		StringBuilder passS = new StringBuilder();

		for (byte b : bytes) {
			String x = Integer.toHexString(0xFF & b);
			if (x.length() == 1)
				passS.append('0');
			passS.append(x);
			passS.append(":");
		}

		String finalStringSha = passS.toString();

		return finalStringSha;
	}
}