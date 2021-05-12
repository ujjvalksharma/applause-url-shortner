package com.applause.urlshortner.controller;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.applause.urlshortner.dto.UrlRequest;
import com.applause.urlshortner.entity.Url;
import com.applause.urlshortner.service.UrlService;

/**
 * This is the controller class that takes input for url- shortening.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class UrlController {
	
	@Autowired
	private UrlService urlService;

	/**
	 * This function makes original url to shorten url.
	 * @param urlRequest this accepts the url request (original url) 
	 * @return gets shorten url
	 */
	@PostMapping("/url")
	public String saveOriginalUrl(@RequestBody UrlRequest urlRequest) {
		log.info("save new url :{}", urlRequest.getUrlValue());
		return this.urlService.saveOriginalUrl(urlRequest);
	}
  
	/**
	 * This function get original url from shorten url.
	 * @param urlRequest this accepts the url request (original url) 
	 * @return gets original url
	 */
	@PutMapping("/url")
	public String getOriginalUrl(@RequestBody UrlRequest urlRequest){
		log.info("fetch url :{}", urlRequest.getUrlValue());
		return urlService.getOriginalUrl(urlRequest.getUrlValue());
	}

	/**
	 * Gets all the url sorted by call count order.
	 * @return
	 */
	@GetMapping("/url")
	public List<Url> getAllUrls() {
		log.info("fetch all url");
		return urlService.getAllUrl();
	}
}
