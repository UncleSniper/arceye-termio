package org.unclesniper.arceye.termio;

import org.unclesniper.arceye.utils.ColorMap;

public class XT25628ColorMapper implements ColorMapper {

	public static final ColorMapper instance = new XT25628ColorMapper();

	public XT25628ColorMapper() {}

	public int mapColor(int color) {
		return ColorMap.highColorTo8Color(color);
	}

}
