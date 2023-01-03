
Feature: Validate ContactUs send mail functionality
   
    @UI  @Smoke
  Scenario: Validate ContactUs functionality by triggering mail
  	Given user launches the browser
  
    Given I am on the AdvantageOnline login page  
    Then Wait for page loaded successfully   
    When I click ContactUS link
    And I set "Gadgets" from category drop down
    And I set "TypeofGadegates" from sub catagory drop down
    And I enter "Email" address
    And I enter "Subject" details
    And I press send button
    And I validate Success message    
    Then I close the browser
    
    
  