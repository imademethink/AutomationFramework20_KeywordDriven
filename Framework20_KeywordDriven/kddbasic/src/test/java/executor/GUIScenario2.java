package executor;

import utilityBrowser.Util01BrowserProcessor;
import utilityBrowser.Util02BrowserNavigate;
import utilityBrowser.Util04BrowserHousekeeping;

public class GUIScenario2 {
    public static void main(String[] args) throws Exception{
        System.out.println("Scenario 2");

        Util01BrowserProcessor.BrowserInitPrivate("chrome",null);

        Util02BrowserNavigate.OpenWebsite(
                "https://www.seleniumeasy.com/test/basic-first-form-demo.html");

        Util04BrowserHousekeeping.CompareTitle(
                "Selenium Easy Demo - Simple Form to Automate using Selenium");

        Util01BrowserProcessor.BrowserClose();
    }
}
