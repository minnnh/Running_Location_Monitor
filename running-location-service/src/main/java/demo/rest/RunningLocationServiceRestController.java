package demo.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunningLocationServiceRestController {

    @RequestMapping("/")
    public @ResponseBody String greeting() {
        return "Hello, World";
    }

}

