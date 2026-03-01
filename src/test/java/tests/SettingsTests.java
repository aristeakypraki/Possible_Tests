package tests;

import helpers.BaseTest;
import helpers.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.*;
import screens.LoginScreen;
import screens.SettingsScreen;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.MalformedURLException;

import static org.testng.Assert.*;

public class SettingsTests extends BaseTest {
    private static final Logger logger = Logger.getLogger(SettingsTests.class.getName());


    static {
        logger.setLevel(Level.INFO);
    }
    private AndroidDriver driver;
    private SettingsScreen settingsScreen;
    private LoginScreen loginScreen;

    @BeforeMethod
    public void setupTest() throws MalformedURLException {
        if (!DriverFactory.isDriverAlive()) {
            driver = DriverFactory.getDriver();
        } else {
            driver = DriverFactory.getDriver(); // παίρνουμε τον ίδιο driver
        }
        settingsScreen = new SettingsScreen(driver);
        loginScreen = new LoginScreen(driver);
        loginScreen.waitForMainActivity(2);
        settingsScreen.tapSettingsButton();
        // Βεβαιωνόμαστε ότι είμαστε στο Settings root
        goToSettingsRoot();

        // Φτιάχνουμε το screen object

    }


    @Test
    public void buttonHasClicked() {


        settingsScreen.openSystemInformation();

        Assert.assertTrue(settingsScreen.isTitleVisible(),"something went wrong");

    }

    @Test
    public void buttonHasClicked2() {


        settingsScreen.openSystemInformation();

        Assert.assertTrue(settingsScreen.isTitleVisible(),"something went wrong");

    }


    @AfterMethod
    public void resetState() {
        // Επαναφέρουμε στο Settings root για το επόμενο test
        goToSettingsRoot();
    }


}
