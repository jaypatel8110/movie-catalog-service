package io.javabrains.moviecatalogservice.resources;

import io.javabrains.moviecatalogservice.models.Dbsettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AnnotationsExamples {

    @Value("${my.test:Default hello world}")
    private String getHelloWorld;

    @Autowired
    private Dbsettings dbsettings;

    @RequestMapping("/hello")
    public String getGetHelloWorld(){
        return  getHelloWorld;
    }

    @RequestMapping("/greet")
    public String getGreetings(){
        return  dbsettings.getConnection();
    }

}
