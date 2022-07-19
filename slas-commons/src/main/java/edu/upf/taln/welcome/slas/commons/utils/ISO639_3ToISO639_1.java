package edu.upf.taln.welcome.slas.commons.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ISO639_3ToISO639_1 {
	
	private static Map<String, Locale> localeMap = new HashMap<String, Locale>();
	static {
		String[] languages = Locale.getISOLanguages();
		for (String language : languages) {
		    Locale locale = new Locale(language);
		    localeMap.put(locale.getISO3Language(), locale);
		}
	}
	
	/**
	 * Returns the ISO 639-1 code of ISO 639-3 code languages.
	 * @param lang
	 * @return 
	 */
	public static String getISO639_1(String lang) {
		String resultLang = null;
		Locale locale = localeMap.get(lang);
		if (locale != null)
			resultLang = locale.getLanguage();
		return resultLang;
	}
}
