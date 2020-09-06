package io.javabrains.moviecatalogservice.resources;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AnnotationsExamples {

    @Value("${my.test:Default hello world}")
    private String getHelloWorld;

    @RequestMapping("/hello")
    public String getGetHelloWorld(){
        return  getHelloWorld;
    }


}
