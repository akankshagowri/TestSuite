package com.qa.saucedemo.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    private final By cartItems =
            By.className("cart_item");

    private final By itemNames =
            By.className("inventory_item_name");

    private final By checkoutButton =
            By.id("checkout");

    private final By continueShoppingButton =
            By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public List<String> getCartItemNames() {
        return driver.findElements(itemNames)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public CartPage removeItemByName(String itemName) {
        String testId = itemName
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", "-");

        By removeButton = By.id("remove-" + testId);

        int currentCount = getCartItemCount();

        if (currentCount == 0) {
            throw new IllegalStateException(
                    "Cannot remove an item because the cart is empty."
            );
        }

        click(removeButton);

        waitUtils.waitForNumberOfElements(
                cartItems,
                currentCount - 1
        );

        return this;
    }

    public InventoryPage continueShopping() {
        clickAndWaitForNavigation(
                continueShoppingButton,
                "inventory.html"
        );

        return new InventoryPage(driver);
    }

    public CheckoutInfoPage proceedToCheckout() {
        clickAndWaitForNavigation(
                checkoutButton,
                "checkout-step-one.html"
        );

        return new CheckoutInfoPage(driver);
    }
}