package utilityBrowser;

import globalItem.GlobalObject;

public class Util03BrowserOther extends GlobalObject {

    public static void HandleCookie() {
        globalDriver.manage().deleteAllCookies();
    }
}


