@checkout
Feature: Checkout
  As a logged-in user with items in my cart
  I want to complete the checkout process
  So that I can successfully place an order

  Background:
    Given the user is logged in as "standard_user"
    And the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    And the user proceeds to checkout

  @smoke
  Scenario: Complete checkout information with valid data
    When the user fills checkout information with first name "John", last name "Doe", postal code "10001"
    Then the user should be on the checkout overview page

  @regression @negative
  Scenario: Checkout fails when first name is missing
    When the user fills checkout information with first name "", last name "Doe", postal code "10001"
    Then the user should see the checkout error "Error: First Name is required"

  @regression @negative
  Scenario: Checkout fails when last name is missing
    When the user fills checkout information with first name "John", last name "", postal code "10001"
    Then the user should see the checkout error "Error: Last Name is required"

  @regression @negative
  Scenario: Checkout fails when postal code is missing
    When the user fills checkout information with first name "John", last name "Doe", postal code ""
    Then the user should see the checkout error "Error: Postal Code is required"

  @regression
  Scenario: Checkout overview shows correct item count
    Given the user fills checkout information with first name "John", last name "Doe", postal code "10001"
    Then the checkout overview should list 1 item

  @regression
  Scenario: Checkout overview total equals subtotal plus tax
    Given the user fills checkout information with first name "John", last name "Doe", postal code "10001"
    Then the order total should equal the subtotal plus tax

  @smoke @positive
  Scenario: Successfully complete an order
    Given the user fills checkout information with first name "John", last name "Doe", postal code "10001"
    When the user finishes the checkout
    Then the order confirmation header should read "Thank you for your order!"

  @regression
  Scenario: Cancel from checkout information returns to cart
    When the user cancels checkout from the information page
    Then the user should be on the cart page

  @regression
  Scenario: Cancel from checkout overview returns to cart
    Given the user fills checkout information with first name "John", last name "Doe", postal code "10001"
    When the user cancels checkout from the overview page
    Then the user should be on the cart page

  @regression
  Scenario: Back to products from order confirmation returns to inventory
    Given the user fills checkout information with first name "John", last name "Doe", postal code "10001"
    And the user finishes the checkout
    When the user selects back to products
    Then the user should be redirected to the inventory page
