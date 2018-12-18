Feature:  BE: Security portal service: Manage products

  @BE
  Scenario Outline: BE: Get the list of products -C4204717
    Given login to Continuum
    And endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/products'
    And with parameters
      | key       | value       |
      | partnerId | 0           |
      | clientId  | <client_id> |
    When GET request was send
    Then response status code is 200
    And response JSON body without values should be
    """
      {"products":[{
        "id":"<Office 365 Integration>","enabled":false,"name":"Office 365 Integration"},
        {"id":"<Profile and Protect>","enabled":false,"name":"Profile and Protect"},
        {"id":"<Detect and Respond>","enabled":false,"name":"Detect and Respond"}],
      "errors":null}
    """
    And retry '3' times for response JSON body should have value
    """
  {"products":
    [{"id":"<Office 365 Integration>","enabled":"${json-unit.any-boolean}","name":"Office 365 Integration"}
    ,{"id":"<Profile and Protect>","enabled":"${json-unit.any-boolean}","name":"Profile and Protect"}
    ,{"id":"<Detect and Respond>","enabled":"${json-unit.any-boolean}","name":"Detect and Respond"}],
  "errors": null}
    """

  @DT-BE
    Examples:
      | client_id | Office 365 Integration               | Profile and Protect                  | Detect and Respond                   |
      | 50110217  | 27f2ae1c-ab8f-11e8-9d84-0ae0fc1ff2aa | 07bf62a1-1235-11e8-baf9-02170cb549b8 | e4ce5333-1234-11e8-9f18-0ae0fc1ff2aa |
      | 50110218  | 27f2ae1c-ab8f-11e8-9d84-0ae0fc1ff2aa | 07bf62a1-1235-11e8-baf9-02170cb549b8 | e4ce5333-1234-11e8-9f18-0ae0fc1ff2aa |

  @QA-BE
    Examples:
      | client_id | Office 365 Integration               | Profile and Protect                  | Detect and Respond                   |
      | 50000124  | 98c4cd29-ae05-11e8-bf38-0efb1b08c7fe | 2200b37b-540d-11e8-87d6-0efb1b08c7fe | 70b8b6c4-540d-11e8-9db3-125463bbd412 |
      | 50000206  | 98c4cd29-ae05-11e8-bf38-0efb1b08c7fe | 2200b37b-540d-11e8-87d6-0efb1b08c7fe | 70b8b6c4-540d-11e8-9db3-125463bbd412 |


  @BE
  Scenario Outline: BE: Check error message is displayed after calling endpoint with wrong data (invalid token) -C4205313
    Given login to Continuum
    And endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/products'
    And with parameters
      | key       | value       |
      | partnerId | 0           |
      | clientId  | <client_id> |
    And headers are
      | key                 | value   |
      | IPlanetDirectoryPro | invalid |
    When GET request was send
    Then response status code is 401
    And response body string contains values 'Failed to validate'

  @DT-BE
    Examples:
      | client_id |
      | 50110217  |
      | 50110218  |

  @QA-BE
    Examples:
      | client_id |
      | 50000124  |
      | 50000206  |

  @BE
  Scenario Outline: BE: Check that product status changing to inactive -C4205909
    Given login to Continuum
    And endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/products'
    And with parameters
      | key       | value       |
      | partnerId | 0           |
      | clientId  | <client_id> |
    And request JSON body is
    """
      { "products": [ { "id": "<Profile and Protect>", "enabled": true, "data": { } } ] }
    """
    When PUT request was send
    Then response status code is 200
    And  retry '3' times for response JSON body should have value
    """
    {
    "products": [
        {
            "id": "<Profile and Protect>",
            "enabled": true,
            "name": "Profile and Protect"
        }
    ],
    "errors": []
    }
    """
    Given endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/products'
    And with parameters
      | key       | value       |
      | partnerId | 0           |
      | clientId  | <client_id> |
    And request JSON body is
    """
      { "products": [ { "id": "<Profile and Protect>", "enabled": false, "data": { } } ] }
    """
    When PUT request was send
    Then response status code is 200
    And  retry '3' times for response JSON body should have value
    """
    {
    "products": [
        {
            "id": "<Profile and Protect>",
            "enabled": false,
            "name": "Profile and Protect"
        }
    ],
    "errors": []
    }
    """

  @DT-BE
    Examples:
      | client_id | Profile and Protect                  |
      | 50110217  | 07bf62a1-1235-11e8-baf9-02170cb549b8 |
      | 50110218  | 07bf62a1-1235-11e8-baf9-02170cb549b8 |

  @QA-BE
    Examples:
      | client_id | Profile and Protect                  |
      | 50000124  | 2200b37b-540d-11e8-87d6-0efb1b08c7fe |
      | 50000206  | 2200b37b-540d-11e8-87d6-0efb1b08c7fe |

  @BE
  Scenario Outline:  BE: Check error message for invalid product id in request body -C4240837
    Given login to Continuum
    And endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/products'
    And with parameters
      | key       | value       |
      | partnerId | 0           |
      | clientId  | <client_id> |
    And request JSON body is
    """
    { "products": [ { "id": "<Profile and Protect>", "enabled": false, "data": { } },
     { "id": "<Office 365 Integration>", "enabled": false, "data": { } },
     { "id": "<Detect and Respond>", "enabled": false, "data": { } }] }
    """
    When PUT request was send
    Then response status code is 200
    And  retry '3' times for response JSON body should have value
    """
    {
    "products": [
        {  "id": "<Profile and Protect>", "enabled": false, "name": "Profile and Protect"},
        {  "id": "<Detect and Respond>", "enabled": false, "name": "Detect and Respond"},
        { "id": "<Office 365 Integration>", "enabled": false, "name": "Office 365 Integration"}
        ],
    "errors": []
    }
    """
    Given endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/products'
    And with parameters
      | key       | value       |
      | partnerId | 0           |
      | clientId  | <client_id> |
    And request JSON body is
    """
    { "products": [ { "id": "invalid", "enabled": true, "data": { } } ] }
    """
    When PUT request was send
    Then response status code is 422
    And  retry '3' times for response JSON body should have value
    """
    {
    "products": [
        { "id": "<Detect and Respond>", "enabled": "${json-unit.any-boolean}", "name": "Detect and Respond"},
        { "id": "<Office 365 Integration>", "enabled": "${json-unit.any-boolean}", "name": "Office 365 Integration"},
        { "id": "<Profile and Protect>", "enabled": "${json-unit.any-boolean}", "name": "Profile and Protect" }
            ],
    "errors": [
        {  "id": "invalid", "message": "product doesn't exist"}
    ]
    }
    """

  @DT-BE
    Examples:
      | client_id | Office 365 Integration               | Profile and Protect                  | Detect and Respond                   |
      | 50110217  | 27f2ae1c-ab8f-11e8-9d84-0ae0fc1ff2aa | 07bf62a1-1235-11e8-baf9-02170cb549b8 | e4ce5333-1234-11e8-9f18-0ae0fc1ff2aa |
      | 50110218  | 27f2ae1c-ab8f-11e8-9d84-0ae0fc1ff2aa | 07bf62a1-1235-11e8-baf9-02170cb549b8 | e4ce5333-1234-11e8-9f18-0ae0fc1ff2aa |

  @QA-BE
    Examples:
      | client_id | Office 365 Integration               | Profile and Protect                  | Detect and Respond                   |
      | 50000124  | 98c4cd29-ae05-11e8-bf38-0efb1b08c7fe | 2200b37b-540d-11e8-87d6-0efb1b08c7fe | 70b8b6c4-540d-11e8-9db3-125463bbd412 |
      | 50000206  | 98c4cd29-ae05-11e8-bf38-0efb1b08c7fe | 2200b37b-540d-11e8-87d6-0efb1b08c7fe | 70b8b6c4-540d-11e8-9db3-125463bbd412 |

  @BE
  Scenario Outline:  BE: Check O365 auth redirect request -C4241480
    Given login to Continuum
    And endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/products'
    And with parameters
      | key       | value       |
      | partnerId | 0           |
      | clientId  | <client_id> |
    And request JSON body is
    """
   { "products": [ { "id": "<Office 365 Integration>", "enabled": false,"data": { } } ] }
    """
    When PUT request was send
    Then response status code is 200
    And  retry '3' times for response JSON body should have value
    """
     {
    "products": [
        { "id": "<Office 365 Integration>", "enabled": false, "name": "Office 365 Integration"}
    ],
    "errors": []
    }
    """
    Given endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/sites/{clientId}/products/{productId}/auth/redirect'
    And with parameters
      | key       | value                    |
      | partnerId | 0                        |
      | clientId  | <client_id>              |
      | productId | <Office 365 Integration> |
    When GET request was send
    Then response status code is 200
    And response body string contains values 'Sign in to your account'

  @DT-BE
    Examples:
      | client_id | Office 365 Integration               |
      | 50110217  | 27f2ae1c-ab8f-11e8-9d84-0ae0fc1ff2aa |
      | 50110218  | 27f2ae1c-ab8f-11e8-9d84-0ae0fc1ff2aa |

  @QA-BE
    Examples:
      | client_id | Office 365 Integration               |
      | 50000124  | 98c4cd29-ae05-11e8-bf38-0efb1b08c7fe |
      | 50000206  | 98c4cd29-ae05-11e8-bf38-0efb1b08c7fe |

  @BE
  Scenario Outline:  BE: Check O365 auth redirect request (invalid data) -C4241481
    Given login to Continuum
    And endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/products'
    And with parameters
      | key       | value       |
      | partnerId | 0           |
      | clientId  | <client_id> |
    And request JSON body is
    """
   { "products": [ { "id": "<Office 365 Integration>", "enabled": false,"data": { } } ] }
    """
    When PUT request was send
    Then response status code is 200
    And  retry '3' times for response JSON body should have value
    """
     {
    "products": [
        { "id": "<Office 365 Integration>", "enabled": false, "name": "Office 365 Integration"}
    ],
    "errors": []
    }
    """
    Given endpoint is 'securityportal/v1/partners/{partnerId}/clients/{clientId}/sites/{clientId}/products/{productId}/auth/redirect'
    And with parameters
      | key       | value                                |
      | partnerId | 0                                    |
      | clientId  | <client_id>                          |
      | productId | 27f2ae1c-ab8f-11e8-9d84-0ae0fc1ff2aR |
    When GET request was send
    Then response status code is 500
    And response JSON body should have value
    """
     {"error":{"message":"product with id 27f2ae1c-ab8f-11e8-9d84-0ae0fc1ff2aR not found"}}
    """

  @DT-BE
    Examples:
      | client_id | Office 365 Integration               |
      | 50110217  | 27f2ae1c-ab8f-11e8-9d84-0ae0fc1ff2aa |
      | 50110218  | 27f2ae1c-ab8f-11e8-9d84-0ae0fc1ff2aa |

  @QA-BE
    Examples:
      | client_id | Office 365 Integration               |
      | 50000124  | 98c4cd29-ae05-11e8-bf38-0efb1b08c7fe |
      | 50000206  | 98c4cd29-ae05-11e8-bf38-0efb1b08c7fe |