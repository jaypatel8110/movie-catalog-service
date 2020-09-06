package io.javabrains.moviecatalogservice.resources;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import io.javabrains.moviecatalogservice.services.MovieInfo;
import io.javabrains.moviecatalogservice.services.UserRatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRatingInfo userRatingInfo;

    @Value("${my.test:Default hello world}")
    private String getHelloWorld;

    private UserRating ratings;


    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating ratings = userRatingInfo.getUserRatings(userId);
        return ratings.getRatings().stream().map(
                rating -> {
                    // for each movie id , call movie info service and get details
                    return movieInfo.getCatalogItem(rating);
                }
        ).collect(Collectors.toList());

    }

    @RequestMapping("/hello")
    public String getGetHelloWorld(){
        return  getHelloWorld;
    }


}

  /* Movie movie =webClientBuilder.build() // For Sping FLUX
                .get()
                .uri("http://localhost:8082/movies/" + rating.getMovieId())
                      .retrieve()
                      .bodyToMono(Movie.class)
                      .block();*/