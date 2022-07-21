package edu.upf.taln.welcome.slas.commons.utils;

public class LanguageConverter {
	public static String convertLanguage(String originalLanguage) {
		//Converting language code ISO 639-3 to ISO 639-1
		String globalLang = Language2Macrolanguage.getMacrolanguage(originalLanguage);
		String resultLang = ISO639_3ToISO639_1.getISO639_1(globalLang);
		if (resultLang == null) {
			resultLang = originalLanguage;
		}
		return resultLang;
	}
}
