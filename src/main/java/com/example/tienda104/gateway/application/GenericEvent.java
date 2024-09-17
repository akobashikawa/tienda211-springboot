package com.example.tienda104.gateway.application;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

public class GenericEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;

	private String eventType;
	private Map<String, Object> payload;

	public GenericEvent(Object source, String eventType, Map<String, Object> payload) {
		super(source);
		this.eventType = eventType;
		this.payload = payload;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	public void setPayload(Map<String, Object> payload) {
		this.payload = payload;
	}

}
