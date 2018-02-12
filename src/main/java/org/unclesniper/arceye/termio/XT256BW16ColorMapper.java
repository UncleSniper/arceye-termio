package org.unclesniper.arceye.termio;

import org.unclesniper.arceye.utils.ColorMap;

public class XT256BW16ColorMapper implements ColorMapper {

	public static final ColorMapper instance = new XT256BW16ColorMapper();

	public XT256BW16ColorMapper() {}

	public int mapColor(int color) {
		return ColorMap.contrast16To(color);
	}

}
