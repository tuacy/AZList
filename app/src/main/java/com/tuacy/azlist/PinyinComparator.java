package com.tuacy.azlist;


import com.tuacy.azlist.azlist.AZItemEntity;

import java.util.Comparator;

public class PinyinComparator implements Comparator<AZItemEntity<String>> {

	public int compare(AZItemEntity<String> o1, AZItemEntity<String> o2) {
		if (o1.getLetters().equals("@")
			|| o2.getLetters().equals("#")) {
			return 1;
		} else if (o1.getLetters().equals("#")
				   || o2.getLetters().equals("@")) {
			return -1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
