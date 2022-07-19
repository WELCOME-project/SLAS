package edu.upf.taln.welcome.slas.commons.utils;

import java.util.HashMap;
import java.util.Map;

public class Language2Macrolanguage {
	
	//Only Arabic included for now
	private static final Map<String, String> macroLanguages = new HashMap<>();
	static {
	    macroLanguages.put("aao", "ara"); //https://iso639-3.sil.org/code/ara
	    macroLanguages.put("abh", "ara");
	    macroLanguages.put("abv", "ara");
	    macroLanguages.put("acm", "ara");
	    macroLanguages.put("acq", "ara");
	    macroLanguages.put("acw", "ara");
	    macroLanguages.put("acx", "ara");
	    macroLanguages.put("acy", "ara");
	    macroLanguages.put("adf", "ara");
	    macroLanguages.put("aeb", "ara");
	    macroLanguages.put("aec", "ara");
	    macroLanguages.put("afb", "ara");
	    macroLanguages.put("ajp", "ara");
	    macroLanguages.put("apc", "ara");
	    macroLanguages.put("apd", "ara");
	    macroLanguages.put("arb", "ara");
	    macroLanguages.put("arq", "ara");
	    macroLanguages.put("ars", "ara");
	    macroLanguages.put("ary", "ara");
	    macroLanguages.put("arz", "ara");
	    macroLanguages.put("auz", "ara");
	    macroLanguages.put("avl", "ara");
	    macroLanguages.put("ayh", "ara");
	    macroLanguages.put("ayl", "ara");
	    macroLanguages.put("ayn", "ara");
	    macroLanguages.put("ayp", "ara");
	    macroLanguages.put("bbz", "ara");
	    macroLanguages.put("pga", "ara");
	    macroLanguages.put("shu", "ara");
	    macroLanguages.put("ssh", "ara");
	}
	
	/**
	 * Returns the macrolanguage of a specific language, or the input language if no macrilanguage is found.
	 * @param lang
	 * @return 
	 */
	public static String getMacrolanguage(String lang) {
		String macro = macroLanguages.get(lang);
		if (macro == null)
			macro = lang;
		return macro;
	}
}
