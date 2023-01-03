package com.drivers.mobile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Objects;

import org.json.JSONObject;
import org.openqa.selenium.MutableCapabilities;

import com.framework.utils.GlobalVariables;
import com.framework.utils.Reporter;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.testdata.DBManager;

import io.appium.java_client.android.AndroidDriver;

public class AndroidRealDeviceDriver {

	private static AndroidDriver driver;
	static String entity;	
	static int emuCount;

	public AndroidDriver getDriver(String urlList) {
		try {	
			String[] url = urlList.split(",");					
			int portid = getCount();
			driver = new AndroidDriver(new URL(url[portid]), convertToW3C(portid));			
			driver.manage().timeouts().implicitlyWait(GlobalVariables.waitTime);
			System.out.println("Android Real Device Driver initialized");
		} catch (Exception e) {			
			Reporter.TScenario.get().log("Unable to initialize Android Real Device driver : "+e.getMessage());			
		}		
		return driver;
	}

	public static MutableCapabilities convertToW3C(int id) {
		MutableCapabilities caps = new MutableCapabilities();		
		JSONObject obj = new JSONObject(getJson());
		Iterator<String> iter = obj.keys();
		while (iter.hasNext()) {
			String key = iter.next();
			System.out.println(key);
			if(key.equals("appium:app")) {
				caps.setCapability(key, System.getProperty("user.dir")+obj.get(key));
			}
			else if(key.equals("appium:udid")) {	
				System.out.println(emuCount + GlobalVariables.configProp.getProperty("DeviceID").split(",")[id]);
				caps.setCapability(key, GlobalVariables.configProp.getProperty("DeviceID").split(",")[id]);				
			}
			else {
			caps.setCapability(key, obj.get(key));}
		}	
		System.out.println(caps);
		return caps;
	}

	/*
	 * Gson to read capabilities from JSON file
	 * 
	 */
	public static String getJson() {
		Gson gson = new Gson();
		if(!Objects.isNull(entity)) {
			return entity;
		}
		else {
			try {
				System.out.println("Delete 2 " + System.getProperty("user.dir")
						+ GlobalVariables.configProp.getProperty("CAPSFilePath") + GlobalVariables.currentMOS.get()
						+ ".json");
				Object jsonObject = gson.fromJson(new FileReader(GlobalVariables.configProp.getProperty("CAPSFilePath")
						+ GlobalVariables.currentMOS.get() + ".json"), Object.class);
				entity = gson.toJson(jsonObject);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return entity;
	}
	
	public static synchronized int getCount() {
		return emuCount++;		
	}

}
