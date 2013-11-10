package sweetpi.picontrol.webview;

import sweetpi.picontrol.PiControlActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

final public class PiChromeClient extends WebChromeClient {
	/**
	 * 
	 */
	private final PiControlActivity piControlActivity;

	/**
	 * @param piControlActivity
	 */
	public PiChromeClient(PiControlActivity piControlActivity) {
		this.piControlActivity = piControlActivity;
	}

	@Override
	public boolean onJsAlert(WebView view, String url,
			String message, final JsResult result) {
		final AlertDialog alertDialog = new AlertDialog.Builder(this.piControlActivity).create();
		alertDialog.setMessage(message);
		alertDialog.setButton(Dialog.BUTTON_NEUTRAL,"OK",
				new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface dialog, int id) {
						result.confirm();
						alertDialog.dismiss();
					}
				});
		alertDialog.show();
		
		return true;//super.onJsAlert(view, url, message, result);
	}
}