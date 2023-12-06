//package demo;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.messaging.MessageChannel;
//
//@Configuration
//public class MessageChannelConfiguration {
//    @Bean
//    public MessageChannel output() {
//        return new DirectChannel();
//    }
//
//}
//

//package demo;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Sinks;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import reactor.core.publisher.Sinks;
//
//@Service
//public class MessageChannelConfiguration {
//
//    @Bean
//    public Flux<String> outputFlux() {
//        return Flux.just("Adam");
//    }
//
//
//    @Bean
//    @Qualifier("output")
//    public Sinks.Many<String> outputSink() {
//        return Sinks.many().unicast().onBackpressureBuffer();
//    }
//
//}


//@Configuration
//public class MessageChannelConfiguration {
//
//    private Sinks.Many<String> outputSink = Sinks.many().unicast().onBackpressureBuffer();
//
//    @Bean
//    public Sinks.Many<String> outputSink() {
//        return outputSink;
//    }
//}



//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
//import java.util.function.Supplier;
//
//@Configuration
//@Slf4j
//public class MessageChannelConfiguration {
//
//    @Bean
//    @Qualifier("locations")
//    public Supplier<String> output() {
//        return () -> {
//            // Simulate producing content, replace with your actual logic
//            String content = "Hello, this is the content!";
//            log.info("Producing content: " + content);
//            return content;
//        };
//    }
//}

package demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
//
//import java.util.function.Supplier;
//
//@Configuration
//@Slf4j
//public class MessageChannelConfiguration {
//
//    @Bean
//    @Qualifier("locations")
//    public Supplier<String> output(String positionInfo) {
//        return () -> {
//            // Simulate producing content, replace with your actual logic
//            log.info("Producing content: " + positionInfo);
//            return positionInfo;
//        };
//    }
//}