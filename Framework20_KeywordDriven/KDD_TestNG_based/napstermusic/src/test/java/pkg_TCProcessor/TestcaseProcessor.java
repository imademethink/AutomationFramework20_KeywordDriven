package pkg_TCProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.testng.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import pkg_ItemProcessor.RobotProcessor;
import pkg_ItemProcessor.SeleinumJavascriptProcessor;
import pkg_ItemProcessor.SeleinumProcessor;
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
            if(null == objTcId){continue;}
            else if( String.valueOf(objTcId).isEmpty()){continue;}
            else if( String.valueOf(objTcId).trim().isEmpty()){continue;}
            else if (! objTcId.toString().equals(sTcId)){continue;}
            else{
                String sTcIndex          = exlWorkSheetTC.getRow(nRow).getCell(0).toString();
                hmGlobalData.put("currenttcindex", sTcIndex);
                String sTcActionCategory = exlWorkSheetTC.getRow(nRow).getCell(2).toString().trim();
                String sTcAction         = null;
                String sTcParticular     = null;

                switch (sTcActionCategory){
                    case "tc_create":
                    case "tc_enable":
                    case "tc_tag":
                        break;
                    case "selenium":
                        sTcAction         = exlWorkSheetTC.getRow(nRow).getCell(3).toString().trim();
                        sTcParticular     = exlWorkSheetTC.getRow(nRow).getCell(4).toString();
                        try{
                            SeleinumProcessor.ProcessSeleniumItem(sTcIndex, sTcAction, sTcParticular);
                        }catch (Exception tcProcessor) {
                            UpdateExceptionDetails(sTcIndex, tcProcessor);
                            GenerateScreenshot();
                            Assert.fail("Log: TCProcessor \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                        }
                        break;
                    case "javascript":
                        sTcAction         = exlWorkSheetTC.getRow(nRow).getCell(3).toString().trim();
                        sTcParticular     = exlWorkSheetTC.getRow(nRow).getCell(4).toString();
                        try{
                            SeleinumJavascriptProcessor.ProcessSeleniumJavascriptItem(sTcIndex, sTcAction, sTcParticular);
                        }catch (Exception tcProcessor) {
                            UpdateExceptionDetails(sTcIndex, tcProcessor);
                            Assert.fail("Log: TCProcessor \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                        }
                        break;
                    case "robot":
                        sTcAction         = exlWorkSheetTC.getRow(nRow).getCell(3).toString().trim();
                        sTcParticular     = exlWorkSheetTC.getRow(nRow).getCell(4).toString();
                        try{
                            RobotProcessor.ProcessRobotItem(sTcIndex, sTcAction, sTcParticular);
                        }catch (Exception tcProcessor) {
                            UpdateExceptionDetails(sTcIndex, tcProcessor);
                            Assert.fail("Log: TCProcessor \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                        }
                        break;
                    default:
                        if(sTcActionCategory.startsWith("tc_start_step_") || sTcActionCategory.startsWith("tc_end_step_") ){
                            myLog.info("Log: ProcessTestCase " + sTcActionCategory);
                        }
                        break;
                }

            }
        }
    }

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

}
