package pkg_global;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.HashMap;

public class GlobalObjects {

    // Logger
    private static final Logger myLog = Logger.getLogger(GlobalObjects.class);
    // excel file path
    public static final String sKDDexcelpath = "\\src\\test\\resources\\kdd_testcases.xlsx";

    // excel sheet names
    public static final String sKDDexcelsheet_config     = "config";
    public static final String sKDDexcelsheet_pageobject = "pageobject";
    public static final String sKDDexcelsheet_fixture    = "fixture";
    public static final String sKDDexcelsheet_testcases  = "testcases";
    public static String sKDDexcelsheet_resultsheet        = "";

    // result sheet result column number
    public static final int nKDDexcelsheet_resultcolumn  = 5;

    // Db connection
    public static Connection sqliteConn                   = null;

    // Browser instance
    public static WebDriver realDriver                    = null;
    public static boolean bBrowserInvoked                 = false;

    // A hash-map of all data items
    public static HashMap<String, String> hmGlobalData    = new HashMap<>();

    // User data read from Excel table
    public static FileInputStream exlInStream             = null;
    public static FileOutputStream exlOutStream           = null;
    public static XSSFWorkbook exlWorkBook                = null;
    public static XSSFSheet exlWorkSheet                  = null;

    // for explicit wait
    public static WebDriverWait wdWait                    = null;

    public static Robot utilRobot                         = null;

    public static final String sBootText = "\n _  __                                _      ____       _                 \n" + "| |/ /___ _   ___      _____  _ __ __| |    |  _ \\ _ __(_)_   _____ _ __  \n" + "| ' // _ \\ | | \\ \\ /\\ / / _ \\| '__/ _` |    | | | | '__| \\ \\ / / _ \\ '_ \\ \n" + "| . \\  __/ |_| |\\ V  V / (_) | | | (_| |    | |_| | |  | |\\ V /  __/ | | |\n" + "|_|\\_\\___|\\__, | \\_/\\_/ \\___/|_|  \\__,_|    |____/|_|  |_| \\_/ \\___|_| |_|\n" + "          |___/                                                           \n";
    static {myLog.fatal(sBootText);}
}
