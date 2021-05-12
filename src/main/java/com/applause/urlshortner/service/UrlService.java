package com.applause.urlshortner.service;

import com.applause.urlshortner.config.AppConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.applause.urlshortner.dto.UrlRequest;
import com.applause.urlshortner.entity.Url;


/**
 * This is a Url service class that implements function to to
 * insert a url and fetch original url based on shorten url.
 */
@Service
public class UrlService {
  
	@Autowired
    private LRUCacheService cacheService;
	@Autowired
    private ShortenedUrlService urlService;
	@Autowired
    private AppConfig config;

    private HashMap<Integer, Integer> mapIndexToUrlId = new HashMap<>();

    UrlService(AppConfig config) {
        this.config = config;
        int index = 0;
        for (int startId = config.getPossibleCharacterCount(); startId <= config.getCapacity(); startId++) {
            mapIndexToUrlId.put(index, startId);
            index++;
        }
    }

    /**
     * This function saves url request in the cache and return the shorten url.
     * @param urlRequest
     * @return
     */
    public String saveOriginalUrl(UrlRequest urlRequest) {
        boolean isfullCache = false;
        int indexOfUrlID = -1;
        try {
            indexOfUrlID = getRandomUrlId(0, mapIndexToUrlId.size());
        } catch (IllegalArgumentException e) {
            isfullCache = true;
        }
        String newShortUrlSuffix = null;
        if (!isfullCache) {
        	newShortUrlSuffix=urlService.getShortURL(mapIndexToUrlId.get(indexOfUrlID));
        }
        String newShortUrl = this.config.getShortenUrlPrefix() + newShortUrlSuffix;
        final Url url = Url.builder()
                .shortenedURL(newShortUrl)
                .originalURL(urlRequest.getUrlValue())
                .callCount(1)
                .updateTime(new Date(System.currentTimeMillis()))
                .build();
        cacheService.put(url, mapIndexToUrlId, indexOfUrlID);
        return newShortUrl;
    }

    /**
     * This function return orignal url based on the shorten url.
     *
     * @param shortUrl shorten url
     * @return original url
     */
    public String getOriginalUrl(String shortUrl) {
        return cacheService.getOriginalUrl(shortUrl);
    }

    /**
     * This function returns the list of all url in the cache.
     *
     * @return list of url
     */
    public List<Url> getAllUrl() {
        List<Url> allUrls = new LinkedList<>();
        for (Map.Entry<String, LRUCacheService.LRUCacheElement> mapElement : cacheService.getCache().entrySet()) {
            allUrls.add(mapElement.getValue().url);
        }
        return allUrls.stream().sorted((a, b) -> b.getCallCount() - (a.getCallCount())).collect(Collectors.toList());
    }

    /**
     * Finds random number between a range.
     * @param min start element (inclusive)
     * @param max end element (exclusive)
     * @return random number between the range
     */
    public int getRandomUrlId(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
