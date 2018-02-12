package org.unclesniper.arceye.termio;

import java.util.List;
import java.util.LinkedList;

public class ChainedColorMapper implements ColorMapper {

	private final List<ColorMapper> mappers = new LinkedList<ColorMapper>();

	public ChainedColorMapper() {}

	public void addMapper(ColorMapper mapper) {
		if(mapper != null)
			mappers.add(mapper);
	}

	public boolean removeMapper(ColorMapper mapper) {
		return mapper != null && mappers.remove(mapper);
	}

	public void clearMappers() {
		mappers.clear();
	}

	public int mapColor(int color) {
		for(ColorMapper mapper : mappers)
			color = mapper.mapColor(color);
		return color;
	}

}
