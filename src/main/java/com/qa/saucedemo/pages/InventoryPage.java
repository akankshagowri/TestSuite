package com.qa.saucedemo.pages;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class InventoryPage extends BasePage {

    private final By pageTitle =
            By.className("title");

    private final By inventoryItems =
            By.className("inventory_item");

    private final By itemNames =
            By.className("inventory_item_name");

    private final By itemPrices =
            By.className("inventory_item_price");

    private final By sortDropdown =
            By.className("product_sort_container");

    private final By cartBadge =
            By.className("shopping_cart_badge");

    private final By cartIcon =
            By.className("shopping_cart_link");

    private final By burgerMenuButton =
            By.id("react-burger-menu-btn");

    private final By logoutLink =
            By.id("logout_sidebar_link");

    private final By resetAppStateLink =
            By.id("reset_sidebar_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public int getDisplayedItemCount() {
        waitUtils.waitForPresence(inventoryItems);

        return driver.findElements(inventoryItems).size();
    }

    public List<String> getAllItemNames() {
        return waitUtils.waitForAllVisible(itemNames)
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public List<Double> getAllItemPrices() {
        return waitUtils.waitForAllVisible(itemPrices)
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .map(text -> text.replace("$", ""))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    public InventoryPage addItemToCartByName(String itemName) {
        String testId = toDataTestSuffix(itemName);

        By addButton =
                By.id("add-to-cart-" + testId);

        By removeButton =
                By.id("remove-" + testId);

        int currentCount = getCartBadgeCountAsInt();
        int expectedCount = currentCount + 1;

        waitUtils.waitForClickable(addButton).click();

        waitUtils.waitForTextEquals(
                cartBadge,
                String.valueOf(expectedCount)
        );

        waitUtils.waitForVisibility(removeButton);

        return this;
    }

    public InventoryPage removeItemFromCartByName(
            String itemName
    ) {
        String testId = toDataTestSuffix(itemName);

        By removeButton =
                By.id("remove-" + testId);

        By addButton =
                By.id("add-to-cart-" + testId);

        int currentCount = getCartBadgeCountAsInt();

        if (currentCount <= 0) {
            throw new IllegalStateException(
                    "Cannot remove item because the cart is empty"
            );
        }

        int expectedCount = currentCount - 1;

        waitUtils.waitForClickable(removeButton).click();

        waitUtils.waitForVisibility(addButton);

        if (expectedCount == 0) {
            waitUtils.waitForInvisibility(cartBadge);
        } else {
            waitUtils.waitForTextEquals(
                    cartBadge,
                    String.valueOf(expectedCount)
            );
        }

        return this;
    }

    public String getCartBadgeCount() {
        List<WebElement> badges =
                driver.findElements(cartBadge);

        if (badges.isEmpty()) {
            return "0";
        }

        try {
            String badgeText =
                    badges.get(0).getText();

            if (badgeText == null
                    || badgeText.trim().isEmpty()) {
                return "0";
            }

            return badgeText.trim();
        } catch (StaleElementReferenceException exception) {
            List<WebElement> refreshedBadges =
                    driver.findElements(cartBadge);

            if (refreshedBadges.isEmpty()) {
                return "0";
            }

            String refreshedText =
                    refreshedBadges.get(0).getText();

            if (refreshedText == null
                    || refreshedText.trim().isEmpty()) {
                return "0";
            }

            return refreshedText.trim();
        }
    }

    public boolean isCartBadgeDisplayed() {
        List<WebElement> badges =
                driver.findElements(cartBadge);

        if (badges.isEmpty()) {
            return false;
        }

        try {
            return badges.get(0).isDisplayed();
        } catch (StaleElementReferenceException exception) {
            List<WebElement> refreshedBadges =
                    driver.findElements(cartBadge);

            return !refreshedBadges.isEmpty()
                    && refreshedBadges.get(0).isDisplayed();
        }
    }

    public CartPage goToCart() {
        clickAndWaitForNavigation(
                cartIcon,
                "cart.html"
        );

        return new CartPage(driver);
    }

    public InventoryPage sortBy(
            String visibleOptionText
    ) {
        WebElement dropdown =
                waitUtils.waitForVisibility(sortDropdown);

        Select select =
                new Select(dropdown);

        select.selectByVisibleText(
                visibleOptionText
        );

        waitUtils.waitForElementValue(
                sortDropdown,
                getSortValue(visibleOptionText)
        );

        return this;
    }

    public LoginPage logout() {
        waitUtils.waitForClickable(
                burgerMenuButton
        ).click();

        waitUtils.waitForClickable(
                logoutLink
        );

        clickAndWaitForNavigation(
                logoutLink,
                "saucedemo.com"
        );

        return new LoginPage(driver);
    }

    public InventoryPage resetAppState() {
        waitUtils.waitForClickable(
                burgerMenuButton
        ).click();

        waitUtils.waitForClickable(
                resetAppStateLink
        ).click();

        waitUtils.waitForInvisibility(
                cartBadge
        );

        return this;
    }

    private int getCartBadgeCountAsInt() {
        String badgeCount =
                getCartBadgeCount();

        try {
            return Integer.parseInt(
                    badgeCount
            );
        } catch (NumberFormatException exception) {
            throw new IllegalStateException(
                    "Cart badge did not contain a valid integer: "
                            + badgeCount,
                    exception
            );
        }
    }

    private String toDataTestSuffix(
            String itemName
    ) {
        if (itemName == null
                || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Item name cannot be null or empty"
            );
        }

        return itemName
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    private String getSortValue(
            String visibleOptionText
    ) {
        switch (visibleOptionText) {
            case "Name (A to Z)":
                return "az";

            case "Name (Z to A)":
                return "za";

            case "Price (low to high)":
                return "lohi";

            case "Price (high to low)":
                return "hilo";

            default:
                throw new IllegalArgumentException(
                        "Unsupported sort option: "
                                + visibleOptionText
                );
        }
    }
}