package android.demo.res.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;


public class NotificationsFragment extends Composite {

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	interface MyUiBinder extends UiBinder<Widget, NotificationsFragment> {
	}

	public NotificationsFragment() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
