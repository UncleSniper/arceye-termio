package org.unclesniper.arceye.termio;

public interface TerminalInput {

	KeySym readBlocking();

	KeySym readNonBlocking();

}
