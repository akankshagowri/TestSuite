package com.qa.saucedemo.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Runs only @smoke-tagged scenarios. Intended for a fast CI gate (PR checks)
 * separate from the full @regression suite used in the nightly build.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.qa.saucedemo.stepdefinitions", "com.qa.saucedemo.hooks"},
        tags = "@smoke",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/smoke-cucumber.html",
                "json:target/cucumber-reports/smoke-cucumber.json"
        },
        monochrome = true
)
public class SmokeTestRunner extends AbstractTestNGCucumberTests {
}
