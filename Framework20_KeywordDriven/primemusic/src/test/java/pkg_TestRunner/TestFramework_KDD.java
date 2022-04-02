package pkg_TestRunner;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pkg_FixtureProcessor.FixtureProcessor;
import pkg_TCProcessor.TestcaseProcessor;
import pkg_global.GlobalObjects;
import pkg_Utility.Utility_Filehandler;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestFramework_KDD extends GlobalObjects {
    private static final Logger myLog = Logger.getLogger(TestFramework_KDD.class);
    public String sCurrentTcId        = "";

    public TestFramework_KDD(String sCurrentTcIdTemp) {sCurrentTcId = sCurrentTcIdTemp;}

    // Parameterized will provide iteration related data (e.g. total iteration, sCurrentTcId)
    @Parameterized.Parameters
    public static Collection DataSupplier() {
        Object[] aryTcId = Utility_Filehandler.ExcelSheetProcessor_GetTestCaseIds();
        return Arrays.asList(aryTcId);
    }





    // before_all steps from excel
    @BeforeClass
    public static void globalSetUp(){
        hmGlobalData.put("globalexception","no");
        Utility_Filehandler.ExcelProcessorInit();
        FixtureProcessor.ProcessFixture_BeforeAll();
    }

    // before_test steps from excel
    @Before
    public void beforeEachTestCase(){
        FixtureProcessor.ProcessFixture_BeforeTest();
    }





    // core test case execution
    @Test
    public void KDD_TestCaseExecutor(){
        myLog.info("\n==================Tc Execution begins for "+sCurrentTcId+" ==================\n");
        TestcaseProcessor.ProcessTestCase(sCurrentTcId);
        myLog.info("\n==================Tc Execution ends for   "+sCurrentTcId+" ==================\n");
    }






    @After // after_test steps from excel
    public void afterEachTestCase(){
        FixtureProcessor.ProcessFixture_AfterTest(sCurrentTcId);
        hmGlobalData.put("globalexception","no");
    }

    @AfterClass // after_all steps from excel
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
//  Command line execution -> mvn clean test -Dcurrent_environment="sit"  -Dtest_priority="high"
//  Maven for jar file dependency management
//  Reporting - HTML (Extent), Excel, PDF
//  Failed tests with screenshot
//  Report emailing
//  Rerun failed tests n times - TBD
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


