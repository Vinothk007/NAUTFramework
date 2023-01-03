package cucumberExecutor;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.drivers.mobile.MobileDriverManager;
import com.drivers.web.WebDriverManager;
import com.framework.utils.CalendarUtils;
import com.framework.utils.ExceptionHandler;
import com.framework.utils.GlobalVariables;
import com.framework.utils.LoggerHelper;
import com.framework.utils.Reporter;
import com.jira.util.JiraServiceUtility;
import com.testdata.DBManager;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.Result;
import retryAnalyser.RetryFailedTests;

public class Hooks {

	private Logger log = LoggerHelper.getLogger(Hooks.class);
	private String env = null;

	@Before
	public void setup(Scenario scenario) throws InterruptedException {
		log.debug("Current Scenario Running: " + scenario.getName());

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
			env = GlobalVariables.currentBrowser.get();			
		} else {
			scenario.log("<strong>Executed Environment : </strong>" + GlobalVariables.currentMOS.get());
			env = GlobalVariables.currentMOS.get();
		}

		// added by sathiyaraj
		LinkedList<String> tags = new LinkedList<String>(sourceTagNames);
		for (String tagName : sourceTagNames) {
			String tagnameLower = tagName.toLowerCase();
			if (tagnameLower.contains("smoke")) {
				GlobalVariables.currentTagLabel.set(tagnameLower);
				break;
			} else if (tagnameLower.contains("regression")) {
				GlobalVariables.currentTagLabel.set(tagnameLower);
				break;
			} else if (tagnameLower.contains("sanity")) {
				GlobalVariables.currentTagLabel.set(tagnameLower);
				break;
			} else if (tagnameLower.contains("e2e") || tagnameLower.contains("end2end")
					|| tagnameLower.contains("endtoend")) {
				GlobalVariables.currentTagLabel.set(tagnameLower);
				break;
			}
		}

		try {
			GlobalVariables.currentTagLabel.get().isEmpty();
		} catch (Exception e) {
			GlobalVariables.currentTagLabel.set(tags.get(0));
		}

	}

	@After
	public void teardown(Scenario scenario) {
		String screenshotPath = null;
		// added by sathiyaraj
		if (scenario.isFailed() && !DBManager.getData("Type").equals("api")) {
			try {
				Reporter.addScreenCapture();
				screenshotPath = Reporter.getScreenshotPath();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (GlobalVariables.configProp.getProperty("LogDefect").equalsIgnoreCase("Yes"))// added by sathiyaraj
			{
				// added by sathiyaraj---------------
				int currentRetryCount = 0, maxRetryCount = 0;
				try {
					currentRetryCount = GlobalVariables.currentRetryCount.get();
					maxRetryCount = GlobalVariables.maxRetryCount.get();
				} catch (Exception e) {
					RetryFailedTests r = new RetryFailedTests();
					currentRetryCount = r.getCurrentRetryCount();
					maxRetryCount = Integer.parseInt(GlobalVariables.configProp.getProperty("retryCount"));

				}

				if (currentRetryCount == maxRetryCount) {

					Throwable e = ExceptionHandler.TException.get();

					if (e == null)
						e = getFailureMessage(scenario);

					String errorDescription = e.getMessage().split("\n")[0];

					errorDescription = errorDescription.replace("\"", "");
					errorDescription = errorDescription.replace("{", "[");
					errorDescription = errorDescription.replace("}", "]");

					errorDescription = errorDescription.replace("element", "UI Field/Item");

					String summary = GlobalVariables.currentFeature.get() + " "
							+ CalendarUtils.getTimeStamp("MM/dd/yyyy HH:mm:ss");
					String description = scenario.getName();
					String expected = GlobalVariables.currentStep.get().replace("\"", "");
					String actual = expected + " ~ step is failed with error!!! " + errorDescription;
					String testCaseName = DBManager.getData("testCaseName");
					String label = GlobalVariables.currentTagLabel.get();
					String attachment = screenshotPath;
					String comment = "created by test automation";

					JiraServiceUtility j = null;

					try {
						j = new JiraServiceUtility();
						j.logDefect(summary, description, expected, actual, testCaseName, label, attachment, comment,
								env, e);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (j.issueKey != null) {
						GlobalVariables.defectList.put(j.issueKey, summary + "#" + description);
						scenario.log("<strong>Defect Created : </strong>" + j.issueKey);
					}

				}

			}

			if (!GlobalVariables.appType.contains(GlobalVariables.currentMOS.get())) {
				WebDriverManager.quitDriver();
			} else {
				MobileDriverManager.quitDriver();
			}
		}
		if (DBManager.getData("Type").equals("UI")) {
			Capabilities caps = ((RemoteWebDriver) WebDriverManager.getDriver()).getCapabilities();			
		}

		if (DBManager.getData("Type").equals("Mobile")) {
			// ((JavascriptExecutor)MobileDriverManager.getDriver()).executeScript("sauce:job-result=",scenario.isFailed()?"Failed":"Passed");
		}
	}

	public Throwable getFailureMessage(Scenario scenario) {
		Result failResult = null;

		try {
			// Get the delegate from the scenario
			Field delegate = scenario.getClass().getDeclaredField("delegate");
			delegate.setAccessible(true);
			TestCaseState testCaseState = (TestCaseState) delegate.get(scenario);

			// Get the test case results from the delegate
			Field stepResults = testCaseState.getClass().getDeclaredField("stepResults");
			stepResults.setAccessible(true);
			List<Result> results = (List<Result>) stepResults.get(testCaseState);

			for (Result result : results) {
				if (result.getStatus().name().equalsIgnoreCase("FAILED")) {
					failResult = result;
				}
			}
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		Throwable e = failResult.getError();

		return e;
	}

}
