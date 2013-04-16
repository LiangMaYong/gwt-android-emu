package androidemu.view;

import androidemu.util.Log;
import androidemu.widget.Button;
import androidemu.widget.CheckBox;
import androidemu.widget.EditText;
import androidemu.widget.ImageButton;
import androidemu.widget.ListView;
import androidemu.widget.RadioButton;
import androidemu.widget.ScrollView;
import androidemu.widget.Spinner;
import androidemu.widget.TextView;

import com.google.gwt.dom.client.Element;

public class ViewFactory {

	static final String TAG = "ViewFactory";

	public static View createViewFromElement(Element element) {

		if (element.getNodeName() == "DIV") {
			String className = element.getAttribute("class").toUpperCase();
			if (className != null) {
				if (className.indexOf("LISTVIEW") >= 0) {
					return new ListView(element);
				} else if (className.indexOf("SCROLLVIEW") >= 0) {
					return new ScrollView(element);
				}
				return new TextView(element);
			}
		} else if (element.getNodeName() == "INPUT") {
			String type = element.getAttribute("type").toUpperCase();
			if (type.equals("TEXT")) {
				return new EditText(element);
			} else if (type.equals("PASSWORD")) {
				return new EditText(element);
			} else if (type.equals("BUTTON")) {
				return new Button(element);
			} else if (type.equals("RADIO")) {
				return new RadioButton(element);
			} else if (type.equals("CHECKBOX")) {
				return new CheckBox(element);
			} else if (type.equals("IMAGE")) {
				return new ImageButton(element);
			}
		} else if (element.getNodeName() == "SELECT") {
			return new Spinner(element);
		}

		return new View(element);
	}

	public static Element getElementById(Element element, String id) {
		if (id.equals(element.getId())) {
			return element;
		}
		Element child = element.getFirstChildElement();
		if (child != null) {
			Element out = getElementById(child, id);
			if (out != null) {
				return out;
			}
		}
		Element next = element.getNextSiblingElement();
		if (next != null) {
			Element out = getElementById(next, id);
			if (out != null) {
				return out;
			}
		}
		return null;
	}

	public static View findViewById(Element element, String id) {
		Element elementFound = ViewFactory.getElementById(element, id);

		if (elementFound == null) {
			Log.e(TAG, "View not found: " + id);
			return null;
		}
		return ViewFactory.createViewFromElement(elementFound);
	}

}
