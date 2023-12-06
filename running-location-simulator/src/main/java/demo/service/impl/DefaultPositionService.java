package demo.service.impl;

import demo.model.CurrentPosition;
import demo.service.PositionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DefaultPositionService implements PositionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPositionService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public DefaultPositionService() {
        super();
    }

    @CircuitBreaker(name = "myCircuit", fallbackMethod = "processPositionInfoFallback")
    @Override
    public void processPositionInfo(long id, CurrentPosition currentPosition, boolean exportPositionsToKml,
                                    boolean sendPositionsToDistributionService) {
        // String runningLocationDistribution = "http://running-location-distribution";
        String runningLocationDistribution = "http://localhost:9006";
        if (sendPositionsToDistributionService) {
            log.info("Simulator is callling distribution REST API");
            this.restTemplate.postForLocation(runningLocationDistribution + "/api/locations", currentPosition);

        }
    }


    public void processPositionInfoFallback(long id, CurrentPosition currentPosition, boolean exportPositionsToKml,
                                            boolean sendPositionsToDistributionService) {
        LOGGER.error("Resilience4j Fallback Method. Unable to send message for distribution.");
    }
}
