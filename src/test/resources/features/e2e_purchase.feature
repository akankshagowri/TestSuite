@e2e
Feature: End-to-End Purchase Flow
  As a customer
  I want to complete a full purchase journey from login to order confirmation
  So that the entire critical path is verified in one continuous flow

  @smoke @e2e
  Scenario: Full purchase flow with a single item
    Given the user is on the SauceDemo login page
    When the user logs in with username "standard_user" and a valid password
    And the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    And the user proceeds to checkout
    And the user fills checkout information with first name "Jane", last name "Smith", postal code "94107"
    And the user finishes the checkout
    Then the order confirmation header should read "Thank you for your order!"

  @regression @e2e
  Scenario: Full purchase flow with multiple items
    Given the user is on the SauceDemo login page
    When the user logs in with username "standard_user" and a valid password
    And the user adds "Sauce Labs Backpack" to the cart
    And the user adds "Sauce Labs Bike Light" to the cart
    And the user adds "Sauce Labs Fleece Jacket" to the cart
    And the user opens the cart
    Then the cart should contain 3 items
    When the user proceeds to checkout
    And the user fills checkout information with first name "Jane", last name "Smith", postal code "94107"
    Then the checkout overview should list 3 items
    When the user finishes the checkout
    Then the order confirmation header should read "Thank you for your order!"

  @regression @e2e
  Scenario: Full purchase flow followed by logout
    Given the user is on the SauceDemo login page
    When the user logs in with username "standard_user" and a valid password
    And the user adds "Sauce Labs Onesie" to the cart
    And the user opens the cart
    And the user proceeds to checkout
    And the user fills checkout information with first name "Jane", last name "Smith", postal code "94107"
    And the user finishes the checkout
    And the user selects back to products
    And the user logs out
    Then the user should be redirected to the login page

  @regression @e2e
  Scenario: Full purchase flow with sorted inventory selection
    Given the user is on the SauceDemo login page
    When the user logs in with username "standard_user" and a valid password
    And the user sorts products by "Price (low to high)"
    And the user adds "Sauce Labs Onesie" to the cart
    And the user opens the cart
    And the user proceeds to checkout
    And the user fills checkout information with first name "Jane", last name "Smith", postal code "94107"
    And the user finishes the checkout
    Then the order confirmation header should read "Thank you for your order!"
