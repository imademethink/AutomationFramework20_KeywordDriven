package pkg_testrunner;
import org.apache.log4j.Logger;
import org.testng.annotations.*;
import pkg_FixtureProcessor.FixtureProcessor;
import pkg_TCProcessor.TestcaseProcessor;
import pkg_global.GlobalObjects;
import pkg_utility.Utility_Filehandler;

public class TestFramework_KDD extends GlobalObjects {
    private static final Logger myLog = Logger.getLogger(TestFramework_KDD.class);
    public String sCurrentTcId        = "";

    // before_all steps from excel
    @BeforeSuite
    public static void globalSetUp(){
        hmGlobalData.put("globalexception","no");
        Utility_Filehandler.ExcelProcessorInit();
        FixtureProcessor.ProcessFixture_BeforeAll();
    }

    // before_test steps from excel
    @BeforeMethod
    public void beforeEachTestCase(){
        FixtureProcessor.ProcessFixture_BeforeTest();
    }




    @DataProvider(name="TcIdProvider")
    public Object[][] getDataFromDataproviderExcel(){
        Object[] aryTcId = Utility_Filehandler.ExcelSheetProcessor_GetTestCaseIds();
        int nTotalTc     = aryTcId.length;
        Object[][] ary2D = new Object[nTotalTc][2];

        for (int k=0; k < nTotalTc; k++){
            ary2D[k][0] = k;
            ary2D[k][1] = aryTcId[k];
        }
        return ary2D;
    }

    // core test case execution
    @Test(dataProvider="TcIdProvider")
    public void KDD_TestCaseExecutor(int nCnt, String sCurrentTcIdActual){
        sCurrentTcId = sCurrentTcIdActual;
        myLog.info("\n==================Tc Execution begins for "+sCurrentTcId+" ==================\n");
        TestcaseProcessor.ProcessTestCase(sCurrentTcId);
        myLog.info("\n==================Tc Execution ends for   "+sCurrentTcId+" ==================\n");
    }






    // after_test steps from excel
    @AfterMethod
    public void afterEachTestCase(){
        FixtureProcessor.ProcessFixture_AfterTest(sCurrentTcId);
        hmGlobalData.put("globalexception","no");
    }

    // after_all steps from excel
    @AfterSuite
    public static void globalTearDown(){
        FixtureProcessor.ProcessFixture_AfterAll();

        Utility_Filehandler.ExcelProcessorClose();
        hmGlobalData.put("globalexception","no");
    }


}



//  KDD framework





//  Conventional linear framework
//  Test scenario writing using cucumber
//  Tag controlled scenario filtering
//  Junit based test execution
//  Command line execution
//  Maven for jar file dependency management
//  Reporting - HTML (Extent), Excel, PDF
//  Failed tests with screenshot
//  Report emailing
//  Rerun failed tests n times
//  Extent reporting with failed tests logs as well
//  Cucumber hooks implementation so each scenario can focus on actual task
//  Junit fixtures usage - for external activities e.g. docker, database, website
//  Reading data from external files properties, csv, excel, pdf
//  Core Java concepts Inheritance, Collection framework
//  Numerous utilities for file handling, selenium
//  Utility to run tests headless, without image loading for speedy execution
//  Page Object, Page factory implementation
//  Logging using Log4j
//  Browser invoking - local, remote (selenium grid)
//  Sikuli / Robot framework
//  Galenium for alignment verification


