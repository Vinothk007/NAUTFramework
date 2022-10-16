package pageObjects;

import com.action.util.MobileAction;
import com.framework.utils.GlobalVariables;
import com.testdata.DBManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MobileAddressPage {
		
	public static String name = "//*[@content-desc='Full Name* input field']";
	public static String address1 = "//*[@content-desc='Address Line 1* input field']";
	public static String address2 = "//*[@content-desc='Address Line 2 input field']";
	public static String city = "//*[@content-desc='City* input field']";
	public static String state = "//*[@content-desc='State/Region input field']";
	public static String zip = "//*[@content-desc='Zip Code* input field']";
	public static String country = "//*[@content-desc='Country* input field']";
	public static String paymentBtn = "//*[@text='To Payment']";
	
	public static String nameIOS = "//*[@name='Full Name* input field']";
	public static String address1IOS = "//*[@name='Address Line 1* input field']";
	public static String address2IOS = "//*[@name='Address Line 2 input field']";
	public static String cityIOS = "//*[@name='City* input field']";
	public static String stateIOS = "//*[@name='State/Region input field']";
	public static String zipIOS = "//*[@name='Zip Code* input field']";
	public static String countryIOS = "//*[@name='Country* input field']";
	public static String paymentBtnIOS = "//*[@name='To Payment button']";
	
	
	@Then("the app should be redirected to User delivery Address page")
	public void the_app_should_be_redirected_to_user_delivery_address_page() {
	    MobileAction.isPresent("xpath", 
	    		GlobalVariables.currentMOS.get().contains("android") ? paymentBtn : paymentBtnIOS);
	}

	@When("I enter {string} in the name input field")
	public void i_enter_in_the_name_input_field(String inputName) {
		MobileAction.sendKeys("xpath", GlobalVariables.currentMOS.get().contains("android") ? name : nameIOS,
				DBManager.getData(inputName));
	}

	@When("I enter {string} in the Address Line1 input field")
	public void i_enter_in_the_address_line1_input_field(String inputAddress1) {
		MobileAction.sendKeys("xpath", GlobalVariables.currentMOS.get().contains("android") ? address1 : address1IOS,
				DBManager.getData(inputAddress1));
	}

	@When("I enter {string} in the Address Line2 input field")
	public void i_enter_in_the_address_line2_input_field(String inputAddress2) {
		MobileAction.sendKeys("xpath", GlobalVariables.currentMOS.get().contains("android") ? address2 : address2IOS,
				DBManager.getData(inputAddress2));
	}

	@When("I enter {string} in the city input field")
	public void i_enter_in_the_city_input_field(String cityInput) {
		MobileAction.sendKeys("xpath", GlobalVariables.currentMOS.get().contains("android") ? city : cityIOS,
				DBManager.getData(cityInput));
	}

	@When("I enter {string} in the state input field")
	public void i_enter_in_the_state_input_field(String stateInput) {
		MobileAction.sendKeys("xpath", GlobalVariables.currentMOS.get().contains("android") ? state : stateIOS,
				DBManager.getData(stateInput));
	}

	@When("I enter {string} in the Zip code input field")
	public void i_enter_in_the_zip_code_input_field(String zipInput) {
		MobileAction.sendKeys("xpath", GlobalVariables.currentMOS.get().contains("android") ? zip : zipIOS,
				DBManager.getData(zipInput));
	}

	@When("I enter {string} in the Country input field")
	public void i_enter_in_the_country_input_field(String countryInput) {		
		MobileAction.sendKeys("xpath", GlobalVariables.currentMOS.get().contains("android") ? country : countryIOS,
				DBManager.getData(countryInput));		
	}

	@When("I click on Payment button")
	public void i_click_on_payment_button() {
		if(GlobalVariables.currentMOS.get().contains("ios")) {
		MobileAction.hideKeys();}
		MobileAction.click("xpath", GlobalVariables.currentMOS.get().contains("android") ? paymentBtn : paymentBtnIOS);
	}
}
