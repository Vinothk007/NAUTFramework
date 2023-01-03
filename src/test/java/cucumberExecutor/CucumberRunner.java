package cucumberExecutor;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import com.drivers.mobile.MobileDriverManager;
import com.drivers.web.WebDriverManager;
import com.framework.utils.EWSEmailService;
import com.framework.utils.GlobalVariables;
import com.framework.utils.PropertyFileUtils;
import com.testdata.DBManager;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = { "src/test/resources" }, glue = { "pageObjects",
		"cucumberExecutor" }, dryRun = false, monochrome = false, plugin = { "pretty",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"json:target/cucumber-reports/cucumber.json",
				"timeline:target/ParallelTimeline-report" }, publish = false, tags = "@API or @UI or @Mobile")

public class CucumberRunner extends CustomAbstractTestNGCucumberTests {

	@Override
	@DataProvider(parallel = true)
	public Object[][] scenarios() {
		return super.scenarios();
	}

	@BeforeSuite
	public void initialSetUp() {
		/* Initiating configuration properties file */
		GlobalVariables.configProp = new PropertyFileUtils("config.properties");

		/* Initiating TestData & Feature file execution list */
		DBManager.InitializeDB(GlobalVariables.configProp.getProperty("DBType"));
	}

	@BeforeTest
	@Parameters("instance")
	public void setTestInstance(String instance) throws Exception {
		if (instance.equals("android") || instance.equals("ios") || instance.equals("androidEmu")
				|| instance.equals("iosSim") || instance.equals("androidReal")) {
			GlobalVariables.currentMOS.set(instance);
			MobileDriverManager.configureDriver();
		} else if (instance.equals("androidWeb") || instance.equals("iosWeb") || instance.equals("androidEmuWeb")
				|| instance.equals("iosSimWeb") || instance.equals("androidRealWeb")) {
			GlobalVariables.currentBrowser.set(instance);
			GlobalVariables.currentMOS.set(instance);
		} else if (instance.equals("api")) {
		} else{
			GlobalVariables.currentBrowser.set(instance);
			GlobalVariables.currentMOS.set(instance);
			WebDriverManager.configureDriver();
		}
	}

	@AfterSuite
	public void teardown() {
		// Open html test result - Temporary purpose
		htmlEditer f=new htmlEditer();	
		String newcontent=f.defectTableFormat(GlobalVariables.defectList);
		
		try {
			f.editHTMLReport(newcontent);
			Desktop.getDesktop().browse(
					new File(System.getProperty("user.dir") + GlobalVariables.configProp.getProperty("reportPath"))
							.toURI());
			EWSEmailService.sendeMail();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}