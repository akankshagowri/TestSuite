package com.qa.saucedemo.pages;

import com.qa.saucedemo.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * Base class for all Page Objects. Holds the shared WebDriver reference and
 * a WaitUtils instance so every page interacts with the DOM only through
 * explicit waits.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils waitUtils;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    protected void click(By locator) {
        WebElement element = waitUtils.waitForClickable(locator);
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            // Headless/CI browsers occasionally report an element as
            // "clickable" a frame before it's actually interactable
            // (animation/overlay still settling). A JS click bypasses the
            // native click-interception check and is the standard,
            // reliable fallback rather than adding a fixed sleep.
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /** Use for any click that triggers navigation to a new page/URL.
     * Confirms the destination URL fragment appears AND the document has
     * finished loading before returning control to the caller — prevents
     * the next page object's element waits from racing an in-flight
     * navigation, which is what caused intermittent CI-only timeouts. */
    protected void clickAndWaitForNavigation(By locator, String expectedUrlFragment) {
        click(locator);
        waitUtils.waitForNavigationTo(expectedUrlFragment);
    }

    protected void type(By locator, String text) {
        WebElement element = waitUtils.waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitUtils.waitForVisibility(locator).getText();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitUtils.waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }
}
