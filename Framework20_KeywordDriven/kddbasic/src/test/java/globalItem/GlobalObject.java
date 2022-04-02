package globalItem;

import org.openqa.selenium.WebDriver;

public class GlobalObject {

    public static WebDriver globalDriver = null;

    public static String sChromeBinary   =
            System.getProperty("user.dir") + "\\src\\test\\resources\\browserdriver\\chromedriver.exe";

    public static final String sxlEmpDataPath =
            System.getProperty("user.dir") + "\\src\\test\\resources\\employee_data.xlsx";
    public static final String sxlKDDFilePath =
            System.getProperty("user.dir") + "\\src\\test\\resources\\kdd_step_basic.xlsx";


}
