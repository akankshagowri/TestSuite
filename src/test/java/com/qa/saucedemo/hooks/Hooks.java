package com.qa.saucedemo.hooks;

import com.qa.saucedemo.utils.ConfigReader;
import com.qa.saucedemo.utils.DriverFactory;
import com.qa.saucedemo.utils.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {

    private static final Logger LOGGER = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        LOGGER.info(">>> Starting scenario: {}", scenario.getName());
        DriverFactory.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        if (scenario.isFailed() && driver != null) {
            LOGGER.error("<<< FAILED: {} — attaching screenshot", scenario.getName());
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName().replaceAll("\\s+", "_"));
        } else {
            LOGGER.info("<<< PASSED: {}", scenario.getName());
        }
        DriverFactory.quitDriver();
        TestContext.reset();
    }

    public static String baseUrl() {
        return ConfigReader.get("base.url");
    }
}
