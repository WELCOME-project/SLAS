package edu.upf.taln.welcome.slas.commons.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


public class LanguageConverterTest {
	@ParameterizedTest(name = "{0}")
	@CsvSource({
		"'South Levantine Arabic Language','ajp','ar'",
		"'North Levantine Arabic Language','apc','ar'",
		"'Spanish Language','spa','es'",
		"'Catalan language','cat','ca'",
		"'German language','deu','de'",
		"'Greek language','ell','el'",
		"'English language','eng','en'",
		"'Wrong language','epe','epe'"
	})
	public void languageISOTest(String testName, String lang, String expectedLang) {
		Assertions.assertEquals(expectedLang, LanguageConverter.convertLanguage(lang));
	}
}
