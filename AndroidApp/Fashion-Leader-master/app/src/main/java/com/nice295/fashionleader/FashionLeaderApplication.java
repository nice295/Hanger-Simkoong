package com.nice295.fashionleader;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.util.Arrays;

public class FashionLeaderApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();

		Parse.enableLocalDatastore(getApplicationContext());
		Parse.initialize(this, "---", "---");

		ParseInstallation.getCurrentInstallation().put("channels", Arrays.asList("Android", "Hanger"));
		ParseInstallation.getCurrentInstallation().saveInBackground(); //for push

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		ParseACL.setDefaultACL(defaultACL, true);
	}

}
