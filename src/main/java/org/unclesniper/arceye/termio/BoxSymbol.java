package org.unclesniper.arceye.termio;

public enum BoxSymbol {

	BLANK(' ', ' '),
	HORIZONTAL_BAR('\u2500', '-'),
	VERTICAL_BAR('\u2502', '|'),
	LEFT_UPPER_CORNER('\u250C', '+'),
	RIGHT_UPPER_CORNER('\u2510', '+'),
	LEFT_LOWER_CORNER('\u2514', '+'),
	RIGHT_LOWER_CORNER('\u2518', '+'),
	LEFT_T('\u251C', '+'),
	RIGHT_T('\u2524', '+'),
	UPPER_T('\u252C', '+'),
	LOWER_T('\u2534', '+'),
	CROSS('\u253C', '+'),
	DOUBLE_HORIZONTAL_BAR('\u2550', '='),
	DOUBLE_VERTICAL_BAR('\u2551', '|'),
	DOUBLE_LEFT_UPPER_CORNER('\u2554', '+'),
	DOUBLE_RIGHT_UPPER_CORNER('\u2557', '+'),
	DOUBLE_LEFT_LOWER_CORNER('\u255A', '+'),
	DOUBLE_RIGHT_LOWER_CORNER('\u255D', '+'),
	DOUBLE_LEFT_T('\u2560', '+'),
	DOUBLE_RIGHT_T('\u2563', '+'),
	DOUBLE_UPPER_T('\u2566', '+'),
	DOUBLE_LOWER_T('\u2569', '+'),
	DOUBLE_CROSS('\u256C', '+'),
	LEFT_UPPER_CORNER_DOUBLE_TOP('\u2552', '+'),
	LEFT_UPPER_CORNER_DOUBLE_LEFT('\u2553', '+'),
	RIGHT_UPPER_CORNER_DOUBLE_TOP('\u2555', '+'),
	RIGHT_UPPER_CORNER_DOUBLE_RIGHT('\u2556', '+'),
	LEFT_LOWER_CORNER_DOUBLE_BOTTOM('\u2558', '+'),
	LEFT_LOWER_CORNER_DOUBLE_LEFT('\u2559', '+'),
	RIGHT_LOWER_CORNER_DOUBLE_BOTTOM('\u255B', '+'),
	RIGHT_LOWER_CORNER_DOUBLE_RIGHT('\u255C', '+'),
	LEFT_T_DOUBLE_RIGHT('\u255E', '+'),
	LEFT_T_DOUBLE_LEFT('\u255F', '+'),
	RIGHT_T_DOUBLE_LEFT('\u2561', '+'),
	RIGHT_T_DOUBLE_RIGHT('\u2562', '+'),
	UPPER_T_DOUBLE_BOTTOM('\u2565', '+'),
	UPPER_T_DOUBLE_TOP('\u2564', '+'),
	LOWER_T_DOUBLE_TOP('\u2568', '+'),
	LOWER_T_DOUBLE_BOTTOM('\u2567', '+'),
	CROSS_DOUBLE_HORIZONTAL('\u256A', '+'),
	CROSS_DOUBLE_VERTICAL('\u256B', '+'),
	CHECKBOX_EMPTY('\u25A1', '.'),
	CHECKBOX_HALF('\u25A3', 'o'),
	CHECKBOX_FULL('\u25A0', 'O'),
	TRIANGLE_UP('\u25B2', '^'),
	TRIANGLE_DOWN('\u25BC', 'v'),
	TRIANGLE_LEFT('\u25C4', '<'),
	TRIANGLE_RIGHT('\u25BA', '>'),
	BIG_CHECKBOX_EMPTY('\u2610', '.'),
	BIG_CHECKBOX_FULL('\u2611', 'O'),
	DAGGER('\u2670', '+'),
	HOURGLASS('\u231B', '&'),
	INFINITY('\u221E', '*'),
	LEFT_ARROW('\u2190', '<'),
	RIGHT_ARROW('\u2192', '>'),
	UP_ARROW('\u2191', '^'),
	DOWN_ARROW('\u2193', 'v'),
	HORIZONTAL_ARROW('\u2194', '-'),
	VERTICAL_ARROW('\u2195', '|'),
	TAB_ARROW('\u21A6', '>'),
	RETURN_ARROW('\u21B3', '#'),
	FULL_BLOCK('\u2588', '#'),
	LOWER_BLOCK_18('\u2581', ' '),
	LOWER_BLOCK_28('\u2582', ' '),
	LOWER_BLOCK_38('\u2583', ' '),
	LOWER_BLOCK_48('\u2584', ' '),
	LOWER_BLOCK_58('\u2585', '#'),
	LOWER_BLOCK_68('\u2586', '#'),
	LOWER_BLOCK_78('\u2587', '#'),
	LEFT_BLOCK_18('\u258F', ' '),
	LEFT_BLOCK_28('\u258E', ' '),
	LEFT_BLOCK_38('\u258D', ' '),
	LEFT_BLOCK_48('\u258C', ' '),
	LEFT_BLOCK_58('\u258B', '#'),
	LEFT_BLOCK_68('\u258A', '#'),
	LEFT_BLOCK_78('\u2589', '#');

	private final char character;

	private final char alternateCharacter;

	private static final BoxSymbol[] lowerBlocks = new BoxSymbol[] {
		BLANK,
		LOWER_BLOCK_18,
		LOWER_BLOCK_28,
		LOWER_BLOCK_38,
		LOWER_BLOCK_48,
		LOWER_BLOCK_58,
		LOWER_BLOCK_68,
		LOWER_BLOCK_78,
		FULL_BLOCK
	};

	private static final BoxSymbol[] leftBlocks = new BoxSymbol[] {
		BLANK,
		LEFT_BLOCK_18,
		LEFT_BLOCK_28,
		LEFT_BLOCK_38,
		LEFT_BLOCK_48,
		LEFT_BLOCK_58,
		LEFT_BLOCK_68,
		LEFT_BLOCK_78,
		FULL_BLOCK
	};

	private BoxSymbol(char character, char alternateCharacter) {
		this.character = character;
		this.alternateCharacter = alternateCharacter;
	}

	public char getCharacter() {
		return character;
	}

	public char getAlternateCharacter() {
		return alternateCharacter;
	}

	public static BoxSymbol lowerBlock(int fraction) {
		return BoxSymbol.lowerBlocks[fraction];
	}

	public static BoxSymbol leftBlock(int fraction) {
		return BoxSymbol.leftBlocks[fraction];
	}

}
