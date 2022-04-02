package utilityBrowser;

import globalItem.GlobalObject;
import org.junit.Assert;

public class Util04BrowserHousekeeping extends GlobalObject {

    public static void CompareUrl(String sExpectedUrl){
        if( ! sExpectedUrl.contains(globalDriver.getCurrentUrl())){
            Assert.fail("Log: Actual and Expected Url does not match");
        }
    }

    public static void CompareTitle(String sExpectedTitle){
        if( ! sExpectedTitle.contains(globalDriver.getTitle())){
            Assert.fail("Log: Actual and Expected Title does not match");
        }
    }

}
