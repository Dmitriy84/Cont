Feature: Activate configuration wizard - Step 2

  Background:
    Given login to Continuum
    And endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/products'
    And with parameters
      | key       | value    |
      | partnerId | 0        |
      | clientId  | 50111123 |
    And request JSON body is
    """
    { "products": [ { "id": "07bf62a1-1235-11e8-baf9-02170cb549b8", "enabled": false, "data": { } },
     { "id": "27f2ae1c-ab8f-11e8-9d84-0ae0fc1ff2aa", "enabled": false, "data": { } },
     { "id": "e4ce5333-1234-11e8-9f18-0ae0fc1ff2aa", "enabled": false, "data": { } }] }
    """
    And PUT request was send
    And response status code is 200
    And logout from Continuum
    And login in as default user
    And open "Security Solution" page
    And select option 'Activate and Configure Offerings' from "Configure" dropdown
    And "Select client site" step of wizard is opened
    And select client's site 'Test site 1' on the "Select client site" step
    And click button with name 'Continue' in footer
    And "Activate solutions" step of wizard is opened

  @UI
  Scenario: UI: Check Office 365 Integration product tab -C4203524,C4329623
    When select product OFFICE365 on "Activate solution" step
    And click 'Learn more in doc center' link
    Then wait quantity of opened tabs to be 2
    When switch to tab with index 1
    Then current page URL should contain 'https://doccenter.itsupport247.net/#Security_Office_365.htm'
    When change status of product to DISABLED
    Then state of product OFFICE365 should be DISABLED
    And Login to Office 365 button should be VISIBLE
    And Login to Office 365 button should be DISABLED
    When change status of product to ENABLED
    Then state of product OFFICE365 should be ENABLED
    And Login to Office 365 button should be VISIBLE
    And Login to Office 365 button should be ENABLED
    When click 'Go to Office 365' button
    And login to Office 365 with email 'hardik@gaware.onmicrosoft.com' and password 'Cont@123'
    Then message in "Login to Office 365" block should be 'Log in with Office 365 credentials. Log in with an Office 365 account has been completed.'
    And Login to Office 365 button should be INVISIBLE
    When click button with name 'Back' in footer
    And select client's site 'Test site 2' on the "Select client site" step
    And click button with name 'Continue' in footer
    And "Activate solutions" step of wizard is opened
    And select product OFFICE365 on "Activate solution" step
    Then state of product OFFICE365 should be DISABLED
    And Login to Office 365 button should be VISIBLE
    And Login to Office 365 button should be DISABLED


  @UI
  Scenario: UI: Check default state of products on the second step of "Activate and configure solutions" popup is  -C4059145
    Then selected product on "Activate Solutions" step should be PROFILE
    And buttons "Back, Cancel, Finish" in footer should be VISIBLE
    And buttons "Back, Cancel, Finish" in footer should be ENABLED
    And buttons "Back, Cancel" in footer should have color WHITE
    And button "Finish" in footer should have color DARK_BLUE
    And buttons "Close, Continue" in footer should be INVISIBLE
    And order of products on "Activate Solutions" step should be PROFILE, OFFICE365
    And active step in Progress Tracker should be ACTIVATE_SOLUTIONS

  @UI
  Scenario: UI: Proceed to the third step of "Activate and configure solutions" popup is  with active 'Office 365 Integration' product -C4059146
    When select product OFFICE365 on "Activate solution" step
    And change status of product to ENABLED
    And click 'Go to Office 365' button
    And login to Office 365 with email 'hardik@gaware.onmicrosoft.com' and password 'Cont@123'
    And click button with name 'Finish' in footer
    Then "Get Started" step of wizard is opened

  @UI
  Scenario: UI: Error on completing second step with active O365 product without login into it -C4355163
    When select product OFFICE365 on "Activate solution" step
    And change status of product to ENABLED
    And click button with name 'Finish' in footer
    Then error message on product's tab with text 'Office 365 can\'t be activated without authorization. Log in to valid Microsoft Account.' should be VISIBLE
    And OFFICE365 product's tab should be highlighted in RED
    When change status of product to ENABLED
    And click 'Go to Office 365' button
    And login to Office 365 with email 'hardik@gaware.onmicrosoft.com' and password 'Cont@123'
    Then error message on product's tab with text 'Office 365 can\'t be activated without authorization. Log in to valid Microsoft Account.' should be INVISIBLE
    And message in "Login to Office 365" block should be 'Log in with Office 365 credentials. Log in with an Office 365 account has been completed.'


  @UI
  Scenario: UI: Proceed to the third step of "Activate and configure solutions" popup is  with inactive 'Office 365 Integration' product -C4059147
    When select product OFFICE365 on "Activate solution" step
    And change status of product to DISABLED
    And click button with name 'Finish' in footer
    Then "Get Started" step of wizard is opened


  @UI
  Scenario: UI: Using 'Back' and 'Continue' buttons transfers user between 1st and 2nd steps -C4059148
    When select product PROFILE on "Activate solution" step
    And get current status of selected product
    And change status of product to opposite
    And click button with name 'Back' in footer
    Then "Select client site" step of wizard is opened
    When click button with name 'Continue' in footer
    And "Activate solutions" step of wizard is opened
    And select product PROFILE on "Activate solution" step
    Then status of currently selected product should be as it was previously

  @UI
  Scenario: UI: Integration: Failed login to MS O365 -C4204120
    When select product OFFICE365 on "Activate solution" step
    And change status of product to ENABLED
    And click 'Go to Office 365' button
    And login to Office 365 with invalid email 'hardik@gaware.onmicrosoft.com' and password 'Cont@12'
    Then message in "Login to Office 365" block should be 'Link site to an Office 365 account'
    And Login to Office 365 button should be VISIBLE
    And Login to Office 365 button should be ENABLED

  @UI
  Scenario Outline: UI: Check P&P product after activating it through wizard -C4358056,C4358057
    When select product PROFILE on "Activate solution" step
    And change status of product to <status>
    And click button with name 'Finish' in footer
    And "Get Started" step of wizard is opened
    And open page from "Continuum" portal with url 'qadashb/QuickAccess/NewDesktops'
    Then "Profile and Protect" column should be <visibility> for site 'Test site 1' on "Devices" page

    Examples:
      | status   | visibility |
      | ENABLED  | VISIBLE    |
      | DISABLED | INVISIBLE  |


