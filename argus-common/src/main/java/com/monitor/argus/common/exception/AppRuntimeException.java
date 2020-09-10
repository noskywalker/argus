package com.monitor.argus.common.exception;


/**
 * <p>
 * 自定义运行时异常，继承RuntimeException
 * </p>
 * 
 * @Author null
 * @Date 2014-6-18 上午10:53:32
 * 
 */
public class AppRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -4692378827648162659L;

	/**
	 * <p>
	 * Constructs a new DAOexception with null as its detail message. The cause
	 * is not initialized, and may subsequently be initialized by a call to
	 * initCause.
	 * </p>
	 */
	public AppRuntimeException() {
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the getMessage() method.
	 */
	public AppRuntimeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            cause the cause (which is saved for later retrieval by the
	 *            getCause() method). (A null value is permitted, and indicates
	 *            that the cause is nonexistent or unknown.)
	 */
	public AppRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the getMessage() method.
	 * @param cause
	 *            cause the cause (which is saved for later retrieval by the
	 *            getCause() method). (A null value is permitted, and indicates
	 *            that the cause is nonexistent or unknown.)
	 */
	public AppRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
