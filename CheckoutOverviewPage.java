package com.qa.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutOverviewPage extends BasePage {

    private final By cartItems =
            By.className("cart_item");

    private final By subtotalLabel =
            By.className("summary_subtotal_label");

    private final By taxLabel =
            By.className("summary_tax_label");

    private final By totalLabel =
            By.className("summary_total_label");

    private final By finishButton =
            By.id("finish");

    private final By cancelButton =
            By.id("cancel");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        return driver.findElements(
                cartItems
        ).size();
    }

    public double getSubtotal() {
        return parseAmount(
                getText(subtotalLabel)
        );
    }

    public double getTax() {
        return parseAmount(
                getText(taxLabel)
        );
    }

    public double getTotal() {
        return parseAmount(
                getText(totalLabel)
        );
    }

    private double parseAmount(String label) {
        if (label == null || label.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Price label cannot be null or empty"
            );
        }

        String numericValue =
                label.replaceAll("[^0-9.]", "");

        if (numericValue.isEmpty()) {
            throw new IllegalStateException(
                    "Could not extract amount from label: "
                            + label
            );
        }

        return Double.parseDouble(
                numericValue
        );
    }

    public CheckoutCompletePage finishCheckout() {
        clickAndWaitForNavigation(
                finishButton,
                "checkout-complete.html"
        );

        return new CheckoutCompletePage(
                driver
        );
    }

    public InventoryPage cancel() {
        clickAndWaitForNavigation(
                cancelButton,
                "inventory.html"
        );

        return new InventoryPage(
                driver
        );
    }
}