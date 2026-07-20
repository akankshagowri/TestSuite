@cart
Feature: Shopping Cart
  As a logged-in user
  I want to review and modify the contents of my cart
  So that I only purchase the items I intend to buy

  Background:
    Given the user is logged in as "standard_user"

  @smoke
  Scenario: Cart correctly reflects a single added item
    Given the user adds "Sauce Labs Backpack" to the cart
    When the user opens the cart
    Then the cart should contain 1 item
    And the cart should list "Sauce Labs Backpack"

  @regression
  Scenario: Cart correctly reflects multiple added items
    Given the user adds "Sauce Labs Backpack" to the cart
    And the user adds "Sauce Labs Onesie" to the cart
    When the user opens the cart
    Then the cart should contain 2 items

  @regression
  Scenario: Remove an item directly from the cart page
    Given the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    When the user removes "Sauce Labs Backpack" from the cart page
    Then the cart should contain 0 items

  @regression
  Scenario: Continue shopping returns to the inventory page
    Given the user adds "Sauce Labs Backpack" to the cart
    And the user opens the cart
    When the user selects continue shopping
    Then the user should be redirected to the inventory page

  @regression @negative
  Scenario: Proceeding to checkout with an empty cart still reaches checkout info
    When the user opens the cart
    And the user proceeds to checkout
    Then the user should be on the checkout information page

  @regression
  Scenario: Proceed to checkout from a populated cart
    Given the user adds "Sauce Labs Bike Light" to the cart
    And the user opens the cart
    When the user proceeds to checkout
    Then the user should be on the checkout information page
