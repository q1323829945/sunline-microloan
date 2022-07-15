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
      | customerId |
      | 10086      |



