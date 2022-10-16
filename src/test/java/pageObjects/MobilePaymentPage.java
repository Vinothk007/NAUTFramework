package pageObjects;

import com.action.util.MobileAction;
import com.framework.utils.GlobalVariables;
import com.testdata.DBManager;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MobilePaymentPage {
	
	public static String cardNumber = "//*[@content-desc='Card Number* input field']";
	public static String expDate = "//*[@content-desc='Expiration Date* input field']";
	public static String secCode = "//*[@content-desc='Security Code* input field']";
	public static String reviewOrder = "//*[@text='Review Order']";
	public static String placeOrder = "//*[@text='Place Order']";
	public static String checkoutCompletion = "//*[@content-desc='checkout complete screen']";
	
	public static String cardNumberIOS = "//*[@name='Card Number* input field']";
	public static String expDateIOS = "//*[@name='Expiration Date* input field']";
	public static String secCodeIOS = "//*[@name='Security Code* input field']";
	public static String reviewOrderIOS = "//*[@name='Review Order button']";
	public static String placeOrderIOS = "//*[@name='Place Order button']";
	public static String checkoutCompletionIOS = "//*[@name='checkout complete screen']";
	
		
	@Then("Payment details page should be displayed")
	public void payment_details_page_should_be_displayed() {
		MobileAction.isPresent("xpath",
				GlobalVariables.currentMOS.get().contains("android") ? reviewOrder : reviewOrderIOS);
	}

	@When("I enter {string} in the Card Number input field")
	public void i_enter_in_the_card_number_input_field(String cardNumberInput) {
		MobileAction.sendKeys("xpath", GlobalVariables.currentMOS.get().contains("android") ? cardNumber : cardNumberIOS,
				DBManager.getData(cardNumberInput));
	}

	@When("I enter {string} in the Expiration Date input field")
	public void i_enter_in_the_expiration_date_input_field(String expDateInput) {
		MobileAction.sendKeys("xpath", GlobalVariables.currentMOS.get().contains("android") ? expDate : expDateIOS,
				DBManager.getData(expDateInput));
	}

	@When("I enter {string} in the Security Code input field")
	public void i_enter_in_the_security_code_input_field(String secCodeInput) {
		MobileAction.sendKeys("xpath", GlobalVariables.currentMOS.get().contains("android") ? secCode : secCodeIOS,
				DBManager.getData(secCodeInput));
	}

	@When("I click on ReviewOrder button")
	public void i_click_on_review_order_button() {
		if(GlobalVariables.currentMOS.get().contains("ios")) {
			MobileAction.hideKeys();
			MobileAction.click("xpath", GlobalVariables.currentMOS.get().contains("android") ? reviewOrder : reviewOrderIOS);}		
		MobileAction.click("xpath", GlobalVariables.currentMOS.get().contains("android") ? reviewOrder : reviewOrderIOS);		
	}

	@Then("Place Order page should be displayed")
	public void place_order_page_should_be_displayed() {
		MobileAction.isPresent("xpath",
				GlobalVariables.currentMOS.get().contains("android") ? placeOrder : placeOrderIOS);
	}

	@When("I click on PlaceOrder button")
	public void i_click_on_place_order_button() {
		if(GlobalVariables.currentMOS.get().contains("ios")) {
			MobileAction.hideKeys();}
		MobileAction.click("xpath", GlobalVariables.currentMOS.get().contains("android") ? placeOrder : placeOrderIOS);
	}

	@Then("Checkout completion page should be displayed with success message")
	public void checkout_completion_page_should_be_displayed_with_success_message() {
		MobileAction.isPresent("xpath",
				GlobalVariables.currentMOS.get().contains("android") ? checkoutCompletion : checkoutCompletionIOS);
	}
}
