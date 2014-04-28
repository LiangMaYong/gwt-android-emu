GWT Android Emu
===============

This library emulates some Android APIs over GWT. The idea is to allow porting your Android Apps to HTML5 and run them in Chrome, Firefox OS, PhoneGAP, etc.

The main difficult is that all the app interface must be redesigned with the GWT UiBinder (or HTML), but we can keep a lot of Java code in common between the Android App and the HTML5 App.

The components that this library emulates are:

* Activities: supports onCreate(), onResume(), onPause(), onDestroy()
* Intents: you can pass data in a Bundle, launch another activities, etc.
* Views (you cannot create them from code, must be searched with findViewById, each HTML element is mapped to a View type; ListViews start to work with custom adapters)
* Menu and MenuItems
* AlertDialogs and ProgressDialogs
* Toasts
* SharedPreferences: implemented using LocalStorage
* Handlers and Messages
* Log
* FloatMath
* SystemClock

The "android." package names are renamed to "androidemu.", you need to make a "Source->Organize imports" in Eclipse.

This is a work in progress in continuous evolution. At Mobialia we used this library to port some of our Android apps to GWT. It's far from complete and very fitted to our needs, but we make it public in the hope that it will be useful for other developers.

It's released under the MIT License, so feel free to use it anywhere.

Demo Project
============

We include a demo project to see some usage examples. You can view this demo at:

http://gwt-android-emu.appspot.com/

It's a GWT app coded like an Android App, you can see the MainActivity class and find the differences:

https://github.com/albertoruibal/gwt_android_emu/blob/master/demo/src/main/java/androidemu/demo/MainActivity.java

It includes two Activities with Strings and Layouts as resources and shows the usage of Menus, Toasts, AlertDialogs, etc.

Tools
=====

We also include a tool ConvertStrings (in the package utils) to convert from ant Android XML file to GWT Class + Key-properties files

HTML Elements to Android Widgets Mappings
=========================================

Those HTML elements are supported and converted to Android widgets: (see also the ViewFactory class)

| HTML Element             | Android Widget |
| -------------------------|----------------|
| <div class="ListView">   | ListView       |
| <div class="ScrollView"> | ScrollView     |
| <div>                    | TextView       |
| <input type="text">      | EditText       |
| <input type="number">    | EditText       |
| <input type="password">  | EditText       |
| <input type="button">    | Button         |
| <input type="radio">     | RadioButton    |
| <input type="checkbox">  | CheckBox       |
| <input type="image">     | ImageButton    |
| <select>                 | Spinner        |
| <img>                    | ImageView      |

