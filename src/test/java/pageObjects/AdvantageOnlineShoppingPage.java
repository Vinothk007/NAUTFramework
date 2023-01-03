package pageObjects;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.action.util.WebAction;
import com.drivers.web.WebDriverManager;
import com.framework.utils.ExceptionHandler;
import com.testdata.DBManager;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
public class AdvantageOnlineShoppingPage {
	
	public static String advUsername = "usernameRegisterPage";
	public static String advPassword = "passwordRegisterPage";
	public static String advEmail = "emailRegisterPage";
	public static String advConfirmPass = "confirm_passwordRegisterPage";
	public static String advFirstName = "first_nameRegisterPage";
	public static String advLastName = "last_nameRegisterPage";
	public static String advPhoneNumber = "phone_numberRegisterPage";
	public static String advCity = "cityRegisterPage";
	public static String advAddress = "addressRegisterPage";
	public static String advState = "state_/_province_/_regionRegisterPage";
	public static String advPostalCode = "postal_codeRegisterPage";
	public static String createnewaccountbutton="//*[text()='CREATE NEW ACCOUNT']";
	//countryListboxRegisterPage
	
	@Given("^I am on the AdvantageOnline login page$")
    public void i_am_on_the_advantageonline_login_page() throws Throwable {
		WebAction.openURL(DBManager.getData("URL"));
		
		//WebAction.pause(6000);
		 
		//ExceptionHandler.handleException(new Exception("this is custom error"));
		//WebAction.highlightElement(null, null);
    }
	@Given("^Verify page loaded successfully$")
    public void homePageLoading() throws Throwable {
		
		WebAction.waitforVisible("//*[@id='speakersTxt']");
		//Assert.fail("page load issue simulated");
    }
	
	@Given("^Wait for page loaded successfully$")
    public void waitPageLoading() throws Throwable {
		
		WebAction.waitforVisiblilty("//*[@id='speakersTxt']");
		
    }
	
	
	
	@When("^I click on Createnewuser by clicking on user$")
    public void i_click_on_createnewuser_by_clicking_on_user() throws Throwable {
		WebAction.waitForPageLoad();
		WebAction.click("id", "menuUserLink");
        WebAction.pause(4000);
        WebAction.click("xpath", createnewaccountbutton);
    }
	
    @When("^I enter \"([^\"]*)\" in the Adv USername field$")
    public void i_enter_something_in_the_adv_username_field(String UserName) throws Throwable {
    	WebAction.waitForPageLoad();
        WebAction.isPresent("linktext", "CREATE ACCOUNT");
        WebAction.pause(5000);
    	WebAction.sendKeys("name", advUsername, DBManager.getData(UserName));
    }

    @And("^I enter \"([^\"]*)\" in the Adv password field$")
    public void i_enter_something_in_the_adv_password_field(String Password) throws Throwable {
    	WebAction.sendKeys("name", advPassword, DBManager.getData(Password));
    }

    @And("^I enter \"([^\"]*)\" in the Adv email field$")
    public void i_enter_something_in_the_adv_email_field(String Email) throws Throwable {
    	WebAction.sendKeys("name", advEmail, DBManager.getData(Email));
    }

    @And("^I enter \"([^\"]*)\" in the confirm password field$")
    public void i_enter_something_in_the_confirm_password_field(String ConfirmPassword) throws Throwable {
    	WebAction.sendKeys("name", advConfirmPass, DBManager.getData(ConfirmPassword));
    }

    @And("^I enter \"([^\"]*)\" in the FirstName field$")
    public void i_enter_something_in_the_firstname_field(String FirstName) throws Throwable {
    	WebAction.sendKeys("name", advFirstName, DBManager.getData(FirstName));
    }

    @And("^I enter \"([^\"]*)\" in the LastName field$")
    public void i_enter_something_in_the_lastname_field(String LastName) throws Throwable {
    	WebAction.sendKeys("name", advLastName, DBManager.getData(LastName));
    	
    }

    @And("^I enter \"([^\"]*)\" in the PhoneNumber field$")
    public void i_enter_something_in_the_phonenumber_field(String PhoneNumber) throws Throwable {
    	WebAction.sendKeys("name", advPhoneNumber, DBManager.getData(PhoneNumber));
    }

    @And("^I set \"([^\"]*)\" from the Country dropdown field$")
    public void i_select_something_from_the_country_dropdown_field(String strArg1) throws Throwable {
    	System.out.println("hi");
    	//WebDriver driver = null;
    	//Select se = new Select (WebAction.getElement(driver, "name", "countryListboxRegisterPage"));
    	//WebAction.selectByVisibleText(se,strArg1);
    }

    @And("^I enter \"([^\"]*)\" in the City field$")
    public void i_enter_something_in_the_city_field(String City) throws Throwable {
    	WebAction.sendKeys("name", advCity, DBManager.getData(City));
    	WebAction.pause(2000);
    }

    @And("^I enter \"([^\"]*)\" in the Address field$")
    public void i_enter_something_in_the_address_field(String Address) throws Throwable {
    	WebAction.sendKeys("name", advAddress, DBManager.getData(Address));
    }

    @And("^I enter \"([^\"]*)\" in the State field$")
    public void i_enter_something_in_the_state_field(String State) throws Throwable {
    	WebAction.sendKeys("name", advState, DBManager.getData(State));
    }

    @And("^I enter \"([^\"]*)\" in the PostalCode field$")
    public void i_enter_something_in_the_postalcode_field(String PostalCode) throws Throwable {
    	WebAction.sendKeys("name", advPostalCode, DBManager.getData(PostalCode));
    }

    @And("^I click on I Agree checkbox$")
    public void i_click_on_i_agree_checkbox() throws Throwable {
    	WebAction.click("name", "i_agree");
    	WebAction.pause(2000);
    }

    @And("^I press Register button$")
    public void i_press_register_button() throws Throwable {
    	WebAction.click("id", "register_btnundefined");
    	WebAction.pause(2000);
    }

	
    @Then("^I should see my UserName \"([^\"]*)\" in user title$")
    public void i_should_see_my_username_something_in_user_title(String UserName) throws Throwable {
    	WebAction.pause(2000);
    	String actulUserName=WebAction.getText("id", "menuUserLink");
        System.out.println("Actual Name:: "+actulUserName);
        Assert.assertEquals(actulUserName,DBManager.getData(UserName));
    }
    
    @And("^I logout from the AdvantageOnline page$")
    public void i_logout_from_the_advantageonline_page() throws Throwable {
    	WebAction.pause(2000);
    	WebAction.click("id", "menuUserLink");
    	WebAction.click("xpath", "//div[@id='loginMiniTitle']/label[3]");
    }
	
    @And("^I should see \"([^\"]*)\" message$")
    public void i_can_see_already_register_user_message(String message) throws Throwable {
    	WebAction.pause(2000);
        WebAction.isPresent("xpath", "//*[contains(text(),'"+message+"')]");
    }
    
    @Then("^I close the browser$")
    public void i_close_the_browser() throws Throwable {
        WebAction.closeBrowser();
    }


    @When("^I click ContactUS link$")
    public void i_click_contactus_link() throws Throwable {
    	WebAction.pause(8000);
    	WebAction.click("linktext", "CONTACT US");
    }

    @And("^I set \"([^\"]*)\" from category drop down$")
    public void i_set_something_from_category_drop_down(String Gadgets) throws Throwable {
    	WebAction.pause(4000);
    	WebElement element = WebAction.getElement(WebDriverManager.getDriver(), "xpath", "//select[@name='categoryListboxContactUs']");
    	Select categoryList = new Select(element);
    	WebAction.selectByVisibleText(categoryList, DBManager.getData(Gadgets));
    }

    @And("^I set \"([^\"]*)\" from sub catagory drop down$")
    public void i_set_something_from_sub_catagory_drop_down(String TypeofGadegates) throws Throwable {
    	WebAction.pause(4000);
    	WebElement element1 = WebAction.getElement(WebDriverManager.getDriver(), "xpath", "//select[@name='productListboxContactUs']");
    	Select subcategoryList = new Select(element1);
    	WebAction.selectByVisibleText(subcategoryList, DBManager.getData(TypeofGadegates));
    }
    @And("^I enter \"([^\"]*)\" address$")
    public void i_enter_something_address(String Email) throws Throwable {
    	WebAction.sendKeys("name", "emailContactUs", DBManager.getData(Email));
    }
    @And("^I enter cc \"([^\"]*)\" address$")
    public void i_enter_something_ccMail(String Email) throws Throwable {

    	String data="cc mail";
    //	Assert.assertEquals(data, "cd mail");
    	WebAction.sendKeys("name", "cc mail", DBManager.getData(Email));
    }

    @And("^I enter \"([^\"]*)\" details$")
    public void i_enter_something_details(String Subject) throws Throwable {
    	WebAction.sendKeys("name", "subjectTextareaContactUs", DBManager.getData(Subject));
    }

    @And("^I press send button$")
    public void i_press_send_button() throws Throwable {
    	WebAction.click("id", "send_btnundefined");
    }

    @And("^I validate Success message$")
    public void i_validate_success_message() throws Throwable {
    	WebAction.pause(4000);
    	String actulMessage=WebAction.getText("xpath", "//p[contains(.,'Thank you for contacting Advantage support.')]");
    	System.out.println("Actual Name:: "+actulMessage);
    	Assert.assertEquals(actulMessage,"Thank you for contacting Advantage support.");
    }
    
    @When("^I click on \"([^\"]*)\"$")
    public void i_set_something(String Product) throws Throwable {
    	WebAction.pause(4000);
    	WebAction.click("id", DBManager.getData(Product));
    }

    @Then("^I click on BuyNow$")
    public void i_click_on_buynow() throws Throwable {
    	WebAction.pause(8000);
    	WebAction.click("name", "buy_now");
    }

    @And("^I press second radio button for color$")
    public void i_press_second_radio_button_for_color() throws Throwable {
    	WebAction.pause(8000);
    	WebAction.click("xpath","//div[@id='productProperties']/div/div/span[2]");
    	
    }

    @And("^I add \"([^\"]*)\"$")
    public void i_add_something(String Quantity) throws Throwable {
    	int val = Integer.parseInt(DBManager.getData(Quantity));
    	System.out.println("value is::"+val);
    	for (int i=1 ; i<=val ; i++)
    	{
    		System.out.println("hi");
    		WebAction.click("css",".plus");
    	}
    }

    @And("^I click on AddToCart$")
    public void i_click_on_addtocart() throws Throwable {
    	WebAction.click("name","save_to_cart");
    }

    @And("^I click on shoppingCart$")
    public void i_click_on_shoppingcart() throws Throwable {
    	WebDriverWait wait = new WebDriverWait(WebDriverManager.getDriver(), 20);
    	WebElement element = wait.until(
    	        ExpectedConditions.visibilityOfElementLocated(By.id("shoppingCartLink")));
    	WebAction.click("id","shoppingCartLink");
    }

    @And("^I click on Remove$")
    public void i_click_on_remove() throws Throwable {
    	WebAction.pause(4000);
 		WebAction.click("linktext","REMOVE");
    }

    @And("^I navigate back to home page$")
    public void i_navigate_back_to_home_page() throws Throwable {
    	WebAction.pause(4000);
    	WebAction.click("linktext","HOME");
    }
    
    @When("^I verify if \"([^\"]*)\" present in cart$")
    public void verifyifpresentincart(String Product) throws Throwable {
    	WebAction.pause(4000);
    	
    	WebAction.isPresent("xpath", "//*[contains(text(),'"+Product+"')]");
    	
    	
    }
  
    
    
    
}
