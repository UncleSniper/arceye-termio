package org.unclesniper.arceye.termio;

public class SBoxColorMapper implements ColorMapper {

	private final int[] sbox = new int[256];

	public SBoxColorMapper() {
		for(int i = 0; i < 256; ++i)
			sbox[i] = i;
	}

	public SBoxColorMapper(ColorMapper initialMapping) {
		for(int i = 0; i < 256; ++i)
			sbox[i] = initialMapping.mapColor(i);
	}

	public void putMapping(int input, int output) {
		sbox[input] = output;
	}

	public int mapColor(int color) {
		return sbox[color];
	}

}
