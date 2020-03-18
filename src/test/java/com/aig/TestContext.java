package com.aig;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class TestContext extends TestWatcher {
    private WebDriver driver;

    private JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }

    public void setDriver(WebDriver webDriver) {
        driver = webDriver;
    }
    @Override
    public void failed(Throwable e, Description description) {
        getJSExecutor().executeScript("sauce:job-result=failed");
        driver.quit();
    }

    @Override
    public void succeeded(Description description) {
        getJSExecutor().executeScript("sauce:job-result=passed");
        driver.quit();
    }
}
