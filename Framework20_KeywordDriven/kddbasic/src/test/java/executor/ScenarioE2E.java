package executor;

import utilityParser.KeywordDrivenParser;
import utilityXLHandler.XLUtil;

public class ScenarioE2E {

    public static void main(String[] args) throws Exception{
        System.out.println("Scenario E2E");

        String [][] aryKddSteps = XLUtil.ReadData2DAry_KDDbasic();

        KeywordDrivenParser.KeywordParser(aryKddSteps);

    }

}
