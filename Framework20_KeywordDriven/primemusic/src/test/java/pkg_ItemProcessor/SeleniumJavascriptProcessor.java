package pkg_ItemProcessor;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import pkg_global.GlobalObjects;

public class SeleniumJavascriptProcessor extends GlobalObjects {
    private static final Logger myLog = Logger.getLogger(SeleniumJavascriptProcessor.class);

    public static void ProcessSeleniumJavascriptItem(String sTcIndex, String sTcAction, String sTcParticular){
        myLog.info("Log: TCProcessor : " + sTcIndex + " " + sTcAction + " " + sTcParticular);

        switch (sTcAction){
            case "scroll_bottom":
                ScrollUsingJavaScriptBottom();
                break;
            case "scroll_down":
                VerticalScrollJavaScript( sTcParticular.replace("pixel","") , "+");
                break;
            case "scroll_up":
                VerticalScrollJavaScript( sTcParticular.replace("pixel","") , "-");
                break;
            case "click_javascript":
                WebElement jsElement = realDriver.findElement(
                        By.cssSelector(
                                hmGlobalData.get(sTcParticular).replace("cssselector=","")));
                ClickJavaScriptBottom(jsElement);
                break;
            case "js_alert_accept":
                ProcessJavaScriptAlert("accept");
                break;
            case "js_alert_dismiss":
                ProcessJavaScriptAlert("dismiss");
                break;
            default:
                break;
        }
        hmGlobalData.put(sTcIndex, "pass");
    }


    public static void VerticalScrollJavaScript(String sHeightInPixel, String sDirection){
        ((JavascriptExecutor)realDriver).executeScript("window.scrollBy(0," + sDirection + sHeightInPixel + ")");
    }

    public static void ScrollUsingJavaScriptBottom(){
        ((JavascriptExecutor)realDriver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public static void ClickJavaScriptBottom(WebElement clickMe){
        ((JavascriptExecutor)realDriver).executeScript("arguments[0].click();",clickMe);
    }

    public static void ProcessJavaScriptAlert(String sAction){
        if(sAction.equals("accept")){
            realDriver.switchTo().alert().accept();
        }
        if(sAction.equals("dismiss")){
            realDriver.switchTo().alert().dismiss();
        }
    }

}
