package io.javabrains.moviecatalogservice.resources;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserRating ratings;


    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating ratings = getUserRatings(userId);
        return ratings.getRatings().stream().map(
                rating -> {
                    // for each movie id , call movie info service and get details
                    return getCatalogItem(rating);
                }
        ).collect(Collectors.toList());

    }

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    private CatalogItem getCatalogItem(Rating rating) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        //Put them together
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }

    private CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }

    @HystrixCommand(fallbackMethod = "getFallBackUserRating")
    private UserRating getUserRatings(@PathVariable("userId") String userId) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);
    }


    private UserRating getFallBackUserRating(@PathVariable("userId") String userId) {
      UserRating userRating = new UserRating();
      userRating.setUserId(userId);
      userRating.setRatings(Arrays.asList(
              new Rating("0",0)
      ));
      return  userRating;
    }

}
  /* Movie movie =webClientBuilder.build() // For Sping FLUX
                .get()
                .uri("http://localhost:8082/movies/" + rating.getMovieId())
                      .retrieve()
                      .bodyToMono(Movie.class)
                      .block();*/