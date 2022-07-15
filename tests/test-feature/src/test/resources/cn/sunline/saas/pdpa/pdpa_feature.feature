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
                        "key": "name",
                        "name": "name"
                    },
                    {
                        "key": "alias",
                        "name": "alias"
                    },
                    {
                        "key": "name pinyin",
                        "name": "name pinyin"
                    },
                    {
                        "key": "alias pinyin",
                        "name": "alias pinyin"
                    },
                    {
                        "key": "gender",
                        "name": "gender"
                    },
                    {
                        "key": "birth",
                        "name": "birth"
                    },
                    {
                        "key": "internationgal",
                        "name": "internationgal"
                    },
                    {
                        "key": "register address",
                        "name": "register address"
                    },
                    {
                        "key": "hdb type",
                        "name": "hdb type"
                    },
                    {
                        "key": "address",
                        "name": "address"
                    },
                    {
                        "key": "notice",
                        "name": "notice"
                    },
                    {
                        "key": "mobile phone",
                        "name": "mobile phone"
                    },
                    {
                        "key": "email",
                        "name": "email"
                    }
                ]
            },
            {
                "item": "perpon informationn",
                "information":
                [
                    {
                        "key": "outline",
                        "name": "outline"
                    },
                    {
                        "key": "compangy",
                        "name": "compangy"
                    },
                    {
                        "key": "address",
                        "name": "address"
                    },
                    {
                        "key": "uens",
                        "name": "uens"
                    },
                    {
                        "key": "finance",
                        "name": "finance"
                    },
                    {
                        "key": "capital",
                        "name": "capital"
                    },
                    {
                        "key": "leader",
                        "name": "leader"
                    },
                    {
                        "key": "shareholder",
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
                        "key": "name",
                        "name": "name"
                    },
                    {
                        "key": "alias",
                        "name": "alias"
                    },
                    {
                        "key": "name pinyin",
                        "name": "name pinyin"
                    },
                    {
                        "key": "alias pinyin",
                        "name": "alias pinyin"
                    },
                    {
                        "key": "gender",
                        "name": "gender"
                    },
                    {
                        "key": "birth",
                        "name": "birth"
                    },
                    {
                        "key": "internationgal",
                        "name": "internationgal"
                    },
                    {
                        "key": "register address",
                        "name": "register address"
                    },
                    {
                        "key": "hdb type",
                        "name": "hdb type"
                    },
                    {
                        "key": "address",
                        "name": "address"
                    },
                    {
                        "key": "notice",
                        "name": "notice"
                    },
                    {
                        "key": "mobile phone",
                        "name": "mobile phone"
                    },
                    {
                        "key": "email",
                        "name": "email"
                    }
                ]
            },
            {
                "item": "perpon informationn",
                "information":
                [
                    {
                        "key": "outline",
                        "name": "outline"
                    },
                    {
                        "key": "compangy",
                        "name": "compangy"
                    },
                    {
                        "key": "address",
                        "name": "address"
                    },
                    {
                        "key": "uens",
                        "name": "uens"
                    },
                    {
                        "key": "finance",
                        "name": "finance"
                    },
                    {
                        "key": "capital",
                        "name": "capital"
                    },
                    {
                        "key": "leader",
                        "name": "leader"
                    },
                    {
                        "key": "shareholder",
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
                      "key": "company type",
                      "name": "company type"
                  },
                  {
                      "key": "Juristic name",
                      "name": "Juristic name"
                  },
                  {
                      "key": "Juristic ID",
                      "name": "Juristic ID"
                  },
                  {
                      "key": "Register Date",
                      "name": "Register Date"
                  }
              ]
          },
          {
              "item": "contact information",
              "information":
              [
                  {
                      "key": "Mr./Mrs./Ms./Otiher",
                      "name": "Mr./Mrs./Ms./Otiher"
                  },
                  {
                      "key": "Contact perpon",
                      "name": "Contact perpon"
                  },
                  {
                      "key": "Address",
                      "name": "Address"
                  },
                  {
                      "key": "Road",
                      "name": "Road"
                  },
                  {
                      "key": "Sub district",
                      "name": "Sub district"
                  },
                  {
                      "key": "District",
                      "name": "District"
                  },
                  {
                      "key": "Province",
                      "name": "Province"
                  },
                  {
                      "key": "Postal code",
                      "name": "Postal code"
                  },
                  {
                      "key": "Telphone No.",
                      "name": "Telphone No."
                  },
                  {
                      "key": "fax No",
                      "name": "fax No"
                  }
              ]
          },
          {
              "item": "Business information",
              "information":
              [
                  {
                      "key": "businesstype",
                      "name": "businesstype"
                  },
                  {
                      "key": "average revenue",
                      "name": "average revenue"
                  },
                  {
                      "key": "anerage revenue per year",
                      "name": "anerage revenue per year"
                  },
                  {
                      "key": "No.of Employees",
                      "name": "No.of Employees"
                  }
              ]
          },
          {
              "item": "Loan Details",
              "information":
              [
                  {
                      "key": "what is your purpose",
                      "name": "what is your purpose"
                  },
                  {
                      "key": "requlred loan amount",
                      "name": "requlred loan amount"
                  },
                  {
                      "key": "loan type",
                      "name": "loan type"
                  },
                  {
                      "key": "other required products/services",
                      "name": "other required products/services"
                  },
                  {
                      "key": "negative financial record",
                      "name": "negative financial record"
                  },
                  {
                      "key": "convenient time to call",
                      "name": "convenient time to call"
                  }
              ]
          },
          {
              "item": "Preferred Branch",
              "information":
              [
                  {
                      "key": "select province",
                      "name": "select province"
                  },
                  {
                      "key": "select branch",
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
                      "key": "company type",
                      "name": "company type"
                  },
                  {
                      "key": "Juristic name",
                      "name": "Juristic name"
                  },
                  {
                      "key": "Juristic ID",
                      "name": "Juristic ID"
                  },
                  {
                      "key": "Register Date",
                      "name": "Register Date"
                  }
              ]
          },
          {
              "item": "contact information",
              "information":
              [
                  {
                      "key": "Mr./Mrs./Ms./Otiher",
                      "name": "Mr./Mrs./Ms./Otiher"
                  },
                  {
                      "key": "Contact perpon",
                      "name": "Contact perpon"
                  },
                  {
                      "key": "Address",
                      "name": "Address"
                  },
                  {
                      "key": "Road",
                      "name": "Road"
                  },
                  {
                      "key": "Sub district",
                      "name": "Sub district"
                  },
                  {
                      "key": "District",
                      "name": "District"
                  },
                  {
                      "key": "Province",
                      "name": "Province"
                  },
                  {
                      "key": "Postal code",
                      "name": "Postal code"
                  },
                  {
                      "key": "Telphone No.",
                      "name": "Telphone No."
                  },
                  {
                      "key": "fax No",
                      "name": "fax No"
                  }
              ]
          },
          {
              "item": "Business information",
              "information":
              [
                  {
                      "key": "businesstype",
                      "name": "businesstype"
                  },
                  {
                      "key": "average revenue",
                      "name": "average revenue"
                  },
                  {
                      "key": "anerage revenue per year",
                      "name": "anerage revenue per year"
                  },
                  {
                      "key": "No.of Employees",
                      "name": "No.of Employees"
                  }
              ]
          },
          {
              "item": "Loan Details",
              "information":
              [
                  {
                      "key": "what is your purpose",
                      "name": "what is your purpose"
                  },
                  {
                      "key": "requlred loan amount",
                      "name": "requlred loan amount"
                  },
                  {
                      "key": "loan type",
                      "name": "loan type"
                  },
                  {
                      "key": "other required products/services",
                      "name": "other required products/services"
                  },
                  {
                      "key": "negative financial record",
                      "name": "negative financial record"
                  },
                  {
                      "key": "convenient time to call",
                      "name": "convenient time to call"
                  }
              ]
          },
          {
              "item": "Preferred Branch",
              "information":
              [
                  {
                      "key": "select province",
                      "name": "select province"
                  },
                  {
                      "key": "select branch",
                      "name": "select branch"
                  }
              ]
          }
      ]
    """


    Examples:
      | country | language |
      | THA     | ENGLISH  |
