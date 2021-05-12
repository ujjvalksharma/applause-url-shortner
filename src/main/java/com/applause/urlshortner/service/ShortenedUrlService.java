package com.applause.urlshortner.service;

import com.applause.urlshortner.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * This class shortens the original url based on the id assiged to it.
 *
 */
@Service
public class ShortenedUrlService {
    @Autowired AppConfig config;

    /**
     * This function shorten the url based on the id given to the url.
     * @param originalUrlId url id
     * @return shorten suffix of the url
     */
    public String getShortURL(int originalUrlId) {
        char map[] = config.getPossibleCharacter().toCharArray();
        StringBuffer result = new StringBuffer();
        while (originalUrlId > 0) {
            result.append(map[originalUrlId % config.getPossibleCharacterCount()]);
            originalUrlId = originalUrlId / config.getPossibleCharacterCount();
        }
        return result.reverse().toString();
    }
}
