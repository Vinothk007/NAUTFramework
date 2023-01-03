
Feature: Already user registered message validation
   
  
   @UI @Smoke
  Scenario:  Already user registered message validation
  	Given user launches the browser
  
    Given I am on the AdvantageOnline login page     
    #When Verify page loaded successfully
    Then Wait for page loaded successfully
    When I click on Createnewuser by clicking on user
    When I enter "UserName" in the Adv USername field
    And I enter "Password" in the Adv password field
    And I enter "Email" in the Adv email field
    And I enter "ConfirmPassword" in the confirm password field
    And I enter "FirstName" in the FirstName field
    And I enter "LastName" in the LastName field
    And I enter "PhoneNumber" in the PhoneNumber field
    And I set "Country" from the Country dropdown field
    And I enter "City" in the City field
    And I enter "Address" in the Address field
    And I enter "State" in the State field
    And I enter "PostalCode" in the PostalCode field
    
    And I click on I Agree checkbox
    And I press Register button
    
    And I should see "User name is already registered" message
    Then I close the browser