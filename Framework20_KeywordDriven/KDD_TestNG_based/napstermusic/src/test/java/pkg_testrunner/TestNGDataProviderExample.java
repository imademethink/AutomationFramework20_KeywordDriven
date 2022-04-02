package pkg_testrunner;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.*;
import pkg_global.GlobalObjects;
import pkg_utility.Utility_Filehandler;
import pkg_utility.Utility_General;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class TestNGDataProviderExample extends GlobalObjects {
    private static final Logger myLog = Logger.getLogger(TestNGDataProviderExample.class);

    @BeforeClass
    public static void setupBeforeClass() {
//        //GlobalInit
//        new Utility_General().GetCommandLineParam();
//
//        new Utility_Filehandler().PropertiesDataReaderInit();
//
//        new Utility_Filehandler().CsvDataReaderInit();
//
//        new Utility_General().DbConnectionInit();
//
//        // Launching web browser
//        new GlobalObjects().getDriver();
    }

    @AfterClass
    public static void teardownAfterClass() {
//        //GlobalTearDown
//        // Shutting down web browser
//        new GlobalObjects().specialCaseTestNGCloseDriver();
//
//        new Utility_General().DbConnectionClose();

//        new Utility_General().GenerateReport();
//
//        // Send report via email
//        // Extra step for JVM shutdown
//        Runtime runtime = Runtime.getRuntime();
//        // Java shutdown hook are handy to run some code when program exit
//        // e.g. clean up resources, send reports etc
//        runtime.addShutdownHook(new Thread(){
//                                    public void run(){
//                                        try {new Utility_General().SendReportViaEmail();}
//                                        catch (Exception e) { e.printStackTrace();}
//                                    }
//                                }
//        );
//        try{Thread.sleep(5000);}catch (Exception e){}

    }

    @Test(dataProvider="EmpDataProviderExcel")
    public void EmpDataProcessingTest2(int nCurrentRow , HashMap<String, Object> hmExcelRowData) {
//        if(0 == nCurrentRow) {return;}
//
//        myLog.info("Log: action_login      " + hmExcelRowData.get("action_login"));
//        myLog.info("Log: action_search     " + hmExcelRowData.get("action_search"));
//        myLog.info("Log: action_checkout   " + hmExcelRowData.get("action_checkout"));
//        myLog.info("Log: action_payment    " + hmExcelRowData.get("action_payment"));
//        myLog.info("Log: login_email       " + hmExcelRowData.get("login_email"));
//        myLog.info("Log: login_pwd         " + hmExcelRowData.get("login_pwd"));
//        myLog.info("Log: login_expected    " + hmExcelRowData.get("login_expected"));
//        myLog.info("Log: search_term       " + hmExcelRowData.get("search_term"));
//        myLog.info("Log: search_result_count_expected  " + hmExcelRowData.get("search_result_count_expected"));
//        myLog.info("Log: checkout_qty      " + hmExcelRowData.get("checkout_qty"));
//        myLog.info("Log: checkout_expected " + hmExcelRowData.get("checkout_expected"));
//        myLog.info("Log: payment_method    " + hmExcelRowData.get("payment_method"));
//        myLog.info("Log: payment_expected  " + hmExcelRowData.get("payment_expected"));
//        hmGlobalData.put("login_success", "no");
//        utilFileHandler= new Utility_Filehandler();
//        utilGeneral    = new Utility_General();
//        utilGeneral.InitAllPageObject();

//        myLog.info("Log: Navigating to login section " + hmGlobalData.get("sUrlHome"));
//        realDriver.get(hmGlobalData.get("sUrlHome"));
//        utilGeneral.ImplicitWait(8000);
//
//        if(! hmExcelRowData.get("action_login").toString().isEmpty()){
//            myLog.info("Log: attempting login with given data");
//            pgHome.PerformLogin(hmExcelRowData.get("login_email").toString(), hmExcelRowData.get("login_pwd").toString());
//            myLog.info("Log: validating login");
//            if(hmExcelRowData.get("login_expected").toString().equals("success")){
//                pgHome.ValidateLogin(true);
//            }else{
//                pgHome.ValidateLogin(false);
//            }
//        }
//
//
//        if( ! hmExcelRowData.get("action_search").toString().isEmpty()){
//            myLog.info("Log: attempting search with given data");
//            pgSearch.PerformSearch(hmExcelRowData.get("search_term").toString());
//            if( ! hmExcelRowData.get("search_result_count_expected").toString().isEmpty()){
//                myLog.info("Log: validating search");
//                if(hmExcelRowData.get("search_result_count_expected").toString().equals("non zero")){
//                    pgSearch.ValidateSearchResult(true);
//                }else{
//                    pgSearch.ValidateSearchResult(false);
//                }
//            }
//        }
//
//
//        if( ! hmExcelRowData.get("action_checkout").toString().isEmpty()){
//            myLog.info("Log: attempting checkout with given quantity");
//            pgCheckout.PerformCheckout(Integer.valueOf(hmExcelRowData.get("checkout_qty").toString()));
//            if( ! hmExcelRowData.get("checkout_expected").toString().isEmpty()){
//                myLog.info("Log: validating checkout");
//                if(hmExcelRowData.get("checkout_expected").toString().equals("payment_screen")){
//                    pgCheckout.ValidateCheckoutResult(true);
//                }else{
//                    pgCheckout.ValidateCheckoutResult(false);
//                }
//            }
//        }
//
//
//        if( ! hmExcelRowData.get("action_payment").toString().isEmpty()){
//            myLog.info("Log: attempting payment with given data");
//            pgPayment.PerformPayment(hmExcelRowData.get("payment_method").toString());
//            if( ! hmExcelRowData.get("payment_expected").toString().isEmpty()) {
//                myLog.info("Log: validating payment");
//                pgPayment.ValidatePayment(true);
//            }
//        }
//
//        // mandatory last step
//        pgHome.ActualLogout();
//
//        realDriver.manage().deleteAllCookies();
    }


    @DataProvider(name="EmpDataProviderExcel")
    public Object[][] getDataFromDataproviderExcel(){
        String sExcelFilePath     = System.getProperty("user.dir")+ "\\src\\test\\resources\\externalData\\Ecommerce_data.xlsx";
        int nExcelRowCount        = GetExcelRowCount(sExcelFilePath)+1;
        Object[][] ary2DExcelData = new Object[nExcelRowCount][2];

        for (int k=0; k < nExcelRowCount; k++){
            HashMap<String, Object> hmRowData = ReadDataRowInHashMap(sExcelFilePath,k);
            ary2DExcelData[k][0] = k;
            ary2DExcelData[k][1] = hmRowData;
        }
        return ary2DExcelData;
    }

    public static int GetExcelRowCount(String sFilePath) {
        try {
            FileInputStream excelFile = new FileInputStream(new File(sFilePath));
            XSSFWorkbook workbook     = new XSSFWorkbook(excelFile);
            XSSFSheet worksheet       = workbook.getSheetAt(0);
            return worksheet.getLastRowNum();
        }catch(Exception g){
            Assert.fail(g.getStackTrace().toString());
        }
        return 0;
    }

    public static HashMap<String, Object> ReadDataRowInHashMap(String sExcelFile, int nRowIndex){
        HashMap<String, Object> hmDataRow = new HashMap<String, Object>();
        try {
            FileInputStream oInStream = new FileInputStream(new File(sExcelFile));
            XSSFWorkbook oWrkBook     = new XSSFWorkbook(oInStream);
            XSSFSheet oWrkSheet       = oWrkBook.getSheetAt(0);

            for (int k=0; k< oWrkSheet.getRow(nRowIndex).getLastCellNum(); k++){
                hmDataRow.put(
                        oWrkSheet.getRow(0).getCell(k).toString(),    // key
                        String.valueOf(oWrkSheet.getRow(nRowIndex).getCell(k)) // value
                );
            }
            oWrkBook.close();
            oInStream.close();
        }catch(IOException eXl){
            Assert.fail("UtilLog: Given excel file parsing error");
        }
        return hmDataRow;
    }

}
