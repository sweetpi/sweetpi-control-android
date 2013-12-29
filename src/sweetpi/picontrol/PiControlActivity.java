package sweetpi.picontrol;

import java.util.ArrayList;

import sweetpi.picontrol.webview.PiChromeClient;
import sweetpi.picontrol.webview.PiJavaScriptInterface;
import sweetpi.picontrol.webview.PiWebViewClient;

import sweetpi.picontrol.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class PiControlActivity extends Activity {
	
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	
	private PiJavaScriptInterface jsInterface; 
	private PiJavaScriptInterface.JsVoiceActivityCallback voiceCb;
	private WebView mainWebView;
	private SharedPreferences settings;

	private boolean doubleBackToExitPressedOnce = false;

	/** Called when the activity is first created. */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ActionBar actionBar = getActionBar();
		actionBar.hide();

		settings = getSharedPreferences(SettingsActivity.PREFERENCE_FILENAME, 0);
		mainWebView = (WebView) findViewById(R.id.mainWebView);

		mainWebView.setWebChromeClient(new PiChromeClient(this));
		mainWebView.setWebViewClient(new PiWebViewClient(this, mainWebView));

		WebSettings webSettings = mainWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		// Set cache size to 8 mb by default. should be more than enough
		webSettings.setAppCacheMaxSize(1024*1024*8);
		// This next one is crazy. It's the DEFAULT location for your app's cache
		String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
		webSettings.setAppCachePath(appCachePath);
		
		webSettings.setAllowFileAccess(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		
		mainWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		
		jsInterface = new PiJavaScriptInterface(this);
		mainWebView.addJavascriptInterface(jsInterface, "device");
		

		if (savedInstanceState != null) {
			mainWebView.restoreState(savedInstanceState);
		} else {
			String url = settings.getString("url", getResources().getString(R.string.default_url));
			mainWebView.loadUrl(url);
		}

	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	
	public void startVoiceRecognitionActivity(PiJavaScriptInterface.JsVoiceActivityCallback cb) {
		this.voiceCb = cb;
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"Raspberry Pi Sprachsteuerung");
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
			ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			if(voiceCb != null) {
				voiceCb.call(mainWebView, matches);
				voiceCb = null;
			} else {
				System.err.println("Could not call callback, was null!");
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		WebView mainWebView = (WebView) findViewById(R.id.mainWebView);
		mainWebView.saveState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		WebView mainWebView = (WebView) findViewById(R.id.mainWebView);
		mainWebView.restoreState(savedInstanceState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		System.err.println("conf changed");
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(PiControlActivity.this, SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed()
	{
	    if(mainWebView.canGoBack()) {
	    	mainWebView.goBack();
	    } else {
	        if (doubleBackToExitPressedOnce ) {
	            super.onBackPressed();
	            return;
	        }
	        this.doubleBackToExitPressedOnce = true;
	        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
	        new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	             doubleBackToExitPressedOnce=false;   
	            }
	        }, 2000);
	    }
	}
	
	
	
}