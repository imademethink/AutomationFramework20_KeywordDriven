package utilityBrowser;

import globalItem.GlobalObject;

public class Util02BrowserNavigate extends GlobalObject {

    public static void OpenWebsite(String sUrl) throws InterruptedException{
        globalDriver.get(sUrl);
        Thread.sleep(15000);
        //globalDriver.manage().timeouts().implicitlyWait(9000, TimeUnit.MILLISECONDS);
    }

    public static void InsertDelay(String sMilliSec) throws InterruptedException{
        Thread.sleep(Integer.parseInt(sMilliSec.replace("ms","")));
    }

}
