package br.com.hq.myweather.api.exception;

import org.springframework.http.HttpStatus;

public class MyWeatherInternal extends MyWeatherValidationException {

	private static final long serialVersionUID = 1L;

	public MyWeatherInternal(String message) {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
