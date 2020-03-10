package com.aig;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseTest {
    public RemoteWebDriver driver;

    @Rule
    public final TestRule watchman = new TestWatcher() {
        public String testStatus;

        // This method gets invoked if the test fails for any reason:
        @Override
        protected void failed(Throwable e, Description description) {
            testStatus = "failed";
        }
        @Override
        protected void succeeded(Description description) {
            testStatus = "passed";
        }
    };
    @Rule
    public TestName testName = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    @Before
    public void setup() {
        String sauceUsername = System.getenv("SAUCE_USERNAME");
        String sauceAccessKey = System.getenv("SAUCE_ACCESS_KEY");

        ChromeOptions chromeOpts = new ChromeOptions();
        chromeOpts.setCapability(CapabilityType.PLATFORM_NAME, "windows 10");
        chromeOpts.setCapability(CapabilityType.BROWSER_VERSION, "latest");

        MutableCapabilities sauceOpts = new MutableCapabilities();
        sauceOpts.setCapability("username", sauceUsername);
        sauceOpts.setCapability("accessKey", sauceAccessKey);
        //TODO future addition
        sauceOpts.setCapability("name", testName.getMethodName());
        sauceOpts.setCapability("build", "best-practices");
        sauceOpts.setCapability("tags", "['best-practices', 'best-practices']");

        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY,  chromeOpts);
        capabilities.setCapability("sauce:options", sauceOpts);

        String sauceUrl = "https://ondemand.saucelabs.com/wd/hub";
        URL url = null;
        try {
            url = new URL(sauceUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver = new RemoteWebDriver(url, capabilities);
    }

    @After
    public void tearDown() {
        //updateResult(watchman);
        stop();
    }

    private void stop() {
        if(driver != null)
        {
            driver.quit();
        }
    }

    private void updateResult(String result) {
        getJSExecutor().executeScript("sauce:job-result=" + result);
    }
    protected JavascriptExecutor getJSExecutor() {
        return driver;
    }
}