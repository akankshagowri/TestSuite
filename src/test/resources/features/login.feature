@login
Feature: Login
  As a registered SauceDemo user
  I want to log in with valid credentials
  So that I can access the product inventory

  Background:
    Given the user is on the SauceDemo login page

  @smoke @positive
  Scenario: Successful login with standard user
    When the user logs in with username "standard_user" and a valid password
    Then the user should be redirected to the inventory page
    And the page title should be "Products"

  @regression @positive
  Scenario: Successful login with problem user
    When the user logs in with username "problem_user" and a valid password
    Then the user should be redirected to the inventory page

  @regression @positive
  Scenario: Successful login with performance glitch user
    When the user logs in with username "performance_glitch_user" and a valid password
    Then the user should be redirected to the inventory page

  @regression @negative
  Scenario: Login blocked for locked out user
    When the user logs in with username "locked_out_user" and a valid password
    Then the user should see the error message "Epic sadface: Sorry, this user has been locked out."

  @regression @negative
  Scenario: Login fails with invalid password
    When the user attempts to login with username "standard_user" and password "wrong_password_123"
    Then the user should see the error message "Epic sadface: Username and password do not match any user in this service"

  @regression @negative
  Scenario: Login fails with invalid username
    When the user attempts to login with username "not_a_real_user" and a valid password
    Then the user should see the error message "Epic sadface: Username and password do not match any user in this service"

  @regression @negative
  Scenario: Login fails with empty username
    When the user attempts to login with username "" and a valid password
    Then the user should see the error message "Epic sadface: Username is required"

  @regression @negative
  Scenario: Login fails with empty password
    When the user attempts to login with username "standard_user" and password ""
    Then the user should see the error message "Epic sadface: Password is required"

  @regression @negative
  Scenario: Login fails with both fields empty
    When the user attempts to login with username "" and password ""
    Then the user should see the error message "Epic sadface: Username is required"

  @regression @positive
  Scenario: Logout returns user to the login page
    Given the user logs in with username "standard_user" and a valid password
    When the user logs out
    Then the user should be redirected to the login page
