package com.learn.microservice.controller;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.learn.microservice.model.CatalogItem;
import com.learn.microservice.model.Movie;
import com.learn.microservice.model.Rating;
import com.learn.microservice.model.UserRating;
import com.learn.microservice.services.MovieInfo;
import com.learn.microservice.services.UserRatingInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping(value = "/catalog")
public class MovieCatalogController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private WebClient.Builder webClienBuilder;

	@Autowired
	MovieInfo movieInfo;

	@Autowired
	UserRatingInfo userRatingInfo;

	@RequestMapping(value = "/{userId}")
//	@HystrixCommand(fallbackMethod = "getFallbackCatalog")
	public List<CatalogItem> getCatalog(@PathVariable(value = "userId") String userId) {
//		RestTemplate restTemplate = new RestTemplate();
//		Movie movie = restTemplate.getForObject("http://localhost:8082/movies/foo", Movie.class);

		List<ServiceInstance> instances = discoveryClient.getInstances("ratings-data-service");

//		WebClient.Builder builder = WebClient.builder();

		UserRating userRating = userRatingInfo.getUserRating(userId);

		return userRating.getUserRatings().stream().map(rating -> {
			return movieInfo.getCatalogItem(rating);
		}).collect(Collectors.toList());

//		return Arrays.asList(new CatalogItem("Transformers", "Transformers Description", 4),
//				new CatalogItem("Titanic", "Titanic Description", 5),
//				new CatalogItem("Ford v Ferrari", "Ford v Ferrari Description", 4),
//				new CatalogItem("Irishman", "Irishman Description", 4), new CatalogItem("1970", "1970 Description", 5));
	}

	public List<CatalogItem> getFallbackCatalog(@PathVariable(value = "userId") String userId) {
		return Arrays.asList(new CatalogItem("No movie", "", 0));
	}
}
