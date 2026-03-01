package helpers;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import screens.LoginScreen;


import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseTest {

    protected AndroidDriver driver;
    private LoginScreen loginScreen;
    private static final Logger logger = Logger.getLogger(BaseTest.class.getName());


    static {
        logger.setLevel(Level.INFO);
    }


    @BeforeSuite
    public void setUp() throws MalformedURLException {
        logger.info("Setting up the app...");
        // Παίρνει driver μόνο αν δεν υπάρχει ήδη
        if (!DriverFactory.isDriverAlive()) {
            driver = DriverFactory.getDriver();
            logger.info("Driver created and app launched.");
        } else {
            driver = DriverFactory.getDriver(); // παίρνουμε τον ίδιο driver
            logger.info("Using existing driver, app already open.");
        }

        loginScreen = new LoginScreen(driver);

        boolean activityChanged = loginScreen.waitForActivityToChange(
                ".InitialActivity",
                10
        );
        if (!activityChanged) {
            System.out.println("Η εφαρμογή είναι ήδη στο Login Screen ή δεν προχώρησε από InitialActivity");
        }

        loginScreen.enterPin("1111");
        loginScreen.tapOkButton();

        logger.info("logged in");
    }

    private boolean isAtSettingsRoot() {
        return driver.findElements(By.id("tab_view_5_a")).size() > 0;
    }
    public void goToSettingsRoot() {
        // Αν είμαστε σε sub-menu, πατάμε back μέχρι να φτάσουμε στο root
        while (!isAtSettingsRoot()) {
            driver.navigate().back();
        }

    }

    @AfterSuite
    public void globalTearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
