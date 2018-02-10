package org.unclesniper.arceye.termio;

public class TerminalException extends RuntimeException {

	private final String operation;

	private final int errno;

	private final String errorMessage;

	public TerminalException(String operation, int errno, String errorMessage) {
		super("Terminal I/O: " + operation + ": " + errorMessage);
		this.operation = operation;
		this.errno = errno;
		this.errorMessage = errorMessage;
	}

	public String getOperation() {
		return operation;
	}

	public int getErrorCode() {
		return errno;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
