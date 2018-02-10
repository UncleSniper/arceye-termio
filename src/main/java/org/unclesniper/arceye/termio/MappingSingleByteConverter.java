package org.unclesniper.arceye.termio;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class MappingSingleByteConverter implements SingleByteConverter {

	public static final SingleByteConverter CONTROL;

	public static final SingleByteConverter ALTERNATE_BACKSPACE;

	static {
		MappingSingleByteConverter converter = new MappingSingleByteConverter();
		CONTROL = converter;
		converter.registerControlKeys();
		converter = new MappingSingleByteConverter();
		ALTERNATE_BACKSPACE = converter;
		converter.putKey((byte)0177, new KeySym(KeySym.Type.GENERIC, KeySym.Modifier.NONE, '\b'));
	}

	private final Map<Byte, KeySym> keys = new HashMap<Byte, KeySym>();

	public Set<Byte> getMappedBytes() {
		return keys.keySet();
	}

	public KeySym byteToKeySym(byte inputByte) {
		return keys.get(inputByte);
	}

	public void putKey(byte inputByte, KeySym key) {
		if(key == null)
			keys.remove(inputByte);
		else
			keys.put(inputByte, key);
	}

	public void clearKeys() {
		keys.clear();
	}

	public void registerControlKeys() {
		for(char c = 'a'; c <= 'z'; ++c)
			if(c != 'm')  // return has symbolic key
				putKey((byte)(c & 037), new KeySym(KeySym.Type.GENERIC, KeySym.Modifier.CONTROL, c));
	}

}
