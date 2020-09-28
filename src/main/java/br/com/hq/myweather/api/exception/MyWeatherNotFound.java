package br.com.hq.myweather.api.exception;

import org.springframework.http.HttpStatus;

public class MyWeatherNotFound extends MyWeatherValidationException {

	private static final long serialVersionUID = 1L;
	
	public MyWeatherNotFound(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
}
