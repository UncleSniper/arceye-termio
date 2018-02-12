package org.unclesniper.arceye.termio;

import org.unclesniper.arceye.utils.ColorMap;

public class XT256216ColorMapper implements ColorMapper {

	public static final ColorMapper instance = new XT256216ColorMapper();

	public XT256216ColorMapper() {}

	public int mapColor(int color) {
		return ColorMap.highColorTo16Color(color);
	}

}
