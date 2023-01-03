package com.drivers.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.framework.utils.GlobalVariables;

import io.github.bonigarcia.wdm.WebDriverManager;


public class FirefoxBrowser implements BrowserInterface{
	
	private WebDriver driver;

	@Override
	public synchronized WebDriver getDriver() {
		
		FirefoxOptions options = new FirefoxOptions();
		options.addPreference("acceptInsecureCerts", true);		
		
		WebDriverManager.firefoxdriver().capabilities(options);
		
		this.driver = WebDriverManager.firefoxdriver().create();
		driver.manage().timeouts().implicitlyWait(GlobalVariables.waitTime);
		driver.manage().timeouts().pageLoadTimeout(GlobalVariables.waitTime);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		
		
		return driver;
	}

}
