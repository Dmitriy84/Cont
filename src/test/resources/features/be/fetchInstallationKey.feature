Feature: BE: Create the query in GraphQL to fetch the installation Key for the Agent on the 3rd step

  @BE
  Scenario Outline: BE: Fetching the installation Key for the Agent -C4126846
    Given login to Continuum
    And endpoint is 'GraphQL/'
    And request JSON body is
    """
    { "query": "query {registrationTokens(clientID:$clientID,siteID:$siteID,endpointID:$endpointID,agentID:$agentID,legacyRegID:$legacyRegID,ipAddress:$ipAddress){registrationTokenList{edges{cursor,node{token}}}}}", "variables": { "clientID": "<clientID>", "siteID": "<siteID>", "endpointID": "", "agentID": "", "legacyRegID": "", "ipAddress": "1.1.1.1" }}
    """
    When POST request was send
    Then response status code is 200
    And response JSON body should have value
    """
    {"data":{"registrationTokens":{"registrationTokenList":{"edges":[{"cursor":"1","node":{"token":"${json-unit.any-string}"}}]}}}}
    """

  @DT-BE
    Examples:
      | clientID | siteID   |
      | 50110217 | 50110217 |
      | 50110218 | 50110218 |

  @QA-BE
    Examples:
      | clientID | siteID   |
      | 50000206 | 50000206 |
      | 50000124 | 50000124 |

  @BE
  Scenario Outline: BE: Fetching the installation Key for the Agent with wrong header data -C4131528
    Given login to Continuum
    And endpoint is 'GraphQL/'
    And headers are
      | key                 | value   |
      | IPlanetDirectoryPro | invalid |
    And request JSON body is
    """
    { "query":
        "query {registrationTokens(clientID:$clientID,siteID:$siteID,endpointID:$endpointID,agentID:$agentID,legacyRegID:$legacyRegID,ipAddress:$ipAddress)x
            {registrationTokenList{edges{cursor,node{token}}}}}",
                "variables": { "clientID": "<clientID>", "siteID": "<siteID>", "endpointID": "", "agentID": "", "legacyRegID": "", "ipAddress": "1.1.1.1" }}
    """
    When POST request was send
    Then response status code is 403
    And response body string contains values 'Forbidden'

  @DT-BE
    Examples:
      | clientID | siteID   |
      | 50110217 | 50110217 |
      | 50110218 | 50110218 |

  @QA-BE
    Examples:
      | clientID | siteID   |
      | 50000206 | 50000206 |
      | 50000124 | 50000124 |

  @BE
  Scenario Outline: BE: Fetching the installation Key fon the Agent with wrong body data -C4126847
    Given login to Continuum
    And endpoint is 'GraphQL/'
    And request JSON body is
    """
    { "query":
        "query {registrationTokens(clientID:$clientID,siteID:$siteID,endpointID:$endpointID,agentID:$agentID,legacyRegID:$legacyRegID,ipAddress:$ipAddress)
                {registrationTokenList{edges{cursor,node{token}}}}}",
                    "variables": { "clientID": "", "siteID": "<siteID>", "endpointID": "", "agentID": "", "legacyRegID": "", "ipAddress": "1.1.1.1" }}
    """
    When POST request was send
    Then response status code is 500
    And request JSON body is
    """
    {"errors": [{"message": "Schema Object Not Found for Query!!!","locations": [{"line": 222,"column": 0}]}]}
    """

  @DT-BE
    Examples:
      | clientID | siteID   |
      | 50110217 | 50110217 |
      | 50110218 | 50110218 |

  @QA-BE
    Examples:
      | clientID | siteID   |
      | 50000206 | 50000206 |
      | 50000124 | 50000124 |
