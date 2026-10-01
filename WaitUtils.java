package com.qa.saucedemo.utils;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(
                        ConfigReader.getInt("explicit.wait.seconds")
                )
        );

        /*
         * These exceptions can occur briefly while React updates the DOM.
         * WebDriverWait will retry instead of failing immediately.
         */
        this.wait.ignoring(
                StaleElementReferenceException.class
        );
    }

    public WebElement waitForVisibility(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        locator
                )
        );
    }

    public List<WebElement> waitForAllVisible(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        locator
                )
        );
    }

    public WebElement waitForClickable(By locator) {
        return wait.until(
                ExpectedConditions.elementToBeClickable(
                        locator
                )
        );
    }

    public boolean waitForPresence(By locator) {
        return wait.until(currentDriver ->
                !currentDriver.findElements(locator).isEmpty()
        );
    }

    public boolean waitForInvisibility(By locator) {
        return wait.until(currentDriver -> {
            List<WebElement> elements =
                    currentDriver.findElements(locator);

            if (elements.isEmpty()) {
                return true;
            }

            try {
                return elements.stream()
                        .noneMatch(WebElement::isDisplayed);
            } catch (StaleElementReferenceException exception) {
                return true;
            }
        });
    }

    public boolean waitForUrlContains(String fragment) {
        return wait.until(
                ExpectedConditions.urlContains(
                        fragment
                )
        );
    }

    public boolean waitForTextPresent(
            By locator,
            String expectedText
    ) {
        return wait.until(currentDriver -> {
            List<WebElement> elements =
                    currentDriver.findElements(locator);

            if (elements.isEmpty()) {
                return false;
            }

            try {
                String actualText =
                        elements.get(0).getText();

                return actualText != null
                        && actualText.trim()
                                .contains(expectedText);
            } catch (StaleElementReferenceException exception) {
                return false;
            }
        });
    }

    public boolean waitForTextEquals(
            By locator,
            String expectedText
    ) {
        return wait.until(currentDriver -> {
            List<WebElement> elements =
                    currentDriver.findElements(locator);

            if (elements.isEmpty()) {
                return false;
            }

            try {
                String actualText =
                        elements.get(0).getText();

                return actualText != null
                        && actualText.trim()
                                .equals(expectedText);
            } catch (StaleElementReferenceException exception) {
                return false;
            }
        });
    }

    public boolean waitForNumberOfElements(
            By locator,
            int expectedCount
    ) {
        return wait.until(currentDriver ->
                currentDriver.findElements(locator).size()
                        == expectedCount
        );
    }

    public boolean waitForElementAttribute(
            By locator,
            String attribute,
            String expectedValue
    ) {
        return wait.until(
                ExpectedConditions.attributeToBe(
                        locator,
                        attribute,
                        expectedValue
                )
        );
    }

    public boolean waitForElementValue(
            By locator,
            String expectedValue
    ) {
        return wait.until(
                ExpectedConditions.attributeToBe(
                        locator,
                        "value",
                        expectedValue
                )
        );
    }

    public boolean waitForElementToDisappear(
            By locator
    ) {
        return wait.until(currentDriver ->
                currentDriver.findElements(locator).isEmpty()
        );
    }

    public boolean waitForElementCountGreaterThan(
            By locator,
            int minimumCount
    ) {
        return wait.until(currentDriver ->
                currentDriver.findElements(locator).size()
                        > minimumCount
        );
    }

    public void waitForPageLoadComplete() {
        Function<WebDriver, Boolean> pageLoaded =
                currentDriver -> {
                    Object readyState =
                            ((JavascriptExecutor) currentDriver)
                                    .executeScript(
                                            "return document.readyState"
                                    );

                    return "complete".equals(
                            readyState
                    );
                };

        wait.until(pageLoaded);
    }

    public void waitForNavigationTo(
            String urlFragment
    ) {
        waitForUrlContains(urlFragment);
        waitForPageLoadComplete();
    }
}