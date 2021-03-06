package android.support.v7.widget;

import android.Res;
import android.content.Context;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gwt.user.client.ui.RootPanel;

public class PopupMenu {

	Context context;
	View anchor;
	Menu menu;
	OnMenuItemClickListener mMenuItemClickListener;
	private LinearLayout menuLayout;

	boolean isShowing = false;

	public PopupMenu(Context context, View anchor) {
		this(context, anchor, Gravity.NO_GRAVITY);
	}

	public PopupMenu(Context context, View anchor, int gravity) {
		this.context = context;
		this.anchor = anchor;
		menu = new Menu();
	}

	public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
		mMenuItemClickListener = listener;
	}

	public Menu getMenu() {
		return menu;
	}

	public MenuInflater getMenuInflater() {
		return new MenuInflater();
	}

	public void show() {
		if (menuLayout == null) {
			menuLayout = new LinearLayout(context);
			menuLayout.getElement().addClassName(Res.R.style().dialog());
			menuLayout.getElement().addClassName(Res.R.style().gone());

			for (final MenuItem item : menu.menuItems) {
				Button b = new Button(context);
				if (item.getItemId() != 0) {
					b.getElement().setId(context.getResources().getIdAsString(item.getItemId()));
				}
				if (item.getTitle() != 0) {
					b.setText(context.getString(item.getTitle()));
				}
				if (item.getTitleString() != null) {
					b.setText(item.getTitleString());
				}
				b.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
						if (mMenuItemClickListener != null) {
							mMenuItemClickListener.onMenuItemClick(item);
						}
					}
				});
				b.getElement().addClassName(Res.R.style().menuItem());
				b.getElement().addClassName(Res.R.style().controlHighlight());
				menuLayout.addView(b);
			}
			RootPanel.getBodyElement().appendChild(menuLayout.getElement());
		}

		if (menuLayout.getElement().hasClassName(Res.R.style().gone())) {
			menuLayout.getElement().removeClassName(Res.R.style().gone());
		}

		int anchorlocation[] = {0, 0};
		anchor.getLocationOnScreen(anchorlocation);
		menuLayout.getElement().getStyle().setProperty("position", "fixed");
		menuLayout.getElement().getStyle().setPropertyPx("left", anchorlocation[0] + anchor.getWidth() - menuLayout.getWidth());
		menuLayout.getElement().getStyle().setPropertyPx("top", anchorlocation[1] + anchor.getHeight());

		isShowing = true;
	}

	public void dismiss() {
		if (menuLayout != null && !menuLayout.getElement().hasClassName(Res.R.style().gone())) {
			menuLayout.getElement().addClassName(Res.R.style().gone());
		}
		isShowing = false;
	}

	/**
	 * NOT STANDARD
	 */
	public void destroy() {
		if (menuLayout != null) {
			menuLayout.getElement().removeFromParent();
		}
	}

	/**
	 * NOT STANDARD, used by the system action bar
	 */
	public void toggle() {
		if (!isShowing) {
			show();
		} else {
			dismiss();
		}
	}

	public interface OnMenuItemClickListener {
		boolean onMenuItemClick(MenuItem item);
	}
}
