package utilityXLHandler;

import globalItem.GlobalObject;
import org.junit.Assert;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.junit.Assert;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import java.util.Iterator;

public class XLUtil extends GlobalObject {

    public static Object[][] ReadData2DAry_simple(){
        try {
            FileInputStream oInStream = new FileInputStream(new File(sxlEmpDataPath));
            XSSFWorkbook oWrkBook     = new XSSFWorkbook(oInStream);
            XSSFSheet oWrkSheet       = oWrkBook.getSheet("employee");
            int nMaxRow               = oWrkSheet.getLastRowNum();
            int nMaxCol               = oWrkSheet.getRow(0).getLastCellNum();
            Object[][] ary2D          = new Object[nMaxRow][nMaxCol];

            for (int r=0; r < oWrkSheet.getLastRowNum(); r++){
                XSSFRow oRow          = oWrkSheet.getRow(r);
                for (int c=0; c < oRow.getLastCellNum(); c++){
                    ary2D[r][c] = oRow.getCell(c);
                    System.out.print(oRow.getCell(c) + " ");
                }
                System.out.println();
            }
            oWrkBook.close();
            oInStream.close();
            return ary2D;
        }catch(IOException eXl){
            Assert.fail("UtilLog: Given excel file parsing error");
            return null;
        }
    }

    public static String[][] ReadData2DAry_KDDbasic(){
        try {
            FileInputStream oInStream = new FileInputStream(new File(sxlKDDFilePath));
            XSSFWorkbook oWrkBook     = new XSSFWorkbook(oInStream);
            XSSFSheet oWrkSheet       = oWrkBook.getSheet("kdd_basic");
            int nMaxRow               = oWrkSheet.getLastRowNum();
            int nMaxCol               = oWrkSheet.getRow(0).getLastCellNum();
            String[][] ary2D          = new String[nMaxRow][nMaxCol];

            for (int r=0; r < oWrkSheet.getLastRowNum(); r++){
                XSSFRow oRow          = oWrkSheet.getRow(r);
                for (int c=0; c < oRow.getLastCellNum(); c++){
                    ary2D[r][c] = oRow.getCell(c).toString();
                    System.out.print(oRow.getCell(c) + " ");
                }
                System.out.println();
            }
            oWrkBook.close();
            oInStream.close();
            return ary2D;
        }catch(IOException eXl){
            Assert.fail("UtilLog: Given excel file parsing error");
            return null;
        }
    }

}
