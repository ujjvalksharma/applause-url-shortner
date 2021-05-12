package com.applause.urlshortner.dto;

import lombok.*;

/**
 * 
 * This class represent the url request.
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UrlRequest {
	private String urlValue;
}
