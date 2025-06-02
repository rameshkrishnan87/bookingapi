@error
Feature: End to End Booking Tests
  Background: To perform a CRUD operation on booking API

    Given user accesses endpoint "/auth/login"
    When user creates a auth token with login authentication as "admin" and "password"
    Then user should get the response code 200

  Scenario Outline: Create a booking without passing required fields should fail
    Given user accesses endpoint "/booking/"
    When the user tries to books a room with invalid booking details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   | bookingid   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> | <bookingid> |
    Then the response status code should be 400
    And the user should see response with incorrect "<FieldError>"

    Examples:x
      | firstname | lastname | email              | phone        | checkin    | checkout   | FieldError                            |
      |           | API      | api.xyz@gmail.com  | 879558797034 | 2025-03-15 | 2025-03-18 | [Firstname should not be blank]       |
      | Goki.     | Pe       | api.xyz@gmail.com  | 879558797034 | 2025-03-15 | 2025-03-18 | [size must be between 3 and 30]       |
      | Goki      | API      | john               | 879558797034 | 2025-03-15 | 2025-03-18 | [must be a well-formed email address] |
      | Goki      | API      | api.xyz@gmail.com  | 8795587970   | 2025-03-15 | 2025-03-18 | [size must be between 11 and 21]      |
      | Goki      | API      | api.xyz@gmail.com  | 879558797034 |            | 2025-03-18 | [must not be null]                    |