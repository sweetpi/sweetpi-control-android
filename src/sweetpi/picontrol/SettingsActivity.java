package sweetpi.picontrol;

import sweetpi.picontrol.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsActivity extends Activity {

	public static String PREFERENCE_FILENAME = "PiControl";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		final EditText url = (EditText) findViewById(R.id.editText1);
		final EditText basicAuthUser = (EditText) findViewById(R.id.basicAuthUser);
		final EditText basicAuthPW = (EditText) findViewById(R.id.basicAuthPW);
		final CheckBox delTrusted = (CheckBox) findViewById(R.id.deleteCert);
		
		
		final SharedPreferences settings = getSharedPreferences(
				PREFERENCE_FILENAME, 0);

		Button button = (Button) findViewById(R.id.save);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("url", url.getText().toString());
				editor.putString("basicAuthUser", basicAuthUser.getText().toString());
				editor.putString("basicAuthPW", basicAuthPW.getText().toString());
				if(delTrusted.isChecked()) {
					editor.remove("trustedCert");
				}
				editor.commit();
				Intent intent = new Intent(SettingsActivity.this, PiControlActivity.class);
				startActivity(intent);
			}
		});

		url.setText(settings.getString("url", getResources().getString(R.string.default_url)));
		basicAuthUser.setText(settings.getString("basicAuthUser", ""));
		basicAuthPW.setText(settings.getString("basicAuthPW",""));
		if(settings.getString("trustedCert", "").isEmpty()) {
			delTrusted.setEnabled(false);
		}
	}

}
