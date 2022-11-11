# Summary

This API document follows the specifications formulated by OpenAPI



## 1.Path Template

Path templates are template expressions separated by curly braces ({}) to mark a portion of the URL path as replaceable using path parameters.



Each template expression in the path must correspond to a path parameter, which is included in the path item itself and/or the operation of each path item. One exception is that if the path item is empty, for example, due to ACL constraints, no matching path parameters are required.



The values of these path parameters must not contain any unescaped "general syntax" characters described in [RFC3986]: forward slash (/), question mark (?) Or hash (#).

## 2.Version

Version uses `major`.`minor`.`patch` paradigm

## 3.Original Type

Used to describe the field type of json message

| Type    | description    |
| ------- | -------------- |
| integer | Integer number |
| string  | String         |
| object  | Object         |
| array   | Array          |

> Note: For non json messages, such as uploaded pictures or files, see the specific interface description for the field type

## 4.Message example

The interface description is provided with message examples to show the message structure of json for interface testing

> Note: Sample data cannot be used as test cases

## 5.Field required

| Type | Description                                                  |
| ---- | ------------------------------------------------------------ |
| M    | Must                                                         |
| O    | Optional                                                     |
| M?   | It will be used when the parent field is optional, indicating that this field is required when the parent field is filled in |

