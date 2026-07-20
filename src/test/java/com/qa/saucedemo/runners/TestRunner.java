package com.qa.saucedemo.runners;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Master runner. Executes every scenario in src/test/resources/features.
 * Scenarios are executed sequentially while framework state issues are diagnosed.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.qa.saucedemo.stepdefinitions",
                "com.qa.saucedemo.hooks"
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}