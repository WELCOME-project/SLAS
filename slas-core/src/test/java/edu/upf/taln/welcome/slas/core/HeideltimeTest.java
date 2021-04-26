package edu.upf.taln.welcome.slas.core;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.dkpro.core.stanfordnlp.StanfordPosTagger;
import org.dkpro.core.stanfordnlp.StanfordSegmenter;
import org.dkpro.core.udpipe.UDPipePosTagger;
import org.dkpro.core.udpipe.UDPipeSegmenter;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.io.xmi.XmiWriter;
import de.unihd.dbs.uima.annotator.heideltime.HeidelTime;
import de.unihd.dbs.uima.annotator.intervaltagger.IntervalTagger;

public class HeideltimeTest {

	@Test
	public void heidelTest() throws UIMAException {
		/*
		 * It's the exception I got because of the wildcard lookup for the type system descriptions. uimaFIT/DKPro searches for *.xml in types subfolders and HeidelTime has some stylemap xml in such a folder, which causes the parse exception.
		 * */
		
		AnalysisEngine segmenter = AnalysisEngineFactory.createEngine(
			StanfordSegmenter.class, 
    		StanfordSegmenter.PARAM_LANGUAGE, "en");
		
		AnalysisEngine pos = AnalysisEngineFactory.createEngine(
				StanfordPosTagger.class,
				StanfordPosTagger.PARAM_LANGUAGE, "en");
		
		AnalysisEngine heideltime = AnalysisEngineFactory.createEngine(
				HeidelTime.class,
				HeidelTime.PARAM_LANGUAGE, "english",
				HeidelTime.PARAM_TYPE_TO_PROCESS, "news",
				HeidelTime.PARAM_DATE, true,
				HeidelTime.PARAM_TIME, true,
				HeidelTime.PARAM_DURATION, true,
				HeidelTime.PARAM_SET, true,
				HeidelTime.PARAM_TEMPONYMS, false,
				HeidelTime.PARAM_GROUP, true,
				HeidelTime.PARAM_USE_COARSE_VALUE, false,
				HeidelTime.PARAM_DEBUG, false);
		
		AnalysisEngine intervalTagger = AnalysisEngineFactory.createEngine(
				IntervalTagger.class,
				"language", "english",
				"annotate_intervals", true,
				"annotate_interval_candidates", true);
		
		AnalysisEngine writer = AnalysisEngineFactory.createEngine(
				XmiWriter.class,
				XmiWriter.PARAM_TARGET_LOCATION, "src/test/resources/output/xmi/heideltime/",
				XmiWriter.PARAM_OVERWRITE, true);
		
		String text = "Artifacts from the Paleolithic suggest that the moon was used to reckon time as early as 6,000 years ago. Lunar calendars were among the first to appear, with years of either 12 or 13 lunar months (either 354 or 384 days). Without intercalation to add days or months to some years, seasons quickly drift in a calendar based solely on twelve lunar months. Lunisolar calendars have a thirteenth month added to some years to make up for the difference between a full year (now known to be about 365.24 days) and a year of just twelve lunar months. The numbers twelve and thirteen came to feature prominently in many cultures, at least partly due to this relationship of months to years. Other early forms of calendars originated in Mesoamerica, particularly in ancient Mayan civilization. These calendars were religiously and astronomically based, with 18 months in a year and 20 days in a month, plus five epagomenal days at the end of the year. " + 
				"The reforms of Julius Caesar in 45 BC put the Roman world on a solar calendar. This Julian calendar was faulty in that its intercalation still allowed the astronomical solstices and equinoxes to advance against it by about 11 minutes per year. Pope Gregory XIII introduced a correction in 1582; the Gregorian calendar was only slowly adopted by different nations over a period of centuries, but it is now by far the most commonly used calendar around the world. " + 
				"During the French Revolution, a new clock and calendar were invented in an attempt to de-Christianize time and create a more rational system in order to replace the Gregorian calendar. The French Republican Calendar's days consisted of ten hours of a hundred minutes of a hundred seconds, which marked a deviation from the base 12 (duodecimal) system used in many other devices by many cultures. The system was abolished on May the 5th. " +
				"The war lasted form June 23 to November 14, 1845.";
		JCas jCas = JCasFactory.createText(text);
		DocumentMetaData md = DocumentMetaData.create(jCas);
		md.setDocumentId("heideltime");
		md.setLanguage("en");

		SimplePipeline.runPipeline(jCas, segmenter, pos, heideltime, intervalTagger, writer);
	}
	
	
	@Test
	public void heidelTestUDPipe() throws UIMAException {
		/*
		 * It's the exception I got because of the wildcard lookup for the type system descriptions. uimaFIT/DKPro searches for *.xml in types subfolders and HeidelTime has some stylemap xml in such a folder, which causes the parse exception.
		 * */
		
		AnalysisEngine segmenter = AnalysisEngineFactory.createEngine(
				UDPipeSegmenter.class, 
				UDPipeSegmenter.PARAM_LANGUAGE, "en");
	    
		AnalysisEngine posMorphLemma = AnalysisEngineFactory.createEngine(
				UDPipePosTagger.class,
				UDPipePosTagger.PARAM_LANGUAGE, "en");
		
		AnalysisEngine heideltime = AnalysisEngineFactory.createEngine(
				HeidelTime.class,
				HeidelTime.PARAM_LANGUAGE, "english",
				HeidelTime.PARAM_TYPE_TO_PROCESS, "news",
				HeidelTime.PARAM_DATE, true,
				HeidelTime.PARAM_TIME, true,
				HeidelTime.PARAM_DURATION, true,
				HeidelTime.PARAM_SET, true,
				HeidelTime.PARAM_TEMPONYMS, false,
				HeidelTime.PARAM_GROUP, true,
				HeidelTime.PARAM_USE_COARSE_VALUE, true,
				HeidelTime.PARAM_DEBUG, false);
		
		AnalysisEngine intervalTagger = AnalysisEngineFactory.createEngine(
				IntervalTagger.class,
				IntervalTagger.PARAM_LANGUAGE, "english",
				IntervalTagger.PARAM_INTERVALS, true,
				IntervalTagger.PARAM_INTERVAL_CANDIDATES, true);
		
		AnalysisEngine writer = AnalysisEngineFactory.createEngine(
				XmiWriter.class,
				XmiWriter.PARAM_TARGET_LOCATION, "src/test/resources/output/xmi/heideltime/",
				XmiWriter.PARAM_OVERWRITE, true);
		
		String text = "Artifacts from the Paleolithic suggest that the moon was used to reckon time as early as 6,000 years ago. Lunar calendars were among the first to appear, with years of either 12 or 13 lunar months (either 354 or 384 days). Without intercalation to add days or months to some years, seasons quickly drift in a calendar based solely on twelve lunar months. Lunisolar calendars have a thirteenth month added to some years to make up for the difference between a full year (now known to be about 365.24 days) and a year of just twelve lunar months. The numbers twelve and thirteen came to feature prominently in many cultures, at least partly due to this relationship of months to years. Other early forms of calendars originated in Mesoamerica, particularly in ancient Mayan civilization. These calendars were religiously and astronomically based, with 18 months in a year and 20 days in a month, plus five epagomenal days at the end of the year. " + 
				"The reforms of Julius Caesar in 45 BC put the Roman world on a solar calendar. This Julian calendar was faulty in that its intercalation still allowed the astronomical solstices and equinoxes to advance against it by about 11 minutes per year. Pope Gregory XIII introduced a correction in 1582; the Gregorian calendar was only slowly adopted by different nations over a period of centuries, but it is now by far the most commonly used calendar around the world. " + 
				"During the French Revolution, a new clock and calendar were invented in an attempt to de-Christianize time and create a more rational system in order to replace the Gregorian calendar. The French Republican Calendar's days consisted of ten hours of a hundred minutes of a hundred seconds, which marked a deviation from the base 12 (duodecimal) system used in many other devices by many cultures. The system was abolished on May the 5th. " +
				"The war lasted form June 23 to November 14, 1845.";
		JCas jCas = JCasFactory.createText(text);
		DocumentMetaData md = DocumentMetaData.create(jCas);
		md.setDocumentId("heideltime");
		md.setLanguage("en");

		SimplePipeline.runPipeline(jCas, segmenter, posMorphLemma, heideltime, intervalTagger, writer);
	}
	
}
