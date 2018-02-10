package org.unclesniper.arceye.termio;

public interface TerminalOutput extends TerminalCanvas {

	void pushTermMode(TermMode mode);

	TermMode popTermMode();

	TermMode getTermMode();

	boolean hasTermSizeChanged();

	TermSize getTermSize();

	void flush();

}
