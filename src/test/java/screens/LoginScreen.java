package screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumBy;

import java.util.NoSuchElementException;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;


public class LoginScreen {
    private static final Logger logger = Logger.getLogger(LoginScreen.class.getName());


    static {
        logger.setLevel(Level.INFO);
    }

    private AndroidDriver driver;

    private By backButton = AppiumBy.id("gr.cardlink.possible:id/t9_key_back");
    private By okButton = AppiumBy.id("gr.cardlink.possible:id/t9_key_ok");
    private By exitButton = AppiumBy.xpath("//android.widget.ImageView[@index='0']");

    private By titleLabel = AppiumBy.id("gr.cardlink.possible:id/title");
    private By cardViewLabel = AppiumBy.id("gr.cardlink.possible:id/card_view_title");

    private By wrongPinPopup = AppiumBy.xpath("//android.widget.FrameLayout[@resource-id='gr.cardlink.possible:id/card_view']/android.widget.LinearLayout");
    private By wrongPinLabel=AppiumBy.id("gr.cardlink.possible:id/title_tv");
    private By confirmButton=AppiumBy.id("gr.cardlink.possible:id/confirm_button");


    private static final String MAIN_ACTIVITY = ".MainActivity";

    public LoginScreen(AndroidDriver driver) {
        this.driver = driver;
    }

    public void tapKey(String digit) {
        By locator = AppiumBy.id("gr.cardlink.possible:id/t9_key_" + digit);
        driver.findElement(locator).click();
        logger.info("Tapped key " + digit);
    }

    public boolean isKeyVisible(String digit) {
        By locator = AppiumBy.id("gr.cardlink.possible:id/t9_key_" + digit);
        return isElementVisible(locator);
    }

    public void enterPin(String pin) {
        for (char digit : pin.toCharArray()) {
            tapKey(String.valueOf(digit));
        }
    }

    public void tapKeyBack() {
        driver.findElement(backButton).click();
        logger.info("Tapped key ");
    }

    public void tapOkButton() {
        driver.findElement(okButton).click();
        logger.info("Tapped Ok Button");
    }
public void tapExitButton() {
        driver.findElement(exitButton).click();
        logger.info("Tapped Exit Button");
    }

    public void tapConfirmButton(){
        driver.findElement(confirmButton).click();
        logger.info("Tapped New attempt button");
    }

    //visibility check methods


    public boolean isBackButtonVisible() { return isElementVisible(backButton); }
    public boolean isOkButtonVisible() { return isElementVisible(okButton); }
    public boolean isExitButtonVisible() { return isElementVisible(exitButton); }
    public boolean isTitleLabelVisible(){return isElementVisible(titleLabel);}
    public boolean isCardViewLabelVisible(){return isElementVisible(cardViewLabel);}
    //public boolean isWrongPinPopupVisible()  {return isElementVisible(wrongPinPopup);}
    public boolean isWrongPinPopupVisible() {
        try {
            // Αν το element υπάρχει και εμφανίζεται
            return driver.findElement(wrongPinPopup).isDisplayed();
        } catch (NoSuchElementException e) {
            // Αν δεν υπάρχει πλέον, επιστρέφουμε false
            return false;
        } catch (Exception e) {
            logger.warning("Unexpected error checking wrong PIN popup: " + e.getMessage());
            return false;
        }
    }

        //elegxos periexomenou keimenou

    public String getTitleText() { return driver.findElement(titleLabel).getText(); }
    public String getCardViewText() { return driver.findElement(cardViewLabel).getText(); }
    public String getWrongPinText() {return driver.findElement(wrongPinLabel).getText(); }
    public String getConfirmButtonText(){return driver.findElement(confirmButton).getText();}

    private boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }




    public boolean waitForActivityToChange(String initialActivity, int timeoutSeconds) {
        int waited = 0;
        String currentActivity = driver.currentActivity();
        while (waited < timeoutSeconds) {
            try {
                currentActivity= driver.currentActivity();
                if (!currentActivity.equals(initialActivity)) {
                    return true; // Η activity άλλαξε, μπορούμε να συνεχίσουμε
                }
                Thread.sleep(500); // περιμένουμε μισό δευτερόλεπτο πριν ξανατσεκάρουμε
                waited += 0.5;
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.info("current activity " + currentActivity);
                return false;
            }
        }
        return false;
    }
    public boolean waitForMainActivity(int timeoutSeconds) {
        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000;
        String currentActivity = driver.currentActivity();
        while (System.currentTimeMillis() < endTime) {
             currentActivity = driver.currentActivity();
            if (currentActivity.contains(".MainActivity")) {
                logger.info("Test Passed, current activity " + currentActivity);
                return true;
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("current activity " + currentActivity);
        return false;
    }


    }

