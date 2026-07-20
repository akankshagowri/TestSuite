package com.qa.saucedemo.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;

/**
 * Creates and tears down WebDriver instances behind a ThreadLocal so the
 * suite is safe to run in parallel (TestNG parallel="methods"/"classes")
 * without threads stomping on each other's driver sessions.
 *
 * Browser is selected via config.properties "browser" key (or -Dbrowser=...
 * on the command line): chrome (default) | firefox | edge | safari | ie
 *
 * Chrome, Firefox, and Edge use Selenium Manager (built into selenium-java
 * 4.6+) to auto-resolve a matching driver binary for your installed browser
 * version — nothing to download by hand. Safari uses Apple's built-in
 * safaridriver, already on macOS. Internet Explorer is the one exception:
 * see the IE note below.
 */
public final class DriverFactory {

    private static final Logger LOGGER = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        return DRIVER_THREAD_LOCAL.get();
    }

    public static void initDriver() {
        String browser = ConfigReader.get("browser").toLowerCase().trim();
        boolean headless = ConfigReader.getBoolean("headless");
        LOGGER.info("Starting WebDriver session -> browser={}, headless={}", browser, headless);

        WebDriver driver;
        switch (browser) {
            case "firefox":
                FirefoxOptions ffOptions = new FirefoxOptions();
                if (headless) {
                    ffOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(ffOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            case "safari":
                // Safari has no headless mode and no options-based driver
                // download; safaridriver ships with macOS. Enable it once via:
                // `safaridriver --enable` in Terminal before first run.
                if (headless) {
                    LOGGER.warn("Safari does not support headless mode; ignoring headless=true");
                }
                driver = new SafariDriver();
                break;

            case "ie":
            case "internetexplorer":
                // IMPORTANT: Internet Explorer was retired by Microsoft in 2022
                // and Selenium Manager does NOT auto-resolve IEDriverServer.
                // You must download IEDriverServer.exe yourself from
                // https://www.selenium.dev/downloads/ and either put it on
                // your PATH or set -Dwebdriver.ie.driver=<path-to-exe>.
                // IE mode also only runs on Windows. Kept here for completeness,
                // not because it's a realistic default for a new project.
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.ignoreZoomSettings();
                driver = new InternetExplorerDriver(ieOptions);
                break;

            case "chrome":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--window-size=1920,1080");
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInt("implicit.wait.seconds")));
        if (!browser.equals("safari")) {
            driver.manage().window().maximize();
        }
        DRIVER_THREAD_LOCAL.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            LOGGER.info("Closing WebDriver session");
            driver.quit();
            DRIVER_THREAD_LOCAL.remove();
        }
    }
}
