package br.com.hq.myweather.api.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class MyWeatherValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final HttpStatus tipo;

	
	public MyWeatherValidationException(String message) {
		super(message);
		this.tipo = HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
	public MyWeatherValidationException(Throwable cause) {
		super(cause);
		this.tipo = HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
	public MyWeatherValidationException(String message, Throwable cause) {
		super(message, cause);
		this.tipo = HttpStatus.INTERNAL_SERVER_ERROR;
	}
	
	public MyWeatherValidationException(String message, HttpStatus tipo) {
		super(message);
		this.tipo = tipo;
	}
	
	public MyWeatherValidationException(String message, HttpStatus tipo, Throwable cause) {
		super(message, cause);
		this.tipo = tipo;
	}
}