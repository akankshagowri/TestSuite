package com.qa.saucedemo.stepdefinitions;

import org.testng.Assert;

import com.qa.saucedemo.pages.CartPage;
import com.qa.saucedemo.utils.TestContext;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CartSteps {

    @Then("the cart should contain {int} item")
    @Then("the cart should contain {int} items")
    public void the_cart_should_contain_n_items(int expectedCount) {
        CartPage cartPage = TestContext.get().getCartPage();

        Assert.assertNotNull(
                cartPage,
                "CartPage was not initialized. Ensure the cart was opened first."
        );

        Assert.assertEquals(
                cartPage.getCartItemCount(),
                expectedCount,
                "Unexpected number of products in the cart."
        );
    }

    @Then("the cart should list {string}")
    public void the_cart_should_list(String itemName) {
        CartPage cartPage = TestContext.get().getCartPage();

        Assert.assertNotNull(
                cartPage,
                "CartPage was not initialized."
        );

        Assert.assertTrue(
                cartPage.getCartItemNames().contains(itemName),
                "Expected cart to contain: " + itemName
                        + ". Actual items: " + cartPage.getCartItemNames()
        );
    }

    @When("the user removes {string} from the cart page")
    public void the_user_removes_item_from_cart_page(String itemName) {
        CartPage cartPage = TestContext.get().getCartPage();

        Assert.assertNotNull(
                cartPage,
                "CartPage was not initialized."
        );

        cartPage.removeItemByName(itemName);
    }

    @When("the user selects continue shopping")
    public void the_user_selects_continue_shopping() {
        CartPage cartPage = TestContext.get().getCartPage();

        Assert.assertNotNull(
                cartPage,
                "CartPage was not initialized."
        );

        TestContext.get().setInventoryPage(
                cartPage.continueShopping()
        );
    }

    @When("the user proceeds to checkout")
    public void the_user_proceeds_to_checkout() {
        CartPage cartPage = TestContext.get().getCartPage();

        Assert.assertNotNull(
                cartPage,
                "CartPage was not initialized."
        );

        TestContext.get().setCheckoutInfoPage(
                cartPage.proceedToCheckout()
        );
    }

    @Then("the user should be on the checkout information page")
    public void the_user_should_be_on_the_checkout_information_page() {
        Assert.assertNotNull(
                TestContext.get().getCheckoutInfoPage(),
                "CheckoutInfoPage was not initialized."
        );

        Assert.assertTrue(
                TestContext.get()
                        .getCheckoutInfoPage()
                        .getCurrentUrl()
                        .contains("checkout-step-one.html"),
                "Expected checkout information page but current URL was: "
                        + TestContext.get()
                        .getCheckoutInfoPage()
                        .getCurrentUrl()
        );
    }

    @Then("the user should be on the cart page")
    public void the_user_should_be_on_the_cart_page() {
        Assert.assertNotNull(
                TestContext.get().getCartPage(),
                "CartPage was not initialized."
        );

        Assert.assertTrue(
                TestContext.get()
                        .getCartPage()
                        .getCurrentUrl()
                        .contains("cart.html"),
                "Expected cart page but current URL was: "
                        + TestContext.get()
                        .getCartPage()
                        .getCurrentUrl()
        );
    }
}