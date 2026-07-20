package com.qa.saucedemo.stepdefinitions;

import org.testng.Assert;

import com.qa.saucedemo.utils.TestContext;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CheckoutSteps {

  
    @When("the user fills checkout information with first name {string}, last name {string}, postal code {string}")
    public void the_user_fills_checkout_information(String firstName, String lastName, String postalCode) {
        boolean hasEmptyField = firstName.isEmpty() || lastName.isEmpty() || postalCode.isEmpty();
        if (hasEmptyField) {
            TestContext.get().setCheckoutInfoPage(
                    TestContext.get().getCheckoutInfoPage()
                            .attemptContinueWithMissingField(firstName, lastName, postalCode));
        } else {
            TestContext.get().setCheckoutOverviewPage(
                    TestContext.get().getCheckoutInfoPage()
                            .fillInfoAndContinue(firstName, lastName, postalCode));
        }
    }

    @Then("the user should be on the checkout overview page")
    public void the_user_should_be_on_the_checkout_overview_page() {
        Assert.assertTrue(TestContext.get().getCheckoutOverviewPage().getCurrentUrl().contains("checkout-step-two"));
    }

    @Then("the user should see the checkout error {string}")
    public void the_user_should_see_the_checkout_error(String expectedMessage) {
        Assert.assertEquals(TestContext.get().getCheckoutInfoPage().getErrorMessage(), expectedMessage);
    }

    @Then("the checkout overview should list {int} item")
    @Then("the checkout overview should list {int} items")
    public void the_checkout_overview_should_list_n_items(int expectedCount) {
        Assert.assertEquals(TestContext.get().getCheckoutOverviewPage().getItemCount(), expectedCount);
    }

    @Then("the order total should equal the subtotal plus tax")
    public void the_order_total_should_equal_subtotal_plus_tax() {
        double subtotal = TestContext.get().getCheckoutOverviewPage().getSubtotal();
        double tax = TestContext.get().getCheckoutOverviewPage().getTax();
        double total = TestContext.get().getCheckoutOverviewPage().getTotal();
        Assert.assertEquals(total, subtotal + tax, 0.01);
    }

    @When("the user finishes the checkout")
    public void the_user_finishes_the_checkout() {
        TestContext.get().setCheckoutCompletePage(TestContext.get().getCheckoutOverviewPage().finishCheckout());
    }

    @Then("the order confirmation header should read {string}")
    public void the_order_confirmation_header_should_read(String expectedHeader) {
        Assert.assertEquals(TestContext.get().getCheckoutCompletePage().getCompleteHeaderText(), expectedHeader);
    }

    @When("the user cancels checkout from the information page")
    public void the_user_cancels_checkout_from_information_page() {
        TestContext.get().setCartPage(TestContext.get().getCheckoutInfoPage().cancel());
    }

    @When("the user cancels checkout from the overview page")
    public void the_user_cancels_checkout_from_overview_page() {
        TestContext.get().setCartPage(TestContext.get().getCheckoutOverviewPage().cancel());
    }

    @When("the user selects back to products")
    public void the_user_selects_back_to_products() {
        TestContext.get().setInventoryPage(TestContext.get().getCheckoutCompletePage().backToProducts());
    }
}
