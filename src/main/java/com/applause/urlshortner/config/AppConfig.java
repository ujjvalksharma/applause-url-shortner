package com.applause.urlshortner.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 
 * This config class that sets the configration for url shortner like 
 * prefix, count of element, 
 * possible characters.
 *
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "app-config")
public class AppConfig {
    Integer capacity;
    Integer count;
    String shortenUrlPrefix;
    String possibleCharacter;
    Integer possibleCharacterCount;
}
