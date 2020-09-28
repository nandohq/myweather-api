package br.com.hq.myweather.api.exception;

import org.springframework.http.HttpStatus;

public class MyWeatherConflict extends MyWeatherValidationException {

	private static final long serialVersionUID = 1L;

	public MyWeatherConflict(String message) {
		super(message, HttpStatus.CONFLICT);
	}
}