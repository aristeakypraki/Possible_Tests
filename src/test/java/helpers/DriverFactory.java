package helpers;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    private static AndroidDriver driver;

    public static AndroidDriver getDriver() throws MalformedURLException {
        if (!isDriverAlive()) {
            UiAutomator2Options options = new UiAutomator2Options()
                    .setDeviceName("1851057377")
                    .setPlatformName("Android")
                    .setAutomationName("UiAutomator2")
                    .setAppPackage("gr.cardlink.possible")
                    .setAppActivity(".MainActivity")
                    .setNoReset(true);                     // Δεν κάνει reinstall ή clear data

            driver = new AndroidDriver(
                    new URL("http://127.0.0.1:4723/wd/hub"), // Appium server
                    options);
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        }
        return driver;
    }

    // Κλείνει driver
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
    public static boolean isDriverAlive() {
        try {
            return driver != null && driver.getSessionId() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
