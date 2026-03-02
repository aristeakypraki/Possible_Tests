package tests;

import helpers.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.*;
import screens.LoginScreen;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.testng.Assert.*;

public class LoginTests {

    private static final Logger logger = Logger.getLogger(LoginTests.class.getName());

    static {
        logger.setLevel(Level.INFO);
    }

    private AndroidDriver driver;
    private LoginScreen loginScreen;

    @BeforeMethod(alwaysRun = true)
    public void setUpLoginTest() throws MalformedURLException {
        logger.info("Setting up login test...");

        // παίρνουμε driver (είτε είναι νέος είτε όχι)
        driver = DriverFactory.getDriver();
        loginScreen = new LoginScreen(driver);

        // wait να περάσει το initial αν υπάρχει
        boolean activityChanged = loginScreen.waitForActivityToChange(".InitialActivity", 15);
        if (!activityChanged) {
            logger.info("App already on Login screen or did not move from InitialActivity");
        }

        logger.info("Current activity: " + driver.currentActivity());
    }

    // ✅ Attachments only on FAILURE
    @AfterMethod(alwaysRun = true)
    public void attachAllureArtifactsOnFailure(ITestResult result) {
        if (result.getStatus() != ITestResult.FAILURE) return;

        try {
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Failure Screenshot", new ByteArrayInputStream(screenshot));

                String pageSource = driver.getPageSource();
                Allure.addAttachment("Page Source", "text/xml", pageSource, ".xml");

                try {
                    Allure.addAttachment("Current Activity", driver.currentActivity());
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            Allure.addAttachment("Allure attachment error", e.toString());
        }
    }


    @AfterMethod(alwaysRun = true)
    public void restartAppIfWrongPinTestFailed(ITestResult result) {
        if (result.getStatus() != ITestResult.FAILURE) return;
        if (!"verifyWrongPinShowsAlertAndCanRetry".equals(result.getMethod().getMethodName())) return;

        final String APP_ID = "gr.cardlink.possible";

        try {
            logger.warning("Wrong PIN test failed -> force-stop + activate (no data clear)");

            Map<String, Object> args = new HashMap<>();
            args.put("command", "am");
            args.put("args", java.util.Arrays.asList("force-stop", APP_ID));
            ((JavascriptExecutor) driver).executeScript("mobile: shell", args);

            try { Thread.sleep(800); } catch (InterruptedException ignored) {}

            driver.activateApp(APP_ID);

            loginScreen = new LoginScreen(driver);
            loginScreen.waitForActivityToChange(".InitialActivity", 15);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to relaunch app after wrong pin test failure", e);
        }
    }

    @Test(priority = 1, groups = {"tier:regression", "layer:ui", "domain:core", "feature:login"})
    public void shouldDisplayAllPinKeys() {
        Allure.step("Verify PIN keys 0-9 are visible");
        for (int i = 0; i <= 9; i++) {
            boolean keyVisible = loginScreen.isKeyVisible(String.valueOf(i));
            logger.info("Key " + i + " visible? " + keyVisible);
            assertTrue(keyVisible, "Key " + i + " isn't visible");
        }

        Allure.step("Verify main buttons are visible");
        assertTrue(loginScreen.isOkButtonVisible(), "OK Button isn't visible");
        assertTrue(loginScreen.isBackButtonVisible(), "Back Button isn't visible");
        assertTrue(loginScreen.isExitButtonVisible(), "Exit button isn't visible");

        Allure.step("Verify labels are visible");
        assertTrue(loginScreen.isTitleLabelVisible(), "Title Label isn't visible");
        assertTrue(loginScreen.isCardViewLabelVisible(), "Card Label isn't visible");
    }

    @Test(priority = 2, groups = {"tier:regression", "layer:ui", "domain:core", "feature:login"})
    public void shouldDisplayCorrectLoginLabels() {
        Allure.step("Read and verify login screen labels");
        String expectedTitle = "Enter PIN";

        String actualTitle = loginScreen.getTitleText();
        String actualCardLabel = loginScreen.getCardViewText();

        logger.info("Title label: " + actualTitle);
        logger.info("Card view label: " + actualCardLabel);

        assertEquals(actualTitle, expectedTitle, "Title label text is incorrect");
        assertEquals(actualCardLabel, expectedTitle, "Card view text is incorrect");
    }

    @Test(priority = 3, groups = {"tier:regression", "layer:ui", "domain:core", "feature:login"})
    public void verifyWrongPinShowsAlertAndCanRetry() {
        Allure.step("Enter wrong PIN");
        loginScreen.enterPin("1983");

      //  Allure.step("Tap OK");
        loginScreen.tapOkButton();

        Allure.step("Verify wrong PIN popup content and retry");
        String expectedText = "Wrong PIN!";
        String expectedConfirmButton = "New attempt";

        assertTrue(loginScreen.isWrongPinPopupVisible(), "Wrong PIN pop up isn't visible");
        assertEquals(loginScreen.getWrongPinText(), expectedText, "Wrong PIN pop up text is incorrect");
        assertEquals(loginScreen.getConfirmButtonText(), expectedConfirmButton, "Wrong text in confirm button");

        loginScreen.tapConfirmButton();

        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        assertFalse(loginScreen.isWrongPinPopupVisible(), "Wrong PIN popup did not close as expected.");
    }

    @Test(
            priority = 4,
            groups = {"tier:smoke", "tier:regression", "layer:ui", "domain:core", "feature:login"}
    )
    public void shouldNavigateToMainMenuAfterValidPin() {
        Allure.step("Enter valid PIN 1111");
        loginScreen.enterPin("1111");

        Allure.step("Tap OK");
        loginScreen.tapOkButton();

        Allure.step("Verify navigation to Main menu");
        assertTrue(loginScreen.waitForMainActivity(2), "Did not navigate to Main menu after PIN entry");
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}