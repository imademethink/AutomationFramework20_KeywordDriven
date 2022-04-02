package executor;

import utilityBrowser.Util01BrowserProcessor;
import utilityBrowser.Util02BrowserNavigate;
import utilityBrowser.Util05WebElementProcess;

public class GUIScenario3 {
    public static void main(String[] args) throws Exception{
        System.out.println("Scenario 2");

        Util01BrowserProcessor.BrowserInitPrivate("chrome",null);

        Util02BrowserNavigate.OpenWebsite(
                "https://chandanachaitanya.github.io/selenium-practice-site/");

        Util05WebElementProcess.FindElementAndPerformActivity(
                "id", "alertBox","click", null);

        Util01BrowserProcessor.BrowserClose();
    }
}
