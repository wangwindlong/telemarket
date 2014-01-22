package com.example.comparator;

import com.example.telemarket.bean.CustomInfo;

import java.util.Comparator;

public class PinyinComparator implements Comparator<CustomInfo> {

	public int compare(CustomInfo o1, CustomInfo o2) {
		if (o1.getRetriAlpha().equals("@")
				|| o2.getRetriAlpha().equals("#")) {
			return -1;
		} else if (o1.getRetriAlpha().equals("#")
				|| o2.getRetriAlpha().equals("@")) {
			return 1;
		} else {
			return o1.getRetriAlpha().compareTo(o2.getRetriAlpha());
		}
	}

}
