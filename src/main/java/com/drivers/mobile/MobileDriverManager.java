package com.drivers.mobile;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.drivers.web.WebDriverManager;
import com.framework.utils.GlobalVariables;
import com.framework.utils.LoggerHelper;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;


public class MobileDriverManager {

	private static Logger log = LoggerHelper.getLogger(MobileDriverManager.class);
	
	
	private static InheritableThreadLocal<AppiumDriver> driver = new InheritableThreadLocal<AppiumDriver>();	
	private static URL url;
	private static URL localhost;
	
	private static String username = GlobalVariables.configProp.getProperty("SauceUN");
	private static String accesskey = GlobalVariables.configProp.getProperty("SauceKey");
	private static String region = GlobalVariables.configProp.getProperty("Region");
	

	
	static{
        String sauceUrl;        
        if (region.equalsIgnoreCase("eu")) {
            sauceUrl = "@ondemand.eu-central-1.saucelabs.com:443";
        } else {
            sauceUrl = "@ondemand.us-west-1.saucelabs.com:443";
        }
        
        String SAUCE_REMOTE_URL = "https://" + username + ":" + accesskey + sauceUrl +"/wd/hub";
        
        try {
			url = new URL(SAUCE_REMOTE_URL);
			localhost = new URL(GlobalVariables.configProp.getProperty("LocalHost"));
		} catch (MalformedURLException e) {			
			e.printStackTrace();
		}		
	}

	public static void configureDriver(){		
		String driverType = GlobalVariables.currentMOS.get();
		
		switch (driverType) {
		case "android":			
			driver.set(AndroidAppDriver.getDriver(url));
			break;
		case "ios":
			driver.set(IOSAppDriver.getDriver(url));
			break;	
		case "androidEmu":			
			driver.set(AndroidEmulatorDriver.getDriver(localhost));
			break;
		case "iosSim":
			//IP
			break;
		case "androidWeb":	
			WebDriverManager.setDriver(AndroidAppDriver.getDriver(url));			
			break;
		case "iosWeb":	
			WebDriverManager.setDriver(IOSAppDriver.getDriver(url));
			break;	
		case "androidEmuWeb":	
			WebDriverManager.setDriver(AndroidEmulatorDriver.getDriver(localhost));			
			break;
		case "iosSimWeb":	
			//IP
			break;
		}		
	}	
	
	/* start driver */
	public static void startDriver() {
//		if (remote == true) {
//			setRemoteWebDriver(GlobalVariables.currentBrowser.get());
//		} else if (remote == false) {			
//			setDriver(GlobalVariables.currentBrowser.get());			
//		}
	}
	
	/*
	 * This method quit the driver after the execution 
	 */	
	public static void quitDriver(){
		log.info("Shutting down driver");
		driver.get().quit();
	}
	
//	/*
//	 * This method returns driver instance 
//	 */	
//	public static AppiumDriver getDriver(){		
//		return driver.get();
//	}
	
	/*
	 * This method returns driver instance 
	 */	
	public static AppiumDriver getDriver(){		
		return driver.get();
	}

//	public static void getAPPType() {
//		Set<String> contextNames = driver.get().get;
//		for (String contextName : contextNames) {
//		    System.out.println(contextName); //prints out something like NATIVE_APP \n WEBVIEW_1
//		}
//	}
}
