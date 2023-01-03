package com.drivers.mobile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Iterator;

import org.json.JSONObject;
import org.openqa.selenium.MutableCapabilities;

import com.framework.utils.GlobalVariables;
import com.framework.utils.Reporter;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.appium.java_client.android.AndroidDriver;

public class AndroidAppDriver {

	static AndroidDriver driver = null;

	public static AndroidDriver getDriver(URL url) {
		try {
			driver = new AndroidDriver(url, convertToW3C());
			driver.manage().timeouts().implicitlyWait(GlobalVariables.waitTime);
			System.out.println("Android Driver initialized");
		} catch (Exception e) {
			Reporter.TScenario.get().log("Unable to initialize Android driver : "+e.getMessage());
		}		
		return driver;
	}

	public static MutableCapabilities convertToW3C() {
		MutableCapabilities caps = new MutableCapabilities();
		MutableCapabilities sauceOptions = new MutableCapabilities();

		JSONObject obj = new JSONObject(getJson());
		Iterator<String> iter = obj.keys();
		while (iter.hasNext()) {
			String key = iter.next();
			if (key.contains("sauce:options")) {
				JSONObject sauceObj =obj.getJSONObject(key);
				Iterator<String> sauceIter = sauceObj.keys();
				while (sauceIter.hasNext()) {
					String optionkey = sauceIter.next();
					System.out.println(optionkey);
					sauceOptions.setCapability(optionkey, sauceObj.get(optionkey));
				}
			} else {
				System.out.println(key);
				caps.setCapability(key, obj.get(key));
			}
		}
		caps.setCapability("sauce:options", sauceOptions);

		return caps;
	}

	/*
	 * Gson to read capabilities from JSON file
	 * 
	 */
	public static String getJson() {
		Gson gson = new Gson();
		String entity = "";
		try {
			Object jsonObject = gson.fromJson(
					new FileReader(GlobalVariables.configProp.getProperty("CAPSFilePath") + GlobalVariables.currentMOS.get()+".json"),
					Object.class);
			entity = gson.toJson(jsonObject);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}

}
