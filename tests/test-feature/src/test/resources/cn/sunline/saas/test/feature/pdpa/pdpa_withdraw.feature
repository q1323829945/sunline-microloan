@PdpaWithdarw
Feature: PDPA agrees to withdraw the configuration
  It can support the withdrawal of PDPA consent authorization and remind the possible consequences of withdrawal

  Background:
    Given init tenant

  Scenario Outline: PDPA agrees to withdraw
    Given There are customer "<customerId>" PDPA authorization and consent records
    When select withdraw
    Then the customer PDPA authorization is "<status>"


    Examples:
      | customerId | status  |
      | 10086      |         |



