@error

Feature: Booking not found validation
  Background: To validate 404 status code

    Given user accesses endpoint "/auth/login"
    When user creates a auth token with login authentication as "admin" and "password"
    Then user should get the response code 200

  Scenario: To validate 404 status code on booking API on booking API
    When the user searches booking details with invalid booking ID
    Then the response status code should be 404