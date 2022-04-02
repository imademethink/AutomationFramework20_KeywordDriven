package utilityParser;

import utilityBrowser.Util01BrowserProcessor;
import utilityBrowser.Util02BrowserNavigate;

public class KeywordDrivenParser {

    public static void KeywordParser(String [][] aryKddSteps) throws Exception{
        System.out.println("\n\n\n\n\n");
        int nTotalIndex = aryKddSteps.length;

        for(int k=1; k < nTotalIndex; k++){
            if(aryKddSteps[k].length == 1) continue;

            System.out.println("Log: Index " + aryKddSteps[k][0]);
            System.out.println("Log: TC_id " + aryKddSteps[k][1]);

            String sTestStep = aryKddSteps[k][2];
            System.out.println("Log: TC_step " + sTestStep);
            String sTestData = null;
            String sTestSendKey = null;

            switch (sTestStep){
                case "open_browser":
                    sTestData = aryKddSteps[k][3];
                    System.out.println("Log: TC_data " + sTestData);
                    Util01BrowserProcessor.BrowserInitPrivate(sTestData,null);
                    break;
                case "navigate":
                    sTestData = aryKddSteps[k][3];
                    System.out.println("Log: TC_data " + sTestData);
                    Util02BrowserNavigate.OpenWebsite(sTestData);
                    break;
                case "wait":
                    sTestData = aryKddSteps[k][3];
                    System.out.println("Log: TC_data " + sTestData);
                    Util02BrowserNavigate.InsertDelay(sTestData);
                    break;
                case "click":
                    sTestData = aryKddSteps[k][3];
                    System.out.println("Log: TC_data " + sTestData);
                    Util01BrowserProcessor.ClickOnElement(
                                              sTestData.split("=")[0],
                                              sTestData.split("=",2)[1]);
                    break;
                case "sendkeys":
                    sTestData = aryKddSteps[k][3];
                    sTestSendKey = aryKddSteps[k][4];
                    System.out.println("Log: TC_data " + sTestData);
                    System.out.println("Log: TC_sendkeys " + sTestSendKey);
                    Util01BrowserProcessor.SendKeysToElement(
                            sTestData.split("=")[0],
                            sTestData.split("=",2)[1],
                            sTestSendKey);
                    break;
                case "close_browser":
                    Util01BrowserProcessor.BrowserClose();
                    break;
            }
        }

    }


}
