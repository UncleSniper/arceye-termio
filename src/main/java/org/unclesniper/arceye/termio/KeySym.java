package org.unclesniper.arceye.termio;

import org.unclesniper.arceye.utils.ObjectArrayIterable;

public class KeySym {

	public static enum Type {

		WINCH("<winch>"),
		DELETE("Delete"),
		DOWN("Down"),
		END("End"),
		ENTER("Enter"),
		FUNCTION(null),
		HOME("Home"),
		INSERT("Insert"),
		LEFT("Left"),
		PAGE_DOWN("PageDown"),
		PAGE_UP("PageUp"),
		RIGHT("Right"),
		UP("Up"),
		GENERIC(null);

		static final int MODULUS = Type.values().length;

		private final String name;

		private Type(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	public static enum Modifier {

		NONE(""),
		SHIFT("S-"),
		CONTROL("C-"),
		ALT("M-");

		static final int MODULUS = Modifier.values().length;

		private final String prefix;

		private Modifier(String prefix) {
			this.prefix = prefix;
		}

		public String getPrefix() {
			return prefix;
		}

	}

	public static final KeySym WINCH = new KeySym(Type.WINCH, Modifier.NONE, '\000');
	public static final KeySym DELETE = new KeySym(Type.DELETE, Modifier.NONE, '\000');
	public static final KeySym DOWN = new KeySym(Type.DOWN, Modifier.NONE, '\000');
	public static final KeySym END = new KeySym(Type.END, Modifier.NONE, '\000');
	public static final KeySym ENTER = new KeySym(Type.ENTER, Modifier.NONE, '\000');
	public static final KeySym F1 = new KeySym(Type.FUNCTION, Modifier.NONE, '\001');
	public static final KeySym F2 = new KeySym(Type.FUNCTION, Modifier.NONE, '\002');
	public static final KeySym F3 = new KeySym(Type.FUNCTION, Modifier.NONE, '\003');
	public static final KeySym F4 = new KeySym(Type.FUNCTION, Modifier.NONE, '\004');
	public static final KeySym F5 = new KeySym(Type.FUNCTION, Modifier.NONE, '\005');
	public static final KeySym F6 = new KeySym(Type.FUNCTION, Modifier.NONE, '\006');
	public static final KeySym F7 = new KeySym(Type.FUNCTION, Modifier.NONE, '\007');
	public static final KeySym F8 = new KeySym(Type.FUNCTION, Modifier.NONE, '\010');
	public static final KeySym F9 = new KeySym(Type.FUNCTION, Modifier.NONE, '\011');
	public static final KeySym F10 = new KeySym(Type.FUNCTION, Modifier.NONE, '\012');
	public static final KeySym F11 = new KeySym(Type.FUNCTION, Modifier.NONE, '\013');
	public static final KeySym F12 = new KeySym(Type.FUNCTION, Modifier.NONE, '\014');
	public static final KeySym HOME = new KeySym(Type.HOME, Modifier.NONE, '\000');
	public static final KeySym INSERT = new KeySym(Type.INSERT, Modifier.NONE, '\000');
	public static final KeySym LEFT = new KeySym(Type.LEFT, Modifier.NONE, '\000');
	public static final KeySym PAGE_DOWN = new KeySym(Type.PAGE_DOWN, Modifier.NONE, '\000');
	public static final KeySym PAGE_UP = new KeySym(Type.PAGE_UP, Modifier.NONE, '\000');
	public static final KeySym RIGHT = new KeySym(Type.RIGHT, Modifier.NONE, '\000');
	public static final KeySym UP = new KeySym(Type.UP, Modifier.NONE, '\000');
	public static final KeySym BACKSPACE = new KeySym(Type.GENERIC, Modifier.NONE, '\b');
	public static final KeySym TAB = new KeySym(Type.GENERIC, Modifier.NONE, '\t');
	public static final KeySym NEWLINE = new KeySym(Type.GENERIC, Modifier.NONE, '\n');
	public static final KeySym RETURN = new KeySym(Type.GENERIC, Modifier.NONE, '\r');
	public static final KeySym ESCAPE = new KeySym(Type.GENERIC, Modifier.NONE, '\033');
	public static final KeySym SPACE = new KeySym(Type.GENERIC, Modifier.NONE, ' ');

	private final Type type;

	private final Modifier modifier;

	private final char value;

	public KeySym(Type type, Modifier modifier, char value) {
		this.type = type;
		this.modifier = modifier;
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public Modifier getModifier() {
		return modifier;
	}

	public char getValue() {
		return value;
	}

	public String toString() {
		switch(type) {
			case FUNCTION:
				return modifier.getPrefix() + 'F' + String.valueOf((int)value);
			case GENERIC:
				return modifier.getPrefix() + KeySym.nameChar(value);
			default:
				return modifier.getPrefix() + type.getName();
		}
	}

	public static String nameChar(char c) {
		switch(c) {
			case '\000':
				return "Null";
			case '\007':
				return "Bell";
			case '\b':
				return "Backspace";
			case '\t':
				return "Tab";
			case '\n':
				return "Newline";
			case '\f':
				return "FormFeed";
			case '\r':
				return "Return";
			case '\033':
				return "Escape";
			case ' ':
				return "Space";
			default:
				return String.valueOf(c);
		}
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof KeySym))
			return false;
		KeySym other = (KeySym)obj;
		return type == other.type && modifier == other.modifier && value == other.value;
	}

	public int hashCode() {
		return (value * Type.MODULUS + type.ordinal()) * Modifier.MODULUS + modifier.ordinal();
	}

	public static Iterable<KeySym> sequence(KeySym... keys) {
		return new ObjectArrayIterable<KeySym>(keys);
	}

}
