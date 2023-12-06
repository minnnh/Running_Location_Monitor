package demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.model.CurrentPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@Configuration
@Slf4j
public class RunningLocationUpdaterSink {

//    @Autowired
//    private SimpMessagingTemplate template;
//
//    @Autowired
//    private ObjectMapper objectMapper;

    private final SimpMessagingTemplate template;
    private final ObjectMapper objectMapper;
    private final StreamBridge streamBridge;

    @Autowired
    public RunningLocationUpdaterSink(SimpMessagingTemplate template, ObjectMapper objectMapper, StreamBridge streamBridge) {
        this.template = template;
        this.objectMapper = objectMapper;
        this.streamBridge = streamBridge;
    }
//    @Autowired
//    private StreamBridge streamBridge;
//
    @Bean
    public Consumer<String> input() {
        return input -> {
            try {
                log.info("Location input in updater: " + input);
                CurrentPosition payload = objectMapper.readValue(input, CurrentPosition.class);
                payload.setHeartRate(generateHeartRate());
                // template.convertAndSend("/topic/locations", payload);
                streamBridge.send("/topic/locations", payload);
                log.info("convert and send the input to /topic/locations");
            } catch (Exception e) {
                log.error("Error processing location update", e);
            }
        };
    }

    private int generateHeartRate() {
        return ThreadLocalRandom.current().nextInt(50, 160);
    }

}


//
//package demo;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import demo.model.CurrentPosition;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.function.context.PollableBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import reactor.core.publisher.Flux;
//
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.function.Consumer;
//
//@Slf4j
//@Configuration
//public class RunningLocationUpdaterSink {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private SimpMessagingTemplate template;
//
//    @Bean
//    public Consumer<Flux<String>> input() {
//        return flux -> flux.subscribe(input -> {
//            try {
//                updateLocationAndServiceLocations(input);
//            } catch (Exception e) {
//                log.error("Error processing location update: " + e.getMessage(), e);
//            }
//        });
//    }
//
//    private void updateLocationAndServiceLocations(String input) throws Exception {
//        log.info("Location input in updater: " + input);
//        CurrentPosition payload = this.objectMapper.readValue(input, CurrentPosition.class);
//        payload.setHeartRate(generateHeartRate());
//        this.template.convertAndSend("/topic/locations", payload);
//        // Process the payload as needed
//    }
//
//    private int generateHeartRate() {
//        return ThreadLocalRandom.current().nextInt(50, 160);
//    }
//}
//
