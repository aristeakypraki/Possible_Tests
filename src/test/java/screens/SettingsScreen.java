package screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumBy;

import java.util.NoSuchElementException;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import tests.SettingsTests;

public class SettingsScreen {

    private static final Logger logger = Logger.getLogger(SettingsScreen.class.getName());


    static {
        logger.setLevel(Level.INFO);
    }

    private AndroidDriver driver;
    //elements arxikis othonis settings
    private By settingsButton = AppiumBy.id("gr.cardlink.possible:id/tab_view_5_a");
    private By reprintLastBatch = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\" and @text=\"Reprint last batch\"]\n");
    private By systemInformation = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\" and @text=\"System information\"]");
    private By localSalesDetails = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\" and @text=\"Local sales details\"]");
    private By network = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\" and @text=\"Network and connectivity\"]");
    private By parametersUpdate = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\" and @text=\"Parameters update\"]");
    private By automatedBatchClosing = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\" and @text=\"Automated batch closing\"]");
    private By printSettings = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\" and @text=\"Print settings\"]");
    private By tip = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\" and @text=\"Tip\"]");
    private By language = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\" and @text=\"Language\"]");
    private By security = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\" and @text=\"Security\"]");
    private By additionalInfoFields = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\" and @text=\"Additional info fields\"]");

    //elements system information
    private By titleSystemInformation = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"gr.cardlink.possible:id/title\"]");


    public SettingsScreen(AndroidDriver driver) {
        this.driver = driver;
    }

    public void tap(By element) {
        driver.findElement(element).click();
    }

    public void openSystemInformation() {
        tap(systemInformation);
    }
    public void tapSettingsButton(){
        tap(settingsButton);
    }

    public String getTitleTextSystemInformation() {
        return driver.findElement(titleSystemInformation).getText();
    }

    public boolean isTitleVisible() {
        return isElementVisible(titleSystemInformation);
    }

    private boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
