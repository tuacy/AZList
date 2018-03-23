package com.tuacy.azlist;


import com.tuacy.azlist.azlist.AZEntity;

import java.util.Comparator;

public class PinyinComparator implements Comparator<AZEntity<String>> {

	public int compare(AZEntity<String> o1, AZEntity<String> o2) {
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
