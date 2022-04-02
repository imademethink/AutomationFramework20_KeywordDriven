package pkg_FixtureProcessor;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Assert;
import pkg_ItemProcessor.SeleniumProcessor;
import pkg_global.GlobalObjects;
import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FixtureProcessor extends GlobalObjects {

    private static final Logger myLog = Logger.getLogger(FixtureProcessor.class);

    public static void ProcessFixture_BeforeAll(){
        myLog.info("\n\n");
        XSSFSheet exlWorkSheetFixture = exlWorkBook.getSheet(sKDDexcelsheet_fixture);

        for(int nRow=1; nRow <= exlWorkSheetFixture.getLastRowNum(); nRow ++){
            String sFixtureIndex     = exlWorkSheetFixture.getRow(nRow).getCell(0).toString();
            Object objFixtureSchedule= exlWorkSheetFixture.getRow(nRow).getCell(2);

            if(null == objFixtureSchedule){continue;}
            else if( String.valueOf(objFixtureSchedule).isEmpty()){continue;}
            else if( String.valueOf(objFixtureSchedule).trim().isEmpty()){continue;}
            else if(!String.valueOf(objFixtureSchedule).trim().contains("before_all")){continue;}

            String sFixtureType      = exlWorkSheetFixture.getRow(nRow).getCell(1).toString();
            String sFixtureAction    = null;
            if(null != exlWorkSheetFixture.getRow(nRow).getCell(3)){
                sFixtureAction = exlWorkSheetFixture.getRow(nRow).getCell(3).toString();
            }
            String sFixtureParticular= null;
            if(null != exlWorkSheetFixture.getRow(nRow).getCell(4)){
                sFixtureParticular = exlWorkSheetFixture.getRow(nRow).getCell(4).toString();
            }

            switch (sFixtureType.toLowerCase().trim()){
                case "report":
                    ProcessReporting(sFixtureIndex, "", sFixtureAction,  sFixtureParticular);
                    break;
                case "command_line_param":
                    myLog.info("\n\n");
                    ProcessCommandLineParameters(sFixtureIndex, sFixtureParticular);
                    break;
                case "db":
                    myLog.info("\n\n");
                    ProcessDatabaseParameters(sFixtureIndex, sFixtureAction, sFixtureParticular);
                    break;
                case "config":
                    myLog.info("\n\n");
                    ProcessConfigParameters(sFixtureIndex);
                    break;
                case "pageobject":
                    myLog.info("\n\n");
                    ProcessPageObject(sFixtureIndex);
                    break;
            }
        }
    }

    public static void ProcessReporting(String sFixtureIndex, String sCurrentTcId, String sFixtureAction, String  sFixtureParticular){
        switch (sFixtureAction.toLowerCase().trim()){
            case "create_excel_sheet":
                try {
                    // if result sheet already exists then remove it
                    String sResultSheetName    = sFixtureParticular.split("=")[1];
                    sKDDexcelsheet_resultsheet = sResultSheetName;
                    int nResultSheetIndex      = exlWorkBook.getSheetIndex(sResultSheetName);
                    if(-1 != nResultSheetIndex){
                        exlWorkBook.removeSheetAt(nResultSheetIndex);
                    }
                    // now clone testcases sheet and rename it to what user wants
                    exlWorkBook.cloneSheet(0,sResultSheetName);
                    // set result sheet at last position
                    exlWorkBook.setSheetOrder(sResultSheetName,exlWorkBook.getNumberOfSheets() -1 );
                    exlInStream.close();
                    // prepare to write changes to same file
                    exlOutStream = new FileOutputStream(new File(System.getProperty("user.dir") + sKDDexcelpath));
                    exlWorkBook.write(exlOutStream);
                    exlWorkBook.close();
                    exlOutStream.close();
                    // back to normal processing
                    exlInStream   = new FileInputStream(new File(System.getProperty("user.dir") + sKDDexcelpath));
                    exlWorkBook   = new XSSFWorkbook(exlInStream);
                    hmGlobalData.put("resultsheetname", sResultSheetName);
                    myLog.info("Log: FixtureProcessor : ProcessReporting " + sFixtureIndex + " " + sFixtureAction);
                }catch (IOException fixtureProcessor) {
                    hmGlobalData.put("globalexception","yes");
                    hmGlobalData.put("globalexceptionstage",sFixtureIndex);
                    hmGlobalData.put("globalexceptionmsg",((Exception) fixtureProcessor).getMessage());
                    Assert.fail("Log: ProcessReporting \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                }
                break;
            case "send_email":
                try {
                    // close excel file
                    if(null != exlWorkBook)  exlWorkBook.close();
                    if(null != exlInStream)  exlInStream.close();
                    myLog.info("\n\nLog: Please make sure to disable 2 step login for your GMAIL\n");
                    myLog.info("\n\nLog: Please make sure to enable Less secure app login from your GMAIL\n");
                    myLog.info("\n\nLog: Email sending in progress...\n");

                    String sHost      = "smtp.gmail.com";
                    int nPort         = 587;   //  465 is for SSL and 587 is for TLS
                    String sGmailuser = hmGlobalData.get("email_report_from_email");
                    String sGmailPwd  = hmGlobalData.get("email_report_gmail_pwd");

                    String sEmailAddrFrom=hmGlobalData.get("email_report_from_email");
                    String sEmailAddrTo  =hmGlobalData.get("email_report_to_email");
                    String sEmailSubject = "Automation Test Report";
                    String sEmailBody    = "Please find attached Automation Test Report";

                    HtmlEmail email      = new HtmlEmail ();
                    email.setHostName(sHost);
                    email.setSmtpPort(nPort);
                    email.setAuthenticator(new DefaultAuthenticator(sGmailuser, sGmailPwd));
                    email.setSSLOnConnect(true);
                    email.setFrom(sEmailAddrFrom);
                    email.setSubject(sEmailSubject);
                    email.setMsg(sEmailBody);
                    email.addTo(sEmailAddrTo);
                    String sReportFilePath     = System.getProperty("user.dir") + sKDDexcelpath;
                    EmailAttachment attachment = new EmailAttachment();
                    attachment.setPath(sReportFilePath);
                    attachment.setDisposition(EmailAttachment.ATTACHMENT);
                    attachment.setName("TestReport.xlsx");
                    email.attach(attachment);
                    email.send();

                    // back to normal processing
                    exlInStream   = new FileInputStream(new File(System.getProperty("user.dir") + sKDDexcelpath));
                    exlWorkBook   = new XSSFWorkbook(exlInStream);
                    myLog.info("Log: FixtureProcessor : ProcessReporting " + sFixtureIndex + " " + sFixtureAction);
                }catch (IOException | org.apache.commons.mail.EmailException fixtureProcessor) {
                    fixtureProcessor.printStackTrace();
                    hmGlobalData.put("globalexception","yes");
                    hmGlobalData.put("globalexceptionstage",sFixtureIndex);
                    hmGlobalData.put("globalexceptionmsg",((Exception) fixtureProcessor).getMessage());
                    Assert.fail("Log: ProcessReporting \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                }
                break;
            case "update_excel_sheet":
                try {
                    XSSFSheet exlWorkSheetResult = exlWorkBook.getSheet(sKDDexcelsheet_resultsheet);
                    XSSFCellStyle cellStyleGreen = exlWorkBook.createCellStyle();
                    cellStyleGreen.setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 192, 0)));
                    cellStyleGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    XSSFCellStyle cellStyleRed = exlWorkBook.createCellStyle();
                    cellStyleRed.setFillForegroundColor(new XSSFColor(new java.awt.Color(192, 0, 0)));
                    cellStyleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                    int nTCindexcount            = exlWorkSheetResult.getLastRowNum();
                    for(int nRow=1; nRow <= nTCindexcount; nRow ++){
                        String sIndexString = "index_" + String.format("%04d", nRow);
                        if (null == hmGlobalData.get(sIndexString))   {continue;}
                        if (hmGlobalData.get(sIndexString).isEmpty()) {continue;}
                        if( ! exlWorkSheetResult.getRow(1 + nRow).getCell(1).toString().equals(sCurrentTcId) ){continue;}
                        // update result for each test case index
                        exlWorkSheetResult.getRow(1 + nRow).createCell(nKDDexcelsheet_resultcolumn, CellType.STRING);
                        if(hmGlobalData.get(sIndexString).equals("pass")){
                            exlWorkSheetResult.getRow(1 + nRow).getCell(nKDDexcelsheet_resultcolumn).setCellStyle(cellStyleGreen);
                            exlWorkSheetResult.getRow(1 + nRow).getCell(nKDDexcelsheet_resultcolumn).setCellValue(hmGlobalData.get(sIndexString));
                        }else{
                            UpdateFailure(exlWorkBook, exlWorkSheetResult, nRow, cellStyleRed, sIndexString );
                        }
                        myLog.info("Log: ProcessReporting : " + sFixtureIndex + " : " + sIndexString + " : " + hmGlobalData.get(sIndexString));
                    }
                    exlInStream.close();
                    // prepare to write changes to same file
                    exlOutStream = new FileOutputStream(new File(System.getProperty("user.dir") + sKDDexcelpath));
                    exlWorkBook.write(exlOutStream);
                    exlWorkBook.close();
                    exlOutStream.close();
                    // back to normal processing
                    exlInStream   = new FileInputStream(new File(System.getProperty("user.dir") + sKDDexcelpath));
                    exlWorkBook   = new XSSFWorkbook(exlInStream);
                }catch (Exception fixtureProcessor) {
                    hmGlobalData.put("globalexception","yes");
                    hmGlobalData.put("globalexceptionstage",sFixtureIndex);
                    hmGlobalData.put("globalexceptionmsg",((Exception) fixtureProcessor).getMessage());
                    Assert.fail("Log: ProcessReporting \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                }
                break;
        }
    }

    private static void UpdateFailure(XSSFWorkbook exlWorkBook, XSSFSheet exlWorkSheetResult, int nRow, XSSFCellStyle cellStyleRed, String sIndexString){
        exlWorkSheetResult.getRow(1 + nRow).getCell(nKDDexcelsheet_resultcolumn).setCellStyle(cellStyleRed);
        exlWorkSheetResult.getRow(1 + nRow).getCell(nKDDexcelsheet_resultcolumn).setCellValue(hmGlobalData.get(sIndexString));
        exlWorkSheetResult.getRow(1 + nRow).createCell(nKDDexcelsheet_resultcolumn + 1, CellType.STRING);
        exlWorkSheetResult.getRow(1 + nRow).getCell(nKDDexcelsheet_resultcolumn + 1).setCellStyle(cellStyleRed);
        exlWorkSheetResult.getRow(1 + nRow).getCell(nKDDexcelsheet_resultcolumn + 1).setCellValue(hmGlobalData.get("globalexceptionmsg"));
        exlWorkSheetResult.getRow(2 + nRow).createCell(nKDDexcelsheet_resultcolumn + 2, CellType.STRING);

        if (null == hmGlobalData.get("screenshotpath")){return;}
        if (hmGlobalData.get("screenshotpath").isEmpty()){return;}

        try{
            InputStream inStreamImage = new FileInputStream(hmGlobalData.get("screenshotpath"));
            byte[] aryBytes           = IOUtils.toByteArray(inStreamImage);
            int nImageId              = exlWorkBook.addPicture(aryBytes, exlWorkBook.PICTURE_TYPE_PNG);
            inStreamImage.close();
            XSSFDrawing objDrawing    = (XSSFDrawing) exlWorkSheetResult.createDrawingPatriarch();
            XSSFClientAnchor objAnchor= new XSSFClientAnchor();
            // image start column
            objAnchor.setCol1(7);
            // image start row
            objAnchor.setRow1(1 + nRow);
            // image end column
            objAnchor.setCol2(8);
            // image end row
            objAnchor.setRow2(2 + nRow);
            objDrawing.createPicture(objAnchor, nImageId);
            Thread.sleep(5000);
        }catch (Exception insertScreenshot){
            //insertScreenshot.printStackTrace();
        }
    }

    public static void ProcessCommandLineParameters(String sFixtureIndex, String  sFixtureParticular){
        try{
            for(int k=0; k < sFixtureParticular.split(",").length; k++){
                String sPropertyName  = sFixtureParticular.split(",")[k];
                String sPropertyValue = System.getProperty(sPropertyName);
                hmGlobalData.put(sPropertyName, sPropertyValue);
                myLog.info("Log: FixtureProcessor : ProcessCommandLineParameters " + sFixtureIndex + " " +
                        sPropertyName + "=" + sPropertyValue);
            }
        }catch(Exception fixtureProcessor){
            hmGlobalData.put("globalexception","yes");
            hmGlobalData.put("globalexceptionstage",sFixtureIndex);
            hmGlobalData.put("globalexceptionmsg",fixtureProcessor.getStackTrace().toString());
            Assert.fail("Log: ProcessCommandLineParameters \n" + hmGlobalData.get("globalexceptionstage"));
        }
    }

    public static void ProcessDatabaseParameters(String sFixtureIndex, String sFixtureAction, String sFixtureParticular){
        switch (sFixtureAction.toLowerCase().trim()){
            case "connect":
                try {
                    // currently only SQLITE database supported
                    if (! sFixtureParticular.contains("dbtype=sqlite")) {
                        throw new SQLException("Log: This database is not currently supported");
                    }
                    Class.forName("org.sqlite.JDBC");
                    String sSQLiteDbUrl = sFixtureParticular.split("dburl=")[1];
                    sSQLiteDbUrl        = sSQLiteDbUrl.split(",")[0];
                    sSQLiteDbUrl        = sSQLiteDbUrl.replace("__replaceme__", System.getProperty("user.dir"));
                    sqliteConn          = DriverManager.getConnection(sSQLiteDbUrl);
                    myLog.info("Log: FixtureProcessor : ProcessDatabaseParameters " + sFixtureIndex + " " + sFixtureAction);
                }catch (SQLException | ClassNotFoundException fixtureProcessor) {
                    hmGlobalData.put("globalexception","yes");
                    hmGlobalData.put("globalexceptionstage",sFixtureIndex);
                    hmGlobalData.put("globalexceptionmsg",((Exception) fixtureProcessor).getMessage());
                    Assert.fail("Log: ProcessDatabaseParameters \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                }
                break;
            case "disconnect":
                try {
                    if(null!=sqliteConn) sqliteConn.close();
                    sqliteConn = null;
                    myLog.info("Log: FixtureProcessor : ProcessDatabaseParameters " + sFixtureIndex + " " + sFixtureAction);
                }catch (SQLException fixtureProcessor) {
                    hmGlobalData.put("globalexception","yes");
                    hmGlobalData.put("globalexceptionstage",sFixtureIndex);
                    hmGlobalData.put("globalexceptionmsg",((Exception) fixtureProcessor).getMessage());
                    Assert.fail("Log: ProcessDatabaseParameters \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                }
                break;
        }
    }

    public static void ProcessConfigParameters(String sFixtureIndex){
        myLog.info("Log: FixtureProcessor : ProcessConfigParameters " + sFixtureIndex);

        XSSFSheet exlWorkSheetConfig = exlWorkBook.getSheet(sKDDexcelsheet_config);
        for(int nRow=1; nRow <= exlWorkSheetConfig.getLastRowNum(); nRow ++){
            String sConfigIndex = exlWorkSheetConfig.getRow(nRow).getCell(0).toString();
            Object objConfigKey = exlWorkSheetConfig.getRow(nRow).getCell(1);

            if(null == objConfigKey){continue;}
            else if( String.valueOf(objConfigKey).isEmpty()){continue;}
            else if( String.valueOf(objConfigKey).trim().isEmpty()){continue;}
            else{
                String sConfigValue = exlWorkSheetConfig.getRow(nRow).getCell(2).toString().trim();
                // correction if integer type is read e.g. 5 is read as 5.00 so correcting it
                sConfigValue = sConfigValue.replace(".00","");
                sConfigValue = sConfigValue.replace(".0","");
                myLog.info("Log: ProcessConfigParameters : " + sConfigIndex + " : " +
                        String.valueOf(objConfigKey).trim() + "=" + sConfigValue);
                hmGlobalData.put(String.valueOf(objConfigKey).trim(), sConfigValue);
            }
        }
    }

    public static void ProcessPageObject(String sFixtureIndex){
        myLog.info("Log: FixtureProcessor : ProcessPageObject " + sFixtureIndex);

        XSSFSheet exlWorkSheetPageObject = exlWorkBook.getSheet(sKDDexcelsheet_pageobject);
        for(int nRow=1; nRow <= exlWorkSheetPageObject.getLastRowNum(); nRow ++){
            String sPageObjectIndex  = exlWorkSheetPageObject.getRow(nRow).getCell(0).toString();
            Object objPageObjectKey1 = exlWorkSheetPageObject.getRow(nRow).getCell(1);

            if(null == objPageObjectKey1){continue;}
            else if( String.valueOf(objPageObjectKey1).isEmpty()){continue;}
            else if( String.valueOf(objPageObjectKey1).trim().isEmpty()){continue;}
            else{
                String sPageObjectKey1  = exlWorkSheetPageObject.getRow(nRow).getCell(1).toString().trim();
                String sPageObjectKey2  = exlWorkSheetPageObject.getRow(nRow).getCell(2).toString().trim();
                String sPageObjectValue = exlWorkSheetPageObject.getRow(nRow).getCell(3).toString().trim();
                myLog.info("Log: ProcessPageObject : " + sPageObjectIndex + " : " +
                        sPageObjectKey1 + "_" + sPageObjectKey2 + " = " + sPageObjectValue);
                hmGlobalData.put(sPageObjectKey1 + "_" + sPageObjectKey2, sPageObjectValue);
            }
        }
    }

    public static void ProcessFixture_BeforeTest(){
        XSSFSheet exlWorkSheetFixture = exlWorkBook.getSheet(sKDDexcelsheet_fixture);

        for(int nRow=1; nRow <= exlWorkSheetFixture.getLastRowNum(); nRow ++){
            String sFixtureIndex     = exlWorkSheetFixture.getRow(nRow).getCell(0).toString();
            Object objFixtureSchedule= exlWorkSheetFixture.getRow(nRow).getCell(2);

            if(null == objFixtureSchedule){continue;}
            else if( String.valueOf(objFixtureSchedule).isEmpty()){continue;}
            else if( String.valueOf(objFixtureSchedule).trim().isEmpty()){continue;}
            else if(!String.valueOf(objFixtureSchedule).trim().contains("before_test")){continue;}

            String sFixtureType      = exlWorkSheetFixture.getRow(nRow).getCell(1).toString();
            String sFixtureAction    = null;
            if(null != exlWorkSheetFixture.getRow(nRow).getCell(3)){
                sFixtureAction = exlWorkSheetFixture.getRow(nRow).getCell(3).toString();
            }
            String sFixtureParticular= null;
            if(null != exlWorkSheetFixture.getRow(nRow).getCell(4)){
                sFixtureParticular = exlWorkSheetFixture.getRow(nRow).getCell(4).toString();
            }

            switch (sFixtureType.toLowerCase().trim()){
                case "set_param":
                    hmGlobalData.put(sFixtureParticular.split("=")[0], sFixtureParticular.split("=")[1]);
                    myLog.info("Log: FixtureProcessor : SetParam " + sFixtureIndex + " " +
                            sFixtureParticular.split("=")[0] + "=" + sFixtureParticular.split("=")[1]);
                    break;
                case "selenium":
                    ProcessSeleniumParameters(sFixtureIndex, sFixtureAction, sFixtureParticular);
                    break;
            }
        }
    }

    public static void ProcessSeleniumParameters(String sFixtureIndex, String sFixtureAction, String sFixtureParticular){
        switch (sFixtureAction.toLowerCase().trim()){
            case "launch_browser":
                try {
                    // currently only chrome browser supported
                    if( ! hmGlobalData.get("browser_type").equals("chrome")){
                        Assert.fail("Log: This browser is not currently supported " + hmGlobalData.get("browser_type"));
                    }
                    SeleniumProcessor.InvokeBrowser();
                    myLog.info("Log: FixtureProcessor : ProcessSeleniumParameters " + sFixtureIndex + " " + sFixtureAction);
                }catch ( Exception fixtureProcessor) {
                    hmGlobalData.put("globalexception","yes");
                    hmGlobalData.put("globalexceptionstage",sFixtureIndex);
                    hmGlobalData.put("globalexceptionmsg",((Exception) fixtureProcessor).getMessage());
                    Assert.fail("Log: ProcessSeleniumParameters \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                }
                break;
            case "quit_browser":
                try {
                    if(null!=realDriver) realDriver.quit();
                    realDriver      = null;
                    bBrowserInvoked = false;
                    myLog.info("Log: Chrome browser is closed");
                    myLog.info("Log: FixtureProcessor : ProcessSeleniumParameters " + sFixtureIndex + " " + sFixtureAction);
                }catch (Exception fixtureProcessor) {
                    hmGlobalData.put("globalexception","yes");
                    hmGlobalData.put("globalexceptionstage",sFixtureIndex);
                    hmGlobalData.put("globalexceptionmsg",((Exception) fixtureProcessor).getMessage());
                    Assert.fail("Log: ProcessSeleniumParameters \n" + hmGlobalData.get("globalexceptionstage") + "\n" + hmGlobalData.get("globalexceptionmsg"));
                }
                break;
        }
    }

    public static void ProcessFixture_AfterTest(String sCurrentTcId){
        XSSFSheet exlWorkSheetFixture = exlWorkBook.getSheet(sKDDexcelsheet_fixture);

        for(int nRow=1; nRow <= exlWorkSheetFixture.getLastRowNum(); nRow ++){
            String sFixtureIndex     = exlWorkSheetFixture.getRow(nRow).getCell(0).toString();
            Object objFixtureSchedule= exlWorkSheetFixture.getRow(nRow).getCell(2);

            if(null == objFixtureSchedule){continue;}
            else if( String.valueOf(objFixtureSchedule).isEmpty()){continue;}
            else if( String.valueOf(objFixtureSchedule).trim().isEmpty()){continue;}
            else if(!String.valueOf(objFixtureSchedule).trim().contains("after_test")){continue;}

            String sFixtureType      = exlWorkSheetFixture.getRow(nRow).getCell(1).toString();
            String sFixtureAction    = null;
            if(null != exlWorkSheetFixture.getRow(nRow).getCell(3)){
                sFixtureAction = exlWorkSheetFixture.getRow(nRow).getCell(3).toString();
            }
            String sFixtureParticular= null;
            if(null != exlWorkSheetFixture.getRow(nRow).getCell(4)){
                sFixtureParticular = exlWorkSheetFixture.getRow(nRow).getCell(4).toString();
            }

            switch (sFixtureType.toLowerCase().trim()){
                case "report":
                    ProcessReporting(sFixtureIndex, sCurrentTcId, sFixtureAction, sFixtureParticular);
                    break;
                case "selenium":
                    ProcessSeleniumParameters(sFixtureIndex, sFixtureAction, sFixtureParticular);
                    break;
            }
        }
    }

    public static void ProcessFixture_AfterAll(){
        XSSFSheet exlWorkSheetFixture = exlWorkBook.getSheet(sKDDexcelsheet_fixture);

        for(int nRow=1; nRow <= exlWorkSheetFixture.getLastRowNum(); nRow ++){
            String sFixtureIndex     = exlWorkSheetFixture.getRow(nRow).getCell(0).toString();
            Object objFixtureSchedule= exlWorkSheetFixture.getRow(nRow).getCell(2);

            if(null == objFixtureSchedule){continue;}
            else if( String.valueOf(objFixtureSchedule).isEmpty()){continue;}
            else if( String.valueOf(objFixtureSchedule).trim().isEmpty()){continue;}
            else if(!String.valueOf(objFixtureSchedule).trim().contains("after_all")){continue;}

            String sFixtureType      = exlWorkSheetFixture.getRow(nRow).getCell(1).toString();
            String sFixtureAction    = null;
            if(null != exlWorkSheetFixture.getRow(nRow).getCell(3)){
                sFixtureAction = exlWorkSheetFixture.getRow(nRow).getCell(3).toString();
            }
            String sFixtureParticular= null;
            if(null != exlWorkSheetFixture.getRow(nRow).getCell(4)){
                sFixtureParticular = exlWorkSheetFixture.getRow(nRow).getCell(4).toString();
            }

            switch (sFixtureType.toLowerCase().trim()){
                case "db":
                    ProcessDatabaseParameters(sFixtureIndex, sFixtureAction, sFixtureParticular);
                    break;
                case "report":
                    ProcessReporting(sFixtureIndex, "", sFixtureAction, sFixtureParticular);
                    break;
            }
        }
    }

}
