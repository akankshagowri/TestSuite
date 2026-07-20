package com.qa.saucedemo.stepdefinitions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.testng.Assert;

import com.qa.saucedemo.utils.TestContext;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class InventorySteps {

    @Then("{int} products should be displayed on the inventory page")
    public void n_products_should_be_displayed(int expectedCount) {
        Assert.assertEquals(TestContext.get().getInventoryPage().getDisplayedItemCount(), expectedCount);
    }

   
    @When("the user adds {string} to the cart")
    public void the_user_adds_item_to_the_cart(String itemName) {
        TestContext.get().getInventoryPage().addItemToCartByName(itemName);
    }

    @When("the user removes {string} from the cart on the inventory page")
    public void the_user_removes_item_from_inventory(String itemName) {
        TestContext.get().getInventoryPage().removeItemFromCartByName(itemName);
    }

    @Then("the cart badge should show {string}")
    public void the_cart_badge_should_show(String expectedCount) {
        Assert.assertEquals(TestContext.get().getInventoryPage().getCartBadgeCount(), expectedCount);
    }

    @Then("the cart badge should not be displayed")
    public void the_cart_badge_should_not_be_displayed() {
        Assert.assertEquals(TestContext.get().getInventoryPage().getCartBadgeCount(), "0");
    }

    @When("the user sorts products by {string}")
    public void the_user_sorts_products_by(String option) {
        TestContext.get().getInventoryPage().sortBy(option);
    }

    @Then("the product names should be sorted alphabetically ascending")
    public void the_product_names_should_be_sorted_ascending() {
        List<String> names = TestContext.get().getInventoryPage().getAllItemNames();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(Comparator.naturalOrder());
        Assert.assertEquals(names, sorted);
    }

    @Then("the product names should be sorted alphabetically descending")
    public void the_product_names_should_be_sorted_descending() {
        List<String> names = TestContext.get().getInventoryPage().getAllItemNames();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(Comparator.reverseOrder());
        Assert.assertEquals(names, sorted);
    }

    @Then("the product prices should be sorted ascending")
    public void the_product_prices_should_be_sorted_ascending() {
        List<Double> prices = TestContext.get().getInventoryPage().getAllItemPrices();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Comparator.naturalOrder());
        Assert.assertEquals(prices, sorted);
    }

    @Then("the product prices should be sorted descending")
    public void the_product_prices_should_be_sorted_descending() {
        List<Double> prices = TestContext.get().getInventoryPage().getAllItemPrices();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Comparator.reverseOrder());
        Assert.assertEquals(prices, sorted);
    }

    @When("the user opens the cart")
    public void the_user_opens_the_cart() {
        TestContext.get().setCartPage(TestContext.get().getInventoryPage().goToCart());
    }
}
