Feature: PDPA online authority mode configuration
  It can support different online authority methods to meet different user needs

  Background:
    Given init tenant
    Given init pdpa authority way

  Scenario Outline: Configure single online authority way
    Given the authority way is "<authorityWay>"
    When query the authority way
    Then the customer PDPA authority way is "<answerAuthorityWay>"


    Examples:
      |                 authorityWay                    |                answerAuthorityWay                  |
      |                electronicSignature                  |                 electronicSignature                    |
      |                  faceRecognition                    |                    faceRecognition                     |
      |                    fingerprint                      |                     fingerprint                        |
      |         electronicSignature,faceRecognition         |           electronicSignature,faceRecognition          |
      |   electronicSignature,faceRecognition,fingerprint   |    electronicSignature,faceRecognition,fingerprint     |



