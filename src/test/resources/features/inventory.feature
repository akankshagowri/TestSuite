@inventory
Feature: Product Inventory
  As a logged-in user
  I want to browse, sort, and add products to my cart
  So that I can prepare items for purchase

  Background:
    Given the user is logged in as "standard_user"

  @smoke
  Scenario: Inventory page displays all six products
    Then 6 products should be displayed on the inventory page

  @regression
  Scenario: Add a single item to the cart
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart badge should show "1"

  @regression
  Scenario: Add multiple items to the cart
    When the user adds "Sauce Labs Backpack" to the cart
    And the user adds "Sauce Labs Bike Light" to the cart
    And the user adds "Sauce Labs Bolt T-Shirt" to the cart
    Then the cart badge should show "3"

  @regression
  Scenario: Remove an item from the inventory page after adding it
    Given the user adds "Sauce Labs Backpack" to the cart
    When the user removes "Sauce Labs Backpack" from the cart on the inventory page
    Then the cart badge should not be displayed

  @regression
  Scenario: Cart badge is absent when cart is empty
    Then the cart badge should not be displayed

  @regression
  Scenario: Sort products by name A to Z
    When the user sorts products by "Name (A to Z)"
    Then the product names should be sorted alphabetically ascending

  @regression
  Scenario: Sort products by name Z to A
    When the user sorts products by "Name (Z to A)"
    Then the product names should be sorted alphabetically descending

  @regression
  Scenario: Sort products by price low to high
    When the user sorts products by "Price (low to high)"
    Then the product prices should be sorted ascending

  @regression
  Scenario: Sort products by price high to low
    When the user sorts products by "Price (high to low)"
    Then the product prices should be sorted descending

  @regression
  Scenario: Navigating to the cart from the inventory page
    Given the user adds "Sauce Labs Fleece Jacket" to the cart
    When the user opens the cart
    Then the cart should contain 1 item
