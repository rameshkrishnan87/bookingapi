@happyPathScenario

Feature: Create Booking Test
  Background: To create a booking on booking api

    Given user accesses endpoint "api/auth/login"
    When user creates a auth token with login authentication as "admin" and "password"
    Then user should get the response code 200

  Scenario Outline: To create a room booking

    Given user accesses endpoint "api/booking"
    When the user books a room with the mentioned booking details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 200

    Examples:
      | firstname | lastname | email              | phone         | checkin    | checkout   |
      | Goki      | App      | goki.app@gmail.com | 1234567890098 | 2025-05-12 | 2025-05-14 |