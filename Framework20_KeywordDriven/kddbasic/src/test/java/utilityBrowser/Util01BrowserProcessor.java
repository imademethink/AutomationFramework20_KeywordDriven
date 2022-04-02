package utilityBrowser;

import globalItem.GlobalObject;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.HashMap;
import java.util.Map;

public class Util01BrowserProcessor extends GlobalObject {

    public static void BrowserInit(String sBrowserType) throws InterruptedException{
        if ( ! sBrowserType.contains("chrome")) {
            Assert.fail("Log: Currently only Chrome browser is supported");
        }
        System.setProperty("webdriver.chrome.driver", sChromeBinary);
        globalDriver = new ChromeDriver();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        globalDriver.manage().window().maximize();
        Thread.sleep(5000);
        globalDriver.manage().window().setPosition(new Point(100, 200));
        Thread.sleep(5000);
    }

    public static void BrowserInitPrivate(String sBrowserType, ChromeOptions chromeOptionsObj) throws InterruptedException{
        if ( ! sBrowserType.contains("chrome")) {
            Assert.fail("Log: Currently only Chrome browser is supported");
        }
        System.setProperty("webdriver.chrome.driver", sChromeBinary);
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        System.setProperty("webdriver.chrome.silentOutput", "true");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--privileged");
        chromeOptions.addArguments("window-size=1850,800");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--no-sandbox"); // Bypass OS security model

        // to disables the driver to install other chrome extensions
        chromeOptions.setExperimentalOption("useAutomationExtension", false);

        // Disable image loading - to speedup test execution
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.managed_default_content_settings.images", 2);
        //chromeOptions.setExperimentalOption("prefs", prefs);

        globalDriver = new ChromeDriver(chromeOptions);
    }

    public static void BrowserClose(){
        globalDriver.close();
    }

    public static void ClickOnElement(String sLocatorType, String sLocatorData){
        switch (sLocatorType){
            case "id":
                globalDriver.findElement(By.id(sLocatorData)).click();
                break;
            case "css":
                globalDriver.findElement(By.cssSelector(sLocatorData)).click();
                break;
            case "xpath":
                globalDriver.findElement(By.xpath(sLocatorData)).click();
                break;
        }
    }

    public static void SendKeysToElement(String sLocatorType, String sLocatorData, String sKeysToSend){
        switch (sLocatorType){
            case "id":
                globalDriver.findElement(By.id(sLocatorData)).sendKeys(sKeysToSend);
                break;
            case "css":
                globalDriver.findElement(By.cssSelector(sLocatorData)).sendKeys(sKeysToSend);
                break;
            case "xpath":
                globalDriver.findElement(By.xpath(sLocatorData)).sendKeys(sKeysToSend);
                break;
        }
    }

}
