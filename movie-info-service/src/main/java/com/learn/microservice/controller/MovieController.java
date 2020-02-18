package com.learn.microservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.learn.microservice.model.Movie;
import com.learn.microservice.model.MovieSummary;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

	@Value("${movie.db.apikey}")
	private String apiKey;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping(value = "/{movieId}")
	public Movie getMovieInfo(@PathVariable(value = "movieId") String movieId) {
		MovieSummary movieSummary = restTemplate.getForObject(
				"https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey, MovieSummary.class);
		return new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());
	}
}
