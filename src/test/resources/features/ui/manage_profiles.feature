Feature:  UI: Manage Profile & Protect profiles

#  @UI
  Scenario: UI: Verify Manage Profile & Protect profiles page -C4383025,C4385028,C4385029,C4459632
    Given login to Continuum
    And endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/products'
    And with parameters
      | key       | value    |
      | partnerId | 0        |
      | clientId  | 50111123 |
    And request JSON body is
    """
    { "products": [ { "id": "07bf62a1-1235-11e8-baf9-02170cb549b8", "enabled": true, "data": { } }] }
    """
    And PUT request was send
    And response status code is 200
    And logout from Continuum
    And login in as default user
    And open "Security Solution" page
    And select option 'Manage Profile & Protect profiles' from "Configure" dropdown
    And "Manage Profile & Protect profiles" page is opened
    Then breadcrumbs in the header should be "SECURITY, MANAGE PROFILE PROTECT PROFILES"
    When remember security risk categories with values for profile 'Advanced User Account Security'
    And sort security risk categories by ASCENDING order
    Then security risk categories order should be ASCENDING
    And security risk categories with values should be the same as remembered values for profile 'Advanced User Account Security'
    When sort security risk categories by DESCENDING order
    Then security risk categories order should be DESCENDING
    And security risk categories with values should be the same as remembered values for profile 'Advanced User Account Security'
    When click "Back" button on "Manage Profiles" page
    Then "Security" tab should be opened

#  @UI
  Scenario: UI: Verify User account profiles (disabled) -C4459631
    Given login to Continuum
    And endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/products'
    And with parameters
      | key       | value    |
      | partnerId | 0        |
      | clientId  | 50111123 |
    And request JSON body is
    """
    { "products": [ { "id": "07bf62a1-1235-11e8-baf9-02170cb549b8", "enabled": false, "data": { } }] }
    """
    And PUT request was send
    And response status code is 200
    And logout from Continuum
    And login in as default user
    And open "Security Solution" page
    And select option 'Manage Profile & Protect profiles' from "Configure" dropdown
    And "Manage Profile & Protect profiles" page is opened without available profiles

