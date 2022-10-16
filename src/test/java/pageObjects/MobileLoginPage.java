package pageObjects;

import com.action.util.MobileAction;
import com.drivers.mobile.MobileDriverManager;
import com.framework.utils.GlobalVariables;
import com.testdata.DBManager;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MobileLoginPage {
	
	public static String txtUsername = "//*[@content-desc='Username input field']";
	public static String txtPassword = "//*[@content-desc='Password input field']";
	public static String loginBtn = "//*[@content-desc='Login button']";
	
	public static String txtUsernameIOS = "//*[@name='Username input field']";
	public static String txtPasswordIOS = "//*[@name='Password input field']";
	public static String loginBtnIOS = "//*[@name='Login button']";
	
	@Given("^I am on the Ecommerce MobileApp Home page$")
	public void i_am_on_the_Mobileapp_login_page() {
		// have to pass app id
	}

	@Then("Application login screent should be displayed")
	public void application_login_screent_should_be_displayed() {
		MobileAction.isPresent("xpath", GlobalVariables.currentMOS.get().contains("android") ? loginBtn : loginBtnIOS);
	}

	@When("I enter {string} in the user name input field")
	public void i_enter_in_the_user_name_input_field(String username) {
		MobileAction.sendKeys("xpath",
				GlobalVariables.currentMOS.get().contains("android") ? txtUsername : txtUsernameIOS,
				DBManager.getData(username));
	}

	@When("I enter {string} in the password input field")
	public void i_enter_in_the_password_input_field(String password) {
		MobileAction.sendKeys("xpath",
				GlobalVariables.currentMOS.get().contains("android") ? txtPassword : txtPasswordIOS,
				DBManager.getData(password));
	}

	@When("I press App Login button")
	public void i_press_app_login_button() {
		MobileAction.click("xpath", GlobalVariables.currentMOS.get().contains("android") ? loginBtn : loginBtnIOS);
	}

	@And("^user closes the MobileApp$")
	public void user_quit_the_MobileApp() {
		MobileDriverManager.quitDriver();
	}

}
