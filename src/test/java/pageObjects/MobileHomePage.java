package pageObjects;

import com.action.util.MobileAction;
import com.framework.utils.GlobalVariables;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MobileHomePage {

	public static String menu = "//*[@content-desc='open menu']";
	public static String logout = "//*[@content-desc='menu item log out']";
	
	public static String product = "//*[@text='Sauce Labs Backpack']";
	public static String addToChartBtn = "//*[@content-desc='Add To Cart button']";
	public static String cartIcon = "//*[@content-desc='cart badge']";
	public static String checkout = "//*[@text='Proceed To Checkout']";
		
	public static String logoutPopupBtn = "//*[@text='LOG OUT']";
	public static String okPopupBtn = "//*[@text='OK']";
	
	public static String menuIOS = "//*[@name='tab bar option menu']";
	public static String logoutIOS = "//*[@name='menu item log out']";
	
	public static String productIOS = "//*[@name='Sauce Labs Backpack']";
	public static String addToChartBtnIOS = "//*[@name='Add To Cart button']";
	public static String cartIconIOS = "//*[@name='tab bar option cart']";
	public static String checkoutIOS = "//*[@name='Proceed To Checkout button']";
		
	public static String logoutPopupBtnIOS = "//XCUIElementTypeButton[@name='Log Out']";
	public static String okPopupBtnIOS = "//*[@name='OK']";
	
	
	@When("I click on logout button from application menu icon")
	public void i_click_on_logout_button_from_application_menu_icon() {
		MobileAction.click("xpath", GlobalVariables.currentMOS.get().contains("android") ? menu : menuIOS);
		MobileAction.click("xpath", GlobalVariables.currentMOS.get().contains("android") ? logout : logoutIOS);
	}

	@When("I click on Logout button in confirmation popup")
	public void i_click_on_logout_button_in_confiration_popup() {
		MobileAction.click("xpath",
				GlobalVariables.currentMOS.get().contains("android") ? logoutPopupBtn : logoutPopupBtnIOS);
	}

	@When("I click on Ok button")
	public void i_click_on_ok_button() {
		MobileAction.click("xpath", GlobalVariables.currentMOS.get().contains("android") ? okPopupBtn : okPopupBtnIOS);
	}

	@When("I select product under prduct list")
	public void i_select_product_under_prduct_list() {
		MobileAction.click("xpath", GlobalVariables.currentMOS.get().contains("android") ? product : productIOS);
	}

	@When("I click on Add to cart button")
	public void i_click_on_add_to_cart_button() {
		MobileAction.click("xpath",
				GlobalVariables.currentMOS.get().contains("android") ? addToChartBtn : addToChartBtnIOS);
	}

	@Then("Selected product should be added in the Cart")
	public void selected_product_should_be_added_in_the_cart() {
		// validation yet to be added
	}

	@When("I click on Cart menu icon")
	public void i_click_on_cart_menu_icon() {
		MobileAction.click("xpath", GlobalVariables.currentMOS.get().contains("android") ? cartIcon : cartIconIOS);
	}

	@When("click on Checkout under prduct list")
	public void click_on_checkout_under_prduct_list() {
		MobileAction.click("xpath", GlobalVariables.currentMOS.get().contains("android") ? checkout : checkoutIOS);
	}
	
}
