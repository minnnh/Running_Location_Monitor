//
//package demo;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.integration.support.MessageBuilder;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.stereotype.Component;
//
//@RestController
//@Slf4j
//
//public class RunnerPositionsSource {
//    @Bean
//    @RequestMapping(path = "/api/locations", method = RequestMethod.POST)
//    public void locations(@RequestBody String positionInfo) {
//        this.output.send(MessageBuilder.withPayload(positionInfo).build());
//    }
//
//}




//
//package demo;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.cloud.stream.function.StreamBridge;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Sinks;
//
//
//@Controller
//@Slf4j
//public class RunnerPositionsSource {
//
//    private final Sinks.Many<String> outputSink;
//
//    @Autowired
//    private StreamBridge streamBridge;
//
//    public RunnerPositionsSource(@Qualifier("output") Sinks.Many<String> outputSink) {
//        this.outputSink = outputSink;
//    }
//
//    @PostMapping("/api/locations")
//    public void locations(@RequestBody String positionInfo) {
//        log.info("Received position info: {}", positionInfo);
//        outputSink.tryEmitNext(positionInfo);
//    }
//
//    @GetMapping("/api/getData")
//    public Flux<String> getData() {
//        return outputSink.asFlux();
//    }
//
//    // Use @MessageMapping to consume messages
//    @MessageMapping("output")
//    public void processOutput(String output) {
//        log.info("Received output message: {}", output);
//        // You can process the received message here if needed
//    }
//}


//package demo;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import java.util.function.Supplier;
//
//@Component
//@Configuration
//@Slf4j
//public class RunnerPositionsSource {
//
//    private String producedContent;

//    @Bean
//    public Supplier<String> output() {
//        return () -> {
//            // Simulate producing content, replace with your actual logic
//            String content = "Hello, this is the content!";
//            log.info("Producing content: " + content);
//            return content;
//        };
//    }
//
//    @PostMapping("/api/locations")
//    public ResponseEntity<String> triggerContentProduction() {
//        // Trigger the contentSupplier when an HTTP request is made to "/api/locations"
//        String producedContent = output().get();
//        this.producedContent = producedContent;
//
//        return new ResponseEntity<>("Content sent successfully: " + producedContent, HttpStatus.OK);
//    }
//    @RequestMapping(path = "/api/locations", method = RequestMethod.POST)
//    @Qualifier("locations")
//    public Supplier<String> output(@RequestBody String positionInfo) {
//        return () -> {
//            // Simulate producing content, replace with your actual logic
//            // String content = "Hello, this is the content!";
//            log.info("Producing content: " + positionInfo);
//            return positionInfo;
//        };
//    }
//
//}

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