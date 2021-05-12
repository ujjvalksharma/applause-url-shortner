package com.applause.urlshortner.service;

import com.applause.urlshortner.entity.Url;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.UrlValidator;

import static com.applause.urlshortner.constant.ConstantUtil.SHORTENED_URL_IS_NOT_PRESENT;
import static com.applause.urlshortner.constant.ConstantUtil.THIS_URL_IS_ALREADY_SHORTENED;
import static com.applause.urlshortner.constant.ConstantUtil.INVALID_URL;
import static com.applause.urlshortner.constant.ConstantUtil.CANNOT_SHORTEN_APPLAUSE_URL;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


/**
 * This class is used to implement LRU cache service that will store
 * Url elements and provide functions to insert and
 * get elements from cache.
 */
@Service
public class LRUCacheService {
    private HashMap<String, LRUCacheElement> cache;
    private Set<String> originalUsedUrls = new HashSet<>();
    private LRUCacheElement head, tail;

    /**
     * This is class that signify element of LRU cache.
     */
    class LRUCacheElement {
        Url url;
        LRUCacheElement pre;
        LRUCacheElement post;
    }

    /**
     * This contructs the LRU service.
     */
    public LRUCacheService() {
        cache = new HashMap<String, LRUCacheElement>();
        head = new LRUCacheElement();
        head.pre = null;
        tail = new LRUCacheElement();
        tail.post = null;
        head.post = tail;
        tail.pre = head;
    }

    /**
     * This adds elements to the cache next to head.
     * @param node node is added to after the head of the cache
     */
    private void addNode(LRUCacheElement node) {
        node.pre = head;
        node.post = head.post;
        head.post.pre = node;
        head.post = node;
    }

    /**
     * This function removes the node from LRU cache.
     * @param node node that has to be removed
     */
    private void removeNode(LRUCacheElement node) {
        LRUCacheElement pre = node.pre;
        LRUCacheElement post = node.post;
        pre.post = post;
        post.pre = pre;
    }

    /**
     * This function makes the current node the new head.
     * @param node node that is made to be head
     */
    private void moveToHead(LRUCacheElement node) {
        this.removeNode(node);
        this.addNode(node);
    }

    /**
     * Removes the least recently used element.
     * @return least recently element
     */
    private LRUCacheElement popTail() {
        LRUCacheElement res = tail.pre;
        this.removeNode(res);
        return res;
    }

    /**
     * This function makes the original url to a shorten url.
     * @param shortenUrl shorten url of the original url
     * @return orginal url of shorten url
     */
    public String getOriginalUrl(String shortenUrl) {
        final LRUCacheElement node = Optional.ofNullable(cache.get(shortenUrl))
                .orElseThrow(() -> new IllegalArgumentException(SHORTENED_URL_IS_NOT_PRESENT));
        node.url.setUpdateTime(new Date(System.currentTimeMillis()));
        node.url.setCallCount(node.url.getCallCount() + 1);
        this.moveToHead(node);
        return node.url.getOriginalURL();
    }

    /**
     * This function insert a element into into the LRU cache.
     * @param url             url which has shorten and original element
     * @param mapIndexToUrlId url id map to their index
     * @param indexOfUrlID    current index of the url referred
     */
    public void put(Url url, HashMap<Integer, Integer> mapIndexToUrlId, int indexOfUrlID) {
        validateUrl(url);
        LRUCacheElement newNode = new LRUCacheElement();
        newNode.url = url;
        if (url.getShortenedURL() == null) {

            LRUCacheElement leastRecentlyUsedElement = this.popTail();
            newNode.url.setShortenedURL(leastRecentlyUsedElement.url.getShortenedURL());
            this.cache.remove(tail.url.getShortenedURL());
            this.originalUsedUrls.remove(tail.url.getOriginalURL());
        }
        this.originalUsedUrls.add(newNode.url.getOriginalURL());
        this.cache.put(url.getShortenedURL(), newNode);
        this.addNode(newNode);
    }

    /**
     * Validates the url if we inserting the same url in the cache again.
     * @param url url component of original and shorten url
     */
    private void validateUrl(Url url) {
    	 UrlValidator urlValidator = new UrlValidator();
        if (originalUsedUrls.contains(url.getOriginalURL())) {
            throw new IllegalArgumentException(THIS_URL_IS_ALREADY_SHORTENED);
        }
        if(!urlValidator.isValid(url.getOriginalURL())){
        	 throw new IllegalArgumentException(INVALID_URL);
        }
        if(url.getOriginalURL().contains("applau.se")) {
        	 throw new IllegalArgumentException(CANNOT_SHORTEN_APPLAUSE_URL);
        }
    }

    /**
     * Gets the cache hashmap.
     * @return cache
     */
    public HashMap<String, LRUCacheElement> getCache() {
        return cache;
    }
}
