@PdpaInformationAccess
Feature: PDPA access
  Data subjects access collected information items

  Background:
    Given init tenant
    Given init pdpa

  Scenario Outline: PDPA information access
    Given there are customer "<customerId>" already confirm PDPA authorization
    When access PDPA information
    Then obtain PDPA authorization information
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
      | customerId |
      | 10086      |



