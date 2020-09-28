package br.com.hq.myweather.api.validator;

import static br.com.hq.myweather.api.utils.Utils.isValidList;

import java.util.List;

import org.springframework.http.HttpStatus;

import br.com.hq.myweather.api.exception.MyWeatherBadRequest;
import br.com.hq.myweather.api.exception.MyWeatherConflict;
import br.com.hq.myweather.api.exception.MyWeatherInternal;
import br.com.hq.myweather.api.exception.MyWeatherNotFound;
import br.com.hq.myweather.api.exception.MyWeatherValidationException;
import br.com.hq.myweather.api.model.BaseEntity;

public abstract class Validator<E extends BaseEntity> {

	void validate(List<E> entities) {
		if (isValidList(entities))
			entities.forEach(this::validate);
	}
	
	E validate(E entity) {
		return null;
	}
	
	protected void throwNotFound(String message) {
		throw new MyWeatherNotFound(message);
	}
	
	protected void throwConflict(String message) {
		throw new MyWeatherConflict(message);
	}
	
	protected void throwBadRequest(String message) {
		throw new MyWeatherBadRequest(message);
	}
	
	protected void throwInternalError(String message) {
		throw new MyWeatherInternal(message);
	}
	
	protected void throwValidation(String message, HttpStatus status) {
		throw new MyWeatherValidationException(message, status);
	}
}