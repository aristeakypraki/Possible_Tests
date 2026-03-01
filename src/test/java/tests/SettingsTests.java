package tests;

import helpers.BaseTest;
import helpers.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import screens.LoginScreen;
import screens.SettingsScreen;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.MalformedURLException;


public class SettingsTests extends BaseTest {
    private static final Logger logger = Logger.getLogger(SettingsTests.class.getName());


    static {
        logger.setLevel(Level.INFO);
    }
    private SettingsScreen settingsScreen;

    @BeforeMethod
    public void setupTest() throws MalformedURLException {
        AndroidDriver driver = DriverFactory.getDriver();

        settingsScreen = new SettingsScreen(driver);
        LoginScreen loginScreen = new LoginScreen(driver);
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