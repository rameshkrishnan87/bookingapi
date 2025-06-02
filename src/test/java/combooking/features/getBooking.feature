@happyPathScenario

Feature: Get Booking Test
  Background: To perform a Get operation on booking API

    Given user accesses endpoint "/auth/login"
    When user creates a auth token with login authentication as "admin" and "password"
    Then user should get the response code 200

  Scenario Outline: To perform a Get operation API

    Given user accesses endpoint "/booking/"
    When the user books a room with the mentioned booking details
      | firstname   | lastname   | email   | phone   | checkin   | checkout   |
      | <firstname> | <lastname> | <email> | <phone> | <checkin> | <checkout> |
    Then the response status code should be 201
    When the user finds booking details with booking ID
    Then the response status code should be 200

    Examples:
      | firstname | lastname | email              | phone         | checkin    | checkout   |
      | Goki      | App      | goki.app@gmail.com | 1234567890098 | 2025-05-12 | 2025-05-14 |