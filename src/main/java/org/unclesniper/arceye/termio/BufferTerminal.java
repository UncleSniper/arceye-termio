package org.unclesniper.arceye.termio;

public class BufferTerminal implements Terminal {

	public static class Cell {

		private char character;

		private int foreground;

		private int background;

		private char visibleCharacter;

		private int visibleForeground;

		private int visibleBackground;

		public Cell(char character, int foreground, int background,
				char visibleCharacter, int visibleForeground, int visibleBackground) {
			this.character = character;
			this.foreground = foreground;
			this.background = background;
			this.visibleCharacter = visibleCharacter;
			this.visibleForeground = visibleForeground;
			this.visibleBackground = visibleBackground;
		}

		public char getCharacter() {
			return character;
		}

		public void setCharacter(char character) {
			this.character = character;
		}

		public int getForeground() {
			return foreground;
		}

		public void setForeground(int foreground) {
			this.foreground = foreground;
		}

		public int getBackground() {
			return background;
		}

		public void setBackground(int background) {
			this.background = background;
		}

		public char getVisibleCharacter() {
			return visibleCharacter;
		}

		public int getVisibleForeground() {
			return visibleForeground;
		}

		public int getVisibleBackground() {
			return visibleBackground;
		}

		public void putCharacter(char character, int foreground, int background) {
			this.character = character;
			this.foreground = foreground;
			this.background = background;
		}

	}

	private final TerminalOutput outputSlave;

	private final TerminalInput inputSlave;

	private Cell[][] contents;

	private int cursorRow;

	private int cursorColumn;

	private TermSize currentSize;

	private int currentForeground = -1;

	private int currentBackground = -1;

	private final boolean useUTF8;

	private int scrollDelta;

	private boolean pendingResize;

	public BufferTerminal(TerminalOutput outputSlave, TerminalInput inputSlave) {
		this.outputSlave = outputSlave;
		this.inputSlave = inputSlave;
		currentSize = outputSlave.getTermSize();
		contents = new Cell[currentSize.rows][];
		useUTF8 = outputSlave.isUTF8();
	}

	public BufferTerminal(TerminalOutput outputSlave) {
		this(outputSlave, outputSlave instanceof TerminalInput ? (TerminalInput)outputSlave : null);
	}

	public TerminalOutput getOutputSlave() {
		return outputSlave;
	}

	public TerminalInput getInputSlave() {
		return inputSlave;
	}

	public int getCursorRow() {
		return cursorRow;
	}

	public int getCursorColumn() {
		return cursorColumn;
	}

	public int getForegroundColor() {
		return currentForeground;
	}

	public int getBackgroundColor() {
		return currentBackground;
	}

	public KeySym readBlocking() {
		KeySym sym = inputSlave.readBlocking();
		if(sym != null && sym.getType() == KeySym.Type.WINCH) {
			updateSize();
			pendingResize = false;
		}
		return sym;
	}

	public KeySym readNonBlocking() {
		KeySym sym = inputSlave.readNonBlocking();
		if(sym != null && sym.getType() == KeySym.Type.WINCH) {
			updateSize();
			pendingResize = false;
		}
		return sym;
	}

	public void pushTermMode(TermMode mode) {
		outputSlave.pushTermMode(mode);
	}

	public TermMode popTermMode() {
		return outputSlave.popTermMode();
	}

	public TermMode getTermMode() {
		return outputSlave.getTermMode();
	}

	private boolean checkWinch() {
		if(!outputSlave.hasTermSizeChanged())
			return false;
		updateSize();
		pendingResize = true;
		return true;
	}

	private void updateSize() {
		TermSize newSize = outputSlave.getTermSize();
		boolean rchanged = newSize.rows != currentSize.rows, cchanged = newSize.columns != currentSize.columns;
		if(!rchanged && !cchanged)
			return;
		Cell[][] newTable = rchanged ? new Cell[newSize.rows][] : contents;
		for(int r = 0; r < newSize.rows; ++r) {
			if(cchanged) {
				Cell[] newRow = new Cell[newSize.columns];
				int maxColumn = newSize.columns < currentSize.columns ? newSize.columns : currentSize.columns;
				for(int c = 0; c < maxColumn; ++c)
					newRow[c] = contents[r][c];
				newTable[r] = newRow;
			}
			else if(r < contents.length)
				newTable[r] = contents[r];
			else
				newTable[r] = null;
		}
		contents = newTable;
		if(cursorRow >= newSize.rows)
			cursorRow = newSize.rows > 0 ? newSize.rows - 1 : 0;
		if(cursorColumn > newSize.columns)
			cursorColumn = newSize.columns;
		currentSize = newSize;
	}

	public boolean hasTermSizeChanged() {
		if(!checkWinch())
			return false;
		pendingResize = false;
		return true;
	}

	public TermSize getTermSize() {
		checkWinch();
		return currentSize;
	}

	public void carriageReturn() {
		checkWinch();
		cursorColumn = 0;
	}

	public void clearScreen() {
		checkWinch();
		for(int i = 0; i < contents.length; ++i)
			contents[i] = null;
		cursorRow = cursorColumn = 0;
	}

	public void moveTo(int row, int column) {
		checkWinch();
		if(row < 0)
			cursorRow = 0;
		else if(row >= currentSize.rows)
			cursorRow = currentSize.rows > 0 ? currentSize.rows - 1 : 0;
		else
			cursorRow = row;
		if(column < 0)
			cursorColumn = 0;
		else if(column > currentSize.columns)
			cursorColumn = currentSize.columns;
		else
			cursorColumn = column;
	}

	public void cursorDown() {
		checkWinch();
		if(cursorRow < currentSize.rows - 1)
			++cursorRow;
	}

	public void cursorUp() {
		checkWinch();
		if(cursorRow > 0)
			--cursorRow;
	}

	public void cursorLeft() {
		checkWinch();
		if(cursorColumn > 0)
			--cursorColumn;
	}

	public void cursorRight() {
		checkWinch();
		if(cursorColumn < currentSize.columns)
			++cursorColumn;
	}

	public void newLine() {
		checkWinch();
		cursorColumn = 0;
		putc('\n');
	}

	public void setForegroundColor(int color) {
		if(color < 0 || color > 255)
			throw new IllegalArgumentException("Terminal color code out of range: " + color);
		currentForeground = color;
	}

	public void setBackgroundColor(int color) {
		if(color < 0 || color > 255)
			throw new IllegalArgumentException("Terminal color code out of range: " + color);
		currentBackground = color;
	}

	public void resetAttributes() {
		currentForeground = currentBackground = -1;
	}

	public boolean isUTF8() {
		return useUTF8;
	}

	public void write(String data) {
		checkWinch();
		int i, length = data.length();
		for(i = 0; i < length; ++i)
			putc(data.charAt(i));
	}

	public void write(BoxSymbol symbol) {
		checkWinch();
		putc(useUTF8 ? symbol.getCharacter() : symbol.getAlternateCharacter());
	}

	public void write(char c) {
		checkWinch();
		putc(c);
	}

	private void putc(char c) {
		switch(c) {
			case '\b':
				if(cursorColumn > 0)
					--cursorColumn;
				break;
			case '\t':
				cursorColumn = (cursorColumn / 8 + 1) * 8;
				if(cursorColumn <= currentSize.columns)
					break;
				cursorColumn = 0;
			case '\n':
				if(cursorRow < contents.length - 1)
					++cursorRow;
				else
					scroll(1);
				break;
			case '\r':
				cursorRow = 0;
				break;
			default:
				{
					if(cursorColumn >= currentSize.columns) {
						cursorColumn = 0;
						if(cursorRow < contents.length - 1)
							++cursorRow;
						else
							scroll(1);
					}
					Cell[] row;
					if(contents[cursorRow] == null) {
						row = new Cell[currentSize.columns];
						contents[cursorRow] = row;
					}
					else
						row = contents[cursorRow];
					if(row[cursorColumn] == null)
						row[cursorColumn] = new Cell(c, currentForeground, currentBackground, ' ', -1, -1);
					else
						row[cursorColumn].putCharacter(c, currentForeground, currentBackground);
					if(++cursorColumn > currentSize.columns) {
						cursorColumn = 0;
						if(cursorRow < contents.length - 1)
							++cursorRow;
						else
							scroll(1);
					}
					break;
				}
		}
	}

	private void scroll(int delta) {
		for(int dest = 0; dest < contents.length; ++dest) {
			int src = dest + delta;
			contents[dest] = src < contents.length ? contents[src] : null;
		}
		scrollDelta += delta;
	}

	public void flush() {
		checkWinch();
		if(scrollDelta >= currentSize.rows)
			outputSlave.clearScreen();
		else {
			outputSlave.moveTo(currentSize.rows - 1, 0);
			for(; scrollDelta > 0; --scrollDelta)
				outputSlave.cursorDown();
		}
		int inRow = -1, inColumn = -1;
		int foreground = -1, background = -1;
		for(int r = 0; r < currentSize.rows; ++r) {
			Cell[] row = contents[r];
			if(row == null)
				continue;
			for(int c = 0; c < currentSize.columns; ++c) {
				Cell cell = row[c];
				if(cell == null)
					continue;
				boolean shouldBeBlank = cell.character == ' ' ||
						(cell.foreground == cell.background && cell.foreground >= 0);
				boolean isBlank = cell.visibleCharacter == ' ' ||
						(cell.visibleForeground == cell.visibleBackground && cell.visibleForeground >= 0);
				if(shouldBeBlank && isBlank && cell.background == cell.visibleBackground)
					continue;
				if(inRow == r) {
					if(inColumn == c)
						;
					else if(inColumn == c - 1)
						outputSlave.cursorRight();
					else
						outputSlave.moveTo(r, c);
				}
				else if(inRow == r - 1 && inColumn == currentSize.columns)
					;
				else
					outputSlave.moveTo(r, c);
				inRow = r;
				inColumn = c + 1;
				if(foreground != cell.foreground) {
					if(cell.foreground < 0) {
						outputSlave.resetAttributes();
						background = -1;
					}
					else
						outputSlave.setForegroundColor(cell.foreground);
					foreground = cell.foreground;
				}
				if(background != cell.background) {
					if(cell.background < 0) {
						outputSlave.resetAttributes();
						if(foreground >= 0)
							outputSlave.setForegroundColor(foreground);
					}
					else
						outputSlave.setBackgroundColor(cell.background);
					background = cell.background;
				}
				outputSlave.write(cell.character);
			}
		}
		outputSlave.flush();
	}

}
