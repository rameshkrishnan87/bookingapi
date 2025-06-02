@error
Feature: End to End Booking Test
  Scenario: To perform a CURD operation on booking API
    Given user accesses endpoint "/auth/login"
    When user creates a auth token with login authentication as "admin" and "password123"
    Then user should get the response code 403