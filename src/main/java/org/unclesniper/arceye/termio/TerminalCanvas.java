package org.unclesniper.arceye.termio;

public interface TerminalCanvas {

	void carriageReturn();

	void clearScreen();

	void moveTo(int row, int column);

	void cursorDown();

	void cursorUp();

	void cursorLeft();

	void cursorRight();

	void newLine();

	void setForegroundColor(int color);

	void setBackgroundColor(int color);

	void resetAttributes();

	boolean isUTF8();

	void write(String data);

	void write(BoxSymbol symbol);

	void write(char c);

}
