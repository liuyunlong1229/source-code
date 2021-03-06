package org.springboot.integration.exception;

public class RepeatSubmitException extends RuntimeException {
    private String code="999";
	private String message;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public RepeatSubmitException(String message) {
		this.message = message;
	}
	public RepeatSubmitException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
}
