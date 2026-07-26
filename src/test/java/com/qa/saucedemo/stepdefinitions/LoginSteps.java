package com.qa.saucedemo.stepdefinitions;

import org.testng.Assert;

import com.qa.saucedemo.hooks.Hooks;
import com.qa.saucedemo.pages.LoginPage;
import com.qa.saucedemo.utils.ConfigReader;
import com.qa.saucedemo.utils.DriverFactory;
import com.qa.saucedemo.utils.TestContext;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {

    @Given("the user is on the SauceDemo login page")
    public void the_user_is_on_the_login_page() {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver())
                .open(Hooks.baseUrl());

        TestContext.get().setLoginPage(loginPage);
    }

    @Given("the user is logged in as {string}")
    public void the_user_is_logged_in_as(String username) {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver())
                .open(Hooks.baseUrl());

        TestContext.get().setLoginPage(loginPage);

        TestContext.get().setInventoryPage(
                loginPage.loginAs(
                        username,
                        ConfigReader.get("password.valid")
                )
        );
    }

    @When("the user logs in with username {string} and a valid password")
    public void the_user_logs_in_with_valid_password(String username) {
        LoginPage loginPage = TestContext.get().getLoginPage();

        Assert.assertNotNull(
                loginPage,
                "LoginPage was not initialized. Ensure the login-page Given step runs first."
        );

        TestContext.get().setInventoryPage(
                loginPage.loginAs(
                        username,
                        ConfigReader.get("password.valid")
                )
        );
    }

    @When("the user attempts to login with username {string} and a valid password")
    public void the_user_attempts_login_with_valid_password(String username) {
        LoginPage loginPage = TestContext.get().getLoginPage();

        Assert.assertNotNull(
                loginPage,
                "LoginPage was not initialized. Ensure the login-page Given step runs first."
        );

        TestContext.get().setLoginPage(
                loginPage.attemptLogin(
                        username,
                        ConfigReader.get("password.valid")
                )
        );
    }

    @When("the user attempts to login with username {string} and password {string}")
    public void the_user_attempts_to_login_with(
            String username,
            String password
    ) {
        LoginPage loginPage = TestContext.get().getLoginPage();

        Assert.assertNotNull(
                loginPage,
                "LoginPage was not initialized. Ensure the login-page Given step runs first."
        );

        TestContext.get().setLoginPage(
                loginPage.attemptLogin(username, password)
        );
    }

    @Then("the user should be redirected to the inventory page")
    public void the_user_should_be_redirected_to_inventory() {
        Assert.assertTrue(
                DriverFactory.getDriver()
                        .getCurrentUrl()
                        .contains("inventory.html"),
                "Expected to land on inventory.html but current URL was: "
                        + DriverFactory.getDriver().getCurrentUrl()
        );
    }

    @Then("the page title should be {string}")
    public void the_page_title_should_be(String expectedTitle) {
        Assert.assertNotNull(
                TestContext.get().getInventoryPage(),
                "InventoryPage was not initialized after login."
        );

        Assert.assertEquals(
                TestContext.get().getInventoryPage().getPageTitle(),
                expectedTitle
        );
    }

    @Then("the user should see the error message {string}")
    public void the_user_should_see_the_error_message(
            String expectedMessage
    ) {
        Assert.assertNotNull(
                TestContext.get().getLoginPage(),
                "LoginPage was not initialized."
        );

        Assert.assertTrue(
                TestContext.get().getLoginPage().isErrorDisplayed(),
                "Expected a login error message to be displayed."
        );

        Assert.assertEquals(
                TestContext.get().getLoginPage().getErrorMessage(),
                expectedMessage
        );
    }

    @When("the user logs out")
    public void the_user_logs_out() {
        Assert.assertNotNull(
                TestContext.get().getInventoryPage(),
                "InventoryPage was not initialized."
        );

        TestContext.get().setLoginPage(
                TestContext.get().getInventoryPage().logout()
        );
    }

    @Then("the user should be redirected to the login page")
    public void the_user_should_be_redirected_to_login_page() {
        Assert.assertNotNull(
                TestContext.get().getLoginPage(),
                "LoginPage was not initialized after logout."
        );

        Assert.assertTrue(
                TestContext.get().getLoginPage().isLoginButtonDisplayed(),
                "Expected the login button to be displayed."
        );
    }
}