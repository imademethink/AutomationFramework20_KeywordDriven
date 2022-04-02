package pkg_TCProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import pkg_ItemProcessor.RobotProcessor;
import pkg_ItemProcessor.SQLProcessor;
import pkg_ItemProcessor.SeleniumJavascriptProcessor;
import pkg_ItemProcessor.SeleniumProcessor;
import pkg_global.GlobalObjects;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class TestcaseProcessor extends GlobalObjects {
    private static final Logger myLog = Logger.getLogger(TestcaseProcessor.class);

    public static void ProcessTestCase(String sTcId){
        hmGlobalData.put("currenttcid", sTcId);
        if(hmGlobalData.get("globalexception").contains("yes")){
            myLog.info("Log: TCProcessor : "+ "Unable to proceed due to existing exception in fixtures " + sTcId);
            return;
        }

        ProcessTcName(sTcId);

        // Start processing actual steps
        XSSFSheet exlWorkSheetTC = exlWorkBook.getSheet(sKDDexcelsheet_testcases);
        for(int nRow=1; nRow <= exlWorkSheetTC.getLastRowNum(); nRow ++){
            Object objTcId = exlWorkSheetTC.getRow(nRow).getCell(1);
            if( ! validateTcIdColmn(objTcId, sTcId)) {continue;}

            String sTcIndex          = exlWorkSheetTC.getRow(nRow).getCell(0).toString();
            hmGlobalData.put("currenttcindex", sTcIndex);
            String sTcActionCategory = exlWorkSheetTC.getRow(nRow).getCell(2).toString().trim();
            String sTcActivity       = null;
            String sTcParticular     = null;

            // perform as per specific category
            switch (sTcActionCategory){
                case "tc_create":
                case "tc_enable":
                case "tc_tag":
                    // these individual validation is taken care earlier
                    break;
                case "selenium":
                    sTcActivity       = exlWorkSheetTC.getRow(nRow).getCell(3).toString().trim();
                    sTcParticular     = exlWorkSheetTC.getRow(nRow).getCell(4).toString();
                    try{
                        SeleniumProcessor.ProcessSeleniumItem(sTcIndex, sTcActivity, sTcParticular);
                    }catch (Exception tcProcessor) {
                        UpdateExceptionDetails(sTcIndex, tcProcessor);
                        GenerateScreenshot();
                        Assert.fail("Log: TCProcessor \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                    }
                    break;
                case "javascript":
                    sTcActivity       = exlWorkSheetTC.getRow(nRow).getCell(3).toString().trim();
                    sTcParticular     = exlWorkSheetTC.getRow(nRow).getCell(4).toString();
                    try{
                        SeleniumJavascriptProcessor.ProcessSeleniumJavascriptItem(sTcIndex, sTcActivity, sTcParticular);
                    }catch (Exception tcProcessor) {
                        UpdateExceptionDetails(sTcIndex, tcProcessor);
                        Assert.fail("Log: TCProcessor \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                    }
                    break;
                case "robot":
                    sTcActivity       = exlWorkSheetTC.getRow(nRow).getCell(3).toString().trim();
                    sTcParticular     = exlWorkSheetTC.getRow(nRow).getCell(4).toString();
                    try{
                        RobotProcessor.ProcessRobotItem(sTcIndex, sTcActivity, sTcParticular);
                    }catch (Exception tcProcessor) {
                        UpdateExceptionDetails(sTcIndex, tcProcessor);
                        Assert.fail("Log: TCProcessor \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                    }
                    break;
                case "db":
                    sTcActivity       = exlWorkSheetTC.getRow(nRow).getCell(3).toString().trim();
                    sTcParticular     = exlWorkSheetTC.getRow(nRow).getCell(4).toString();
                    try{
                        SQLProcessor.ProcessSQLItem(sTcIndex, sTcActivity, sTcParticular);
                    }catch (Exception tcProcessor) {
                        UpdateExceptionDetails(sTcIndex, tcProcessor);
                        GenerateScreenshot();
                        Assert.fail("Log: TCProcessor \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                    }
                    break;
                case "api":
                    sTcActivity       = exlWorkSheetTC.getRow(nRow).getCell(3).toString().trim();
                    sTcParticular     = exlWorkSheetTC.getRow(nRow).getCell(4).toString();
                    // TBD
                default:
                    if(sTcActionCategory.startsWith("tc_start_step_") || sTcActionCategory.startsWith("tc_end_step_") ){
                        myLog.info("Log: ProcessTestCase " + sTcActionCategory);
                    }
                    break;
            }
        }
    }

    // house keeping item tc_create is must for any test case
    // for every test case to begin tc_create is must
    public static void ProcessTcName(String sTcId){
        XSSFSheet exlWorkSheetTC = exlWorkBook.getSheet(sKDDexcelsheet_testcases);
        for(int nRow=1; nRow <= exlWorkSheetTC.getLastRowNum(); nRow ++){
            Object objTcId = exlWorkSheetTC.getRow(nRow).getCell(1);
            if(null == objTcId){continue;}
            else if( String.valueOf(objTcId).isEmpty()){continue;}
            else if( String.valueOf(objTcId).trim().isEmpty()){continue;}
            else{
                String sTcIdTemp = objTcId.toString();
                if(sTcIdTemp.equals(sTcId)){
                    if (exlWorkSheetTC.getRow(nRow).getCell(2).toString().contains("tc_create")){
                        hmGlobalData.put("currenttcname", exlWorkSheetTC.getRow(nRow).getCell(3).toString());
                        myLog.info("Log: TCProcessor : Current TcName    : " + hmGlobalData.get("currenttcname"));
                        return;
                    }
                }
            }
        }
        Assert.fail("Log: No test name found for test case with id :" + sTcId);
        return;
    }

    private static void UpdateExceptionDetails(String sTcIndex, Exception exceptionProcessor){
        hmGlobalData.put(sTcIndex, "fail");
        hmGlobalData.put("globalexception","yes");
        hmGlobalData.put("globalexceptionstage",sTcIndex);
        hmGlobalData.put("globalexceptionmsg",Arrays.toString(exceptionProcessor.getStackTrace()));
    }

    public static void GenerateScreenshot(){
        try{
            File fileSource              = ((TakesScreenshot)realDriver).getScreenshotAs(OutputType.FILE);
            String sScreenshotBasePath   = System.getProperty("user.dir") + hmGlobalData.get("screenshot_path");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String sScreenshotFilePath   =
                    sScreenshotBasePath + File.separator + "screenshot_" + simpleDateFormat.format(new Date()) + ".png";
            FileUtils.copyFile(fileSource, new File(sScreenshotFilePath));
            hmGlobalData.put("screenshotpath", sScreenshotFilePath);
        }catch (Exception screenshotEx){
            screenshotEx.printStackTrace();
        }
    }

    // just to make sure it is not null, not empty, matches with test case id
    // sometimes due to human error this colmn can be empty
    public static boolean validateTcIdColmn(Object objTcId, String sTcId){
        if(null == objTcId){return false;}
        else if( String.valueOf(objTcId).isEmpty()){return false;}
        else if( String.valueOf(objTcId).trim().isEmpty()){return false;}
        else if (! objTcId.toString().equals(sTcId)){return false;}
        return true;
    }
}
