package org.unclesniper.arceye.termio;

public class BoxDrawing {

	public static void hline(TerminalCanvas term, int row, int column, int length,
			BoxSymbol line, BoxSymbol leftEnd, BoxSymbol rightEnd) {
		if(length <= 0)
			return;
		if(leftEnd == null)
			leftEnd = line;
		if(rightEnd == null)
			rightEnd = line;
		term.moveTo(row, column);
		if(length < 2) {
			term.write(line);
			return;
		}
		term.write(leftEnd);
		for(length -= 2; length > 0; --length)
			term.write(line);
		term.write(rightEnd);
	}

	public static void hline(TerminalCanvas term, int row, int column, int length, BoxSymbol line) {
		BoxDrawing.hline(term, row, column, length, line, null, null);
	}

	public static void hline(TerminalCanvas term, int row, int column, int length) {
		BoxDrawing.hline(term, row, column, length, BoxSymbol.HORIZONTAL_BAR, null, null);
	}

	public static void vline(TerminalCanvas term, int row, int column, int length,
			BoxSymbol line, BoxSymbol topEnd, BoxSymbol bottomEnd) {
		if(length <= 0)
			return;
		if(topEnd == null)
			topEnd = line;
		if(bottomEnd == null)
			bottomEnd = line;
		int bottom = row + length - 1;
		boolean withEnds = length >= 2;
		for(int r = row; r <= bottom; ++r) {
			term.moveTo(r, column);
			if(withEnds && r == row)
				term.write(topEnd);
			else if(withEnds && r == bottom)
				term.write(bottomEnd);
			else
				term.write(line);
		}
	}

	public static void vline(TerminalCanvas term, int row, int column, int length, BoxSymbol line) {
		BoxDrawing.vline(term, row, column, length, line, null, null);
	}

	public static void vline(TerminalCanvas term, int row, int column, int length) {
		BoxDrawing.vline(term, row, column, length, BoxSymbol.VERTICAL_BAR, null, null);
	}

	public static void box(TerminalCanvas term, int row, int column, int width, int height,
			BoxSymbol hline, BoxSymbol vline, BoxSymbol luCorner, BoxSymbol ruCorner,
			BoxSymbol llCorner, BoxSymbol rlCorner) {
		if(width <= 1 || height <= 1)
			return;
		BoxDrawing.hline(term, row, column, width, hline, luCorner, ruCorner);
		BoxDrawing.vline(term, row + 1, column, height - 2, vline, null, null);
		BoxDrawing.vline(term, row + 1, column + width - 1, height - 2, vline, null, null);
		BoxDrawing.hline(term, row + height - 1, column, width, hline, llCorner, rlCorner);
	}

	public static void box(TerminalCanvas term, int row, int column, int width, int height, boolean doubleLines) {
		if(doubleLines)
			BoxDrawing.box(term, row, column, width, height,
					BoxSymbol.DOUBLE_HORIZONTAL_BAR, BoxSymbol.DOUBLE_VERTICAL_BAR,
					BoxSymbol.DOUBLE_LEFT_UPPER_CORNER, BoxSymbol.DOUBLE_RIGHT_UPPER_CORNER,
					BoxSymbol.DOUBLE_LEFT_LOWER_CORNER, BoxSymbol.DOUBLE_RIGHT_LOWER_CORNER);
		else
			BoxDrawing.box(term, row, column, width, height, BoxSymbol.HORIZONTAL_BAR, BoxSymbol.VERTICAL_BAR,
					BoxSymbol.LEFT_UPPER_CORNER, BoxSymbol.RIGHT_UPPER_CORNER,
					BoxSymbol.LEFT_LOWER_CORNER, BoxSymbol.RIGHT_LOWER_CORNER);
	}

	public static void box(TerminalCanvas term, int row, int column, int width, int height) {
		BoxDrawing.box(term, row, column, width, height, false);
	}

	public static void fill(TerminalCanvas term, int row, int column, int width, int height, char c) {
		if(width <= 0 || height <= 0)
			return;
		int bottom = row + height - 1;
		for(int r = row; r <= bottom; ++r) {
			term.moveTo(r, column);
			for(int i = 0; i < width; ++i)
				term.write(c);
		}
	}

	public static void fill(TerminalCanvas term, int row, int column, int width, int height, BoxSymbol symbol) {
		BoxDrawing.fill(term, row, column, width, height,
				term.isUTF8() ? symbol.getCharacter() : symbol.getAlternateCharacter());
	}

	public static void fill(TerminalCanvas term, int row, int column, int width, int height) {
		BoxDrawing.fill(term, row, column, width, height, ' ');
	}

}
