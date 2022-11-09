package rshb.intech.psql_agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.concurrent.TimeUnit;

import static rshb.intech.psql_agent.UtilityLog.printResultSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuerieScheduler {
    private final InfluxDB influxDB;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.user}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    private final String query1 = "select count(*) from pg_stat_activity where datname='FOO' and usename='user_X'";
    private final String query2 = "select count(*) from pg_stat_activity where datname='FOO' and usename='user_Z'";
    private final String query3 = "select count(*) from pg_stat_activity where datname='FOO'";
    // private final String query4 = "SELECT datname FROM pg_database WHERE datistemplate = false"; // Просмотр всех существующих БД

    @Scheduled(fixedRate = 2000)
    private void selectStatus() {
        try (final Connection con = DriverManager.getConnection(url, user, password); final PreparedStatement pst = con.prepareStatement(query1)) {
            ResultSet rs = pst.executeQuery();
            int counter;
            while (rs.next()) {
                counter = rs.getInt("count");
                printResultSet(rs, "user_X");
                influxDB.write(Point.measurement("psqlStatistics")
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .addField("count", counter)
                        .tag("usename", "user_X")
                        .build());
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

        try (final Connection con = DriverManager.getConnection(url, user, password); final PreparedStatement pst = con.prepareStatement(query2)) {
            ResultSet rs = pst.executeQuery();
            int counter;
            while (rs.next()) {
                counter = rs.getInt("count");
                printResultSet(rs, "user_Z");
                influxDB.write(Point.measurement("psqlStatistics")
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .addField("count", counter)
                        .tag("usename", "user_Z")
                        .build());
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

        try (final Connection con = DriverManager.getConnection(url, user, password); final PreparedStatement pst = con.prepareStatement(query3)) {
            ResultSet rs = pst.executeQuery();
            int counter;
            while (rs.next()) {
                counter = rs.getInt("count");
                printResultSet(rs, "all");
                influxDB.write(Point.measurement("psqlStatistics")
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .addField("count", counter)
                        .tag("usename", "all_users")
                        .build());
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

       /* try (final Connection con = DriverManager.getConnection(url, user, password); final PreparedStatement pst = con.prepareStatement(query4)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                printResultSet(rs, "datname");
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }*/
    }
}
