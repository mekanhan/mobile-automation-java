package steps;

import io.appium.java_client.AppiumDriver;
import mobile.automation.pages.app1.IOSExplorerPage;
import mobile.automation.pages.base.BasePage;

/**
 * Shared test context for dependency injection between Hooks and Steps
 * This is instantiated once per scenario by Cucumber's PicoContainer
 */
public class TestContext {

    private AppiumDriver driver;
    private IOSExplorerPage explorerPage;
    private BasePage currentPage;

    /**
     * Set the driver (called from Hooks)
     */
    public void setDriver(AppiumDriver driver) {
        this.driver = driver;
        this.explorerPage = new IOSExplorerPage(driver);
        this.currentPage = explorerPage;
    }

    /**
     * Get the driver
     */
    public AppiumDriver getDriver() {
        return driver;
    }

    /**
     * Get the Explorer page
     */
    public IOSExplorerPage getExplorerPage() {
        return explorerPage;
    }

    /**
     * Get current page
     */
    public BasePage getCurrentPage() {
        return currentPage;
    }

    /**
     * Set current page
     */
    public void setCurrentPage(BasePage page) {
        this.currentPage = page;
    }
}
