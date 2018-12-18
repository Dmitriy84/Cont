Feature: Activate configuration wizard -Step 3

  Background:
    Given login in as default user
    And open "Security Solution" page
    And select option 'Activate and Configure Offerings' from "Configure" dropdown
    And "Select client site" step of wizard is opened
    And select client's site 'Test site 1' on the "Select client site" step
    And click button with name 'Continue' in footer
    And "Activate solutions" step of wizard is opened

  @UI
  Scenario: UI: Providing the installation key for the Agent. -C4073006
    And click button with name 'Finish' in footer
    When "Get Started" step of wizard is opened
    Then wait for "Agent Installation Key" field to be filled

  @UI @skip
  Scenario: UI: Copying Installation Key on the third step -C4138371
    When click button with name 'Finish' in footer
    And "Get Started" step of wizard is opened
    And wait for "Agent Installation Key" field to be filled
    And click "Copy" button next to "Agent Installation Key" field
    Then copied to clipboard "Agent Installation key" matches value in the field

  @UI
  Scenario: UI: Integration: Download the Agent installation file -C4196975
    When click button with name 'Finish' in footer
    And "Get Started" step of wizard is opened
    Then agent file should be downloaded

  @UI
  Scenario: UI: Check third step design -C4208889
    When click button with name 'Finish' in footer
    And "Get Started" step of wizard is opened
    Then button "Close" in footer should be VISIBLE
    And button "Close" in footer should be ENABLED
    And button "Close" in footer should have color DARK_BLUE
    And buttons "Cancel, Finish, Back, Continue" in footer should be INVISIBLE
    And active step in Progress Tracker should be GET_STARTED
    And site name on "Get Started" step should be 'Test site 1'
    And installation step number 0 should have title 'Install the agent on your client’s desktop'
    And installation step number 0 should have body
    """
    The agent is flexible and can be installed on any desktop. Once installed, the Setup Wizard will guide you through the setup process.
    """
    And installation step number 1 should have title 'Enter your installation key in the Setup Wizard'
    And installation step number 1 should have body
    """
    The installation key associates the desktop with your company.
    """
    And installation step number 2 should have title 'Ensure your client’s desktop is connected to the Internet'
    And installation step number 2 should have body
    """
    After completing the setup process, the agent will need to access the Internet to report monitoring information back to the ITSupport Portal.
    The desktop will be associated with your client’s site using your client’s Public IP Address.
    """
    And installation step number 3 should have title 'Begin monitoring your client’s desktop'
    And installation step number 3 should have body
    """
    The desktop will automatically appear in the ITSupport Portal once the agent checks in.
    Verify that the desktop is assigned to the correct site. If the desktop is marked as Unassigned, you will need to assign it to the correct site.
    """


