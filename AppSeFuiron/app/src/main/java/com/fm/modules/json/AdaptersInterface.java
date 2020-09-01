package com.fm.modules.json;

import java.util.List;

public interface AdaptersInterface<T> {

	public List<T> parser(String contenido);
	
}
