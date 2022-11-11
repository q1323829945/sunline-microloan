# Query PDPA content

## Request

| Path        | /pdpa/{country}/{language}/retrieve |
| ----------- | ----------------------------------- |
| Method      | GET                                 |
| Description | Query PDPA content                  |

### Request header

[see](../../header.md)

### Path parameter

| Parameter | Type   | M/O  | Description                                                  |
| --------- | ------ | ---- | ------------------------------------------------------------ |
| country   | string | M    | Country code<br />[See Appendix Country Code](../../appendices/country_code.md) |
| language  | string | M    | language<br />[See Appendix Dictionary Code - Language](../../appendices/dictionary_code.md) |

## Response

### Response body

| Parameter       | Type   | Description                  |
| --------------- | ------ | ---------------------------- |
| pdpaInformation | array  | PDPA information             |
| ∟ item          | string | Major categories of elements |
| ∟ information   | array  | List of primary subclasses   |
| ∟∟ label        | string | Element key                  |
| ∟∟ name         | string | Element Name                 |
| id              | string | PDPA template id             |

### Example of response body

```json
{
  "pdpaInformation": [
      {
          "item":"personalInformation",
          "information":[
              {
                  “label”:"name",
                  "name":"Name"
              },
              {
                  “label”:"aliasName",
                  "name":"Alias Name"
              },
              {
                  “label”:"gender",
                  "name":"Gender"
              },
              {
                  “label”:"dateOfBirth",
                  "name":"Date of Birth"
              }
          ]
      },
      {
          "item":"corporateInformation",
          "information":[
              {
                  “label”:"entityBasicProfile",
                  "name":"Entity Basic Profile"
              },
              {
                  “label”:"entityPreviousNames",
                  "name":"Entity Previous Names"
              },
              {
                  “label”:"entityAddresses",
                  "name":"Entity Addresses"
              }
          ]
      }
  ],
  "id": "123456789"
}
```
