package edu.upf.taln.welcome.slas.core;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import edu.upf.taln.welcome.slas.commons.exceptions.WelcomeException;
import edu.upf.taln.welcome.slas.commons.input.DeepAnalysisInput;
import edu.upf.taln.welcome.slas.commons.input.InputData;

public class PreprocessTest {
	
	@Test
	public void finalPunctTest() throws WelcomeException {
		String noFinalPunct = "I hope you are well   ";
		
		DeepAnalysisInput input = new DeepAnalysisInput();
		InputData inputData = new InputData();
		inputData.setText(noFinalPunct);
		input.setData(inputData);
		
		Analyzer.preprocess(input);
		
		Assert.assertEquals("I hope you are well .", input.getData().getText());
	}
	
	@Test
	public void yeahTest() throws WelcomeException {
		String noFinalPunct = "You come tomorrow, yeah? \nYeah, for sure dude!";
		
		DeepAnalysisInput input = new DeepAnalysisInput();
		InputData inputData = new InputData();
		inputData.setText(noFinalPunct);
		input.setData(inputData);
		
		Analyzer.preprocess(input);
		
		Assert.assertEquals("You come tomorrow, yes? \nYes, for sure dude!", input.getData().getText());
	}
	
	@ParameterizedTest(name = "{0}")
	@CsvSource({
		"TwoLines,'First line. \nThis is a test.'",
		"FinalExclamation,This is a test!",
		"FinalDot,This is a test.",
		"FinalQuestionMark,Is this a test?",
		"FinalLineBreak,Is this a test.\n  "
	})
	public void noChangesTest(String testName, String text) {
		DeepAnalysisInput input = new DeepAnalysisInput();
		InputData inputData = new InputData();
		inputData.setText(text);
		input.setData(inputData);
		
		Analyzer.preprocess(input);
		
		Assert.assertEquals(text, input.getData().getText());
	}
}
