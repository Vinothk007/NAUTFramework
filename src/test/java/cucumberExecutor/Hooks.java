package cucumberExecutor;

import java.io.IOException;
import java.util.Collection;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.drivers.mobile.MobileDriverManager;
import com.drivers.web.WebDriverManager;
import com.framework.utils.GlobalVariables;
import com.framework.utils.LoggerHelper;
import com.framework.utils.Reporter;
import com.testdata.DBManager;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

	private Logger log = LoggerHelper.getLogger(Hooks.class);

	@Before
	public void setup(Scenario scenario) throws InterruptedException {
		log.debug("******************************************");
		log.debug("Current Scenario Running: " + scenario.getName());
		log.debug("******************************************");

		String uri = scenario.getId();
		GlobalVariables.currentFeature.set(uri.substring(uri.lastIndexOf('/') + 1, uri.indexOf('.')));

		Reporter.TScenario.set(scenario);

		Collection<String> sourceTagNames = scenario.getSourceTagNames();
		for (String tagName : sourceTagNames) {
			if (tagName.contains("td_")) {
				GlobalVariables.currentTag = tagName.substring(4);
			}
		}
		if (DBManager.getData("Type").equals("UI")) {
			scenario.log("<strong>Executed Environment : </strong>" + GlobalVariables.currentBrowser.get());						
		} else {			
			scenario.log("<strong>Executed Environment : </strong>" + GlobalVariables.currentMOS.get());
		}
	}

	@After
	public void teardown(Scenario scenario) throws IOException {
		if (scenario.isFailed() && !DBManager.getData("Type").equals("api")) {
			try {
				Reporter.addScreenCapture();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(!GlobalVariables.appType.contains(GlobalVariables.currentMOS.get())) {
				WebDriverManager.quitDriver();
			} else {				
				MobileDriverManager.quitDriver();
			}
		}
		if(DBManager.getData("Type").equals("UI")) {
			Capabilities caps = ((RemoteWebDriver) WebDriverManager.getDriver()).getCapabilities();
//			scenario.log("<strong>Executed PlatformName() : </strong>" + caps.getPlatformName());
		}
				
		if(DBManager.getData("Type").equals("Mobile")) {
			//((JavascriptExecutor)MobileDriverManager.getDriver()).executeScript("sauce:job-result=",scenario.isFailed()?"Failed":"Passed");			
		}
	}

	@Before(order = 2)
	public void jira(Scenario scenario)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		/*
		 * //To collect scenario content from feature
		 * 
		 * TestCase r = (TestCase)
		 * scenario.getClass().getDeclaredField("testCase").get(scenario);
		 * 
		 * 
		 * 
		 * List<PickleStepTestStep> stepDefs = r.getTestSteps() .stream() .filter(x -> x
		 * instanceof PickleStepTestStep) .map(x -> (PickleStepTestStep) x)
		 * .collect(Collectors.toList());
		 * 
		 * for(PickleStepTestStep p : stepDefs){ System.out.println(p.getStep()); }
		 */}

}
