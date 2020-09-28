package br.com.hq.myweather.api.exception.handler;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.hq.myweather.api.exception.MyWeatherError;
import br.com.hq.myweather.api.exception.MyWeatherValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class MyWeatherExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource msg;
	
	/**
	 * Trata os erros ao contatar a API externa Open Weather
	 * @param ex {@link WebClientResponseException} Exceção relacionada ao acesso externo
	 * @return {@link ResponseEntity} Objeto de erro personalizado
	 */
	@ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<MyWeatherError> handleWebClientResponseException(WebClientResponseException ex) {
		
		int codigo = ex.getRawStatusCode();
		String tipo = ex.getStatusText();
		String mensagem = ex.getMessage();
		
        log.error("Erro ao consultar API externa - Status {}, Body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
        
        var erro = new MyWeatherError(codigo, tipo, Arrays.asList(mensagem));        
        return ResponseEntity.status(ex.getRawStatusCode()).body(erro);
    }
	
	/**
	 * Trata os erros de validação da API
	 * @param ex {@link MyWeatherValidationException} Exceção relacioanda às validações da API, ou suas filhas
	 * @return {@link ResponseEntity} Objeto de erro personalizado
	 */
	@ExceptionHandler(MyWeatherValidationException.class)
    public ResponseEntity<MyWeatherError> handleMyWeatherValidationException(MyWeatherValidationException ex) {
		int codigo = ex.getTipo().value();
		String tipo = ex.getTipo().getReasonPhrase();
		
        log.error("Erro ao validar processo - {}", ex.getMessage());
        
        var erro = new MyWeatherError(codigo, tipo, Arrays.asList(ex.getMessage()));        
        return ResponseEntity.status(ex.getTipo()).body(erro);
    }
	
	/**
	 * Trata os erros relacionados a dados inconsistentes validados pelo validator nativo
	 * @return {@link ResponseEntity} Objeto de erro personalizado
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		var erro = obterErros(status.value(), HttpStatus.BAD_REQUEST, ex.getBindingResult());
		return handleExceptionInternal(ex, erro, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private MyWeatherError obterErros(int codigo, HttpStatus tipo, BindingResult bindingResult){
		var erro = new MyWeatherError(codigo, tipo.getReasonPhrase(), new ArrayList<>());
		
		for(FieldError field : bindingResult.getFieldErrors()) {
			String message = msg.getMessage(field, LocaleContextHolder.getLocale());			
			erro.addError(message);
		}
		
		return erro;
	}
}