package com.tuacy.azlist;


import com.tuacy.azlist.azlist.AZItemEntity;

import java.util.Comparator;

public class PinyinComparator implements Comparator<AZItemEntity<String>> {

	public int compare(AZItemEntity<String> o1, AZItemEntity<String> o2) {
		if (o1.getSortLetters().equals("@")
			|| o2.getSortLetters().equals("#")) {
			return 1;
		} else if (o1.getSortLetters().equals("#")
				   || o2.getSortLetters().equals("@")) {
			return -1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
