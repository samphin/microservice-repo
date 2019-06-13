package org.common.eureka.exception;

/**
 * 自定义Token异常处理
 * @author samphin
 */
public class TokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message;

	public TokenException(String essage) {
		super();
		this.message = essage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String essage) {
		this.message = essage;
	}
}
