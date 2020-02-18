package com.learn.microservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.microservice.model.Rating;
import com.learn.microservice.model.UserRating;

@RestController
@RequestMapping(value = "/ratingsdata")
public class RatingsDataController {

	@GetMapping(value = "/{movieId}")
	public Rating getRating(@PathVariable(value = "movieId") String movieId) {
		return new Rating(movieId, 4);
	}

	@GetMapping(value = "/users/{userId}")
	public UserRating getRatings(@PathVariable(value = "userId") String userId) {
//		List<Rating> ratings = Arrays.asList(new Rating("transformers", 4), new Rating("Titanic", 5),
//				new Rating("Irishman", 4));
		UserRating userRating = new UserRating();
		userRating.initData(userId);
		userRating.setUserRatings(userRating.getUserRatings());
		return userRating;
	}
}
