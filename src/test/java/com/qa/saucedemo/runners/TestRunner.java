package com.qa.saucedemo.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Master runner. Executes every scenario in src/test/resources/features.
 * Parallel execution is configured via cucumber.properties
 * (cucumber.execution.parallel.enabled=true) and testng.xml thread-count.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.qa.saucedemo.stepdefinitions", "com.qa.saucedemo.hooks"},
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
    @org.testng.annotations.DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
