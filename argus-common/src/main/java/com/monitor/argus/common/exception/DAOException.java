package com.monitor.argus.common.exception;
/**
 * <p>
 * 自定义DAO异常，继承AppRuntimeException
 * </p>
 * 
 * @Author null
 * @Date 2014-6-18 上午10:53:32
 * 
 */
public class DAOException extends AppRuntimeException {
	private static final long serialVersionUID = -4692378827648162659L;

	/**
	 * <p>
	 * Constructs a new DAOexception with null as its detail message. The cause
	 * is not initialized, and may subsequently be initialized by a call to
	 * initCause.
	 * </p>
	 */
	public DAOException() {
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the getMessage() method.
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 *            cause the cause (which is saved for later retrieval by the
	 *            getCause() method). (A null value is permitted, and indicates
	 *            that the cause is nonexistent or unknown.)
	 */
	public DAOException(Throwable cause) {
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
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}
}
