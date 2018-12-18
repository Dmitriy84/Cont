Feature: Activate configuration wizard - Step 1

  Background:
    Given login in as default user
    And open "Security Solution" page
    And select option 'Activate and Configure Offerings' from "Configure" dropdown
    And "Select client site" step of wizard is opened

  @UI
  Scenario: UI: Complete first step on "Activate and configure solutions" popup is  -C4050457
    Then note on the "Select client site" step should be 'After you select the site, you\'ll be able to activate one or more security solutions for it.'
    And buttons "Continue, Cancel" in footer should be VISIBLE
    And button "Cancel" in footer should be ENABLED
    And button "Continue" in footer should be DISABLED
    And button "Continue" in footer should have color LIGHT_BLUE
    And button "Cancel" in footer should have color WHITE
    And buttons "Finish, Close, Back" in footer should be INVISIBLE
    And order of steps in Progress tracker should be SELECT_SITE, ACTIVATE_SOLUTIONS, GET_STARTED
    And  active step in Progress Tracker should be SELECT_SITE
    When select random client's site from 5 sites on the "Select client site" step
    Then button "Continue" in footer should be ENABLED
    And button "Continue" in footer should have color DARK_BLUE
    When click button with name 'Continue' in footer
    And "Activate solutions" step of wizard is opened

  @UI
  Scenario: UI: Search field on Site selector_2 -C4137837
    Then search for client's sites 'Test site 1' with expected result "Test site 1"

  @UI
  Scenario: UI: Search field on Site selector -C4137836
    Then search for client's sites 'Auto' with expected result "Automation site 1, Automation site 2"

  @UI
  Scenario: UI: Search field on Site selector (invalid data) -C4137838
    Then search for client's sites 'invalid' with expected result "no results"

  @UI
  Scenario: UI: Clear data in Search field on Site selector -C4138368
    When remember currently displayed sites' names
    And search for client's site 'Test site 1' with expected result "Test site 1"
    And clear search field on the Select Client Site element
    Then compare current sites' list with previous one
    When remember currently displayed sites' names
    And search for client's sites 'Test' with expected result "Test site 1, Test site 2"
    And clear search field on the Select Client Site element
    Then compare current sites' list with previous one

  @UI
  Scenario: UI: Close 'Activate and configure solutions' modal window -C4050458
    When click button with name 'Cancel' in footer
    Then "Activate and configure solutions" popup is INVISIBLE
    When select option 'Activate and Configure Offerings' from "Configure" dropdown
    And "Select client site" step of wizard is opened
    And close "Activate and configure solutions" popup is via close button
    Then "Activate and configure solutions" popup is INVISIBLE
    When select option 'Activate and Configure Offerings' from "Configure" dropdown
    And "Select client site" step of wizard is opened
    And click keyboard key ESCAPE
    Then "Activate and configure solutions" popup is INVISIBLE


  @UI
  Scenario: UI: Integration: Login on ITSPortal (different partners) with the different credentials. -C4192807
    When remember currently displayed sites' names
    And open page from "Continuum" portal with url 'qadashb/QuickAccess/NewDesktops'
    And click element containing text 'ALL SITES'
    Then page should contain values saved on previous step with name 'sitesNames'
    When logout from 'Continuum' portal via GUI
    And login in as user 'anna.ostrovska@continuum.net' with password 'Pass@1234'
    And open "Security Solution" page
    When select option 'Activate and Configure Offerings' from "Configure" dropdown
    And "Select client site" step of wizard is opened
    And remember currently displayed sites' names
    And open page from "Continuum" portal with url 'qadashb/QuickAccess/NewDesktops'
    And click element containing text 'ALL SITES'
    Then page should contain values saved on previous step with name 'sitesNames'