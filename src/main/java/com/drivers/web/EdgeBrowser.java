package com.drivers.web;

import org.openqa.selenium.WebDriver;

import com.framework.utils.GlobalVariables;

import io.github.bonigarcia.wdm.WebDriverManager;

public class EdgeBrowser implements BrowserInterface{
	
	private WebDriver driver;

	@Override
	public synchronized WebDriver getDriver() {
		this.driver = WebDriverManager.edgedriver().create();
		driver.manage().timeouts().implicitlyWait(GlobalVariables.waitTime);
		driver.manage().timeouts().pageLoadTimeout(GlobalVariables.waitTime);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		return driver;
	}

}
