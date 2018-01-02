package com.garland.helpcenter.utility;

import java.util.ArrayList;
import java.util.Arrays;

public class FireList extends ArrayList<String> {

	
	public FireList add(String ...items) {
		Arrays.sort(items,String.CASE_INSENSITIVE_ORDER);
		for(String item:items)
			add(item);
		return this;
	}

}
