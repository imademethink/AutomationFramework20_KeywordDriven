package pkg_utility;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import pkg_global.GlobalObjects;

import java.util.concurrent.TimeUnit;

public class Utility_General extends GlobalObjects {

    private static final Logger myLog = Logger.getLogger(Utility_General.class);

    public static WebDriverWait ExplicitWaitNormal(){
        return new WebDriverWait(realDriver, 8);
    }

    public static WebDriverWait ExplicitWaitLow(){
        return new WebDriverWait(realDriver, 5);
    }

    public static WebDriverWait ExplicitWaitHigh(){
        return new WebDriverWait(realDriver, 15);
    }

    public static void ImplicitWait(int nMilliSec){
        realDriver.manage().timeouts().implicitlyWait(nMilliSec, TimeUnit.MILLISECONDS);
    }

    public static void Sleep(int nMillisec){
        try{Thread.sleep(nMillisec);}catch(Exception time){}
    }

}
