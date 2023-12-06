package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableDiscoveryClient
//@EnableCircuitBreaker
public class RunningLocationUpdaterApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RunningLocationUpdaterApplication.class, args);
    }

}
