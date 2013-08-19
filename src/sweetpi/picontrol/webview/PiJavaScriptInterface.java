package sweetpi.picontrol.webview;

import java.util.ArrayList;

import org.json.JSONArray;

import sweetpi.picontrol.PiControlActivity;

import android.webkit.WebView;
import android.widget.Toast;

public class PiJavaScriptInterface {
	
	public class JsVoiceActivityCallback{
		private String callbackFunction;
		
		public JsVoiceActivityCallback(String callbackFunction) {
			this.callbackFunction = callbackFunction;
		}

		public void call(WebView wv, ArrayList<String> result) {
			JSONArray jsonResult = new JSONArray(result);
			wv.loadUrl("javascript:" + callbackFunction +  "(" + jsonResult + ")");
		}
		
	};
	
	PiControlActivity piControl;

	public PiJavaScriptInterface(PiControlActivity piControlActivity) {
		this.piControl = piControlActivity;
	}

	public void showToast(String toast) {
		Toast.makeText(piControl, toast, Toast.LENGTH_SHORT).show();
	}
	
	public void startVoiceRecognition(String callback) {
		piControl.startVoiceRecognitionActivity(new JsVoiceActivityCallback(callback));
	}

}