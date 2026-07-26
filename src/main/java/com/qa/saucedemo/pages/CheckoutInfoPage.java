package com.qa.saucedemo.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckoutInfoPage extends BasePage {

    private final By firstNameInput =
            By.id("first-name");

    private final By lastNameInput =
            By.id("last-name");

    private final By postalCodeInput =
            By.id("postal-code");

    private final By continueButton =
            By.id("continue");

    private final By cancelButton =
            By.id("cancel");

    private final By errorMessage =
            By.cssSelector("h3[data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutOverviewPage fillInfoAndContinue(
            String firstName,
            String lastName,
            String postalCode
    ) {
        enterCheckoutInformation(
                firstName,
                lastName,
                postalCode
        );

        click(continueButton);

        try {
            waitUtils.waitForNavigationTo(
                    "checkout-step-two.html"
            );
        } catch (TimeoutException exception) {
            throw new AssertionError(
                    buildCheckoutFailureMessage(
                            firstName,
                            lastName,
                            postalCode
                    ),
                    exception
            );
        }

        return new CheckoutOverviewPage(driver);
    }

    public CheckoutInfoPage attemptContinueWithMissingField(
            String firstName,
            String lastName,
            String postalCode
    ) {
        enterCheckoutInformation(
                firstName,
                lastName,
                postalCode
        );

        click(continueButton);

        waitUtils.waitForVisibility(errorMessage);

        return this;
    }

    private void enterCheckoutInformation(
            String firstName,
            String lastName,
            String postalCode
    ) {
        type(firstNameInput, firstName);
        waitUtils.waitForElementValue(firstNameInput, firstName);

        type(lastNameInput, lastName);
        waitUtils.waitForElementValue(lastNameInput, lastName);

        type(postalCodeInput, postalCode);
        waitUtils.waitForElementValue(postalCodeInput, postalCode);
    }

    private String buildCheckoutFailureMessage(
            String expectedFirstName,
            String expectedLastName,
            String expectedPostalCode
    ) {
        String actualFirstName = getInputValue(firstNameInput);
        String actualLastName = getInputValue(lastNameInput);
        String actualPostalCode = getInputValue(postalCodeInput);
        String validationMessage = getOptionalErrorMessage();

        return "Checkout did not navigate to checkout-step-two.html."
                + System.lineSeparator()
                + "Current URL: " + driver.getCurrentUrl()
                + System.lineSeparator()
                + "Expected first name: " + expectedFirstName
                + System.lineSeparator()
                + "Actual first name: " + actualFirstName
                + System.lineSeparator()
                + "Expected last name: " + expectedLastName
                + System.lineSeparator()
                + "Actual last name: " + actualLastName
                + System.lineSeparator()
                + "Expected postal code: " + expectedPostalCode
                + System.lineSeparator()
                + "Actual postal code: " + actualPostalCode
                + System.lineSeparator()
                + "Validation message: " + validationMessage;
    }

    private String getInputValue(By locator) {
        List<WebElement> elements = driver.findElements(locator);

        if (elements.isEmpty()) {
            return "<element not found>";
        }

        String value = elements.get(0).getAttribute("value");

        return value == null ? "<null>" : value;
    }

    private String getOptionalErrorMessage() {
        List<WebElement> errors = driver.findElements(errorMessage);

        if (errors.isEmpty()) {
            return "<no validation message displayed>";
        }

        return errors.get(0).getText();
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public CartPage cancel() {
        clickAndWaitForNavigation(
                cancelButton,
                "cart.html"
        );

        return new CartPage(driver);
    }
}