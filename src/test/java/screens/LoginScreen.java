package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;

import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginScreen {

    private static final Logger logger = Logger.getLogger(LoginScreen.class.getName());

    static {
        logger.setLevel(Level.INFO);
    }

    private final AndroidDriver driver;

    private final By backButton = AppiumBy.id("gr.cardlink.possible:id/t9_key_back");
    private final By okButton = AppiumBy.id("gr.cardlink.possible:id/t9_key_ok");
    private final By exitButton = AppiumBy.xpath("//android.widget.ImageView[@index='0']");

    private final By titleLabel = AppiumBy.id("gr.cardlink.possible:id/title");
    private final By cardViewLabel = AppiumBy.id("gr.cardlink.possible:id/card_view_title");

    private final By wrongPinPopup = AppiumBy.xpath("//android.widget.FrameLayout[@resource-id='gr.cardlink.possible:id/card_view']/android.widget.LinearLayout");
    private final By wrongPinLabel = AppiumBy.id("gr.cardlink.possible:id/title_tv");
    private final By confirmButton = AppiumBy.id("gr.cardlink.possible:id/confirm_button");

    public LoginScreen(AndroidDriver driver) {
        this.driver = driver;
    }

    public void tapKey(String digit) {
        Allure.step("Tap key '" + digit + "'");
        By locator = AppiumBy.id("gr.cardlink.possible:id/t9_key_" + digit);
        driver.findElement(locator).click();
        logger.info("Tapped key " + digit);
    }

    public boolean isKeyVisible(String digit) {
        Allure.step("Check key '" + digit + "' is visible");
        By locator = AppiumBy.id("gr.cardlink.possible:id/t9_key_" + digit);
        return isElementVisible(locator);
    }

    public void enterPin(String pin) {
        Allure.step("Enter PIN: " + pin);
        for (char digit : pin.toCharArray()) {
            tapKey(String.valueOf(digit));
        }
    }

    public void tapKeyBack() {
        Allure.step("Tap Back key");
        driver.findElement(backButton).click();
        logger.info("Tapped back key");
    }

    public void tapOkButton() {
        Allure.step("Tap OK button");
        driver.findElement(okButton).click();
        logger.info("Tapped OK Button");
    }

    public void tapExitButton() {
        Allure.step("Tap Exit button");
        driver.findElement(exitButton).click();
        logger.info("Tapped Exit Button");
    }

    public void tapConfirmButton() {
        Allure.step("Tap 'New attempt' confirm button");
        driver.findElement(confirmButton).click();
        logger.info("Tapped New attempt button");
    }

    // visibility checks
    public boolean isBackButtonVisible() {
        Allure.step("Check Back button is visible");
        return isElementVisible(backButton);
    }

    public boolean isOkButtonVisible() {
        Allure.step("Check OK button is visible");
        return isElementVisible(okButton);
    }

    public boolean isExitButtonVisible() {
        Allure.step("Check Exit button is visible");
        return isElementVisible(exitButton);
    }

    public boolean isTitleLabelVisible() {
        Allure.step("Check Title label is visible");
        return isElementVisible(titleLabel);
    }

    public boolean isCardViewLabelVisible() {
        Allure.step("Check Card view label is visible");
        return isElementVisible(cardViewLabel);
    }

    public boolean isWrongPinPopupVisible() {
        Allure.step("Check Wrong PIN popup is visible");
        try {
            return driver.findElement(wrongPinPopup).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        } catch (Exception e) {
            logger.warning("Unexpected error checking wrong PIN popup: " + e.getMessage());
            return false;
        }
    }

    // texts
    public String getTitleText() {
        Allure.step("Read Title label text");
        return driver.findElement(titleLabel).getText();
    }

    public String getCardViewText() {
        Allure.step("Read Card view label text");
        return driver.findElement(cardViewLabel).getText();
    }

    public String getWrongPinText() {
        Allure.step("Read Wrong PIN popup text");
        return driver.findElement(wrongPinLabel).getText();
    }

    public String getConfirmButtonText() {
        Allure.step("Read Confirm button text");
        return driver.findElement(confirmButton).getText();
    }

    private boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForActivityToChange(String initialActivity, int timeoutSeconds) {
        Allure.step("Wait for activity to change from '" + initialActivity + "' (timeout " + timeoutSeconds + "s)");
        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000L;

        while (System.currentTimeMillis() < endTime) {
            String currentActivity = driver.currentActivity();
            if (!currentActivity.equals(initialActivity)) {
                return true;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return false;
            }
        }
        return false;
    }

    public boolean waitForMainActivity(int timeoutSeconds) {
        Allure.step("Wait for MainActivity (timeout " + timeoutSeconds + "s)");
        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000L;

        while (System.currentTimeMillis() < endTime) {
            String currentActivity = driver.currentActivity();
            if (currentActivity.contains(".MainActivity")) {
                logger.info("MainActivity reached: " + currentActivity);
                return true;
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {}
        }
        return false;
    }
}