package pkg_ItemProcessor;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import pkg_global.GlobalObjects;
import pkg_Utility.Utility_General;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeleniumProcessor extends GlobalObjects {
    private static final Logger myLog = Logger.getLogger(SeleniumProcessor.class);

    public static void ProcessSeleniumItem(String sTcIndex, String sTcAction, String sTcParticular){
        myLog.info("Log: TCProcessor : " + sTcIndex + " " + sTcAction + " " + sTcParticular);
        String sElementName   = "";
        String sKeysToSend    = "";
        String sMatchingCondn = "";
        String sElementIndex  = "";

        switch (sTcAction){
            case "navigate":
                realDriver.navigate().to(hmGlobalData.get(sTcParticular));
                break;
            case "implicit_wait":
                int nMilliSec = 0;
                if(sTcParticular.startsWith("implicit_wait=")){
                    sTcParticular = sTcParticular.replace("implicit_wait=", "");
                    sTcParticular = sTcParticular.replace("ms", "");
                    nMilliSec = Integer.valueOf(sTcParticular);
                }else{
                    nMilliSec = Integer.valueOf(hmGlobalData.get(sTcParticular).replace("ms",""));
                }
                Utility_General.ImplicitWait(nMilliSec);
                break;
            case "click":
                FindThisSingleElement(sTcIndex, hmGlobalData.get(sTcParticular)).click();
                break;
            case "click_nth_element":
                sElementName  = sTcParticular.split("=")[0];
                sElementIndex = sTcParticular.replace(sElementName + "=","");
                FindTheseMultipleElements(sTcIndex, hmGlobalData.get(sElementName))
                        .get(Integer.valueOf(sElementIndex)).click();
                break;
            case "verify_element_displayed":
                if( ! FindThisSingleElement(sTcIndex, hmGlobalData.get(sTcParticular)).isDisplayed()){
                    Assert.fail("Log: Following webelement display condition failed : " + hmGlobalData.get(sTcParticular));
                }
                break;
            case "verify_element_not_displayed":
                if( FindTheseMultipleElements(sTcIndex, hmGlobalData.get(sTcParticular)).size() > 0){
                    Assert.fail("Log: Following webelement display condition failed : " + hmGlobalData.get(sTcParticular));
                }
                break;
            case "send_keys":
                sElementName = sTcParticular.split("=")[0];
                sKeysToSend  = sTcParticular.split("=")[1];
                FindThisSingleElement(sTcIndex, hmGlobalData.get(sElementName))
                        .sendKeys(hmGlobalData.get(sKeysToSend));
                break;
            case "verify_multiple_element_size":
                sElementName   = sTcParticular.split("=")[0];
                sMatchingCondn = sTcParticular.replace(sElementName+"=","");
                int nActualElementsSize = FindTheseMultipleElements(sTcIndex, hmGlobalData.get(sElementName)).size();
                if(sMatchingCondn.equals("nonzero")){
                    if (nActualElementsSize <= 0){
                        Assert.fail("Log: Following webelement size condition failed : " + hmGlobalData.get(sElementName));
                    }
                }else if(sMatchingCondn.equals("zero")){
                    if (nActualElementsSize > 0){
                        Assert.fail("Log: Following webelement size condition failed : " + hmGlobalData.get(sElementName));
                    }
                }
                break;
            case "action":
            case "action_movetoelement_click":
                PerformActionsItem(sTcIndex, sTcAction, sTcParticular);
                break;
            case "store_main_window_handle":
                hmGlobalData.put(sTcParticular, realDriver.getWindowHandle());
                break;
            case "close_new_window":
            case "close_window_with_specific_handle":
                CloseNewWindow(sTcIndex, sTcParticular);
                break;
            case "switch_to_main_window":
                realDriver.switchTo().window(hmGlobalData.get(sTcParticular));
                break;
//            case "click_nth_element":
//                break;
            default:
                break;
        }
        hmGlobalData.put(sTcIndex, "pass");
    }

    public static void PerformActionsItem(String sTcIndex, String sTcAction, String sTcParticular){
        sTcAction = sTcAction.replace("action_","");

        Actions objActions =  new Actions(realDriver);
        if(sTcAction.startsWith("movetoelement")){
            objActions.moveToElement(FindThisSingleElement(sTcIndex,hmGlobalData.get(sTcParticular)));
        }
        if(sTcAction.contains("click")){
            objActions.click();
        }
        objActions.build().perform();
    }

    public static List<WebElement> FindTheseMultipleElements(String sTcIndex, String sElementDetails){
        String sLocatorByMethod = sElementDetails.split("=")[0];
        String sLocatorDetail   = sElementDetails.replace(sLocatorByMethod + "=","");
        List<WebElement> elementMultiple = null;
        switch (sLocatorByMethod){
            case "id":
                elementMultiple = realDriver.findElements(By.id(sLocatorDetail));
                break;
            case "cssselector":
                elementMultiple = realDriver.findElements(By.cssSelector(sLocatorDetail));
                break;
            case "xpath":
                elementMultiple = realDriver.findElements(By.xpath(sLocatorDetail));
                break;
            case "className":
                elementMultiple = realDriver.findElements(By.className(sLocatorDetail));
                break;
            case "linkText":
                elementMultiple = realDriver.findElements(By.linkText(sLocatorDetail));
                break;
            case "name":
                elementMultiple = realDriver.findElements(By.name(sLocatorDetail));
                break;
            case "partiallinktext":
                elementMultiple = realDriver.findElements(By.partialLinkText(sLocatorDetail));
                break;
            case "tagname":
                elementMultiple = realDriver.findElements(By.tagName(sLocatorDetail));
                break;
        }
        return elementMultiple;
    }

    public static WebElement FindThisSingleElement(String sTcIndex, String sElementDetails){
        String sLocatorByMethod = sElementDetails.split("=")[0];
        String sLocatorDetail   = sElementDetails.replace(sLocatorByMethod + "=","");
        WebElement elementSingle= null;
        switch (sLocatorByMethod){
            case "id":
                elementSingle = realDriver.findElement(By.id(sLocatorDetail));
                break;
            case "cssselector":
                elementSingle = realDriver.findElement(By.cssSelector(sLocatorDetail));
                break;
            case "xpath":
                elementSingle = realDriver.findElement(By.xpath(sLocatorDetail));
                break;
            case "className":
                elementSingle = realDriver.findElement(By.className(sLocatorDetail));
                break;
            case "linkText":
                elementSingle = realDriver.findElement(By.linkText(sLocatorDetail));
                break;
            case "name":
                elementSingle = realDriver.findElement(By.name(sLocatorDetail));
                break;
            case "partiallinktext":
                elementSingle = realDriver.findElement(By.partialLinkText(sLocatorDetail));
                break;
            case "tagname":
                elementSingle = realDriver.findElement(By.tagName(sLocatorDetail));
                break;
        }
        return elementSingle;
    }

    public static void CloseNewWindow(String sTcIndex, String sTcParticular){

        Set<String> setAllWindowHandles = realDriver.getWindowHandles();
        if(sTcParticular.contains("window_2")){
            if(2 != setAllWindowHandles.size()){
                Assert.fail("Log: Multi window validation NOT successful");
            }
        }

        for(String sOneHandle : setAllWindowHandles) {
            if(! sOneHandle.equals(hmGlobalData.get("handle_main_window"))){
                realDriver.switchTo().window(sOneHandle);
                realDriver.close();
            }
        }
    }

    // Launch browser instance -- Currently only Chrome is supported
    public static WebDriver InvokeBrowser(){
        if (null != realDriver){
            realDriver = null;
        }

        if(false == bBrowserInvoked){

            if(hmGlobalData.get("selenium_grid_flag").equals("no")){
                //String sChromeBinary=System.getProperty("user.dir") + "\\src\\test\\resources\\browserDriver\\chromedriver.exe";
                String sChromeBinary=System.getProperty("user.dir") + hmGlobalData.get("chrome_driver_path");
                System.setProperty("webdriver.chrome.driver", sChromeBinary);
                System.setProperty("webdriver.chrome.silentOutput", "true");

                // Disable image loading - to speedup test execution
                Map<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("profile.managed_default_content_settings.images", 2);

                ChromeOptions options = new ChromeOptions();
                options.addArguments("window-size=1400,800");
                options.addArguments("disable-infobars"); // disabling infobars
                options.addArguments("--disable-extensions"); // disabling extensions
                options.addArguments("--no-sandbox"); // Bypass OS security model
                options.setExperimentalOption("useAutomationExtension", false);
                if(hmGlobalData.get("load_images").equals("no")){
                    options.setExperimentalOption("prefs", prefs);
                }
                String sBrowserheight = hmGlobalData.get("browser_height");
                sBrowserheight        = sBrowserheight.replace("pixel","");
                String sBrowserwidth  = hmGlobalData.get("browser_width");
                sBrowserwidth         = sBrowserwidth.replace("pixel","");

                realDriver = null;
                realDriver = new ChromeDriver(options);

                realDriver.manage().window().setSize(
                        new Dimension(Integer.valueOf(sBrowserwidth), Integer.valueOf(sBrowserheight))
                );
                myLog.info("Log: Chrome browser is launched");

            }else{
                // launch remote chrome using selenium grid

                // Disable image loading - to speedup test execution
                Map<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("profile.managed_default_content_settings.images", 2);

                ChromeOptions options = new ChromeOptions();
                options.addArguments("window-size=1400,800");
                options.addArguments("disable-infobars"); // disabling infobars
                options.addArguments("--disable-extensions"); // disabling extensions
                options.addArguments("--no-sandbox"); // Bypass OS security model
                options.setExperimentalOption("useAutomationExtension", false);
                if(hmGlobalData.get("load_images").equals("no")){
                    options.setExperimentalOption("prefs", prefs);
                }
                options.addArguments("--incognito");
                options.addArguments("--disable-popup-blocking");
                DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
                chromeCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

                String sBrowserheight = hmGlobalData.get("browser_height");
                sBrowserheight        = sBrowserheight.replace("pixel","");
                String sBrowserwidth  = hmGlobalData.get("browser_width");
                sBrowserwidth         = sBrowserwidth.replace("pixel","");

                realDriver = null;
                try{
                    realDriver=new RemoteWebDriver(new URL(hmGlobalData.get("selenium_grid_url")),chromeCapabilities);
                }catch (MalformedURLException eUrl){
                    Assert.fail("Log: Chrome launching failed on Selenium Grid");
                }

                realDriver.manage().window().setSize(
                        new Dimension(Integer.valueOf(sBrowserwidth), Integer.valueOf(sBrowserheight))
                );
                myLog.info("Log: Chrome browser is launched on Selenium Grid");

            }
            realDriver.manage().deleteAllCookies();
            bBrowserInvoked = true;
        }
        return realDriver;
    }


}
