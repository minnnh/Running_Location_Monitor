package demo.service.impl;

import demo.model.CurrentPosition;
import demo.model.SupplyLocation;
import demo.service.SupplyLocationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DefaultSupplyLocationService implements SupplyLocationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSupplyLocationService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @CircuitBreaker(name = "supplyLocations", fallbackMethod = "handleSupplyLocationServiceFailure")
    @Override
    public void updateSupplyLocations(CurrentPosition currentPosition) {

        switch (currentPosition.getRunnerStatus()) {

            case SERVICE_NOW:
            case SERVICE_SOON:
            case STOP_NOW:
                ResponseEntity<EntityModel<SupplyLocation>> result = this.restTemplate.exchange(
                        "http://supply-location-service/supplyLocations/search/findFirstByLocationNear?location={lat},{long}",
                        HttpMethod.GET, new HttpEntity<Void>((Void) null),
                        new ParameterizedTypeReference<EntityModel<SupplyLocation>>() {
                        }, currentPosition.getLocation().getLatitude(),
                        currentPosition.getLocation().getLongitude());
                if (result.getStatusCode() == HttpStatus.OK
                        && result.getBody().getContent() != null) {
                    currentPosition.setSupplyLocation(result.getBody().getContent());
                }
                break;
            default:
        }
    }

    public void handleSupplyLocationServiceFailure(CurrentPosition currentPosition) {
        LOGGER.error("Resilience4j Fallback Method. Unable to retrieve service station info.");
    }

}
