package rshb.intech.psql_agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Slf4j
@Component
public class UtilityLog {

    final public static void printResultSet(ResultSet rs, String transactionName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        for (int i = 1; i <= columnsNumber; i++) {
            if (i > 1) log.info(" | ");
            log.info("result for "+ transactionName + ": " + rs.getString(i));
        }
        log.info("\n");
    }
}
