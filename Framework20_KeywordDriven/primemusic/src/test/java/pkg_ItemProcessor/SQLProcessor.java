package pkg_ItemProcessor;
import org.apache.log4j.Logger;
import pkg_global.GlobalObjects;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLProcessor extends GlobalObjects {
    private static final Logger myLog = Logger.getLogger(SQLProcessor.class);

    public static void ProcessSQLItem(String sTcIndex, String sSqlQueryName, String sVariableName){
        myLog.info("Log: TCProcessor : " + sTcIndex + " " + sSqlQueryName + " " + sVariableName);

        try{
            String sSqlQuery        = hmGlobalData.get(sSqlQueryName);
            PreparedStatement pstmt = sqliteConn.prepareStatement(sSqlQuery);

            ResultSet oResultSet    = pstmt.executeQuery();

            while (oResultSet.next()) {
                hmGlobalData.put(sVariableName, oResultSet.getObject(1).toString());
            }
        }catch (SQLException e){}

        hmGlobalData.put(sTcIndex, "pass");
    }

}
