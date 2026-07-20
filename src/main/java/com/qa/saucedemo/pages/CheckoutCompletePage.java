package com.qa.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private final By completeHeader = By.className("complete-header");
    private final By completeText = By.className("complete-text");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public String getCompleteHeaderText() {
        return getText(completeHeader);
    }

    public String getCompleteBodyText() {
        return getText(completeText);
    }

    public InventoryPage backToProducts() {
        click(backHomeButton);
        return new InventoryPage(driver);
    }
}
