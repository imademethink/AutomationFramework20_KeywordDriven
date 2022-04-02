package executor;

import utilityBrowser.Util01BrowserProcessor;

public class GUIScenario1 {
    public static void main(String[] args) throws Exception{
        System.out.println("Scenario 1");

        Util01BrowserProcessor.BrowserInit("chrome");

        Util01BrowserProcessor.BrowserClose();
    }
}
