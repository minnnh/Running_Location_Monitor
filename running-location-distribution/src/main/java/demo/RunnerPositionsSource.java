package demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.function.Supplier;

@Slf4j
@RestController
public class RunnerPositionsSource {

    private String positionInfo;

    @RequestMapping(path = "/api/locations", method = RequestMethod.POST)
    public void receiveData(@RequestBody String positionInfo) {
        this.positionInfo = positionInfo;
    }

    @Bean
    @Qualifier("locations")
    public Supplier<String> output() {
        return () -> {
            // Simulate producing content, replace with your actual logic
            log.info("Producing content: " + positionInfo);

            return positionInfo;
        };
    }

}
