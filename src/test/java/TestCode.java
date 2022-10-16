import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.drivers.mobile.MobileDriverManager;
import com.drivers.web.WebDriverManager;
import com.framework.utils.GlobalVariables;

public class TestCode {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws InterruptedException {
		
		/*
		 * System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+
		 * "/src/main/resources/drivers/chromedriver_103.exe"); WebDriver driver =new
		 * ChromeDriver(); driver.manage().timeouts().implicitlyWait(20,
		 * TimeUnit.SECONDS); driver.get(
		 * "https://lntinfotech-my.sharepoint.com/personal/cb_vinothkumar_lntinfotech_com/_layouts/15/onedrive.aspx"
		 * );
		 * 
		 * driver.findElement(By.xpath("//button[contains(text(),'Recordings')]")).click
		 * ();
		 * 
		 * Thread.sleep(10000);
		 * 
		 */
		System.out.println(test("androidWeb"));
		
		
	}
	public static String test(String val) {
		
		return !"androidios".contains(val) ?"Yes":"No";
		
	}

}
