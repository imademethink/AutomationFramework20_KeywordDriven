package utilityBrowser;

import globalItem.GlobalObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Util05WebElementProcess extends GlobalObject {

    public static void FindElementAndPerformActivity(
            String sLocatorType, String sLocatorString,
            String sActivityType, String sKeys ){

        WebElement element = null;

        switch (sLocatorType){
            case "id":
                element = globalDriver.findElement(By.id(sLocatorString));
                break;
            case "css":
                element = globalDriver.findElement(By.cssSelector(sLocatorString));
                break;
            case "xpath":
                element = globalDriver.findElement(By.xpath(sLocatorString));
                break;
        }

        switch (sActivityType){
            case "click":
                element.click();
                break;
            case "sendkeys":
                element.sendKeys(sKeys);
                break;
            case "isdisplayed":
                System.out.println(element.isDisplayed());
                break;
        }

    }


}
