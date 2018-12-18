Feature: Activate solutions wizard - full flow

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
    And login in as default user
    And open "Security Solution" page
    And select option 'Activate and Configure Offerings' from "Configure" dropdown
    And "Select client site" step of wizard is opened
    And select client's site "Test site 1" on the "Select client site" step
    And click button with name 'Continue' in footer
    And "Activate solutions" step of wizard is opened

  @UI
  Scenario: UI: Check for changes in the saved products status (enable) -C4196976
    When select product PROFILE on "Activate solution" step
    And change status of product to ENABLED
#    When select product DETECT on "Activate solution" step
#    And change status of product to ENABLED
    When select product OFFICE365 on "Activate solution" step
    And change status of product to ENABLED
    And  click 'Go to Office 365' button
    And login to Office 365 with email 'hardik@gaware.onmicrosoft.com' and password 'Cont@123'
    And message in "Login to Office 365" block should be 'Log in with Office 365 credentials. Log in with an Office 365 account has been completed.'
    And click button with name 'Finish' in footer
    And "Get Started" step of wizard is opened
    And click button with name 'Close' in footer
    And "Activate and configure solutions" popup is INVISIBLE
    And select option 'Activate and Configure Offerings' from "Configure" dropdown
    And "Select client site" step of wizard is opened
    And select client's site "Test site 1" on the "Select client site" step
    And click button with name 'Continue' in footer
    And "Activate solutions" step of wizard is opened
    Then state of product PROFILE should be ENABLED
    And state of product OFFICE365 should be ENABLED
#    And state of product DETECT should be ENABLED


  @UI
  Scenario: UI: Check for changes in the saved products status (disable) -C4355162
    When select product PROFILE on "Activate solution" step
    And change status of product to DISABLED
#    When select product DETECT on "Activate solution" step
#    And change status of product to DISABLED
    When select product OFFICE365 on "Activate solution" step
    And change status of product to DISABLED
    And click button with name 'Finish' in footer
    And "Get Started" step of wizard is opened
    And click button with name 'Close' in footer
    And "Activate and configure solutions" popup is INVISIBLE
    And select option 'Activate and Configure Offerings' from "Configure" dropdown
    And "Select client site" step of wizard is opened
    And select client's site "Test site 1" on the "Select client site" step
    And click button with name 'Continue' in footer
    And "Activate solutions" step of wizard is opened
    Then state of product PROFILE should be DISABLED
    And state of product OFFICE365 should be DISABLED
#    And state of product DETECT should be DISABLED

