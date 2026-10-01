package com.qa.saucedemo.pages;

import org.testng.Assert;

import com.qa.saucedemo.utils.TestContext;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CheckoutSteps {

    @When(
            "the user fills checkout information with first name {string}, "
                    + "last name {string}, postal code {string}"
    )
    public void the_user_fills_checkout_information(
            String firstName,
            String lastName,
            String postalCode
    ) {
        CheckoutInfoPage checkoutInfoPage =
                requireCheckoutInfoPage();

        boolean hasEmptyField =
                firstName == null
                        || lastName == null
                        || postalCode == null
                        || firstName.trim().isEmpty()
                        || lastName.trim().isEmpty()
                        || postalCode.trim().isEmpty();

        if (hasEmptyField) {
            CheckoutInfoPage currentPage =
                    checkoutInfoPage.attemptContinueWithMissingField(
                            firstName == null ? "" : firstName,
                            lastName == null ? "" : lastName,
                            postalCode == null ? "" : postalCode
                    );

            TestContext.get().setCheckoutInfoPage(
                    currentPage
            );

            /*
             * Invalid checkout information means the user remains
             * on checkout-step-one.html. Ensure an old overview-page
             * reference cannot accidentally be reused.
             */
            TestContext.get().setCheckoutOverviewPage(
                    null
            );
        } else {
            CheckoutOverviewPage checkoutOverviewPage =
                    checkoutInfoPage.fillInfoAndContinue(
                            firstName,
                            lastName,
                            postalCode
                    );

            TestContext.get().setCheckoutOverviewPage(
                    checkoutOverviewPage
            );
        }
    }

    @Then("the user should be on the checkout overview page")
    public void the_user_should_be_on_the_checkout_overview_page() {
        CheckoutOverviewPage checkoutOverviewPage =
                requireCheckoutOverviewPage();

        Assert.assertTrue(
                checkoutOverviewPage
                        .getCurrentUrl()
                        .contains("checkout-step-two.html"),
                "Expected checkout overview page, but current URL was: "
                        + checkoutOverviewPage.getCurrentUrl()
        );
    }

    @Then("the user should see the checkout error {string}")
    public void the_user_should_see_the_checkout_error(
            String expectedMessage
    ) {
        CheckoutInfoPage checkoutInfoPage =
                requireCheckoutInfoPage();

        Assert.assertEquals(
                checkoutInfoPage.getErrorMessage(),
                expectedMessage,
                "Unexpected checkout validation message."
        );
    }

    @Then("the checkout overview should list {int} item")
    @Then("the checkout overview should list {int} items")
    public void the_checkout_overview_should_list_n_items(
            int expectedCount
    ) {
        CheckoutOverviewPage checkoutOverviewPage =
                requireCheckoutOverviewPage();

        Assert.assertEquals(
                checkoutOverviewPage.getItemCount(),
                expectedCount,
                "Unexpected number of items on checkout overview."
        );
    }

    @Then("the order total should equal the subtotal plus tax")
    public void the_order_total_should_equal_subtotal_plus_tax() {
        CheckoutOverviewPage checkoutOverviewPage =
                requireCheckoutOverviewPage();

        double subtotal =
                checkoutOverviewPage.getSubtotal();

        double tax =
                checkoutOverviewPage.getTax();

        double total =
                checkoutOverviewPage.getTotal();

        Assert.assertEquals(
                total,
                subtotal + tax,
                0.01,
                "Order total did not equal subtotal plus tax."
        );
    }

    @When("the user finishes the checkout")
    public void the_user_finishes_the_checkout() {
        CheckoutOverviewPage checkoutOverviewPage =
                requireCheckoutOverviewPage();

        CheckoutCompletePage checkoutCompletePage =
                checkoutOverviewPage.finishCheckout();

        TestContext.get().setCheckoutCompletePage(
                checkoutCompletePage
        );
    }

    @Then("the order confirmation header should read {string}")
    public void the_order_confirmation_header_should_read(
            String expectedHeader
    ) {
        CheckoutCompletePage checkoutCompletePage =
                requireCheckoutCompletePage();

        Assert.assertEquals(
                checkoutCompletePage.getCompleteHeaderText(),
                expectedHeader,
                "Unexpected order confirmation header."
        );
    }

    @When("the user cancels checkout from the information page")
    public void the_user_cancels_checkout_from_information_page() {
        CheckoutInfoPage checkoutInfoPage =
                requireCheckoutInfoPage();

        TestContext.get().setCartPage(
                checkoutInfoPage.cancel()
        );
    }

    @When("the user cancels checkout from the overview page")
    public void the_user_cancels_checkout_from_overview_page() {
        CheckoutOverviewPage checkoutOverviewPage =
                requireCheckoutOverviewPage();

        TestContext.get().setInventoryPage(
                checkoutOverviewPage.cancel()
        );
    }

    @When("the user selects back to products")
    public void the_user_selects_back_to_products() {
        CheckoutCompletePage checkoutCompletePage =
                requireCheckoutCompletePage();

        TestContext.get().setInventoryPage(
                checkoutCompletePage.backToProducts()
        );
    }

    private CheckoutInfoPage requireCheckoutInfoPage() {
        CheckoutInfoPage page =
                TestContext.get().getCheckoutInfoPage();

        if (page == null) {
            throw new IllegalStateException(
                    "CheckoutInfoPage was not initialized. "
                            + "The user must proceed to checkout first."
            );
        }

        return page;
    }

    private CheckoutOverviewPage requireCheckoutOverviewPage() {
        CheckoutOverviewPage page =
                TestContext.get().getCheckoutOverviewPage();

        if (page == null) {
            throw new IllegalStateException(
                    "CheckoutOverviewPage was not initialized. "
                            + "The scenario must submit valid checkout "
                            + "information before using an overview-page step."
            );
        }

        return page;
    }

    private CheckoutCompletePage requireCheckoutCompletePage() {
        CheckoutCompletePage page =
                TestContext.get().getCheckoutCompletePage();

        if (page == null) {
            throw new IllegalStateException(
                    "CheckoutCompletePage was not initialized. "
                            + "The user must finish checkout first."
            );
        }

        return page;
    }
}