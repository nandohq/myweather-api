package br.com.hq.myweather.api.exception;

import org.springframework.http.HttpStatus;

public class MyWeatherBadRequest extends MyWeatherValidationException {

	private static final long serialVersionUID = 1L;
	
	public MyWeatherBadRequest(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}
}
