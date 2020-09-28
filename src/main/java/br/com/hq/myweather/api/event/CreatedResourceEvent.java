package br.com.hq.myweather.api.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class CreatedResourceEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private HttpServletResponse response;

	public CreatedResourceEvent(Object source, HttpServletResponse response, Long id) {
		super(source);
		this.id = id;
		this.response = response;
	}
}