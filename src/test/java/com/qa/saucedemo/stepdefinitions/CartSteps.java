package com.qa.saucedemo.stepdefinitions;

import com.qa.saucedemo.utils.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CartSteps {

    @Then("the cart should contain {int} item")
    @Then("the cart should contain {int} items")
    public void the_cart_should_contain_n_items(int expectedCount) {
        Assert.assertEquals(TestContext.get().getCartPage().getCartItemCount(), expectedCount);
    }

    @Then("the cart should list {string}")
    public void the_cart_should_list(String itemName) {
        Assert.assertTrue(TestContext.get().getCartPage().getCartItemNames().contains(itemName));
    }

    @When("the user removes {string} from the cart page")
    public void the_user_removes_item_from_cart_page(String itemName) {
        TestContext.get().getCartPage().removeItemByName(itemName);
    }

    @When("the user selects continue shopping")
    public void the_user_selects_continue_shopping() {
        TestContext.get().setInventoryPage(TestContext.get().getCartPage().continueShopping());
    }

    @When("the user proceeds to checkout")
    public void the_user_proceeds_to_checkout() {
        TestContext.get().setCheckoutInfoPage(TestContext.get().getCartPage().proceedToCheckout());
    }

    @Then("the user should be on the checkout information page")
    public void the_user_should_be_on_the_checkout_information_page() {
        Assert.assertTrue(TestContext.get().getCheckoutInfoPage().getCurrentUrl().contains("checkout-step-one"));
    }

    @Then("the user should be on the cart page")
    public void the_user_should_be_on_the_cart_page() {
        Assert.assertTrue(TestContext.get().getCartPage().getCurrentUrl().contains("cart.html"));
    }
}
