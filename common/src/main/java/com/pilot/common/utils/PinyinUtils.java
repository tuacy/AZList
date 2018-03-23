package com.pilot.common.utils;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class PinyinUtils {

	/**
	 * 获取拼音
	 */
	public static String getPingYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		StringBuilder output = new StringBuilder();

		try {
			for (char curChar : input) {
				if (Character.toString(curChar).matches("[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(curChar, format);
					output.append(temp[0]);
				} else {
					output.append(Character.toString(curChar));
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return output.toString();
	}

	/**
	 * 获取第一个字的拼音首字母
	 */
	public static String getFirstSpell(String chinese) {
		StringBuilder pinYinBF = new StringBuilder();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (char curChar : arr) {
			if (curChar > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(curChar, defaultFormat);
					if (temp != null) {
						pinYinBF.append(temp[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinYinBF.append(curChar);
			}
		}
		return pinYinBF.toString().replaceAll("\\W", "").trim();
	}

	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变，特殊字符丢失 支持多音字，生成方式如（长沙市长:cssc,zssz,zssc,cssz）
	 *
	 * @param chines 汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chines) {
		StringBuilder pinyinName = new StringBuilder();
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (char aNameChar : nameChar) {
			if (aNameChar > 128) {
				try {
					// 取得当前汉字的所有全拼
					String[] str = PinyinHelper.toHanyuPinyinStringArray(aNameChar, defaultFormat);
					if (str != null) {
						for (int j = 0; j < str.length; j++) {
							// 取首字母
							pinyinName.append(str[j].charAt(0));
							if (j != str.length - 1) {
								pinyinName.append(",");
							}
						}
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName.append(aNameChar);
			}
			pinyinName.append(" ");
		}
		// return pinyinName.toString();
		return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
	}

	/**
	 * 汉字转换位汉语全拼，英文字符不变，特殊字符丢失
	 * 支持多音字，生成方式如（重当参:zhongdangcen,zhongdangcan,chongdangcen
	 * ,chongdangshen,zhongdangshen,chongdangcan）
	 *
	 * @param chines 汉字
	 * @return 拼音
	 */
	public static String converterToSpell(String chines) {
		StringBuilder pinyinName = new StringBuilder();
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (char aNameChar : nameChar) {
			if (aNameChar > 128) {
				try {
					// 取得当前汉字的所有全拼
					String[] str = PinyinHelper.toHanyuPinyinStringArray(aNameChar, defaultFormat);
					if (str != null) {
						for (int j = 0; j < str.length; j++) {
							pinyinName.append(str[j]);
							if (j != str.length - 1) {
								pinyinName.append(",");
							}
						}
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName.append(aNameChar);
			}
			pinyinName.append(" ");
		}
		// return pinyinName.toString();
		return parseTheChineseByObject(discountTheChinese(pinyinName.toString()));
	}

	/**
	 * 去除多音字重复数据
	 */
	private static List<Map<String, Integer>> discountTheChinese(String theStr) {
		// 去除重复拼音后的拼音列表
		List<Map<String, Integer>> mapList = new ArrayList<>();
		// 用于处理每个字的多音字，去掉重复
		Map<String, Integer> onlyOne;
		String[] firsts = theStr.split(" ");
		// 读出每个汉字的拼音
		for (String str : firsts) {
			onlyOne = new Hashtable<>();
			String[] china = str.split(",");
			// 多音字处理
			for (String s : china) {
				Integer count = onlyOne.get(s);
				if (count == null) {
					onlyOne.put(s, 1);
				} else {
					onlyOne.remove(s);
					count++;
					onlyOne.put(s, count);
				}
			}
			mapList.add(onlyOne);
		}
		return mapList;
	}

	/**
	 * 解析并组合拼音，对象合并方案(推荐使用)
	 */
	private static String parseTheChineseByObject(List<Map<String, Integer>> list) {
		Map<String, Integer> first = null; // 用于统计每一次,集合组合数据
		// 遍历每一组集合
		for (int i = 0; i < list.size(); i++) {
			// 每一组集合与上一次组合的Map
			Map<String, Integer> temp = new Hashtable<>();
			// 第一次循环，first为空
			if (first != null) {
				// 取出上次组合与此次集合的字符，并保存
				for (String s : first.keySet()) {
					for (String s1 : list.get(i).keySet()) {
						String str = s + s1;
						temp.put(str, 1);
					}
				}
				// 清理上一次组合数据
				if (temp.size() > 0) {
					first.clear();
				}
			} else {
				for (String s : list.get(i).keySet()) {
					temp.put(s, 1);
				}
			}
			// 保存组合数据以便下次循环使用
			if (temp.size() > 0) {
				first = temp;
			}
		}
		StringBuilder returnStr = new StringBuilder();
		if (first != null) {
			// 遍历取出组合字符串
			for (String str : first.keySet()) {
				returnStr.append(str).append(",");
			}
		}
		if (returnStr.length() > 0) {
			returnStr = new StringBuilder(returnStr.substring(0, returnStr.length() - 1));
		}
		return returnStr.toString();
	}
}
