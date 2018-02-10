package org.unclesniper.arceye.termio;

import java.io.IOException;

public class TerminalIOException extends RuntimeException {

	public TerminalIOException(String message, IOException cause) {
		super(message, cause);
	}

}
