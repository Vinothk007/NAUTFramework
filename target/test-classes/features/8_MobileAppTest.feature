@Mobile
Feature: Native Mobile App - Checkout option in Ecommerce application
  User should able to buy product from Ecommerce application

  @ProductSelection
  Scenario: Select the product to buy and add it to the cart
    Given I am on the Ecommerce MobileApp Home page
    When I select product under prduct list
    And I click on Add to cart button
    Then Selected product should be added in the Cart
    When I click on Cart menu icon
    And click on Checkout under prduct list
    Then Application login screent should be displayed    

  @Login
  Scenario: Login to the mobile application
    When I enter "UserName" in the user name input field
    And I enter "Password" in the password input field
    And I press App Login button
    Then the app should be redirected to User delivery Address page

  @UpdateUserAddressDetails
  Scenario: Update user address details
    When I enter "Fullname" in the name input field
    And I enter "AddressLine1" in the Address Line1 input field
    And I enter "AddressLine2" in the Address Line2 input field
    And I enter "City" in the city input field
    And I enter "State" in the state input field
    And I enter "ZipCode" in the Zip code input field
    And I enter "Country" in the Country input field
    And I click on Payment button
    Then Payment details page should be displayed

  @UpdateUserPaymentDetails
  Scenario: Update user mode of payment details
    When I enter "Fullname" in the name input field
    And I enter "CardNumber" in the Card Number input field
    And I enter "SecurityCode" in the Security Code input field
    And I enter "ExpirationDate" in the Expiration Date input field    
    And I click on ReviewOrder button
    Then Place Order page should be displayed

  @PlaceOrder
  Scenario: Review product details and place order
    When I click on PlaceOrder button
    Then Checkout completion page should be displayed with success message

  @Logout
  Scenario: Logout from mobile application
    When I click on logout button from application menu icon
    And I click on Logout button in confirmation popup
    And I click on Ok button
    Then Application login screent should be displayed
    #And user closes the MobileApp
