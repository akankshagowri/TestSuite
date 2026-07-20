package com.qa.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By sortDropdown = By.className("product_sort_container");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartIcon = By.className("shopping_cart_link");
    private final By burgerMenuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");
    private final By resetAppStateLink = By.id("reset_sidebar_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public int getDisplayedItemCount() {
        return driver.findElements(inventoryItems).size();
    }

    public List<String> getAllItemNames() {
        return driver.findElements(itemNames).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public List<Double> getAllItemPrices() {
        return driver.findElements(itemPrices).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    public InventoryPage addItemToCartByName(String itemName) {
        String testId = toDataTestSuffix(itemName);
        click(By.id("add-to-cart-" + testId));
        return this;
    }

    public InventoryPage removeItemFromCartByName(String itemName) {
        String testId = toDataTestSuffix(itemName);
        click(By.id("remove-" + testId));
        return this;
    }

    private String toDataTestSuffix(String itemName) {
        return itemName.toLowerCase().replace(" ", "-");
    }

    public String getCartBadgeCount() {
        return isDisplayed(cartBadge) ? getText(cartBadge) : "0";
    }

    public CartPage goToCart() {
        click(cartIcon);
        return new CartPage(driver);
    }

    public InventoryPage sortBy(String visibleOptionText) {
        org.openqa.selenium.support.ui.Select select =
                new org.openqa.selenium.support.ui.Select(waitUtils.waitForVisibility(sortDropdown));
        select.selectByVisibleText(visibleOptionText);
        return this;
    }

    public LoginPage logout() {
        click(burgerMenuButton);
        click(logoutLink);
        return new LoginPage(driver);
    }

    public InventoryPage resetAppState() {
        click(burgerMenuButton);
        click(resetAppStateLink);
        return this;
    }
}
