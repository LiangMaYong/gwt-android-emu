package androidemu.app;

import java.util.Stack;

import androidemu.content.Intent;
import androidemu.util.Log;
import androidemu.view.View;
import androidemu.view.View.OnClickListener;
import androidemu.view.ViewFactory;
import androidemu.widget.ImageButton;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

public class ActivityManager {
	public final static String TAG = "ActivityManager";

	public final static int STATUS_NEW = 0;
	public final static int STATUS_CREATED = 1;
	public final static int STATUS_RESUMED = 2;
	public final static int STATUS_PAUSED = 3;
	public final static int STATUS_DESTROYED = 4;

	public static Stack<Activity> activityStack = new Stack<Activity>();
	static ImageButton backButton, menuButton;

	public static void setup() {
		if (DOM.getElementById("BackButton") != null) {
			backButton = (ImageButton) ViewFactory.createViewFromElement(DOM.getElementById("BackButton"));
			backButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ActivityManager.back();
				}
			});
		}
		if (DOM.getElementById("MenuButton") != null) {
			menuButton = (ImageButton) ViewFactory.createViewFromElement(DOM.getElementById("MenuButton"));
			menuButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ActivityManager.toggleOptionsMenu(v);
				}
			});
		}

		if (DOM.getElementById("show-while-loading") != null) {
			DOM.getElementById("show-while-loading").setAttribute("style", "display: none");
		}
	}

	public static void startActivity(Intent intent, Integer requestCode) {
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// Open URL
			Window.Location.assign(intent.getData().toString());
		} else {
			Log.d(TAG, "Start activity " + intent.activity.getClass().getName());
			intent.activity.intent = intent;
			activityStack.push(intent.activity);
			intent.activity.requestCode = requestCode;
			checkActivityStackDeferred();
		}
	}

	public static void finish(final Activity activity) {
		Log.d(TAG, "Finish activity " + activity.getClass().getName());
		activity.targetStatus = STATUS_DESTROYED;
		checkActivityStackDeferred();
	}

	public static void back() {
		activityStack.peek().onBackPressed();
		if (activityStack.size() > 1) {
			finish(activityStack.peek());
		}
	}

	public static void toggleOptionsMenu(View v) {
		activityStack.peek().toggleOptionsMenu(v);
	}

	private static void advanceStatus(Activity activity) {
		while (activity.status < activity.targetStatus) {
			switch (activity.status) {
			case STATUS_NEW:
				activity.onCreate(null);
				activity.status = STATUS_CREATED;
				break;
			case STATUS_CREATED:
				activity.onResume();
				activity.createMenu();
				checkButtonsVisibility(activity);
				activity.status = STATUS_RESUMED;
				break;
			case STATUS_RESUMED:
				activity.hideMenu();
				activity.onPause();
				activity.status = STATUS_PAUSED;
				break;
			case STATUS_PAUSED:
				activity.onDestroy();
				activity.status = STATUS_DESTROYED;
				activityStack.remove(activity);
			}
		}
		while (activity.status > activity.targetStatus) {
			switch (activity.status) {
			case STATUS_PAUSED:
				if (activity.returnRequestCode != null) {
					activity.onActivityResult(activity.returnRequestCode, activity.returnResultCode, activity.returnResultData);
				}
				activity.onResume();
				checkButtonsVisibility(activity);
				activity.status = STATUS_RESUMED;
				break;
			}
		}
	}

	private static void checkButtonsVisibility(Activity activity) {
		if (backButton != null) {
			backButton.getElement().removeClassName("hide-while-loading");
			backButton.setVisibility(activityStack.size() > 1 ? View.VISIBLE : View.GONE);
		}
		if (menuButton != null) {
			menuButton.getElement().removeClassName("hide-while-loading");
			menuButton.setVisibility(activity.menu != null && activity.menu.menuItems.size() > 0 ? View.VISIBLE : View.GONE);
		}
	}

	private static void checkActivityStackDeferred() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				try {
					checkActivityStack();
				} catch (Throwable e) {
					Log.e(TAG, e.getMessage());

					StringBuffer stb = new StringBuffer();
					stb.append(e.getMessage()).append("\n\nStack trace:\n");
					for (int i = 0; i < e.getStackTrace().length; i++) {
						stb.append(e.getStackTrace()[i].toString()).append("\n");
					}
					Log.e(TAG, stb.toString());
				}
			}
		});
	}

	private static void checkActivityStack() {
		// Finish Activities (and store result codes in caller activities)
		for (int i = 0; i < activityStack.size(); i++) {
			Activity act = activityStack.get(i);
			if (act.targetStatus == STATUS_DESTROYED) {
				if (i - 1 >= 0) {
					// Save return data in caller activity
					Activity callerActivity = activityStack.get(i - 1);
					callerActivity.returnRequestCode = act.requestCode;
					callerActivity.returnResultCode = act.resultCode;
					callerActivity.returnResultData = act.resultData;
				}
				// TODO deleted element while transversing the list
				advanceStatus(act);
			}
		}

		// Pause all activities not shown
		for (int i = 0; i < activityStack.size() - 1; i++) {
			Activity act = activityStack.get(i);
			if (act.status != STATUS_PAUSED) {
				act.targetStatus = STATUS_PAUSED;
				advanceStatus(act);
			}
		}

		// Start activity to show
		Activity activityToShow = activityStack.peek();
		if (activityToShow.status != STATUS_RESUMED) {
			activityToShow.targetStatus = STATUS_RESUMED;
			advanceStatus(activityToShow);
		}
	}

}