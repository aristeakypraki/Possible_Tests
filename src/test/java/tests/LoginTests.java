package tests;

import helpers.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.*;
import screens.LoginScreen;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.MalformedURLException;

import static org.testng.Assert.*;


public class LoginTests {

    private static final Logger logger = Logger.getLogger(LoginTests.class.getName());


    static {
        logger.setLevel(Level.INFO);
    }


    private AndroidDriver driver;
    private LoginScreen loginScreen;


    @BeforeMethod
    public void setUpLoginTest() throws MalformedURLException {
        logger.info("Setting up login test...");
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
    }


    //visibility test
    @Test (priority = 1)
    public void shouldDisplayAllPinKeys() {
        // Έλεγχος keys 0-9 με loop
        logger.info("Checking visibility of PIN keys 0-9...");
        for (int i = 0; i <= 9; i++) {
            boolean keyVisible = loginScreen.isKeyVisible(String.valueOf(i));
            logger.info("Key " + i + " visible? " + keyVisible);
            assertTrue(keyVisible, "Key " + i + " isn't visible");

        }
        logger.info("Checking OK, Back, Exit buttons and labels...");
        assertTrue(loginScreen.isOkButtonVisible(), "OK Button isn't visible");
        assertTrue(loginScreen.isBackButtonVisible(), "Back Button isn't visible");
        assertTrue(loginScreen.isExitButtonVisible(), "Exit button isn't visible");

        assertTrue(loginScreen.isTitleLabelVisible(), "Title Label isn't visible");
        assertTrue(loginScreen.isCardViewLabelVisible(), "Card Label isn't visible");

        logger.info("All elements visibility checks passed.");
    }

    //test periexomenou keimenou
    @Test (priority = 2)
    public void shouldDisplayCorrectLoginLabels() {
        logger.info("Checking text labels on login screen...");
        String expectedTitle = "Enter PIN";
        String actualTitle = loginScreen.getTitleText();
        String actualCardLabel = loginScreen.getCardViewText();
        logger.info("Title label: " + actualTitle);
        logger.info("Card view label: " + actualCardLabel);

        assertEquals(loginScreen.getTitleText(), expectedTitle,
                "Title label text is incorrect");

        assertEquals(loginScreen.getCardViewText(), expectedTitle,
                "Card view text is incorrect");
        logger.info("Test Passed, all texts in labels are correct!!");

    }

    @Test (priority = 3)
    public void verifyWrongPinShowsAlertAndCanRetry(){
        //eisagwgi lathos PIN
        loginScreen.enterPin("1987");

        loginScreen.tapOkButton();

         String expectedText="Wrong PIN!";
         String expectedTextConfirmButton="New attempt";
         String actualPin=loginScreen.getWrongPinText();

        logger.info("Wrong Pin label: " + actualPin);

        assertTrue(loginScreen.isWrongPinPopupVisible(),"wrong Pin Pop up isn't visible");
        assertEquals(loginScreen.getWrongPinText(),expectedText,"Wrong Pin pop up Text is incorrect");
        assertEquals(loginScreen.getConfirmButtonText(),expectedTextConfirmButton,"wrong Text in confirm Button");
        loginScreen.tapConfirmButton();
        try {
            Thread.sleep(1000); // περιμένουμε 1 δευτερόλεπτο για να κλείσει το popup
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(loginScreen.isWrongPinPopupVisible(),"Wrong PIN popup did not close as expected.");

           logger.info("Test Passed,Wrong PIN popup close as expected");

    }
    @Test (priority = 4)
    public void shouldNavigateToMainMenuAfterValidPin() {

        // Εισαγωγή PIN 1111
        loginScreen.enterPin("1111");

        // Πατάμε OK
        loginScreen.tapOkButton();


        //elegxos epomenis othonis
        assertTrue(loginScreen.waitForMainActivity(2), "Did not navigate to Main menu after PIN entry");
    }


    @AfterClass
    public void tearDownClass() {
        // Εδώ μπορείς να κλείσεις τον driver μόνο όταν τελειώσουν όλα τα tests
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
    }