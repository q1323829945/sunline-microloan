@PdpaFeature
Feature: Configure PDPA authorization information items in different countries
  It can support the configuration of PDPA authorization information items to meet the requirements of PDPA regulations in different countries

  Background:
    Given init tenant

  Scenario Outline: Configure SGP PDPA authorization information items
    Given country is "<country>"
    And language is "<language>"
    And the configured information item
    """json
        [
            {
                "item": "business information",
                "information":
                [
                    {
                        "label": "name",
                        "name": "name"
                    },
                    {
                        "label": "alias",
                        "name": "alias"
                    },
                    {
                        "label": "name pinyin",
                        "name": "name pinyin"
                    },
                    {
                        "label": "alias pinyin",
                        "name": "alias pinyin"
                    },
                    {
                        "label": "gender",
                        "name": "gender"
                    },
                    {
                        "label": "birth",
                        "name": "birth"
                    },
                    {
                        "label": "internationgal",
                        "name": "internationgal"
                    },
                    {
                        "label": "register address",
                        "name": "register address"
                    },
                    {
                        "label": "hdb type",
                        "name": "hdb type"
                    },
                    {
                        "label": "address",
                        "name": "address"
                    },
                    {
                        "label": "notice",
                        "name": "notice"
                    },
                    {
                        "label": "mobile phone",
                        "name": "mobile phone"
                    },
                    {
                        "label": "email",
                        "name": "email"
                    }
                ]
            },
            {
                "item": "perpon informationn",
                "information":
                [
                    {
                        "label": "outline",
                        "name": "outline"
                    },
                    {
                        "label": "compangy",
                        "name": "compangy"
                    },
                    {
                        "label": "address",
                        "name": "address"
                    },
                    {
                        "label": "uens",
                        "name": "uens"
                    },
                    {
                        "label": "finance",
                        "name": "finance"
                    },
                    {
                        "label": "capital",
                        "name": "capital"
                    },
                    {
                        "label": "leader",
                        "name": "leader"
                    },
                    {
                        "label": "shareholder",
                        "name": "shareholder"
                    }
                ]
            }
        ]
    """
    When add PDPA information
    And get PDPA information
    Then the "<country>" PDPA language is "<language>" configure is items
    """json
        [
            {
                "item": "business information",
                "information":
                [
                    {
                        "label": "name",
                        "name": "name"
                    },
                    {
                        "label": "alias",
                        "name": "alias"
                    },
                    {
                        "label": "name pinyin",
                        "name": "name pinyin"
                    },
                    {
                        "label": "alias pinyin",
                        "name": "alias pinyin"
                    },
                    {
                        "label": "gender",
                        "name": "gender"
                    },
                    {
                        "label": "birth",
                        "name": "birth"
                    },
                    {
                        "label": "internationgal",
                        "name": "internationgal"
                    },
                    {
                        "label": "register address",
                        "name": "register address"
                    },
                    {
                        "label": "hdb type",
                        "name": "hdb type"
                    },
                    {
                        "label": "address",
                        "name": "address"
                    },
                    {
                        "label": "notice",
                        "name": "notice"
                    },
                    {
                        "label": "mobile phone",
                        "name": "mobile phone"
                    },
                    {
                        "label": "email",
                        "name": "email"
                    }
                ]
            },
            {
                "item": "perpon informationn",
                "information":
                [
                    {
                        "label": "outline",
                        "name": "outline"
                    },
                    {
                        "label": "compangy",
                        "name": "compangy"
                    },
                    {
                        "label": "address",
                        "name": "address"
                    },
                    {
                        "label": "uens",
                        "name": "uens"
                    },
                    {
                        "label": "finance",
                        "name": "finance"
                    },
                    {
                        "label": "capital",
                        "name": "capital"
                    },
                    {
                        "label": "leader",
                        "name": "leader"
                    },
                    {
                        "label": "shareholder",
                        "name": "shareholder"
                    }
                ]
            }
        ]
    """

    Examples:
      | country | language |
      | SGP     | ENGLISH  |


  Scenario Outline: Configure SGP PDPA authorization information items
    Given country is "<country>"
    And language is "<language>"
    And the configured information item
    """json
      [
          {
              "item": "on behalf of",
              "information":
              [
                  {
                      "label": "company type",
                      "name": "company type"
                  },
                  {
                      "label": "Juristic name",
                      "name": "Juristic name"
                  },
                  {
                      "label": "Juristic ID",
                      "name": "Juristic ID"
                  },
                  {
                      "label": "Register Date",
                      "name": "Register Date"
                  }
              ]
          },
          {
              "item": "contact information",
              "information":
              [
                  {
                      "label": "Mr./Mrs./Ms./Otiher",
                      "name": "Mr./Mrs./Ms./Otiher"
                  },
                  {
                      "label": "Contact perpon",
                      "name": "Contact perpon"
                  },
                  {
                      "label": "Address",
                      "name": "Address"
                  },
                  {
                      "label": "Road",
                      "name": "Road"
                  },
                  {
                      "label": "Sub district",
                      "name": "Sub district"
                  },
                  {
                      "label": "District",
                      "name": "District"
                  },
                  {
                      "label": "Province",
                      "name": "Province"
                  },
                  {
                      "label": "Postal code",
                      "name": "Postal code"
                  },
                  {
                      "label": "Telphone No.",
                      "name": "Telphone No."
                  },
                  {
                      "label": "fax No",
                      "name": "fax No"
                  }
              ]
          },
          {
              "item": "Business information",
              "information":
              [
                  {
                      "label": "businesstype",
                      "name": "businesstype"
                  },
                  {
                      "label": "average revenue",
                      "name": "average revenue"
                  },
                  {
                      "label": "anerage revenue per year",
                      "name": "anerage revenue per year"
                  },
                  {
                      "label": "No.of Employees",
                      "name": "No.of Employees"
                  }
              ]
          },
          {
              "item": "Loan Details",
              "information":
              [
                  {
                      "label": "what is your purpose",
                      "name": "what is your purpose"
                  },
                  {
                      "label": "requlred loan amount",
                      "name": "requlred loan amount"
                  },
                  {
                      "label": "loan type",
                      "name": "loan type"
                  },
                  {
                      "label": "other required products/services",
                      "name": "other required products/services"
                  },
                  {
                      "label": "negative financial record",
                      "name": "negative financial record"
                  },
                  {
                      "label": "convenient time to call",
                      "name": "convenient time to call"
                  }
              ]
          },
          {
              "item": "Preferred Branch",
              "information":
              [
                  {
                      "label": "select province",
                      "name": "select province"
                  },
                  {
                      "label": "select branch",
                      "name": "select branch"
                  }
              ]
          }
      ]
    """
    When add PDPA information
    And get PDPA information
    Then the "<country>" PDPA language is "<language>" configure is items
    """json
      [
          {
              "item": "on behalf of",
              "information":
              [
                  {
                      "label": "company type",
                      "name": "company type"
                  },
                  {
                      "label": "Juristic name",
                      "name": "Juristic name"
                  },
                  {
                      "label": "Juristic ID",
                      "name": "Juristic ID"
                  },
                  {
                      "label": "Register Date",
                      "name": "Register Date"
                  }
              ]
          },
          {
              "item": "contact information",
              "information":
              [
                  {
                      "label": "Mr./Mrs./Ms./Otiher",
                      "name": "Mr./Mrs./Ms./Otiher"
                  },
                  {
                      "label": "Contact perpon",
                      "name": "Contact perpon"
                  },
                  {
                      "label": "Address",
                      "name": "Address"
                  },
                  {
                      "label": "Road",
                      "name": "Road"
                  },
                  {
                      "label": "Sub district",
                      "name": "Sub district"
                  },
                  {
                      "label": "District",
                      "name": "District"
                  },
                  {
                      "label": "Province",
                      "name": "Province"
                  },
                  {
                      "label": "Postal code",
                      "name": "Postal code"
                  },
                  {
                      "label": "Telphone No.",
                      "name": "Telphone No."
                  },
                  {
                      "label": "fax No",
                      "name": "fax No"
                  }
              ]
          },
          {
              "item": "Business information",
              "information":
              [
                  {
                      "label": "businesstype",
                      "name": "businesstype"
                  },
                  {
                      "label": "average revenue",
                      "name": "average revenue"
                  },
                  {
                      "label": "anerage revenue per year",
                      "name": "anerage revenue per year"
                  },
                  {
                      "label": "No.of Employees",
                      "name": "No.of Employees"
                  }
              ]
          },
          {
              "item": "Loan Details",
              "information":
              [
                  {
                      "label": "what is your purpose",
                      "name": "what is your purpose"
                  },
                  {
                      "label": "requlred loan amount",
                      "name": "requlred loan amount"
                  },
                  {
                      "label": "loan type",
                      "name": "loan type"
                  },
                  {
                      "label": "other required products/services",
                      "name": "other required products/services"
                  },
                  {
                      "label": "negative financial record",
                      "name": "negative financial record"
                  },
                  {
                      "label": "convenient time to call",
                      "name": "convenient time to call"
                  }
              ]
          },
          {
              "item": "Preferred Branch",
              "information":
              [
                  {
                      "label": "select province",
                      "name": "select province"
                  },
                  {
                      "label": "select branch",
                      "name": "select branch"
                  }
              ]
          }
      ]
    """


    Examples:
      | country | language |
      | THA     | ENGLISH  |
