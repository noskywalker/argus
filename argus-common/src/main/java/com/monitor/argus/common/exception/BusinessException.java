package com.monitor.argus.common.exception;

public class BusinessException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String returnCode = null;
	private String message = null;
	private Exception innerException = null;
	
	public BusinessException(){}

	public BusinessException(String returnCode, String message) {
		super();
		this.returnCode = returnCode;
		this.message = message;
		this.innerException = null;
	}
	public BusinessException(String returnCode, String message, Exception ex) {
		super();
		this.returnCode = returnCode;
		this.message = message;
		this.innerException = ex;
		
		if(ex instanceof BusinessException){
			if(returnCode == null || "".equals(returnCode.trim())){
				this.returnCode = ((BusinessException)ex).getReturnCode();
			}
			
			if(message == null || "".equals(message.trim())){
				this.message = ((BusinessException)ex).getMessage();
			}
		}
	}
	public String getMessage() {
		return message;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public Exception getInnerException() {
		return innerException;
	}
}
