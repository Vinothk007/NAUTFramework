@Mobile
Feature: Native Mobile App - Negative Test
  User should not able to buy product from Ecommerce application providing invalid credentials

  @Login
  Scenario: Login to the mobile application2
    When I enter "UserName" in the user name input field
    And I enter "Password" in the password input field
    And I press App Login button
    Then the app should be redirected to User delivery Address page

 