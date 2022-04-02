package pkg_testrunner;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pkg_utility.Utility_Filehandler;
import pkg_utility.Utility_General;


public class TestRunnerFramework_DataDriven {

    @BeforeClass
    public static void setupBeforeClass() {
        //GlobalInit
//        new Utility_General().GetCommandLineParam();
//
//        new Utility_Filehandler().ExcelDataReaderInit();
//
//        new Utility_Filehandler().PropertiesDataReaderInit();
//
//        new Utility_Filehandler().CsvDataReaderInit();
//
//        new Utility_General().DbConnectionInit();

        // Making required environment web-app up and running - optional step

        // Launching web browser - To be done before cucumber every scenario
    }

    @AfterClass
    public static void teardownAfterClass() {
        //GlobalTearDown
        // Shutting down web browser - To be done after cucumber every scenario

        // Making required environment web-app down - optional step
//
//        new Utility_General().DbConnectionClose();
//
//        new Utility_Filehandler().ExcelDataReaderClose();
//
//        new Utility_General().GenerateReport();

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


}


// DataDriven framework
//      Using Cucumber
//      Using DataProvider



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
