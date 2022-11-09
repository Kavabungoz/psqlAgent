package rshb.intech.psql_agent;

import lombok.Data;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Configuration
@Data
public class InfluxConfig {
    @Value("${spring.datasource.urlInflux}")
    private String urlInflux;

    @Value("${spring.datasource.influxDatabase}")
    private String influxDatabase;

    @Value("${spring.datasource.influxRetentionPolicy}")
    private String influxRetentionPolicy;

    @Bean
    public InfluxDB influxConnection() {
        InfluxDB influxDB = InfluxDBFactory.connect(urlInflux, "root", "root");
        influxDB.setDatabase(influxDatabase);
        influxDB.setRetentionPolicy(influxRetentionPolicy);
        influxDB.enableBatch(500, 2000, TimeUnit.MILLISECONDS);
        return influxDB;
    }

}
