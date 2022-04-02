package pkg_Utility;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import pkg_global.GlobalObjects;
import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Utility_Filehandler extends GlobalObjects {

    private static final Logger myLog = Logger.getLogger(Utility_Filehandler.class);

    public static void ExcelProcessorInit(){
        try {
            exlInStream   = new FileInputStream(new File(System.getProperty("user.dir") + sKDDexcelpath));
            exlWorkBook   = new XSSFWorkbook(exlInStream);
        }catch(IOException eXl){
            hmGlobalData.put("globalexception","yes");
            hmGlobalData.put("globalexceptionstage","ExcelProcessorInit");
            hmGlobalData.put("globalexceptionmsg",eXl.getStackTrace().toString());
            Assert.fail("Log: Given excel file parsing error " + sKDDexcelpath + "\n" + hmGlobalData.get("globalexceptionstage"));
        }
    }

    public static String[] ExcelSheetProcessor_GetTestCaseIds(){
        ExcelProcessor_Backup();

        ExcelProcessorInit();
        XSSFSheet exlWorkSheetTestCases = exlWorkBook.getSheet(sKDDexcelsheet_testcases);
        ArrayList<String> aryLstTCid    = new ArrayList<>();

        for(int nRow=1; nRow <= exlWorkSheetTestCases.getLastRowNum(); nRow ++){
            Object objTcId = exlWorkSheetTestCases.getRow(nRow).getCell(1);
            if(null == objTcId){continue;}
            else if( String.valueOf(objTcId).isEmpty()){continue;}
            else if( String.valueOf(objTcId).trim().isEmpty()){continue;}
            else{
                String sTcId = objTcId.toString();
                sTcId        = sTcId.replace("tc_","");
                if(sTcId.isEmpty()){continue;}
                if(exlWorkSheetTestCases.getRow(nRow).getCell(2).toString().equals("tc_enable")){
                    String sTcEnabled = exlWorkSheetTestCases.getRow(nRow).getCell(3).toString();
                    // if TC enabled = no then continue
                    if(sTcEnabled.equals("no")){
                        myLog.info("Log: TC with following id is disabled " + "tc_"+sTcId + "\n");
                        continue;
                    }
                    // TC enabled = yes, now check the tag pattern matches or not
                    if(sTcEnabled.equals("yes")){
                        if(ProcessTcTags(
                                "tc_"+sTcId,
                                exlWorkSheetTestCases.getRow(nRow).getCell(4).toString())){
                            aryLstTCid.add("tc_"+sTcId);
                        }
                    }
                }
            }
        }
        hmGlobalData.put("totaltestcount",String.valueOf(aryLstTCid.size()));
        ExcelProcessorClose();

        if(0 == aryLstTCid.size()){
            myLog.info("\n\nLog: Either all TC are disabled OR required tag criteria does not match with any TC \n\n");
            Assert.fail();
        }
        return aryLstTCid.toArray(new String[aryLstTCid.size()]);
    }

    public static void ExcelProcessorClose(){
        try {
            if(null != exlWorkBook)  exlWorkBook.close();
            if(null != exlInStream)  exlInStream.close();
        }catch(IOException eXl){
            Assert.fail("Log: Given excel file closing error " + sKDDexcelpath);
        }
    }

    public static void ExcelProcessor_Backup(){
        // Taking backup of existing excel in case gets corrupted
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime now     = LocalDateTime.now();
            String sKDDexcelpathBackup = sKDDexcelpath.replace(".xlsx","") + dtf.format(now).toLowerCase() + ".xlsx";

            FileUtils.copyFile(
                    new File(System.getProperty("user.dir") + sKDDexcelpath),
                    new File(System.getProperty("user.dir") + sKDDexcelpathBackup)
            );
        }catch(IOException eXl){
            hmGlobalData.put("globalexception","yes");
            hmGlobalData.put("globalexceptionstage","ExcelProcessorInit");
            hmGlobalData.put("globalexceptionmsg",eXl.getStackTrace().toString());
            Assert.fail("Log: Given excel file copying error " + sKDDexcelpath + "\n" + hmGlobalData.get("globalexceptionstage"));
        }
    }

    public static boolean ProcessTcTags(String sTcId, String sTcActualTags){
        // user need to pass required tag(s) as command line parameter
        //  -Dtag="login"
        //  -Dtag="login or search"   // simple OR
        //  -Dtag="login and search"  // simple AND

        // if no tag passed then all TC will be executed
        hmGlobalData.put("tag",System.getProperty("tag"));
        if(null == hmGlobalData.get("tag"))   return true;
        if(hmGlobalData.get("tag").isEmpty()) return true;

        myLog.info("Log: TCProcessor : Current user provided Tc Tags    : " + hmGlobalData.get("tag"));
        sTcActualTags                = sTcActualTags.replace("tc_tag=","");
        String [] aryActualTcTags    = sTcActualTags.split(",");
        List<String> lstActualTcTags = Arrays.asList(aryActualTcTags);
        // logical OR operation
        if(hmGlobalData.get("tag").toLowerCase().contains(" or ")){
            String [] aryRequiredTags    =  hmGlobalData.get("tag").split(" or ");
            for(String sOneRequiredTag : aryRequiredTags){
                if(lstActualTcTags.contains(sOneRequiredTag)) return true;
            }
        }
        // logical AND operation
        if(hmGlobalData.get("tag").toLowerCase().contains(" and ")){
            String [] aryRequiredTags    =  hmGlobalData.get("tag").split(" and ");
            int nAllTagMatch             = 0;
            for(String sOneRequiredTag : aryRequiredTags){
                if(lstActualTcTags.contains(sOneRequiredTag)) nAllTagMatch++;;
            }
            if(aryRequiredTags.length == nAllTagMatch) return true;
        }
        // no condition - single tag
        if(lstActualTcTags.contains(hmGlobalData.get("tag"))) return true;

        myLog.info("Log: TCProcessor : " + "Following TC do not meet tag criteria provided by user " + sTcId);
        return false;
    }

}
