package com.example.board_game_buddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@SpringBootApplication
public class BoardGameBuddyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardGameBuddyApplication.class, args);
	}

	@Bean
	RestClientCustomizer logBookCustomizer(LogbookClientHttpRequestInterceptor interceptor) {
		return restclient -> restclient.requestInterceptor(interceptor);
	}

}
