package rshb.intech.psql_agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PsqlAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PsqlAgentApplication.class, args);
    }


}
