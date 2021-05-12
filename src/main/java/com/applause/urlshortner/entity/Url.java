package com.applause.urlshortner.entity;

import lombok.*;

import java.util.Date;

/**
 * This class represents url entity that has original url, shorten url,
 * call count, and last call time. 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Url {
	private String shortenedURL; 
	private String originalURL;
	private int callCount;
	private Date updateTime;
}
