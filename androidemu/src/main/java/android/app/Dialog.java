package android.app;

import android.Res;
import android.content.DialogInterface;

import com.google.gwt.user.client.ui.PopupPanel;

public class Dialog implements DialogInterface {

	PopupPanel popupPanel;

	public Dialog(boolean cancelable) {
		popupPanel = new PopupPanel(cancelable);
		popupPanel.setStyleName(Res.R.style().dialog());
	}

	public boolean isShowing() {
		return popupPanel.isShowing();
	}

	@Override
	public void cancel() {
		popupPanel.hide();
	}

	@Override
	public void dismiss() {
		popupPanel.hide();
	}

	public void show() {
		popupPanel.center();
		popupPanel.show();
	}

	public void hide() {
		popupPanel.hide();
	}
}
