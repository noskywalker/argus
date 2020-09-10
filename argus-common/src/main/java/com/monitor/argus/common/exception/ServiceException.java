package com.monitor.argus.common.exception;
/**
 * <p>
 * 自定义Service异常，继承DAOException（继承RuntimeException）
 * </p>
 * 
 * @Author null
 * @Date 2014-6-18 上午11:12:20
 * 
 */
public class ServiceException extends DAOException {
	/** 序列化 **/
	private static final long serialVersionUID = 5632284719609495812L;

	/**
	 * <p>
	 * Constructs a new DAOexception with null as its detail message. The cause
	 * is not initialized, and may subsequently be initialized by a call to
	 * initCause.
	 * </p>
	 */
	public ServiceException() {
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the getMessage() method.
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            cause the cause (which is saved for later retrieval by the
	 *            getCause() method). (A null value is permitted, and indicates
	 *            that the cause is nonexistent or unknown.)
	 */
	public ServiceException(Throwable cause) {
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
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
